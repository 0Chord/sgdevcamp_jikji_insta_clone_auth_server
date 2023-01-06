package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.JwtTokenProvider;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.RefreshToken;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.dto.TokenInfoDto;
import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.repository.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {

	private final JwtTokenProvider jwtTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	public JwtService(JwtTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepository) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.refreshTokenRepository = refreshTokenRepository;
	}

	@Transactional
	public void login(TokenInfoDto tokenDto){
		RefreshToken refreshToken = RefreshToken.builder().userEmail(tokenDto.getUserEmail()).refreshToken(
			tokenDto.getRefreshToken()).build();
		if(refreshTokenRepository.existsByUserEmail(refreshToken.getUserEmail())){
			log.info("refresh token 검사");
			refreshTokenRepository.deleteByUserEmail(refreshToken.getUserEmail());
		}
		refreshTokenRepository.save(refreshToken);
	}

	public Optional<RefreshToken> getRefreshToken(String refreshToken){
		return refreshTokenRepository.findByRefreshToken(refreshToken);
	}

	public Map<String, String> validatedRefreshToken(String refreshToken){
		RefreshToken refreshToken1 = getRefreshToken(refreshToken).get();
		String createdAccessToken = jwtTokenProvider.validateRefreshToken(refreshToken1);

		return createdRefreshJson(createdAccessToken);
	}

	public String getAdmin(String accessToken){
		return jwtTokenProvider.getRoles(accessToken);
	}
	public Map<String, String> createdRefreshJson(String createdAccessToken){
		Map<String, String> map = new HashMap<>();
		if(createdAccessToken == null){
			map.put("errorType","Forbidden");
			map.put("status","402");
			map.put("message","RefreshToken이 만료되었습니다. 로그인을 다시 해주시길 바랍니다.");

			return map;
		}
		map.put("status","200");
		map.put("message","Refresh Token을 통한 AccessToken 생성이 완료되었습니다,");
		map.put("accessToken",createdAccessToken);

		return map;
	}
}
