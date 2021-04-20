package it.contrader.hardwaretracking.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


//Questa classe serve a stoppare gli utenti che non hanno effettuato il login o hanno un token sbagliato

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
	
	/*OncePerRequestFilter garantisce che all'interno di una Request può accadere ogni cosa una sola volta */
	
	private JWTTokenProvider jwtTokenProvider;
	
	public JwtAuthorizationFilter(JWTTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if(request.getMethod().equalsIgnoreCase("OPTIONS")) {
			response.setStatus(HttpStatus.OK.value());
		} else {
			String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
				//in questo caso il token è sbagliato
				filterChain.doFilter(request, response);
				return;
			}
			String token = authorizationHeader.substring(7); //Lunghezza di "Bearer "
			String username = jwtTokenProvider.getSubject(token);
			
			if(jwtTokenProvider.isTokenValid(username, token) && 
					SecurityContextHolder.getContext().getAuthentication() == null) {
				List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token);
				Authentication authentication = jwtTokenProvider.getAuthentication(username, authorities, request);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}else {
				SecurityContextHolder.clearContext();
			}
			
		}
		
		filterChain.doFilter(request,  response);

	}

}
