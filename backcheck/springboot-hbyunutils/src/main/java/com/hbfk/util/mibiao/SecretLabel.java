package com.hbfk.util.mibiao;


import com.eetrust.label.LabelOperator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component("SecretLabel")
public class SecretLabel {
    @Resource
    private LabelOperator labelOperator;

    public Integer test(MultipartFile file) {
        //判断是否是 密标文件
        Integer labelledFile = 1;
        try {
            labelledFile = labelOperator.isLabelledFile(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("labelledFile");
        return labelledFile;
    }

    public Integer test2(String filePath) {
        //判断是否是 密标文件
        Integer labelledFile = 1;
        try {
            labelledFile = labelOperator.isLabelledFile(filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("labelledFile："+labelledFile);
        return labelledFile;
    }
    
    public Integer testyz(InputStream decryptedStream) {
        //判断是否是 密标文件
        Integer labelledFile = 1;
        try {
            labelledFile = labelOperator.isLabelledFile(decryptedStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("labelledFile：" + labelledFile);
        return labelledFile;
    }

    public String secrectLabelInfo(MultipartFile file) {
        //获取文件密标密级信息
        int fileLevel = 10;
        try {
            fileLevel = labelOperator.getFileLevel(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("附加密级等级："+fileLevel);
        String AttachmentLevel = "";
        if (fileLevel == 5){
            AttachmentLevel = "公开";
        }else if (fileLevel == 4){
            AttachmentLevel = "内部";
        }else if (fileLevel == 3){
            AttachmentLevel = "秘密";
        }else if(fileLevel == 2){
            AttachmentLevel = "机密";
        }else if(fileLevel == 41){
            AttachmentLevel = "核心商密";
        }else if(fileLevel == 42){
            AttachmentLevel = "普通商密";
        }else if(fileLevel == 43){
            AttachmentLevel = "敏感信息";
        }else{
            AttachmentLevel = "非密";
        }
        System.out.println("AttachmentLevel");
    	return AttachmentLevel;
    }
    
    
    public String secrectLabelInfoz(InputStream decryptedStream) {
        //获取文件密标密级信息
        int fileLevel = 10;
        try {
            fileLevel = labelOperator.getFileLevel(decryptedStream);
            System.out.println("文件流-附件密级等级为："+fileLevel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String AttachmentLevel = "";
        if (fileLevel == 5){
            AttachmentLevel = "公开";
        }else if (fileLevel == 4){
            AttachmentLevel = "内部";
        }else if (fileLevel == 3){
            AttachmentLevel = "秘密";
        }else if(fileLevel == 2){
            AttachmentLevel = "机密";
        }else if(fileLevel == 41){
            AttachmentLevel = "商密";
        }else if(fileLevel == 42){
            AttachmentLevel = "商密";
        }else if(fileLevel == 43){
            AttachmentLevel = "敏感信息";
        }
        System.out.println("附件密级信息AttachmentLevel为：" + AttachmentLevel);
    	return AttachmentLevel;
    }

    public String secrectLabelInfoc(String filePath) {
        //获取文件密标密级信息
        int fileLevel = 10;
        try {
            System.out.println("文件具体地址为："+filePath);
            fileLevel = labelOperator.getFileLevel(filePath);
            System.out.println("文件地址-附件密级等级："+fileLevel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String AttachmentLevel = "";
        if (fileLevel == 5){
            AttachmentLevel = "公开";
        }else if (fileLevel == 4){
            AttachmentLevel = "内部";
        }else if (fileLevel == 3){
            AttachmentLevel = "秘密";
        }else if(fileLevel == 2){
            AttachmentLevel = "机密";
        }else if(fileLevel == 41){
            AttachmentLevel = "商密";
        }else if(fileLevel == 42){
            AttachmentLevel = "商密";
        }else if(fileLevel == 43){
            AttachmentLevel = "敏感信息";
        }
        System.out.println("附件密级信息AttachmentLevel为：" + AttachmentLevel);
        return AttachmentLevel;
    }

    public String secrectLabelInfoReadPath(String filePath) {
        //获取文件密标密级信息-xml 文件地址
        int fileLevel = 10;
        try {
            System.out.println("文件具体地址为："+filePath);
            String xml = labelOperator.readFileLabel(filePath,0);
            System.out.println("文件具体xml流式为-不扩展："+xml);
            xml = labelOperator.readFileLabel(filePath,1);
            System.out.println("文件具体xml流式为-包扩展："+xml);
//            fileLevel = Integer.parseInt(labelOperator.readFileLabel(filePath,0));
//            System.out.println("文件地址-附件密级等级："+fileLevel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String AttachmentLevel = "";
        if (fileLevel == 5){
            AttachmentLevel = "公开";
        }else if (fileLevel == 4){
            AttachmentLevel = "内部";
        }else if (fileLevel == 3){
            AttachmentLevel = "秘密";
        }else if(fileLevel == 2){
            AttachmentLevel = "机密";
        }else if(fileLevel == 41){
            AttachmentLevel = "商密";
        }else if(fileLevel == 42){
            AttachmentLevel = "商密";
        }else if(fileLevel == 43){
            AttachmentLevel = "敏感信息";
        }
        System.out.println("附件密级信息AttachmentLevel为：" + AttachmentLevel);
        return AttachmentLevel;
    }

    public String secrectLabelInfoReadL(InputStream decryptedStream) {
        //获取文件密标密级信息-xml 文件地址
        int fileLevel = 10;
        try {
            System.out.println("文件具体xml流式为-不扩展");
            String xml = labelOperator.readFileLabel(decryptedStream,0);
            System.out.println("文件具体xml流式为-不扩展："+xml);
            xml = labelOperator.readFileLabel(decryptedStream,1);
            System.out.println("文件具体xml流式为-包扩展："+xml);
//            fileLevel = Integer.parseInt(labelOperator.readFileLabel(decryptedStream,0));
//            System.out.println("文件地址-附件密级等级："+fileLevel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String AttachmentLevel = "";
        if (fileLevel == 5){
            AttachmentLevel = "公开";
        }else if (fileLevel == 4){
            AttachmentLevel = "内部";
        }else if (fileLevel == 3){
            AttachmentLevel = "秘密";
        }else if(fileLevel == 2){
            AttachmentLevel = "机密";
        }else if(fileLevel == 41){
            AttachmentLevel = "商密";
        }else if(fileLevel == 42){
            AttachmentLevel = "商密";
        }else if(fileLevel == 43){
            AttachmentLevel = "敏感信息";
        }
        System.out.println("附件密级信息AttachmentLevel为：" + AttachmentLevel);
        return AttachmentLevel;
    }


    //文件解密
    public Integer JmAtt(InputStream inputStream) {
        //判断是否是 密标文件
        Integer labelledFile = 1;
        try {
            File file = new File("D:\\test\\test.docx");
            labelledFile = labelOperator.removeFileLabel(null, String.valueOf(file));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("labelledFile："+labelledFile);
        return labelledFile;
    }
}


