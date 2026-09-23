package com.huabo.system.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;

/**
 * 处理图片访问本地问题
 */
@Configuration
@Slf4j
public class PhotoConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		/**
		 * 访问路径：http://localhost:8086/image/2022001.png
		 * 访问域名看配置：application.file-url
		 * "/system-image/**" 为前端URL访问路径
		 * "file:" + bmpPath 是本地磁盘映射
		 */
		String userUrl = System.getProperty("user.dir").replaceAll("\\\\", "/");
		String fileUrl = "file:" + userUrl + "/upload/imgs/";
		registry.addResourceHandler("/localUpload/imgs/**").addResourceLocations(fileUrl);
	}

}
