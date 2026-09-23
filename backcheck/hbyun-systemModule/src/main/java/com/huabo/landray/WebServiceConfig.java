package com.huabo.landray;

import java.util.ResourceBundle;

/**
 * 解析配置文件
 *
 */
public class WebServiceConfig {

	public static ResourceBundle rb = ResourceBundle.getBundle("setting/landray");

	private static WebServiceConfig cfg = new WebServiceConfig();

	// Web服务的URL
	private String address;

	// Web服务接口
	private Class serviceClass;

	// Web服务标识
	private String serviceBean;

	// 用户
	private String user;

	// 密码
	private String password;

	private WebServiceConfig() {
		loadCfg();
	}

	public static WebServiceConfig getInstance() {
		return cfg;
	}

	/**
	 * 解析配置文件
	 */
	private void loadCfg() {
		try {
			this.address = rb.getString("address");
			String serviceClassName = rb.getString("service_class");
			this.serviceClass = Class.forName(serviceClassName);
			this.serviceBean = rb.getString("service_bean");
			this.user = rb.getString("user");
			this.password = rb.getString("password");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Class getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(Class serviceClass) {
		this.serviceClass = serviceClass;
	}

	public String getServiceBean() {
		return serviceBean;
	}

	public void setServiceBean(String serviceBean) {
		this.serviceBean = serviceBean;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
