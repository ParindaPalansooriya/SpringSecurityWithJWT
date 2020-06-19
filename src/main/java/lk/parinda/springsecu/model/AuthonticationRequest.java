package lk.parinda.springsecu.model;

public class AuthonticationRequest {
	
	private String userName;
	private String passWord;
	
	public AuthonticationRequest() {}
	public AuthonticationRequest(String userName, String passWord) {
		this.userName = userName;
		this.passWord = passWord;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	

}
