package com.hkmc.behavioralpatternanalysis.common.client;

import java.util.Map;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RefreshScope
@FeignClient(url = "${spring.mtgateway.server.url:localhost}", name = "${spring.mtgateway.server.name:localhost}")
public interface MTGatewayClient {

     @GetMapping(value = "{uriPath}")
     ResponseEntity<Map<String, Object>> requestTMUCallGet(@RequestHeader Map<String, String> header,
                                                           @PathVariable("uriPath") String uriPath);

     @PostMapping(value = "{uriPath}")
     ResponseEntity<Map<String, Object>> requestTMUCallPost(@RequestHeader Map<String, String> header,
                                                            Object bodyObject,
                                                            @PathVariable("uriPath") String uriPath);

}