import java.util.ArrayList;

public class ContoCorrente {
	private String owner;
	private ArrayList<Record> records;
	public ContoCorrente(String owner, ArrayList<Record> records) {
		super();
		this.owner = owner;
		this.records = records;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public ArrayList<Record> getRecords() {
		return records;
	}
	public void setRecords(ArrayList<Record> records) {
		this.records = records;
	}
	
	
}
	
