package com.hkmc.behavioralpatternanalysis.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class ValidationFilterWrapper extends HttpServletRequestWrapper {

	private byte[] bodyData;

	public ValidationFilterWrapper(final ServletRequest request) {
		super((HttpServletRequest) request);
		try {
			this.bodyData = IOUtils.toByteArray(super.getInputStream());
		} catch (final IOException e) {
			log.debug(e.getMessage());
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new BufferedServletInputStream(new ByteArrayInputStream(this.bodyData), super.getInputStream());
	}

	@Override
	public String getParameter(final String paramName) {
		return super.getParameter(paramName);
	}

	@Override
	public String[] getParameterValues(final String paramName) {
		return super.getParameterValues(paramName);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return super.getParameterMap();
	}

	public String getBody() {
		return StringUtils.defaultString(new String(bodyData));
	}

}
