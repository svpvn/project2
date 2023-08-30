package project.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,
		prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtTokenFilter tokenFilter;
	
	@Autowired
	public void config(AuthenticationManagerBuilder auth) throws Exception{
		//check pw, quyen truy cap
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(new BCryptPasswordEncoder());
	}
	// md5, bcrypt
	@Bean  // check xac thuc
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration) 
					throws Exception {
		
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public SecurityFilterChain config(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests()
			.antMatchers("/admin/**", "/cache/**" )
//			.hasAnyRole("ROLE_ADMIN")
			.hasAnyAuthority("ADMIN", "SYSADMIN")
			.antMatchers("/member/**")
			.authenticated()
			.anyRequest().permitAll()
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
			.and().httpBasic()
			.and().csrf().disable();
		
		//apply filter
		http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
}
