package com.hkmc.behavioralpatternanalysis.common;

public class Const {
	public static class BehavioralPatternAnalysis {
		public static final String SERVICE_NAME = "behavioralpatternanalysis";
		public static final String BASE_URL = "/"+SERVICE_NAME;
		public static final String VERSION_V1 = BASE_URL+"/v1";
	}

	public static class Header {
		public static final String FROM = "from";
		public static final String Authorization = "authorization";
		public static final String HOST = "host";
    }

	public static final String PHONE = "PHONE";

	public static class Key {
		// key of env
		public static final String DSP_COMMON_URI = "dsp.server.common.uri";
		public static final String DSP_HEADER_AUTH = "dsp.header.auth";
		public static final String SERVICE_DOMAIN_DRIVE_INFO = "service-domain.driveinfo";
		public static final String SERVICE_PATH_ODOMETER = "service-path.odometer";
		// key of error body
		public static final String ERR_CODE_MAP = "errCode";
		// key of consumedMap
		public static final String SAFETY_DRV_SCORE = "safety_drv_score";
		public static final String INS_DISCOUNT_YN = "ins_discount_yn";
		public static final String SCORE_DATE = "score_date";
		public static final String RANGE_DRV_DIST = "range_drv_dist";
		public static final String BRST_ACC_GRADE = "brst_acc_grade";
		public static final String BRST_DEC_GRADE = "brst_dec_grade";
		public static final String NIGHT_DRV_GRADE = "night_drv_grade";
		// column of behaSvdvHist
		public static final String CAR_OID = "carOid";
		public static final String CRTN_YMD = "ifDate";
		public static final String ODOMETER_VALUE = "value";
	}

	public static class ErrMsg {
		public static final String CANNOT_FOUND_VIN = "5003";
		public static final String TYPE_450 = "450";
		public static final String TYPE_595 = "595";
		public static final String TYPE_4002 = "4002";
		public static final String TYPE_4010 = "4010";
		public static final String TYPE_4122 = "4122";
		public static final String TYPE_4123 = "4123";
		public static final String TYPE_5001 = "5001";
	}

	/** etc */
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String FILTER_DATETIME_REGX = "\\/";
	public static final int FILTER_SKIP_INDEX = 3;
	public static final String APP_TYPE_UVO = "UVO";
	public static final String APP_TYPE_BLUE_LINK = "BLU";
	public static final String APP_TYPE_GENESIS_CONNECTED = "GEN";
	public static final String RESULT_SUCCESS = "S";
	public static final String RESULT_FAIL = "F";
}

