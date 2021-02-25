package com.hkmc.behavioralpatternanalysis.common.client;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RefreshScope
@FeignClient(url = "${dsp.server.bluelink.url:dspBluelinkUrl}", name = "${dsp.server.bluelink.name:dspBlueLink}")
public interface InterfaceBluelinkClient {
     @GetMapping(value = "{uriPath}")
     ResponseEntity<Map<String, Object>> requestCallGet(@RequestHeader Map<String, String> header,
                                                        @PathVariable("uriPath") String uriPath,
                                                        @RequestParam("vin") String vinPath);
}