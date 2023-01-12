package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.jwt.service.JwtService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/accessToken")
public class AccessTokenController {
	JwtService jwtService;

	public AccessTokenController(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@PostMapping("/validateAccessToken")
	public ResponseEntity<?> validateAccessToken(@RequestBody MultiValueMap<String, String> body){
		String accessToken = body.get("accessToken").get(0);
		Boolean validatedAccessToken = jwtService.validatedAccessToken(accessToken);
		if(validatedAccessToken){
			return new ResponseEntity<>(true, HttpStatus.OK);
		}else{
			return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/getRoles")
	public ResponseEntity<?> getRoles(@RequestBody MultiValueMap<String, String> body){
		String accessToken = body.get("accessToken").get(0);
		String roles = jwtService.getRoles(accessToken);
		return new ResponseEntity<>(roles,HttpStatus.OK);
	}
}
