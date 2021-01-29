package com.hkmc.behavioralpatternanalysis.common.aop.log;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.code.BehavioralPatternAnalysisServiceEnum;
import com.hkmc.behavioralpatternanalysis.common.model.RemoteAuditProduceDTO;
import com.hkmc.behavioralpatternanalysis.common.model.SpaCommonVO;
import com.hkmc.behavioralpatternanalysis.common.util.RequestScopeUtil;
import com.hkmc.behavioralpatternanalysis.config.kafka.RemoteAuditProducer;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * RemoteAuditService Publish AOP
 * @author E678128
 *
 */
@Aspect
@Slf4j
public class RemoteAuditAspect {

	@Autowired
	private RemoteAuditProducer remoteAuditProducer;

	@Autowired
	private RequestScopeUtil requestScopeUtil;

	private static final ObjectMapper mapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).setSerializationInclusion(Include.NON_NULL);

	private Map<String, String> requestHeaderToString(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headerMap = new HashMap<>();
        while (headerNames.hasMoreElements()) {
        	String key = headerNames.nextElement();
        	headerMap.put(key , request.getHeader(key));
        }

        return headerMap;
    }

	@Before("execution(* com.hkmc.behavioralpatternanalysis..service.impl..*(..)) && !@annotation(com.hkmc.behavioralpatternanalysis.common.util.AopDisable)")
	public void vehicleStatusFromPhone(final JoinPoint joinPoint) throws Exception {
		if(joinPoint.getSignature().getName().toLowerCase().contains("async")) {
			SpaCommonVO spaCommonVO = mapper.convertValue(joinPoint.getArgs()[0], SpaCommonVO.class);
			if(Const.System.PHONE.equals(spaCommonVO.getFromHost())) {
				requestScopeUtil.setAttribute(Const.SPA_COMMON_VO, mapper.convertValue(joinPoint.getArgs()[0], SpaCommonVO.class));
			}else {
				requestScopeUtil.setAttribute(Const.SPA_COMMON_VO, mapper.convertValue(joinPoint.getArgs()[0], SpaCommonVO.class));
			}
		} else {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			Map<String, String> headerMap = requestHeaderToString(request);
			requestScopeUtil.setAttribute(Const.SPA_COMMON_VO, mapper.convertValue(joinPoint.getArgs()[0], SpaCommonVO.class));
		}
	}

    /**
     * Before MTgatewayclient remoteaudit publish Funciton
     * @param joinPoint joinPoint
     * @throws Exception exception
     */
    @Before("execution(* com.hkmc.behavioralpatternanalysis..client..MTGatewayClient.requestTMUCall*(..))")
    public void publishRemoteAuditRequest(final JoinPoint joinPoint) throws Exception {
		Map<String, String> requestHeader = ObjectUtils.allNotNull(joinPoint.getArgs()[0])?mapper.convertValue(joinPoint.getArgs()[0], new TypeReference<Map<String, String>> (){}):new HashMap<>();

		SpaCommonVO spaCommonVO = requestScopeUtil.getAttribute(Const.SPA_COMMON_VO, SpaCommonVO.class);
		log.info("\n [=== REMOTEAUDIT PUBLISH BEFORE REQUEST INFO] \n [HEADER] {} \n [BODY] {}", requestHeader, spaCommonVO.toString());

		String svcUrl = "";
		if(joinPoint.getSignature().getName().toLowerCase().indexOf("get") > -1) svcUrl = (String)joinPoint.getArgs()[1];
		if(joinPoint.getSignature().getName().toLowerCase().indexOf("post") > -1) svcUrl = (String)joinPoint.getArgs()[2];

		String mtgatewayStartTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(Const.YYYYMMDDHHMMSS));
		requestScopeUtil.setAttribute(Const.MTGATEWAY_START_TIME, mtgatewayStartTime);

        remoteAuditProducer.send(RemoteAuditProduceDTO.builder()
				.svcId(spaCommonVO.getServiceNo())
				.ccid(StringUtils.defaultString(spaCommonVO.getCCID()))
				.vin(requestHeader.get(Const.Header.VIN))
				.nadId(requestHeader.get(Const.Header.NADID))
				.svcStartTime(mtgatewayStartTime)
				.svcEndTime(mtgatewayStartTime)
				.resultCode(StringUtils.EMPTY)
				.resultMessage(StringUtils.EMPTY)
				.tid(StringUtils.defaultString(requestHeader.get(Const.Header.TID)))
				.serviceName(getService(svcUrl).getServiceName())
				.fromHost(spaCommonVO.getFromHost())
				.toHost(Const.VehicleStatus.SERVICE_NAME)
				.transactionType(Const.REQ)
				.ctrl(spaCommonVO.getCtrl())
				.svcUrl(svcUrl)
				.xtid(requestHeader.get(Const.Header.XTID))
				.appMode(requestHeader.get(Const.Header.APP_MODE))
				.brandCd(requestHeader.get(Const.Header.BRAND_CD))
				.build());
    }

	@AfterReturning(value = "execution(* com.hkmc.behavioralpatternanalysis..client..MTGatewayClient.requestTMUCall*(..))", returning = "response")
    public void publishRemoteAuditResponse(JoinPoint joinPoint, Object response) {
		response = Optional.ofNullable(response).orElse(new Object());
		Map<String, String> requestHeader = convertObjectToMap(joinPoint.getArgs()[0]);
		ResponseEntity<?> responseEntity = null;

		if (response.getClass().equals(ResponseEntity.class)) {
			responseEntity = (ResponseEntity<?>) response;
			Object body = Optional.ofNullable(responseEntity.getBody());
			log.info("\n [=== RESPONSE] \n [THREAD-ID] {} \n [CODE] {} \n [DATA] {} \n", Thread.currentThread().getId(), responseEntity.getStatusCodeValue(), body.toString());
		} else {
			log.info("\n [=== RESPONSE] \n [THREAD-ID] {} \n [DATA] {} \n", Thread.currentThread().getId(), response.toString());
		}

		if(responseEntity != null) {
			String svcUrl = "";
			if(joinPoint.getSignature().getName().toLowerCase().indexOf("get") > -1) svcUrl = (String)joinPoint.getArgs()[1];
			if(joinPoint.getSignature().getName().toLowerCase().indexOf("post") > -1) svcUrl = (String)joinPoint.getArgs()[2];

			requestScopeUtil.setAttribute(Const.REMOTE_AUDIT_DTO, RemoteAuditProduceDTO.builder()
					.vin(requestHeader.get(Const.Header.VIN))
					.nadId(requestHeader.get(Const.Header.NADID))
					.resultCode( String.valueOf(responseEntity.getStatusCodeValue())  )
					.resultMessage( String.valueOf(responseEntity.getBody()) )
					.tid( StringUtils.defaultString( responseEntity.getHeaders().toSingleValueMap().get(Const.Header.TID)) )
					.serviceName(getService(svcUrl).getServiceName())
					.fromHost(requestHeader.get(Const.Header.TO))
					.toHost(Const.VehicleStatus.SERVICE_NAME)
					.transactionType(Const.RES)
					//.svcUrl( getService(joinPoint.getSignature().getName()).getSvcUrl() )
					.svcUrl(svcUrl)
					.xtid( StringUtils.defaultString(requestHeader.get(Const.Header.XTID) ))
					.appMode(requestHeader.get(Const.Header.APP_MODE))
					.brandCd(requestHeader.get(Const.Header.BRAND_CD))
					.build());
		}
    }

    @AfterThrowing(value = "execution(* com.hkmc.behavioralpatternanalysis..client..*MTGatewayClient.requestTMUCall*(..))", throwing = "exception")
    public void publishRemoteAuditException(JoinPoint joinPoint, FeignException exception) {
		log.info("<<<<<<<<<<<<<<<<<< after throwing >>>>>>>>>>>>>>>>");
		log.info("\n [=== RESPONSE] \n [THREAD-ID] {} \n [ERROR] {} \n", Thread.currentThread().getId(), exception.toString());
		ObjectMapper mapper =  new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).setSerializationInclusion(Include.NON_NULL);

		try {
			Map<String, String> requestHeader = ObjectUtils.allNotNull(joinPoint.getArgs()[0])
					?mapper.convertValue(joinPoint.getArgs()[0], new TypeReference<Map<String, String>> (){}):new HashMap<>();
			String svcUrl = "";
			if(joinPoint.getSignature().getName().toLowerCase().indexOf("get") > -1) svcUrl = (String)joinPoint.getArgs()[1];
			if(joinPoint.getSignature().getName().toLowerCase().indexOf("post") > -1) svcUrl = (String)joinPoint.getArgs()[2];

			RemoteAuditProduceDTO remoteAuditProduceDTO = RemoteAuditProduceDTO.builder()
					.vin(requestHeader.get(Const.Header.VIN))
					.nadId(requestHeader.get(Const.Header.NADID))
					.resultCode(String.valueOf(exception.status()))
					.resultMessage(exception.getMessage())
					.tid(StringUtils.defaultString(requestHeader.get(Const.Header.TID)))
					.serviceName(getService(svcUrl).getServiceName())
					.fromHost(requestHeader.get(Const.Header.TO))
					.toHost(Const.VehicleStatus.SERVICE_NAME)
					.transactionType(Const.RES)
					.svcUrl(svcUrl)
					.xtid( requestHeader.get(Const.Header.XTID) )
					.appMode(requestHeader.get(Const.Header.APP_MODE))
					.brandCd(requestHeader.get(Const.Header.BRAND_CD))
					.build();

			requestScopeUtil.setAttribute(Const.REMOTE_AUDIT_DTO, remoteAuditProduceDTO);

		} catch (Exception e) {
			log.error("AOP Exception : {}",e.getMessage());
		}
    }


    @AfterReturning(value = "execution(* com.hkmc.behavioralpatternanalysis..service.impl..*(..)) && !@annotation(com.hkmc.behavioralpatternanalysis.common.util.AopDisable)")
    public void publishRemoteAuditResponse(JoinPoint joinPoint) {
		String mtgatewayStringTime = requestScopeUtil.getAttribute(Const.MTGATEWAY_START_TIME).toString();
		String mtgatewayEndTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(Const.YYYYMMDDHHMMSS));

		RemoteAuditProduceDTO remoteAuditProduceDTO = ObjectUtils.defaultIfNull(requestScopeUtil.getAttribute(Const.REMOTE_AUDIT_DTO, RemoteAuditProduceDTO.class), new RemoteAuditProduceDTO());
		SpaCommonVO spaCommonVO = requestScopeUtil.getAttribute(Const.SPA_COMMON_VO, SpaCommonVO.class);

		String svcStatusCd = ObjectUtils.defaultIfNull(requestScopeUtil.getAttribute(Const.SVC_STATUS_CD), "F").toString();
		String msgSeq = ObjectUtils.defaultIfNull(requestScopeUtil.getAttribute(Const.MSG_SEQ), "").toString();

		if(remoteAuditProduceDTO.getVin() != null) {
			remoteAuditProduceDTO.setCcid(StringUtils.defaultString(spaCommonVO.getCCID()));
			remoteAuditProduceDTO.setSvcId(spaCommonVO.getServiceNo());
			remoteAuditProduceDTO.setCtrl(spaCommonVO.getCtrl());
			remoteAuditProduceDTO.setSvcStatusCd(svcStatusCd);
			remoteAuditProduceDTO.setMsgSeq(msgSeq);
			remoteAuditProduceDTO.setSvcStartTime(mtgatewayStringTime);
			remoteAuditProduceDTO.setSvcEndTime(mtgatewayEndTime);

			remoteAuditProducer.send(remoteAuditProduceDTO);

			log.info("\n [=== RESPONSE] \n {} \n", remoteAuditProduceDTO.toString());
		}
    }

	@AfterThrowing(value = "execution(* com.hkmc.behavioralpatternanalysis..service.impl..*(..)) && !@annotation(com.hkmc.behavioralpatternanalysis.common.util.AopDisable)", throwing = "exception")
	public void publishRemoteAuditResponseException(JoinPoint joinPoint, Exception exception) {
		if(requestScopeUtil.getAttribute(Const.MTGATEWAY_START_TIME) != null) {
			String mtgatewayStringTime = requestScopeUtil.getAttribute(Const.MTGATEWAY_START_TIME).toString();
			String mtgatewayEndTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(Const.YYYYMMDDHHMMSS));

			RemoteAuditProduceDTO remoteAuditProduceDTO = ObjectUtils.defaultIfNull(requestScopeUtil.getAttribute(Const.REMOTE_AUDIT_DTO, RemoteAuditProduceDTO.class), new RemoteAuditProduceDTO());
			SpaCommonVO spaCommonVO = requestScopeUtil.getAttribute(Const.SPA_COMMON_VO, SpaCommonVO.class);

			String svcStatusCd = ObjectUtils.defaultIfNull(requestScopeUtil.getAttribute(Const.SVC_STATUS_CD), "F").toString();
			String msgSeq = ObjectUtils.defaultIfNull(requestScopeUtil.getAttribute(Const.MSG_SEQ), "").toString();

			if (remoteAuditProduceDTO.getVin() != null) {
				remoteAuditProduceDTO.setCcid(StringUtils.defaultString(spaCommonVO.getCCID()));
				remoteAuditProduceDTO.setSvcId(spaCommonVO.getServiceNo());
				remoteAuditProduceDTO.setCtrl(spaCommonVO.getCtrl());
				remoteAuditProduceDTO.setSvcStatusCd(svcStatusCd);
				remoteAuditProduceDTO.setMsgSeq(msgSeq);
				remoteAuditProduceDTO.setSvcStartTime(mtgatewayStringTime);
				remoteAuditProduceDTO.setSvcEndTime(mtgatewayEndTime);

				remoteAuditProducer.send(remoteAuditProduceDTO);

				log.info("\n [=== RESPONSE] \n {} \n", remoteAuditProduceDTO.toString());
			}
		}
	}

    private BehavioralPatternAnalysisServiceEnum getService(String svcUrl) {
    	if (BehavioralPatternAnalysisServiceEnum.EMPTY.getServiceUrl().equals(svcUrl)) {
			return BehavioralPatternAnalysisServiceEnum.EMPTY;
    	} else {
    		return BehavioralPatternAnalysisServiceEnum.EMPTY;
    	}
    }

    private Map<String, String> convertObjectToMap(Object object) {
		return object != null? mapper.convertValue(object, new TypeReference<> (){}):new HashMap<>();
	}

}
