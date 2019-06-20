package Lockbum.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import Lockbum.security.AuthenticationFilter;
import Lockbum.security.TokenHelper;
import Lockbum.security.model.AuthenticationEntryLocation;
import Lockbum.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenHelper tokenHelper;
	
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		try {			
			auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.POST, "/login");
		web.ignoring().antMatchers(HttpMethod.POST, "/registration");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Komunikacija je stateless
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// Dopusti svim korisnicima da pristupe linku za autentifikaciju
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/login").permitAll();
		// Zabrani pristup ostatku aplikacije ukoliko korisnik nije autentifikovan
        http.authorizeRequests().anyRequest().authenticated();
		// Za neautorizovane zahteve posalji 401 gresku
        http.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryLocation());
        // presretni svaki zahtev filterom
        http.addFilterBefore(new AuthenticationFilter(tokenHelper, userService), BasicAuthenticationFilter.class);
        // enable cors
        http.cors();

		http.csrf().disable();
	}

}
