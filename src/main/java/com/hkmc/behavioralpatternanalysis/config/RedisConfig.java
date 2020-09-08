package com.hkmc.behavioralpatternanalysis.config;

//@Configuration
//@EnableRedisRepositories
//@RequiredArgsConstructor
public class RedisConfig {
	
//	private final RedisProperties redisProperties;
//
//	@Primary
//	@Bean
//	public RedisConnectionFactory redisConnectionFactory() {
//		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//		redisStandaloneConfiguration.setHostName(redisProperties.getHost());
//		redisStandaloneConfiguration.setPort(redisProperties.getPort());
//		redisStandaloneConfiguration.setDatabase(7);
//
//		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
//		return lettuceConnectionFactory;
//	}
//	
//	@Bean
//	public RedisConnectionFactory redisConnectionFactory2() {
//		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//		redisStandaloneConfiguration.setHostName(redisProperties.getHost());
//		redisStandaloneConfiguration.setPort(redisProperties.getPort());
//
//		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
//		return lettuceConnectionFactory;
//	}
//	
//	@Bean
//	public RedisTemplate<byte[], byte[]> redisTemplate() {
//		RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
//		redisTemplate.setConnectionFactory(redisConnectionFactory());
//	redisTemplate.setKeySerializer(new StringRedisSerializer());
//    redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//	
//		return redisTemplate;
//	}
	
}
