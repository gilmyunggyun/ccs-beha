package com.hkmc.behavioralpatternanalysis.common.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "${spring.feign.server.url}", name = "${spring.feign.server.name}")
public interface CustomFeignClient {
	
	/**
     * TMU Get Method 요청
     * @param header
     * @param uriPat
     * @return
     */
     @GetMapping(value="{uriPath}")
     ResponseEntity<Map<String,Object>> requestTMUCallGet(@RequestHeader Map<String, String> header, @PathVariable("uriPath") String uriPath);

     /**
     * TMU PostMethod
     * @param header
     * @param svcResResultVO
     */
     @PostMapping(value="{uriPath}")
     void requestTMUCallPost(@RequestHeader Map<String, String> header, Object bodyObject, @PathVariable("uriPath") String uriPath);
	
}
