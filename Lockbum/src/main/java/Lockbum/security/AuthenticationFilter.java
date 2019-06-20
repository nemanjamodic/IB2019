package Lockbum.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import Lockbum.security.model.AuthenticationToken;
import Lockbum.service.UserService;

public class AuthenticationFilter extends OncePerRequestFilter {

	private TokenHelper tokenHelper;

	private UserService userService;

	public AuthenticationFilter(TokenHelper tokenHelper, UserService userService) {
		super();
		this.tokenHelper = tokenHelper;
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String username;
		String authToken = tokenHelper.getToken(request);

		if (authToken != null) {
			// uzmi username iz tokena
			username = tokenHelper.getUsernameFromToken(authToken);
			if (username != null) {
				// uzmi user-a na osnovu username-a
				UserDetails userDetails = userService.loadUserByUsername(username);
				// proveri da li je prosledjeni token validan
				if (tokenHelper.validateToken(authToken, userDetails)) {
					// kreiraj autentifikaciju
					AuthenticationToken authentication = new AuthenticationToken(userDetails);
					authentication.setToken(authToken);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		filterChain.doFilter(request, response);

	}

}
