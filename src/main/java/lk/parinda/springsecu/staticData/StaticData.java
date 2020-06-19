package lk.parinda.springsecu.staticData;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@ConfigurationProperties(prefix = "application.staticdata")
@Service
public class StaticData {

	String accessTokenHead;
	String refreshTokenHead;
	public String getAccessTokenHead() {
		return accessTokenHead;
	}
	public void setAccessTokenHead(String accessTokenHead) {
		this.accessTokenHead = accessTokenHead;
	}
	public String getRefreshTokenHead() {
		return refreshTokenHead;
	}
	public void setRefreshTokenHead(String refreshTokenHead) {
		this.refreshTokenHead = refreshTokenHead;
	}
}
