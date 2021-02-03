package com.sample.razor.pay.model;

/**
 * 
 * @author Pradeep
 *
 */
public class Response {
	
	private int statusCode;
	private RazorPay razorPay;
	private String errorMsgs;
	
	public Response() {
		this.errorMsgs="";
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public RazorPay getRazorPay() {
		return razorPay;
	}

	public void setRazorPay(RazorPay razorPay) {
		this.razorPay = razorPay;
	}
	public String getErrorMsgs() {
		return errorMsgs;
	}
	public void setErrorMsgs(String errorMsgs) {
		this.errorMsgs = errorMsgs;
	}

	
	
}
