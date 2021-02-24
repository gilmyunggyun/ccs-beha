package com.hkmc.behavioralpatternanalysis.common;

public class Const {
	public static class BehavioralPatternAnalysis {
		public static final String SERVICE_NAME = "behavioralpatternanalysis";
		public static final String BASE_URL = "/"+SERVICE_NAME;
		public static final String VERSION_V1 = BASE_URL+"/v1";
	}

	public static class Header {
		public static final String FROM = "from";
		public static final String Authorization = "Authorization";
    }

	public static class System {
		public static final String PHONE = "PHONE";
	}

	public static class Key {
		public static final String DSP_COMMON_URI = "dsp.server.common.uri";
		public static final String DSP_HEADER_AUTH = "dsp.header.auth";
		public static final String RET_CODE_MAP = "RetCode";
		public static final String RES_CODE_MAP = "resCode";
		public static final String ERR_CODE_MAP = "errCode";
	}

	public static class ErrMsg {
		public static final String CANNOT_FOUND_VIN = "5003";
	}

	public static class Column{
		public static final String CAR_OID = "carOid";
		public static final String CRTN_YMD = "ifDate";
	}

	public static class ResponseCode {
		public static final String SUCCESS_STATUS = "S";
	}

	public static class ResponseMessage {
		public static final String SUCCESS = "Success";
	}

	public static class AppType {
		public static final String UVO = "UVO";
		public static final String BLUE_LINK = "BLU";
		public static final String GENESIS_CONNECTED = "GEN";
	}

	public static class ClientKey {
		public static final String SAFETY_DRV_SCORE = "safety_drv_score";
		public static final String INS_DISCOUNT_YN = "ins_discount_yn";
		public static final String SCORE_DATE = "score_date";
		public static final String RANGE_DRV_DIST = "range_drv_dist";
		public static final String BRST_ACC_GRADE = "brst_acc_grade";
		public static final String BRST_DEC_GRADE = "brst_dec_grade";
		public static final String NIGHT_DRV_GRADE = "night_drv_grade";
	}

	/** etc */
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String DATETIME_REGX = "[ :\\/]";
	public static final String FILTER_DATETIME_REGX = "\\/";
	public static final String LIST_INDEX_REGX = "\\[0\\]";
	public static final int FILTER_SKIP_INDEX = 3;
}

