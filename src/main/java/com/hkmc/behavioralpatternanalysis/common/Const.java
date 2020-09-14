package com.hkmc.behavioralpatternanalysis.common;

public class Const {
	
	public static class BehavioralPatternAnalysis{
		public static final String VERSION_V1= "/behavioralpatternanalysis/v1";
	}
	
	public static final String FROM = "from";
	public static final String SPA = "SPA";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	// 서비스 관련
	public static class SubCode {
		public static final String SCODE01 = "01";
		public static final String SCODE02 = "02";
		public static final String COMMAND01 = "ubiinfo";
		public static final String COMMAND02 = "intcarinfo";
	}

	// KAFKA 관련
	public static final String UBIINFO_TOPIC = "ccsp20.customerbehavioranalysis.ubiinfo";
	public static final String UBIINFO_GROUPID = "CBA_01";
	public static final String INTCARINFO_TOPIC = "ccsp20.customerbehavioranalysis.intcarinfo";
	public static final String INTCARINFO_GROUPID = "CBA_02";
}
