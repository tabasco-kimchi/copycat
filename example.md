package kr.co.openeg.lab.login.model;

// 로그인 (시도) 정보를 담는 객체
public class LoginHistory {
	private String userId;		// 로그인 아이디
	private int retry;			// 로그인 시도회수
	private long lastFailedLogin;	// 마지막으로 로그인 실패 시간
	private long lastSuccessedLogin;	// 마지막으로 로그인 성공 시간
	private String clientIP;		// 로그인 시도한 IP
	private String sessionID;		// 세션ID
		
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getRetry() {
		return retry;
	}
	public void setRetry(int retry) {
		this.retry = retry;
	}
	public long getLastFailedLogin() {
		return lastFailedLogin;
	}
	public void setLastFailedLogin(long lastFailedLogin) {
		this.lastFailedLogin = lastFailedLogin;
	}
	public long getLastSuccessedLogin() {
		return lastSuccessedLogin;
	}
	public void setLastSuccessedLogin(long lastSuccessedLogin) {
		this.lastSuccessedLogin = lastSuccessedLogin;
	}
	public String getClientIP() {
		return clientIP;
	}
	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}	
}
