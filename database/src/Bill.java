
public class Bill {
	
	protected int billId;
	protected int orderId;
	protected double totalPrice;
	protected String status;
	protected String note;
	
	
	public Bill(int billId, int orderId, double totalPrice, String status, String note) {
		super();
		this.billId = billId;
		this.orderId = orderId;
		this.totalPrice = totalPrice;
		this.status = status;
		this.note = note;
	}
	
	
	
	public int getBillId() {
		return billId;
	}
	public void setBillId(int billId) {
		this.billId = billId;
	}
	public int getorderId() {
		return orderId;
	}
	public void setorderId(int orderId) {
		this.orderId = orderId;
	}
	
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
	
	

}
