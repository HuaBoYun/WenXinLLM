package com.management.accountant.util;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;


public class MessageSender {
	private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

	/**
    *
    * 发送邮件-邮件内容为文本格式
    * @param mailInfo
    * @return
    */
   public static boolean sendMail(MailInfo mailInfo) {
       try {
           Message mailMessage = generateBaseInfo(mailInfo);
           // 创建消息部分
           BodyPart messageBodyPart = new MimeBodyPart();
           messageBodyPart.setText(mailInfo.getMailContent());

           // 创建多重消息
           Multipart multipart = new MimeMultipart();

           // 设置文本消息部分
           multipart.addBodyPart(messageBodyPart);

           // 附件部分
           messageBodyPart = new MimeBodyPart();
           // 设置要发送附件的文件路径
           DataSource source = new FileDataSource(mailInfo.getFilename());
           messageBodyPart.setDataHandler(new DataHandler(source));

           // messageBodyPart.setFileName(filename);
           // 处理附件名称中文（附带文件路径）乱码问题
           messageBodyPart.setFileName(MimeUtility.encodeText(mailInfo.getFilename()));
           multipart.addBodyPart(messageBodyPart);

           // 发送完整消息
           mailMessage.setContent(multipart);
          // mailMessage.setText(mailInfo.getMailContent());// 设置邮件消息的主要内容
           Transport.send(mailMessage); // 发送邮件
           System.out.println("【 TEXT 邮件发送完毕，成功时间： " + System.currentTimeMillis()+ " 】");
           return true;
       } catch (MessagingException ex) {
           ex.printStackTrace();
       } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
       }
       return false;
   }

   /**
    * 邮件内容为html格式
    * @param mailInfo
    * @return
    */
   public static boolean sendHtmlMail(MailInfo mailInfo) throws Exception {
        Message mailMessage = generateBaseInfo(mailInfo);
       MimeMultipart mainPart = new MimeMultipart();// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
       MimeBodyPart html = new MimeBodyPart();// 创建一个包含HTML内容的MimeBodyPart
           html.setContent(mailInfo.getMailContent(), "text/html; charset=utf-8");// 设置HTML内容
           mainPart.addBodyPart(html);
           mailMessage.setContent(mainPart);// 将MiniMultipart对象设置为邮件内容
           DataSource source = null;
           if(mailInfo.getPath() != null){
        	   for (int i = 0; i < mailInfo.getPath().length; i++) {
        		   // 附件部分
                   html = new MimeBodyPart();
                   // 设置要发送附件的文件路径
                   source = new FileDataSource(mailInfo.getPath()[i]);
                   html.setDataHandler(new DataHandler(source));

                   // messageBodyPart.setFileName(filename);
                   // 处理附件名称中文（附带文件路径）乱码问题
                   html.setFileName(MimeUtility.encodeText(mailInfo.getPath()[i].substring(mailInfo.getPath()[i].indexOf("_")+1, mailInfo.getPath()[i].length())));
                   mainPart.addBodyPart(html);
        	   }
           }
           Transport.send(mailMessage);// 发送邮件
           System.out.println("【 HTML 邮件发送完毕，成功时间： " + System.currentTimeMillis() + " 】");
           System.out.println("send ok!");
           return true;
   }

   /**
    * 邮件信息安全转换
    * @param mailInfo
    * @return
    * @throws UnsupportedEncodingException
    * @throws MessagingException
    */

   public static Message generateBaseInfo(MailInfo mailInfo) throws UnsupportedEncodingException, MessagingException {
       // 判断是否需要身份认证
       MailAuthenticator authenticator = null;
       Properties pro = mailInfo.getProperties();
      // pro.getProperty()
       // 如果需要身份认证，则创建一个密码验证器
       if (mailInfo.isAuthValidate()) {
           authenticator = new MailAuthenticator(mailInfo.getUserName(),
                   mailInfo.getUserPassword());
       }

       // 根据邮件会话属性和密码验证器构造一个发送邮件的session
       Session sendMailSession = Session.getInstance(pro, authenticator);
       Message mailMessage = null;
       mailMessage = new MimeMessage(sendMailSession); // 根据session创建一个邮件消息
       Address from = new InternetAddress(mailInfo.getFromAddress(), mailInfo.getFromUserName()); // 创建邮件发送者地址
       mailMessage.setFrom(from); // 设置邮件消息的发送者
       if (mailInfo.getToAddress() != null && mailInfo.getToAddress().contains(",")) {
           mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailInfo.getToAddress()));// Message.RecipientType.TO属性表示接收者的类型为TO
       } else {
           Address to = new InternetAddress(mailInfo.getToAddress()); // 创建邮件的接收者地址，并设置到邮件消息中
           mailMessage.setRecipient(Message.RecipientType.TO, to);// Message.RecipientType.TO属性表示接收者的类型为TO
       }
       if (StringUtils.isNotBlank(mailInfo.getCcAddress())) {
           if (mailInfo.getCcAddress().contains(",")) {
               mailMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mailInfo.getCcAddress())); // Message.RecipientType.CC属性表示接收者的类型为CC
           } else {
               Address cc = new InternetAddress(mailInfo.getCcAddress()); // 创建邮件的抄送者地址，并设置到邮件消息中
               mailMessage.setRecipient(Message.RecipientType.CC, cc); // Message.RecipientType.CC属性表示接收者的类型为CC
           }
       }
       mailMessage.setSubject(mailInfo.getMailSubject());// 设置邮件消息的主题
       mailMessage.setSentDate(new Date());// 设置邮件消息发送的时间
       return mailMessage;
   }


   /**
    * 判断是公司邮箱还是企业邮箱
    */
   public static Integer judgeEmailType(String email){
	   if(email.indexOf("qq") != -1 || email.indexOf("163") != -1 ){
		   return 0;//私人邮箱
	   }else{
		   return 1;//企业公众邮箱
	   }
   }
}
