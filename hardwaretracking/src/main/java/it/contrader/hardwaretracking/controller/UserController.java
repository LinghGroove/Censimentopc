package it.contrader.hardwaretracking.controller;

import it.contrader.hardwaretracking.entity.User;
import it.contrader.hardwaretracking.entity.UserPrincipal;
import it.contrader.hardwaretracking.security.JWTTokenProvider;
import it.contrader.hardwaretracking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	
	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final JWTTokenProvider jwtTokenProvider;
		
	@Autowired
	public UserController(UserService userService, AuthenticationManager authenticationManager,
						  JWTTokenProvider jwtTokenProvider) {
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
	}
	
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody User user) throws ExecutionException {
		
		authenticate(user.getUsername(), user.getPassword());
		User loginUser = userService.findUserByUsername(user.getUsername());
		HttpHeaders jwtHeader = getJwtHeader(new UserPrincipal(loginUser));
		
				return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);

	}
	
	@PostMapping("/register")
	public ResponseEntity<User> register(@Valid @RequestBody User user) throws MessagingException {
				User newUser = userService.register(user);
				System.out.println("Sono al controller");
				return new ResponseEntity<>(newUser, HttpStatus.OK);
		}

	@PostMapping("/update")
	public ResponseEntity<User> update(@RequestBody User user){
			return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
		}
	
	
	//--------------------------------------------
	
		private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Jwt-Token", jwtTokenProvider.generateJwtToken(userPrincipal));
		
			return headers;
		}

		private void authenticate(String username, String password) {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		}
		

}
