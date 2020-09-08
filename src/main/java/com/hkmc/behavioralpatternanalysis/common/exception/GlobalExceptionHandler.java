package com.hkmc.behavioralpatternanalysis.common.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkmc.behavioralpatternanalysis.common.model.ResponseErrorDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***************************************************
 * 설     명 : Global exception 처리
 ***************************************************/
@Slf4j
@RestControllerAdvice(basePackages = "com.hkmc")
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	private ObjectMapper mapper = new ObjectMapper();
	
	@ExceptionHandler(value = GlobalCCSException.class)
	public static ResponseEntity<ResponseErrorDTO> globalCCSException(GlobalCCSException e){
		log.debug("Exception : {}", e.getMessage());
		log.debug("Exception : {}", e.getMessage());
		ResponseErrorDTO errorDTO = ResponseErrorDTO.builder()
		        .errorMessage(e.getMessage())
		        .code(e.getCode())
		        .build();
		
		return ResponseEntity.status(checkStatusCode(errorDTO.getCode())).body(errorDTO);
	}
    
    @ExceptionHandler(value = CCSFeignException.class)
    public String ccsFeignClientException(CCSFeignException e, HttpServletResponse response) {
    	response.setStatus(e.status());
    	
    	String body = e.contentUTF8();
    	Map<String, Object> bodyMap = new HashMap<>();
    	try {
			bodyMap = mapper.readValue(body, new TypeReference<Map<String, Object>>(){});
		} catch (JsonProcessingException e1) {
			bodyMap.put("data", body);
		}
    	
    	return body;
    }
    
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseErrorDTO> exception(Exception e) {
    	return new ResponseEntity<>(ResponseErrorDTO.builder().errorMessage(e.getMessage()).build(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    private static int checkStatusCode(final int statusCode) {
    	return (statusCode < HttpStatus.OK.value() && HttpStatus.resolve(statusCode) == null) ? HttpStatus.INTERNAL_SERVER_ERROR.value() : statusCode;
    }
    
}
