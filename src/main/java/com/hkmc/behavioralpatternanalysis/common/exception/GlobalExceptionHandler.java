package com.hkmc.behavioralpatternanalysis.common.exception;

import feign.FeignException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

	private static final String EXCEPTION = "Exception : {}";

	@ExceptionHandler(value = FeignException.class)
	public  ResponseEntity<ResponseErrorDTO> feignException(FeignException e){
		log.debug(EXCEPTION, e.getMessage());
		ResponseErrorDTO errorDTO = ResponseErrorDTO.builder()
				.errorMessage(e.getMessage())
				.code(e.status())
				.build();

		return ResponseEntity.status(checkStatusCode(e.status())).headers(new HttpHeaders()).contentType(MediaType.APPLICATION_JSON).body(errorDTO);
	}

	@ExceptionHandler(value = GlobalCCSException.class)
	public  ResponseEntity<ResponseErrorDTO> globalCCSException(GlobalCCSException e){
		log.debug(EXCEPTION, e.getMessage());
		ResponseErrorDTO errorDTO = ResponseErrorDTO.builder()
				.errorMessage(e.getMessage())
				.code(e.getCode())
				.build();


		return ResponseEntity.status(checkStatusCode(e.getCode())).headers(new HttpHeaders()).contentType(MediaType.APPLICATION_JSON).body(errorDTO);

	}

	@ExceptionHandler(value = GlobalExternalException.class)
	public static ResponseEntity<String> globalExternalException(GlobalExternalException e){
		log.debug(EXCEPTION, e.toString());
		return ResponseEntity.status(e.getStatusCode()).body(e.getBody());
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ResponseErrorDTO> exception(Exception e) {
		log.debug(EXCEPTION, e.getMessage());
		return new ResponseEntity<>(
				ResponseErrorDTO.builder()
						.errorMessage(e.getMessage())
						.build(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private static int checkStatusCode(final int statusCode) {
		return (statusCode < HttpStatus.OK.value() && HttpStatus.resolve(statusCode) == null) ? HttpStatus.INTERNAL_SERVER_ERROR.value() : statusCode;
	}

}
