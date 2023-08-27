package logs;

public class AuditLog {
	
	private int operationId;
	private String userType;
	private int id;
	private String dateAndTime;
	private String operationPerformed;
	public int getOperationId() {
		return operationId;
	}
	public void setOperationId(int operationId) {
		this.operationId = operationId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDateAndTime() {
		return dateAndTime;
	}
	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}
	public String getOperationPerformed() {
		return operationPerformed;
	}
	public void setOperationPerformed(String operationPerformed) {
		this.operationPerformed = operationPerformed;
	}

}
