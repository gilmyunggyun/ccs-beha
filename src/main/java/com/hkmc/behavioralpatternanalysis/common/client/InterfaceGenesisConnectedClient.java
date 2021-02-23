package com.hkmc.behavioralpatternanalysis.common.client;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@RefreshScope
@FeignClient(url = "${dsp.server.genesis.url:dspGenesisConnectedUrl}", name = "${dsp.server.genesis.name:dspGenesisConnected}")
public interface InterfaceGenesisConnectedClient {
     @GetMapping(value = "{uriPath}")
     ResponseEntity<Map<String, Object>> requestGenCallGet(@RequestHeader Map<String, String> header,
                                                           @PathVariable("uriPath") String uriPath,
                                                           @RequestParam("vin") String vinPath);
}