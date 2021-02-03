package com.hkmc.behavioralpatternanalysis.common;

public class Const {
	public static class BehavioralPatternAnalysis {
		public static final String SERVICE_NAME = "behavioralpatternanalysis";
		public static final String BASE_URL = "/behavioralpatternanalysis";
		public static final String VERSION_V1= "/behavioralpatternanalysis/v1";

	}

	public static class VehicleStatus {
		public static final String SERVICE_NAME = "VehicleStatus";
		public static final String BASE_URL = "/vehiclestatus";
		public static final String VERSION_V1 = BASE_URL+"/v1";
		public static final String VERSION_V2 = BASE_URL+"/v2";
		public static final String VERSION_V1_G = VERSION_V1+"/g";
		public static final String VERSION_V2_G = VERSION_V2+"/g";

		public static final String RMWA_V2 = "/rc/modemwaitingtime/alert";
		public static final String RMWA_DATA = RMWA_V2+"/{vinpath}";
	}

	public static class Header {
		public static final String FROM = "from";
		public static final String TO = "to";
		public static final String XTID = "xtid";
		public static final String TID = "tid";
		public static final String VIN = "vin";
		public static final String NADID = "nadid";
		public static final String LANGUAGE = "language";
		public static final String VERSION = "version";
		public static final String OFFSET = "offset";
		public static final String APP_MODE = "appmode";
		public static final String BRAND_CD = "brandcd";
		public static final String HOST = "host";
		public static final String CAR_MODEL = "carmodel";
		public static final String X_ORG_URI = "x-org-uri";
        public static final String CCID = "ccid";
    }

	public static class System {
		public static final String PHONE = "PHONE";
		public static final String ISS = "ISS";
		public static final String TMU = "TMU";
		public static final String IOT = "IOT";
		public static final String VCS = "VCS";
	}

	public static class RmwaStatus {
		public static final String S = "SERVICE_SUCCESS";
		public static final String F05 = "CC05";
		public static final String F06 = "CC06";
	}

	public static class Protocol {
		public static final String RVS_V = "RVS-V";
		public static final String RSC_C = "RSC-C";
		public static final String ECO_E = "ECO-E";
	}

	public static class Key {
		public static final String CHANGE_HEADER_PROPERTY = "change-header.";
		public static final String AUDIT_DOMAIN = "other-service.audit.domain";
		public static final String AUDIT_PATH = "other-service.audit.path";
		public static final String VEHICLE_LOCATION_SAVE_DOMAIN = "other-service.vehicle-location-save.domain";
		public static final String VEHICLE_LOCATION_SAVE_PATH = "other-service.vehicle-location-save.path";
		public static final String VEHICLE_LOCATION_DOMAIN = "other-service.vehicle-location.domain";
		public static final String VEHICLE_LOCATION_PATH = "other-service.vehicle-location.path";
		public static final String DESTINATION_DOMAIN = "other-service.destination.domain";
		public static final String DESTINATION_PATH = "other-service.destination.path";
		public static final String LASTCOORDI_PROPERTY = "LastCoordi";
		public static final String RECENTCARSTATUS_PROPERTY = "RecentCarStatus";
		public static final String POLLCARSTATUS_PROPERTY = "PollCarStatus";
		public static final String EXCLUDED_MODEL_PROPERTY = "recent-car-status.excluded-model";
		public static final String DISTANCE_CHANGE_CAR_MODEL_PROPERTY = "eco-e.distance-change-car-model";
		public static final String EV_APP_MODE_PROPERTY = "ev-app-mode";
		public static final String TO_ENUM = "CARSTATUS_TO_";
		public static final String CREATE_DATE_FIELD = "createDate";
		public static final String VEHICLE_LOCATION_MAP = "vehicleStatusInfo.vehicleLocation";
		public static final String ODOMETER_MAP = "vehicleStatusInfo.odometer";
		public static final String GPS_DETAIL_MAP = "gpsdetail";
		public static final String GPS_DETAIL_COORD_MAP = "gpsdetail.coord";
		public static final String LAT_MAP = "lat";
		public static final String LON_MAP = "lon";
		public static final String TIME_MAP = "time";
		public static final String VEHICLE_STATUS_INFO_MAP = "vehicleStatusInfo";
		public static final String SLEEP_MODE_CHECK_MAP = "vehicleStatusInfo.vehicleStatus.sleepModeCheck";
		public static final String VALUE_MAP = "value";
		public static final String UNIT_MAP = "unit";
		public static final String DESTINATION_LIST_MAP = "destinationList";
		public static final String DESTINATION_LIST_RGSTDTM_MAP = "rgstDtm";
		public static final String DESTINATION_LIST_SHARARVADDR_MAP = "sharArvAddr";
		public static final String DESTINATION_LIST_DESTPOINM_MAP = "destPoiNm";
		public static final String DESTINATION_LIST_GPSLAT_MAP = "gpsLat";
		public static final String DESTINATION_LIST_GPSLON_MAP = "gpsLon";
		public static final String DESTINATION_LIST_DRVRSCTNVAL_MAP = "drvrSctnVal";
		public static final String RET_CODE_MAP = "RetCode";
		public static final String RES_CODE_MAP = "resCode";
		public static final String SVCSTATUSCD_MAP = "svcStatusCd";
		public static final String RESULTCODE_MAP = "resultCode";
		public static final String DRVDISTANCE_MAP = "drvDistance";
		public static final String BATTERYSTATUS_MAP = "batteryStatus";
		public static final String DISTANCETYPE_MAP = "distanceType";
		public static final String EVSTATUS_MAP = "eVStatus";
	}

	public static class ResponseCode {
		public static final String SUCCESS_STATUS = "S";
		public static final String FAIL_STATUS = "F";
		public static final String TIME_OUT_STATUS = "T";
		public static final String WAITING_STATUS = "W";
		public static final String CUSTOM_HTTPSTATUS_740 = "740";
		public static final String CUSTOM_HTTPSTATUS_760 = "760";
		public static final String CUSTOM_HTTPSTATUS_504 = "504";
		public static final String CUSTOM_HTTPSTATUS_556 = "556";
		public static final String CUSTOM_HTTPSTATUS_590 = "590";
		public static final String CUSTOM_HTTPSTATUS_READTIMEOUT = "-1";
		public static final String CUSTOM_HTTPSTATUS_START_7 = "7";
		public static final String HTTPSTATUS_200 = "200";
		public static final String HTTPSTATUS_204 = "204";
		public static final String AUDIT_STATUS_NAN = "NaN";
	}

	public static class ResponseMessage {
		public static final String SUCCESS = "Success";
	}

	public static class ServiceNo {
		public static final String CAR_STATUS = "F6";
		public static final String POLL_CAR_STATUS = "P2";
		public static final String POLL_COODI_OPTION = "P7";
		public static final String UBI_SCORE = "A26";
	}

	public static class AppType {
		public static final String UVO = "UVO";
		public static final String BLUE_LINK = "BLU";
		public static final String GENESIS_CONNECTED = "GEN";
//		public static final String ? = "BLC";
//		public static final String ? = "UVC";
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

	/** Notification */
	public static final String APP_LINK = "REMOTE";
	/** AOP Const */
	public static final String MSG_SEQ = "msgSeq";
	public static final String SVC_STATUS_CD = "svcStatusCd";
	public static final String REMOTE_AUDIT_DTO = "remoteAuditDTO";
	public static final String SPA_COMMON_VO = "spaCommonVO";
	public static final String MTGATEWAY_START_TIME = "mtgatewayStartTime";
	public static final String REQ = "req";
	public static final String RES = "res";
	/** Coord **/
	public static final String LATITUDE_DISTANCE = "92";
	public static final String LONGITUDE_DISTANCE = "114";
	public static final int COORDINATE_RETURN_RANGE = 30;  // x 100meter
	/** Distance unit */
	public static final int DISTANCE_UNIT_KM = 1;
	public static final int BATTERY_STATUS_RANGE = 50;
	public static final int DISTANCE_TYPE_VALUE_RANGE = 140;
	public static final int DISTANCE_TYPE_VALUE_ADD = 512;
	/** etc */
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String GEN1 = "GEN1";
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
	public static final String HOUR = "HH";
	public static final String MINUTE = "mm";
	public static final String DATETIME_REGX = "[ :\\/]";
	public static final String FILTER_DATETIME_REGX = "\\/";
	public static final String LIST_INDEX_REGX = "\\[0\\]";
	public static final int FILTER_SKIP_INDEX = 3;
}

