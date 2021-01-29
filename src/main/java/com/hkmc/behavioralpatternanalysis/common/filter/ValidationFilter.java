package com.hkmc.behavioralpatternanalysis.common.filter;

import com.google.gson.*;
import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.code.SpaResponseCodeEnum;
import com.hkmc.behavioralpatternanalysis.common.model.SpaResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
public class ValidationFilter extends GenericFilterBean {

	private static final String VALIDATION_CHECK_ERROR_FILED_MESSAGE = "Validation check Error Filed[{}]";
	private static final String VALIDFILEDFILE = "/filter/VehicleModeChangeValidField.json";
	private static final String MENDANTORY = "mendantory";
	private static final String TYPE = "type";
	private static final String NAME = "name";
	private static final String FIELD = "field";

	private static final String OBJECT = "object";
	private static final String VALUE = "value";

	private static final String SERVICENO = "ServiceNo";

	private final String validationCheck;

	public ValidationFilter(final String validationCheck) {
		this.validationCheck = validationCheck;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException {
		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		HttpServletResponse httpResponse = ((HttpServletResponse) response);
		ValidationFilterWrapper wrapper = new ValidationFilterWrapper(request);
		String serviceNo = null;

		try {
			JsonObject bodyJson = new JsonObject();

			String contentType = Optional.ofNullable(httpRequest.getHeader("Content-Type")).orElse("");
			if (contentType.startsWith("application/json")) {
				bodyJson = new Gson().fromJson(wrapper.getBody(), JsonObject.class);
			}

			JsonArray checkJsonFileArray = getValidCheckObject(httpRequest.getRequestURI());

			if (Const.TRUE.equals(validationCheck) && Const.System.PHONE.equals(httpRequest.getHeader(Const.Header.FROM))) {
				if ((bodyJson == null || StringUtils.EMPTY.equals(bodyJson.toString()))
						|| Boolean.FALSE.equals(isValidationCheck(bodyJson, checkJsonFileArray))) {
					serviceNo = (bodyJson != null) ? bodyJson.get(SERVICENO).getAsString() : null;

					httpResponse.setCharacterEncoding(StandardCharsets.UTF_8.toString());
					httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
					httpResponse.setStatus(HttpStatus.OK.value());
					httpResponse.getWriter().write(new Gson().toJson(SpaResponseDTO.builder()
							.ServiceNo(serviceNo)
							.svcTime(BigInteger.ZERO.intValue())
							.FncCnt(BigInteger.ZERO.intValue())
							.RetCode(SpaResponseCodeEnum.ERROR_S999.getRetCode())
							.resCode(SpaResponseCodeEnum.ERROR_S999.getResCode())
							.build()));
				} else {
					chain.doFilter(wrapper, response);
				}
			} else {
				chain.doFilter(wrapper, response);
			}
		} catch (Exception ex) {
			log.debug("doFilter Exception : {}", ex.getMessage());

			httpResponse.setCharacterEncoding(StandardCharsets.UTF_8.toString());
			httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
			httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			httpResponse.getWriter().write(
					new Gson().toJson(SpaResponseDTO.builder()
							.ServiceNo(serviceNo)
							.svcTime(BigInteger.ZERO.intValue())
							.FncCnt(BigInteger.ZERO.intValue())
							.RetCode(SpaResponseCodeEnum.ERROR_EX01.getRetCode())
							.resCode(SpaResponseCodeEnum.ERROR_EX01.getResCode())
							.build()));
		}
	}

	/**
	 * SPA 전문형식 체크 Function
	 *
	 * @param bodyJson bodyJson
	 * @param jsonFile jsonFile
	 * @return Boolean
	 */
	private Boolean isValidationCheck(final JsonObject bodyJson, final JsonArray jsonFile) {
		for (JsonElement obj : jsonFile) {
			String typeValue = obj.getAsJsonObject().get(TYPE).getAsString();
			String mendatoryValue = obj.getAsJsonObject().get(MENDANTORY).getAsString();

			if (Const.TRUE.equals(mendatoryValue)) {
				String fieldName = obj.getAsJsonObject().get(NAME).getAsString();
				if (bodyJson.get(fieldName) == null || !validLoopCheck(bodyJson, obj, typeValue, fieldName)) {
					log.info(VALIDATION_CHECK_ERROR_FILED_MESSAGE, fieldName);

					return false;
				}

			} else if (Const.FALSE.equals(mendatoryValue)) {
				String fieldName = obj.getAsJsonObject().get(NAME).getAsString();
				if (bodyJson.get(fieldName) != null && !validLoopCheck(bodyJson, obj, typeValue, fieldName)) {
					log.info(VALIDATION_CHECK_ERROR_FILED_MESSAGE, fieldName);

					return false;
				}
			}
		}

		return true;
	}

	private Boolean validLoopCheck(final JsonObject bodyJson, JsonElement obj, String typeValue, String fieldName) {
		JsonElement bodyRequestValue = bodyJson.get(fieldName);

		try {
			if (VALUE.equals(typeValue)) {
				if (StringUtils.EMPTY.equals(StringUtils.defaultString(bodyRequestValue.getAsString()))) {
					return false;
				}
			} else if (OBJECT.equals(typeValue)) {
				JsonElement bodyElement = bodyJson.get(fieldName);
				JsonElement objectJsonFile = obj.getAsJsonObject().get(FIELD);

				if (bodyElement.isJsonArray()) {
					for (JsonElement jsonObject : bodyElement.getAsJsonArray()) {
						if (Boolean.FALSE.equals(this.isValidationCheck(jsonObject.getAsJsonObject(), objectJsonFile.getAsJsonArray()))) {
							return false;
						}
					}
				} else if (bodyElement.isJsonObject()) {
					JsonArray array = new JsonArray();
					array.add(objectJsonFile);

					if (Boolean.FALSE.equals(this.isValidationCheck(bodyElement.getAsJsonObject(), array))) {
						return false;
					}
				}
			}
		} catch (IllegalStateException ex) {
			log.debug("validLoopCheck Exception : {}", ex.getMessage());

			return false;
		}

		return true;
	}

	/**
	 * filter/*.json Validation File to JsonArray Function
	 *
	 * @param requestUri requestUri
	 * @return JsonArray
	 */
	private JsonArray getValidCheckObject(final String requestUri) {
		JsonArray array = new JsonArray();

		try {
			if (ObjectUtils.isNotEmpty(getClass().getResourceAsStream(VALIDFILEDFILE))) {
				JsonObject jsonObject = JsonParser.parseReader(
						new InputStreamReader(getClass().getResourceAsStream(VALIDFILEDFILE), StandardCharsets.UTF_8))
						.getAsJsonObject();

				for (String key : jsonObject.keySet()) {
					if (compareRequestUriAndJsonKey(requestUri, key)) {
						array = jsonObject.get(key).getAsJsonArray();

						break;
					}
				}
			}
		} catch (Exception e) {
			log.debug("getValidCheckObject Exception :{}", e.getMessage());
		}

		return array;
	}

	private boolean compareRequestUriAndJsonKey(String requestUri, String jsonKey) {
		String[] requestUris = requestUri.split(Const.FILTER_DATETIME_REGX);
		String[] jsonKeys = jsonKey.split(Const.FILTER_DATETIME_REGX);

		if (requestUris.length != (jsonKeys.length + Const.FILTER_SKIP_INDEX)) {
			return false;
		}

		for (int i = 0; i < jsonKeys.length; i++) {
			if (!"*".equals(jsonKeys[i]) && !StringUtils.equals(requestUris[i + Const.FILTER_SKIP_INDEX], jsonKeys[i])) {
				return false;
			}
		}

		return true;
	}

}