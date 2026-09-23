package com.hbfk.util;

import java.util.Date;
import java.util.Map;

import org.springframework.util.Base64Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtils {
	
	private static final String DEFAULTKEY = "hbyun";
	
	private static final long FAILURETIME = 3600000;
	
	/**
	 * 获取认证token
	 */
	public static String createJwt(String id,String subject,Map<String,Object> map) {
		long now = System.currentTimeMillis();
		long exp = now + FAILURETIME;
		String encodeId = Base64Utils.encodeToString(id.getBytes());
		JwtBuilder jwtBuilder = Jwts.builder().setId(id)
				.setSubject(subject)
				.setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS256, DEFAULTKEY);
		
		if(map != null) {
			for(Map.Entry<String,Object> entry: map.entrySet()) {
				jwtBuilder.claim(entry.getKey(), entry.getValue());
			}
		}
		
		String token = jwtBuilder.compact();
		return token;
	}
	
	/**
	 * 解析token
	 */
	public static Claims parseJwt(String token) {
		Claims claims = Jwts.parser().setSigningKey(DEFAULTKEY).parseClaimsJws(token).getBody();
		return claims;
	}
	
}
