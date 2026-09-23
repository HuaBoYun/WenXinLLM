package com.huabo.contract.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public MailInfo(String serverHost, String user, String password, String fromAddress, String title, String content, List<String> receiver, List<String> ccList, String filename) {
        this.mailServerHost = serverHost;
        this.userName = user;
        this.userPassword = password;
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
//    public String getMailServerHost() {
//    	if(this.mailServerHost == null){
//	    	if(MessageSender.judgeEmailType(this.userName) == 0 ){
//	    		this.mailServerHost = "smtp.qq.com";
//			}else if(MessageSender.judgeEmailType(this.userName) == 1){
//				this.mailServerHost = "smtp.exmail.qq.com";
//			}
//    	}
//        return mailServerHost;
//    }
 
    public boolean isAuthValidate() {
        return authValidate;
    }
    public void setAuthValidate(boolean authValidate) {
        this.authValidate = authValidate;
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
