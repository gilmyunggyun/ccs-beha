package com.hkmc.behavioralpatternanalysis.common.exception;

import java.util.Map;

import feign.FeignException;
import lombok.Getter;

@Getter
public class CCSFeignException extends FeignException{
	
	private Map<String, String> header;
	
	public CCSFeignException(FeignException e, Map<String, String> header) {
		super(e.status(), e.getMessage(), e.request(), e, e.content());
		this.header = header;
	}

	private static final long serialVersionUID = 1L;

}
