package com.hkmc.behavioralpatternanalysis.common.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BufferedServletInputStream extends ServletInputStream {

	private final ServletInputStream inputStream;
	private final ByteArrayInputStream bais;

	public BufferedServletInputStream(final ByteArrayInputStream bais, final ServletInputStream inputStream) {
		this.bais = bais;
		this.inputStream = inputStream;
	}

	@Override
	public int available() {
		return this.bais.available();
	}

	@Override
	public int read() {
		return this.bais.read();
	}

	@Override
	public int read(final byte[] buf) throws IOException {
		return this.bais.read(buf);
	}

	@Override
	public int read(final byte[] buf, final int off, final int len) {
		return this.bais.read(buf, off, len);
	}

	@Override
	public boolean isFinished() {
		return this.inputStream.isFinished();
	}

	@Override
	public boolean isReady() {
		return this.inputStream.isReady();
	}

	@Override
	public void setReadListener(final ReadListener listener) {
		this.inputStream.setReadListener(listener);
	}

}