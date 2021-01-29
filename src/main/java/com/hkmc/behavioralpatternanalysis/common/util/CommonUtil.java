package com.hkmc.behavioralpatternanalysis.common.util;

import com.hkmc.behavioralpatternanalysis.common.Const;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

public class CommonUtil {

	@Getter
	public static int consumerPage = 0;
	
	@Getter
	public static long consumerCount = 0;
	
	public static synchronized void addConsumerPage() {
		
		consumerPage++;
		
	}
	
	public static synchronized void resetConsumerPage() {
		
		consumerPage = 0;
		
	}
	
	public static synchronized void addConsumerCount() {
		
		consumerCount++;
		
	}
	
	public static synchronized void resetConsumerCount() {
		
		consumerCount = 0;
		
	}

	public static String getSvcResult(final String httpStatus) {
		String svcResult = Const.ResponseCode.FAIL_STATUS;

		if(StringUtils.equalsAny(httpStatus,
				String.valueOf(HttpStatus.NO_CONTENT.value()), String.valueOf(HttpStatus.OK.value()))){
			svcResult = Const.ResponseCode.SUCCESS_STATUS;
		}else if(StringUtils.equals(httpStatus, Const.ResponseCode.CUSTOM_HTTPSTATUS_740)){
			svcResult = Const.ResponseCode.CUSTOM_HTTPSTATUS_740;
		}else if( StringUtils.equalsAny(httpStatus,
				Const.ResponseCode.CUSTOM_HTTPSTATUS_504, Const.ResponseCode.CUSTOM_HTTPSTATUS_READTIMEOUT)){
			svcResult = Const.ResponseCode.TIME_OUT_STATUS;
		}

		return svcResult;
	}
}