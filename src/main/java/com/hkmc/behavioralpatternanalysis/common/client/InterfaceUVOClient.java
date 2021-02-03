package com.hkmc.behavioralpatternanalysis.common.client;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@RefreshScope
@FeignClient(url = "${interface-server.UVO.url:uvoDspUrl}", name = "${spring.interface.UVO.name:uvoDsp}")
public interface InterfaceUVOClient {
     @GetMapping(value = "{uriPath}")
     ResponseEntity<Map<String, Object>> requestUvoCallGet(@RequestHeader Map<String, String> header,
                                                           @PathVariable("uriPath") String uriPath);
}