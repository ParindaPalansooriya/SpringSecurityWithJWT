package lk.parinda.springsecu.model;

public class ErrorRes {
	
	private final String message;
	private final int status;
	
	public ErrorRes(String message,int status) {
		this.status = status;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public int getStatus() {
		return status;
	}
}
