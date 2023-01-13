package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.service;

import javax.servlet.http.Cookie;

import org.springframework.stereotype.Service;

@Service
public class CookieService {

	public Cookie setAccessCookie(String accessToken){
		Cookie cookie = new Cookie("accessToken",accessToken);
		cookie.setMaxAge(2*60*60);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		return cookie;
	}

	public Cookie setRefreshCookie(String refreshToken){
		Cookie cookie = new Cookie("refreshToken",refreshToken);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(2*24*60*60);
		cookie.setPath("/");
		return cookie;
	}
}
