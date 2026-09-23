package com.global.treasurer.util;

// import lombok.AllArgsConstructor; // 已移除,使用手动编写的getter/setter
// import lombok.Data; // 已移除,使用手动编写的getter/setter
// import lombok.NoArgsConstructor; // 已移除,使用手动编写的getter/setter

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

// @Data // 已移除,使用手动编写的getter/setter
// @NoArgsConstructor // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除,使用手动编写的getter/setter
public class MailInfo {
	private String mailServerHost; // 发送邮件的服务器的IP
    private String mailServerPort = "465"; // 发送邮件的服务器端口
    private String userName; // 登陆邮件发送服务器的用户名
    private String userPassword; // 登陆邮件发送服务器的密码
    private String fromAddress; // 邮件发送者的地址
    private String toAddress; // 邮件接收者的地址管控
    private String ccAddress; // 邮件抄送者的地址
    private String fromUserName = "系统管理员"; // 邮件发送者的名称，显示在他人邮件的发件人
    private String mailSubject; // 邮件主题
    private String mailContent;// 邮件的文本内容
    private String filename;//文件路径
    private boolean authValidate = true; // 是否需要身份验证
    private Properties properties; // 邮件会话属性
    private BigDecimal staffId;
    private String[] path;

    // 无参构造函数
    public MailInfo() {
    }

    // 全参构造函数
    public MailInfo(String serverHost, String user, String password, String fromAddress, String title, String content, List<String> receiver, List<String> ccList, String filename) {
        this.mailServerHost = serverHost;
        this.userName = user;
        this.userPassword = serverHost;
        this.fromAddress = fromAddress;
        this.toAddress = listToStringFormat(receiver);
        this.ccAddress = ccList == null ? "" : listToStringFormat(ccList);
        this.mailSubject = title;
        this.mailContent = content;
        this.filename = filename;
    }


    private synchronized String listToStringFormat(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                stringBuilder.append(list.get(i));
            } else {
                stringBuilder.append(list.get(i)).append(",");
            }
        }
        return stringBuilder.toString();
    }

    public String getMailServerHost() {
    	if(this.mailServerHost == null){
	    	if(MessageSender.judgeEmailType(this.userName) == 0 ){
	    		this.mailServerHost = "smtp.qq.com";
			}else if(MessageSender.judgeEmailType(this.userName) == 1){
				this.mailServerHost = "smtp.exmail.qq.com";
			}
    	}
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isAuthValidate() {
        return authValidate;
    }

    public void setAuthValidate(boolean authValidate) {
        this.authValidate = authValidate;
    }

    public BigDecimal getStaffId() {
        return staffId;
    }

    public void setStaffId(BigDecimal staffId) {
        this.staffId = staffId;
    }

    public String[] getPath() {
        return path;
    }

    public void setPath(String[] path) {
        this.path = path;
    }

    public Properties getProperties() {
        Properties p = new Properties();
        p.put("mail.smtp.host", this.getMailServerHost());
        p.put("mail.smtp.port", this.mailServerPort);
        p.put("mail.smtp.auth", authValidate ? "true" : "false");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.ssl.enable", "true");
        return p;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
