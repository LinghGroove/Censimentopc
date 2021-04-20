package it.contrader.hardwaretracking.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import static java.util.Arrays.stream;

import it.contrader.hardwaretracking.entity.UserPrincipal;

@Component
public class JWTTokenProvider {
	
	private String secret = "root";
	
	public String generateJwtToken(UserPrincipal user) {
	
	String[] claims = getClaimsFromUser(user);
		
	return JWT.create().withIssuer("Contrader")
				.withAudience("Hardware Tracking")
				.withIssuedAt(new Date()).withSubject(user.getUsername())
				.withArrayClaim("authorities", claims)
				.withExpiresAt(new Date(System.currentTimeMillis() + 86_400_000)) 
				.sign(Algorithm.HMAC512(secret.getBytes())); 
		}
	
	public Authentication getAuthentication(String username, List<GrantedAuthority> authorities,
			HttpServletRequest request) {

		//La classe Authentication rappresenta un token

		UsernamePasswordAuthenticationToken userPasswordAuthTok = new UsernamePasswordAuthenticationToken(username, 
							null, authorities);

		userPasswordAuthTok.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		return userPasswordAuthTok;

	}
	
	
	
	public boolean isTokenValid(String username, String token) {
		JWTVerifier verifier = getJWTVerifier();
		return StringUtils.isNotEmpty(username) && !isTokenExpired(verifier, token);
	}
	
	public boolean isTokenExpired(JWTVerifier verifier, String token) {
		Date expiration = verifier.verify(token).getExpiresAt();
		return expiration.before(new Date(System.currentTimeMillis() - 86_400_000));
	}
	
	public String getSubject(String token) {
		JWTVerifier verifier = getJWTVerifier();
		return verifier.verify(token).getSubject();
	}
	
	private JWTVerifier getJWTVerifier() {
		JWTVerifier verifier;
		try {
			Algorithm algorithm = Algorithm.HMAC512(secret);
			verifier = JWT.require(algorithm).withIssuer("Contrader").build();
		} catch(JWTVerificationException e) {
			throw new JWTVerificationException("Token cannot be verified");
		}
		
		return verifier;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//***************************************************************************
	
	//NESSUNA UTITLITA' PRATICA; IL VETTORE DELLE AUTORIZZAZIONI è VUOTO IN QUANTO NON NECCESARIE,
	//L'UNICA AUTORIZZAZIONE RICHIESTA è IL LOGIN. L'UNICA UTILITà è QUELLA DI FAR GIRARE 
	//CORRETTAMENTE IL TOKEN
	
	public List<GrantedAuthority> getAuthorities(String token){
		
		String[] claims = getClaimsFromToken(token);
		
		return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList()); 
	
}
	
	private String[] getClaimsFromToken(String token) {
		
		JWTVerifier verifier = getJWTVerifier();
		return verifier.verify(token).getClaim("authorities").asArray(String.class);
		
	}
	
	private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
		//granted = concesso
		List<String> authorities = new ArrayList<>();
		
		for(GrantedAuthority grandAuth: userPrincipal.getAuthorities())
			authorities.add(grandAuth.getAuthority());
		
		return authorities.toArray(new String[0]);
		
	}
	
	//****************************************************************************
	
	
	
}
