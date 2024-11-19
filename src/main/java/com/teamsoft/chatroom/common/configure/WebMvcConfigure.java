package com.teamsoft.chatroom.common.configure;

import com.teamsoft.chatroom.common.core.LoginIncepter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * MVC相关配置
 * @author dominealex
 * @version 2020/3/17
 */
@Configuration
@EnableWebMvc
public class WebMvcConfigure implements WebMvcConfigurer {
	// 静态资源映射配置
	private static final String[][] RESOURCE_MAPPING = {
			{"/static/**", "classpath:/static/"}
	};

	// 登陆权限拦截器
	@Resource
	private LoginIncepter loginIncepter;

	/**
	 * 配置视图解析器
	 * 1. 配置默认视图为HTML/TEXT(无后缀或.xxx任意非json的后缀)
	 * 2. 配置json视图请求后缀，.json的请求返回json视图
	 * 3. 识别请求头中的Accept，为text/html时，返回网页视图，为application/json时，返回json视图
	 * ignoreAcceptHeader默认值为false，即表示开启Accept识别支持
	 */
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(MediaType.TEXT_HTML);
	}

	/**
	 * 配置json视图解析器适用范围(application/json, text/html)
	 * 解决Swagger请求时，报406错误的问题
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		List<MediaType> list = new ArrayList<>();
		list.add(new MediaType("application", "json", StandardCharsets.UTF_8));
		list.add(new MediaType("application", "*+json", StandardCharsets.UTF_8));
		list.add(new MediaType("text", "html", StandardCharsets.UTF_8));
		converter.setSupportedMediaTypes(list);
		converters.add(converter);
	}

	/**
	 * 设置欢迎页
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// 设置默认地址请求(/)重定向到index页面
		registry.addRedirectViewController("/", "/index");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}

	/**
	 * 添加拦截器配置
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 配置登录权限拦截器
		InterceptorRegistration loginInterceptorRegistration = registry.addInterceptor(loginIncepter)
				.addPathPatterns("/**")
				.excludePathPatterns("/error");
		// 循环排除静态资源拦截
		for (String[] mapping : RESOURCE_MAPPING) {
			loginInterceptorRegistration.excludePathPatterns(mapping[0]);
		}
	}

	/**
	 * 跨域支持
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowCredentials(true)
				.allowedMethods("GET", "POST", "DELETE", "PUT")
				.maxAge(3600 * 24);
	}

	/**
	 * 将静态资源目录、swagger2映射为指定的静态路径
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 过滤静态资源
		for (String[] mapping : RESOURCE_MAPPING) {
			registry.addResourceHandler(mapping[0]).addResourceLocations(mapping[1]);
		}
	}
}