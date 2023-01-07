package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt;

import static io.jsonwebtoken.Jwts.*;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.dto.TokenInfoDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class JwtTokenProvider {

	@Value("${jwtSecretKey}")
	private String secretKey;
	private final Long tokenValidTime = 2 * 60 * 60 * 1000L;
	private final UserDetailsService userDetailsService;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public TokenInfoDto createToken(String userEmail, List<String> roles) {
		Claims claims = claims().setSubject(userEmail);
		claims.put("roles", roles);
		Date now = new Date();
		log.info("secretKey = " + secretKey);

		String accessToken = Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + tokenValidTime))
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();

		String refreshToken = Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + tokenValidTime * 2 * 12))
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
		return TokenInfoDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.userEmail(userEmail)
			.grantType("Bearer")
			.build();
	}

	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String getUserPk(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public String resolveToken(HttpServletRequest request) {
		return request.getHeader("Authorization");
	}

	public boolean validateAccessToken(String jwtToken) {
		try {
			Jws<Claims> claimsJws = parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
			return !claimsJws.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}

	public String recreationAccessToken(String userEmail, Object roles) {
		Claims claims = Jwts.claims().setSubject(userEmail);
		claims.put("roles", roles);
		Date now = new Date();

		String accessToken = Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + tokenValidTime))
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();

		return accessToken;
	}

	public String validateRefreshToken(RefreshToken refreshTokenObj){
		String refreshToken = refreshTokenObj.getRefreshToken();
		try{
			Jws<Claims> claims = parser().setSigningKey(secretKey).parseClaimsJws(refreshToken);

			if(!claims.getBody().getExpiration().before(new Date())){
				return recreationAccessToken(claims.getBody().get("sub").toString(),claims.getBody().get("roles"));
			}
		}catch (Exception e){
			return null;
		}
		return null;
	}

	public String getRoles(String jwtToken){
		try{
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
			String roles = claimsJws.getBody().get("roles").toString();
			return roles.substring(1,roles.length()-1);
		}catch(Exception e){
			return null;
		}
	}
}
