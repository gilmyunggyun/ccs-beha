package com.hkmc.behavioralpatternanalysis.config;

import ccs.core.db.repository.redis.GenericRedisRepository;
import com.hkmc.behavioralpatternanalysis.intelligence.model.NadidVinAuth;
import com.hkmc.behavioralpatternanalysis.intelligence.model.CarTmuBasicInfo;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
	
	private final RedisProperties redisProperties;

	@Bean
	public RedisConnectionFactory redisConnectionFactoryDB00() {

		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(redisProperties.getHost());
		redisStandaloneConfiguration.setPort(redisProperties.getPort());
		redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
		redisStandaloneConfiguration.setDatabase(0);

		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);

		return lettuceConnectionFactory;

	}


	@Bean
	public RedisTemplate<String, Object> redisTemplateDB00() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactoryDB00());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

		return redisTemplate;
	}

	@Bean
	public GenericRedisRepository<NadidVinAuth, String> nadidVinAuthRepository() {
		return new GenericRedisRepository<NadidVinAuth, String>(NadidVinAuth.class, redisTemplateDB00());
	}

	@Primary
	@Bean
	public RedisConnectionFactory redisConnectionFactoryDB09() {

		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(redisProperties.getHost());
		redisStandaloneConfiguration.setPort(redisProperties.getPort());
		redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
		redisStandaloneConfiguration.setDatabase(9);

		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);

		return lettuceConnectionFactory;

	}
	
	@Bean
	public RedisTemplate<String, Object> redisTemplateDB09() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactoryDB09());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
	
		return redisTemplate;
	}

	@Bean
	public GenericRedisRepository<CarTmuBasicInfo, String> carTmuBasicRepository() {
		return new GenericRedisRepository<CarTmuBasicInfo, String>(CarTmuBasicInfo.class, redisTemplateDB09());
	}
}
