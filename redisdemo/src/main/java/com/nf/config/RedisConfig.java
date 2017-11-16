package com.nf.config;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nf.controller.User;

@Configuration
@EnableCaching
public class RedisConfig<T> extends CachingConfigurerSupport{

	// @Bean
	// JedisConnectionFactory jedisConnectionFactory() {
	// return new JedisConnectionFactory();
	// }
	//
	@Bean
	public RedisCacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setDefaultExpiration(60);// 秒
		return cacheManager;
	}

	// @Bean
	// public RedisTemplate<String, T> redisTemplate(RedisConnectionFactory
	// factory) {
	//
	// RedisTemplate<String, T> template = new RedisTemplate<String, T>();
	// template.setConnectionFactory(jedisConnectionFactory());
	// template.setKeySerializer(new StringRedisSerializer());
	// template.setValueSerializer(new RedisObjectSerializer());
	//
	// return template;
	// }

	@Bean
	public RedisTemplate<String, T> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, T> template = new RedisTemplate<String, T>();
		template.setConnectionFactory(connectionFactory);

		// 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
		Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		serializer.setObjectMapper(mapper);

		template.setValueSerializer(serializer);
		// 使用StringRedisSerializer来序列化和反序列化redis的key值
		template.setKeySerializer(new StringRedisSerializer());
		template.afterPropertiesSet();
		return template;
	}

	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};
	}

}