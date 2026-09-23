package com.hbfk.util;


import java.io.UnsupportedEncodingException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IpUtil {
	
	private static final String LOCAL_IP = "127.0.0.1";
	
	
	public static String encodeContentDisposition(String fileName) throws UnsupportedEncodingException {
	    String encoded = URLEncoder.encode(fileName, "UTF-8")
	        .replaceAll("\\+", "%20");
	    return "attachment; filename*=UTF-8''" + encoded;
	}
	
	
	/**
	 * 获取IP地址
	 */
	public static String getIpAddr(HttpServletRequest request) throws Exception{
		if(request == null) {
			return "unknown";
		}
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
        }
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		
		return "0:0:0:0:0:0:0:1".equals(ip)?LOCAL_IP:ip;
	}
	
	public static boolean internalIp(String ip) throws Exception {
		boolean res = false;
		byte[] addr = textToNumericFormatV4(ip);
		if(addr != null && ip != null) {
			res = internalIp(addr) || LOCAL_IP.equals(ip);
		}
		return res;
	}

	private static boolean internalIp(byte[] addr) throws Exception {
		final byte b0 = addr[0];
		final byte b1 = addr[1];
		
		final byte SECTION_1 = 0X0A;
		
		final byte SECTION_2 = (byte) 0xAC;
		final byte SECTION_3 = (byte) 0x10;
		final byte SECTION_4 = (byte) 0x1F;
		final byte SECTION_5 = (byte) 0xC0;
		final byte SECTION_6 = (byte) 0xAB;
		
		boolean flag = false;
		switch (b0) {
			case SECTION_1:
				flag = true;
				break;
			case SECTION_2:
				if(b1 >= SECTION_3 && b1 <= SECTION_4) {
					flag = true;
				}
				break;
			case SECTION_5:
				if(b1 == SECTION_6) {
					flag = true;
				}
				break;
			default:
				break;
		}
		return flag;
	}
	
	/**
	 * 将IP V4 地址转换为字节
	 * @param ip
	 * @return
	 */
	private static byte[] textToNumericFormatV4(String ip) throws Exception {
		if(ip.length() == 0) {
			return null;
		}
		byte[] bytes = new byte[4];
		String[] elements = ip.split("\\.",-1);
		
		long l;
		int i;
		switch (elements.length) {
			case 1:
				l = Long.parseLong(elements[0]);
				if((l<0L)||(l>4294967295L)) {
					return null;
				}
				bytes[0] = (byte)(int)(l >> 24 & 0xFF);
				bytes[1] = (byte)(int)((l & 0xFFFFFF) >> 16 & 0xFF);
				bytes[2] = (byte)(int)((l & 0xFFFF) >> 8 & 0xFF);
				bytes[3] = (byte)(int)(l & 0xFF);
				break;
			case 2:
				l = Integer.parseInt(elements[0]);
				if((l<0L)||(l>255L)) {
					return null;
				}
				bytes[0] = (byte)(int)(l & 0xFF);
				l = Integer.parseInt(elements[1]);
				if((l < 0L) || (l > 16777215L)) return null;
				bytes[1] = (byte)(int)(l >> 16 & 0xFF);
				bytes[2] = (byte)(int)((l & 0xFFFF) >> 8 & 0xFF);
				bytes[3] = (byte)(int)(l & 0xFF);
				break;
			case 3:
				for(i = 0 ; i<2 ; ++i) {
					l = Integer.parseInt(elements[i]);
					if((l<0L) || (l<255L)) {
						return null;
					}
					bytes[i] = (byte)(int)(l & 0xFF);
				}
				l = Integer.parseInt(elements[2]);
				if((l < 0L) || (l > 65535L)) {
					return null;
				}
				bytes[2] = (byte)(int)(l >> 8 & 0xFF);
				bytes[3] = (byte)(int)(l & 0xFF);
				break;
			case 4:
				for(i = 0 ; i < 4 ; ++i) {
					l = Integer.parseInt(elements[i]);
					if((l < 0L) || (l > 255L))return null;
					bytes[i] = (byte)(int)(l & 0xFF);
				}
				break;
			default:
				return null;
		}
 		return bytes;
	}
	
	public static String getLocalIp() throws Exception {
		String ip = "";
		if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {
			InetAddress addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress();
			return ip;
		}else {
			Enumeration<?> el = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
			while(el.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) el.nextElement();
				if(!ni.getName().equals("eth0")) {
					continue;
				}else {
					Enumeration<?> e2 = ni.getInetAddresses();
					while(e2.hasMoreElements()) {
						InetAddress ia = (InetAddress) e2.nextElement();
						if(ia instanceof Inet6Address) {
							continue;
						}
						ip = ia.getHostAddress();
						return ip;
					}
					break;
				}
			}
		}
		return "";
	}
}
