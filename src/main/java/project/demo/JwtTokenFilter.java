package project.demo;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;
import project.demo.service.JwtTokenService;

@Component
@Slf4j
// loc cac request gui len
public class JwtTokenFilter extends OncePerRequestFilter{
	@Autowired
	JwtTokenService jwtTokenService;

	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//doc header authorize
		String bearerToken = request.getHeader("Authorization");
		log.info(bearerToken);
		
		if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
			String token = bearerToken.substring(7);
			String username = jwtTokenService.getUsername(token);
			if (username != null) {
				// token valid, create a authentication
				UserDetails userDetails = 
						userDetailsService.loadUserByUsername(username);
				Authentication authentication
					= new UsernamePasswordAuthenticationToken(userDetails, 
							"",userDetails.getAuthorities());
				// gia lap security
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		filterChain.doFilter(request, response);
	}
	
	
}
