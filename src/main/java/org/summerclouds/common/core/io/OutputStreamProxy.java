package org.summerclouds.common.core.io;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamProxy extends OutputStream {

	private OutputStream out;
	private boolean ignoreClose;
	private volatile boolean closed;

	public OutputStreamProxy(OutputStream out) {
		this.out = out;
	}
	
	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}

	public void close() throws IOException {
		closed = true;
		if (ignoreClose)
			out.flush();
		else
			out.close();
	}

	public boolean isIgnoreClose() {
		return ignoreClose;
	}

	public void setIgnoreClose(boolean ignoreClose) {
		this.ignoreClose = ignoreClose;
	}
	
	public boolean isClosed() {
		return closed;
	}
	

}
