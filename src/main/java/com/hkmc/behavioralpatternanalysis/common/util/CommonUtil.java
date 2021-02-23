package com.hkmc.behavioralpatternanalysis.common.util;

import lombok.Getter;

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
}