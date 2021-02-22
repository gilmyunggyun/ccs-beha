package com.hkmc.behavioralpatternanalysis.common.client;

import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model.DspReqDTO;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RefreshScope
@FeignClient(url = "${interface-server.blu.url:bluDspUrl}", name = "${spring.interface.blu.name:blukDsp}")
public interface InterfaceBluelinkClient {
     @GetMapping(value = "{uriPath}")
     ResponseEntity<Map<String, Object>> requestBluCallGet(@RequestHeader Map<String, String> header,
                                                           @PathVariable("uriPath") String uriPath,
                                                           @RequestParam("vin") String vinPath);
}