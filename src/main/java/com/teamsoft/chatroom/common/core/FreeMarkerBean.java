package com.teamsoft.chatroom.common.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.Properties;

/**
 * FreeMarkerBean配置
 * @author wangyg
 * @version 2020/4/3
 */
@Configuration
public class FreeMarkerBean {
	/**
	 * 生成FreeMarker配置, 配置查找路径
	 */
	@Bean
	public FreeMarkerConfigurer freemarkerConfig() {
		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
		// 设置路径
		configurer.setTemplateLoaderPath("classpath:/templates");
		Properties properties = new Properties();
		properties.put("classic_compatible", "true");
		properties.put("template_exception_handler", "rethrow");
		configurer.setFreemarkerSettings(properties);
		return configurer;
	}

	/**
	 * 生成FreeMarker的ViewResolver
	 */
	@Bean
	@ConditionalOnProperty(name = "spring.freemarker.enabled", matchIfMissing = true)
	public FreeMarkerViewResolver getFreemarkViewResolver() {
		FreeMarkerViewResolver freeMarkerViewResolver = new FreeMarkerViewResolver();
		// 设置缓存
		freeMarkerViewResolver.setCache(false);
		// 设置后缀
		freeMarkerViewResolver.setSuffix(".ftl");
		// 设置contentType
		freeMarkerViewResolver.setContentType("text/html; charset=UTF-8");
		freeMarkerViewResolver.setOrder(1);
		freeMarkerViewResolver.setViewClass(FreeMarkerView.class);
		return freeMarkerViewResolver;
	}
}