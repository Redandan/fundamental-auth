package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import com.h2database.Skein512Small;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private static final List<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList("ROLE_USER");
	
	@Override
	public Authentication authenticate(Authentication authentication) {
		

		String origName = authentication.getName();
		String origShadow = authentication.getCredentials().toString();
		if (test(origName, origShadow)) {
			return new UsernamePasswordAuthenticationToken(origName, origShadow, USER_ROLES);
		} else {
			return null;
		}
	}
/*
2bb276be21e5b167
c4576822f07dcc5c992d8f3307e3cca64dd17ea666fb232e5f4e24f0c69696d882fded9f36148ef80084e0795cd4444d0ed4028d8686601363fe6e2afb539c23

 * */
	public boolean test(String origName, String origShadow) {
		try {
			if (origShadow.equals(Skein512Small.hash(origName))) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
