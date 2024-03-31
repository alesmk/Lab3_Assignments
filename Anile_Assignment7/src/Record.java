
public class Record {
	private String date;
	private String reason;
	public Record(String date, String reason) {
		super();
		this.date = date;
		this.reason = reason;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}
