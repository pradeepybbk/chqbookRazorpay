package com.checqbook.dto;

/**
 * this data transfer object contains order details
 * @author Pradeep
 *
 */
public class OrderDetailDTO {

	private String orderId;
//	private String userId;
	private String marchent;
	private String amountDue;
	private String totalAmount;
	private String createdAt;
	private String status;
	private String attempts;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
//	public String getUserId() {
//		return userId;
//	}
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}
	public String getMarchent() {
		return marchent;
	}
	public void setMarchent(String marchent) {
		this.marchent = marchent;
	}
	public String getAmountDue() {
		return amountDue;
	}
	public void setAmountDue(String amountDue) {
		this.amountDue = amountDue;
	}
	
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
//	public String getRcptId() {
//		return rcptId;
//	}
//	public void setRcptId(String rcptId) {
//		this.rcptId = rcptId;
//	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAttempts() {
		return attempts;
	}
	public void setAttempts(String attempts) {
		this.attempts = attempts;
	}

	
}
