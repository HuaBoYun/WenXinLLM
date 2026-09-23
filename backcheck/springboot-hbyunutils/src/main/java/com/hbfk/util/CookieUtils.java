package com.hbfk.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * cookie工具类
 */
public class CookieUtils {

	 public static Cookie getCookie(HttpServletRequest request, String cookieName){
	        Cookie[] cookies = request.getCookies();
	        Cookie cookie = null;
	        try{
	            if (cookies != null && cookies.length > 0){
	                for (int i = 0; i < cookies.length; i++) {
	                    if (cookies[i].getName().equals(cookieName)){
	                        return cookies[i];
	                    }
	                }
	            }
	        }catch (Exception e){
	            e.printStackTrace();
	        }
	        return cookie;
	    }

	    public static String getCookieValue(HttpServletRequest request, String cookieName){
	        Cookie cookie = getCookie(request,cookieName);
	        if (cookie != null){
	            return cookie.getValue();
	        }else {
	            return null;
	        }
	    }

	    public static Cookie getCookie(Cookie[] cookies, String cookieName) {
	        if (null == cookies) {
	            return null;
	        } else {
	            for (Cookie cookie : cookies) {
	                if (cookie.getName().equals(cookieName)) {
	                    return cookie;
	                }
	            }
	            return null;
	        }
	    }
}
