package lk.parinda.springsecu.model;

public class AuthonticationResponse {

	private final String jwt;
	private final String refreshJwt;
	
	public AuthonticationResponse(String jwt,String refreshJwt) {
		this.refreshJwt = refreshJwt;
		this.jwt = jwt;
	}
	
	public String getJwt() {
		return jwt;
	}

	public String getRefreshJwt() {
		return refreshJwt;
	}
}
