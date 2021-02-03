package com.checqbook.utils;

import com.sample.razor.pay.model.Customer;
import com.sample.razor.pay.model.RazorPay;
import com.sample.razor.pay.model.Response;

public class Validator {

	public Response validateCustomer(Customer customer){
		Response response = new Response();
		if(customer.getCustomerName()==null || customer.getCustomerName().length()==0) {
			response.setErrorMsgs("Please enter your name, don't be afraid :)");
		}
		
		/*if(customer.getPhoneNumber()==null || customer.getPhoneNumber()=="") {
			response.setErrorMsgs("Phone number is invalid");
		}
*/		
		if(customer.getEmail()==null || customer.getEmail().length()==0) {
			response.setErrorMsgs("Please enter your EmailId");
		}
		
		if(customer.getAmount()==null || Double.valueOf(customer.getAmount())<0) {
			response.setErrorMsgs("Please enter some amount to go ahead");
		}
		
		return response;
	}
}
