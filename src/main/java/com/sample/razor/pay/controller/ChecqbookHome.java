package com.sample.razor.pay.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.checqbook.dto.OrderDetailDTO;
import com.checqbook.dto.PaymentDetailDTO;
import com.checqbook.utils.ConnectionHelper;
import com.checqbook.utils.Validator;
import com.google.gson.Gson;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sample.razor.pay.model.Customer;
import com.sample.razor.pay.model.RazorPay;
import com.sample.razor.pay.model.Response;
/**
 * 
 * @author Pradeep Yadav
 * This controller class will be connected with razorpay api 
 * and storing order and payment details in local DB
 */
@Controller
public class ChecqbookHome {
	
	private RazorpayClient client;
	private static Gson gson = new Gson();
	
	/**
	 * add your secretId and secretValue of your RazorPay account.
	 */
	private static final String SECRET_ID = "rzp_test_ZlCeWvVHhQTR1G";
	private static final String SECRET_KEY = "mkwkqdF7npFgM1zLhQ4UkG9S";
	
	Connection sqlite;

	/**
	 *  initializing client object
	 * @throws RazorpayException
	 */
	public ChecqbookHome() throws RazorpayException {
		this.client =  new RazorpayClient(SECRET_ID, SECRET_KEY); 
	}
	
	@RequestMapping(value="/")
	public String getHome() {
		
		return "redirect:/home";
	}
	@RequestMapping(value="/home")
	public String getHomeInit() {
			try {
				validateDatabase();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RazorpayException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return "home";
	}
	
	@RequestMapping(value="/donePayment")
	public String getPymentDetails() {
		try {
			validateDatabase();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RazorpayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "home";
		
	}
	
	/**
	 * This method validates entries from razorpay account with our local database.
	 * @throws RazorpayException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void validateDatabase() throws RazorpayException, ClassNotFoundException, SQLException{
		sqlite = ConnectionHelper.openSqliteConnection("src/main/resources/db_file/checqbook_db.db");
		List<Order> orderList = client.Orders.fetchAll();
		for(Order order:orderList) {
			OrderDetailDTO orderDetailDTO = fillOrderDetailDTO(order);
			addOrderDetailsInDb(orderDetailDTO);
			
			
			List<Payment> paymentList = client.Orders.fetchPayments((String) order.get("id"));
			
			for(Payment payment: paymentList) {
				PaymentDetailDTO paymentDetailDTO = fillPaymentDetailDTO(payment);
				fillPaymentTable(paymentDetailDTO);
			}

			
			
		}
	}
	
	/**
	 * For fetching required details from payment object of razorpay
	 * @param payment
	 * @return payment dto
	 */
	private PaymentDetailDTO fillPaymentDetailDTO(Payment payment) {
		PaymentDetailDTO paymentDetailDTO = new PaymentDetailDTO();
		paymentDetailDTO.setCurrency("INR");
		paymentDetailDTO.setOrderId((String)payment.get("order_id"));
		paymentDetailDTO.setPaymentAmount(String.valueOf(((Integer) payment.get("amount"))/100));
		paymentDetailDTO.setPaymentDesc((String)payment.get("description"));
		paymentDetailDTO.setPaymentId((String)payment.get("id"));
		paymentDetailDTO.setTransId("");
		return paymentDetailDTO;
	}

	/**
	 * This method fetch order details from order object
	 * @param order
	 * @return order detail dto
	 */
	private OrderDetailDTO fillOrderDetailDTO(Order order) {
		// TODO Auto-generated method stub
		OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
		orderDetailDTO.setOrderId((String) order.get("id"));
		orderDetailDTO.setAmountDue(String.valueOf(((Integer) order.get("amount_due"))/100));
		orderDetailDTO.setAttempts(String.valueOf((Integer) order.get("attempts")));
		orderDetailDTO.setCreatedAt(String.valueOf((Date) order.get("created_at")));
		orderDetailDTO.setMarchent("ChqBook");
		orderDetailDTO.setStatus((String) order.get("status"));
		orderDetailDTO.setTotalAmount(String.valueOf(((Integer) order.get("amount"))/100));
		return orderDetailDTO;
	}

	
	/**
	 * Enter order details in local db
	 * @param OrderDetailDTO
	 */
	private void addOrderDetailsInDb(OrderDetailDTO dto) {
		// TODO Auto-generated method stub
		Statement statement = null;
		try {
			statement = ConnectionHelper.sqliteConnection.createStatement();
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		String insertQuery;
		String tableName="ORDER_DETAIL";
		insertQuery = "REPLACE INTO "+tableName+" VALUES('"+dto.getOrderId()+"','"+
				dto.getMarchent()
		+"','"+dto.getAmountDue()
		+"','"+dto.getTotalAmount()
		+"','"+dto.getCreatedAt()
		+"','"+dto.getStatus()
		+"','"+dto.getAttempts()
		+"')";
		System.out.println(insertQuery);
		try {
			statement.executeUpdate(insertQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}


	}

	/**
	 * add payment details in local db
	 * @param PaymentDetailDTO
	 */
	private void fillPaymentTable(PaymentDetailDTO dto) {
		// TODO Auto-generated method stub
		Statement statement = null;
		try {
			statement = ConnectionHelper.sqliteConnection.createStatement();
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		String insertQuery;
		String tableName="PAYMENT_DETAIL";
		insertQuery = "REPLACE INTO "+tableName+" VALUES('"+dto.getPaymentId()+"','"+
				dto.getOrderId()
		+"','"+dto.getPaymentDesc()
		+"','"+dto.getTransId()
		+"','"+dto.getCurrency()
		+"','"+dto.getPaymentAmount()
		+"')";
		System.out.println(insertQuery);
		try {
			statement.executeUpdate(insertQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * main api for creating order on razorpay
	 * @param customer
	 * @return
	 */

	@RequestMapping(value="/createPayment", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> createOrder(@RequestBody Customer customer) {
		//Assuming mobile number as primary key
		Response response = new Response();

		Validator validator = new Validator();
		response = validator.validateCustomer(customer);
		
		if(response.getErrorMsgs().length()>0) {
			response.setStatusCode(500);
			response.setRazorPay(new RazorPay());
			return new ResponseEntity<String>(gson.toJson(response),HttpStatus.EXPECTATION_FAILED);
		}
		
		try {
			sqlite = ConnectionHelper.openSqliteConnection("src/main/resources/db_file/checqbook_db.db");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
		
			/**
			 * creating an order in RazorPay.
			 * new order will have order id. you can get this order id by calling  order.get("id")
			 */
			Order order = createRazorPayOrder( customer.getAmount() );
			String orderId = (String)order.get("id");
			System.out.println("Order Id: "+orderId);
			
			RazorPay razorPay = getRazorPay((String)order.get("id"), customer);
			
			insertCustomerDataInDb(customer,orderId);

			addOrderDetailsInDb(razorPay, customer.getAmount() );
			
				/*sqlite = ConnectionHelper.openSqliteConnection("src/main/resources/db_file/checqbook_db.db");
				Statement statement = sqlite.createStatement();
				ResultSet resultSet = statement.executeQuery("select * from employees");
				while(resultSet.next()) {
					System.out.println(resultSet.getString(1));
				}*/
			
			ResponseEntity responseEntity =  new ResponseEntity<String>(gson.toJson(getResponse(razorPay, 200)),
					HttpStatus.OK);
			
			/*List<Payment> paymentList = client.Orders.fetchPayments((String) order.get("id"));
			
			Payment payment = paymentList.get(0);
			// The the Entity.get("attribute_key") method has flexible return types depending on the attribute
			int amount = payment.get("amount");
			String id = payment.get("id");
			Date createdAt = payment.get("created_at"); */
			
			return responseEntity;
			
			
		} catch (RazorpayException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>(gson.toJson(getResponse(new RazorPay(), 500)),
				HttpStatus.EXPECTATION_FAILED);
	}
	
	
	/**
	 * this method adds customer details in local db
	 * @param customer
	 * @param orderId
	 */
	private void insertCustomerDataInDb(Customer customer, String orderId) {
		Statement statement = null;
		try {
			statement = ConnectionHelper.sqliteConnection.createStatement();
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		String insertQuery;
		String tableName="USER";
		insertQuery = "REPLACE INTO "+tableName+" VALUES('"+customer.getCustomerName()+"','"+
				customer.getEmail()+"','"+orderId+"')";
		System.out.println(insertQuery);
		try {
			statement.executeUpdate(insertQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}


	}

	/**
	 * this is an overloaded method for adding just primary key in Order table
	 * @param razorPay
	 * @param amount
	 */
	private void addOrderDetailsInDb(RazorPay razorPay, String amount) {
		Statement statement = null;
		try {
			statement = ConnectionHelper.sqliteConnection.createStatement();
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		String insertQuery;
		String tableName="ORDER_DETAIL";
		insertQuery = "REPLACE INTO "+tableName+" (ORDER_ID) VALUES('"+razorPay.getRazorpayOrderId()+"')";
		System.out.println("Adding/Updating Order data\n");
		System.out.println(insertQuery);
		try {
			statement.executeUpdate(insertQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		
	}

	/**
	 * Response object of api
	 * @param razorPay
	 * @param statusCode
	 * @return razorpay order response
	 */
	private Response getResponse(RazorPay razorPay, int statusCode) {
		Response response = new Response();
		response.setStatusCode(statusCode);
		response.setRazorPay(razorPay);
		return response;
	}	
	
	/**
	 * this method is returns all order and payment related information
	 * @param orderId
	 * @param customer
	 * @return RazorPay
	 */
	private RazorPay getRazorPay(String orderId, Customer customer) {
		RazorPay razorPay = new RazorPay();
		razorPay.setApplicationFee(convertRupeeToPaise(customer.getAmount()));
		razorPay.setCustomerName(customer.getCustomerName());
		razorPay.setCustomerEmail(customer.getEmail());
		razorPay.setMerchantName("ChecqBook");
		razorPay.setPurchaseDescription("demo purchase");
		razorPay.setRazorpayOrderId(orderId);
		razorPay.setSecretKey(SECRET_ID);
		razorPay.setImageURL("/logo");
		razorPay.setTheme("#F37254");
		razorPay.setNotes("notes"+orderId);
		
		return razorPay;
	}
	
	/**
	 * this method creates an order on razorpay
	 * @param amount
	 * @return Order
	 * @throws RazorpayException
	 */
	private Order createRazorPayOrder(String amount) throws RazorpayException {
		
		JSONObject options = new JSONObject();
		options.put("amount", convertRupeeToPaise(amount));
		options.put("currency", "INR");
		options.put("receipt", "txn_123456");
		options.put("payment_capture", 1);  
		return client.Orders.create(options);
	}
	
	private String convertRupeeToPaise(String paise) {
		if(paise.equals("")) {
			return "0";
		}
		BigDecimal b = new BigDecimal(paise);
		BigDecimal value = b.multiply(new BigDecimal("100"));
		return value.setScale(0, RoundingMode.UP).toString();
		 
	}
	

}
