package org.summerclouds.common.core.io;

import java.io.IOException;
import java.io.InputStream;

import org.summerclouds.common.core.lang.ICloseable;

public class ThreadLocalInputStream extends InputStream {

	private InputStream defaultIn;
	private ThreadLocal<InputStream> input = new ThreadLocal<>();
	
	public ThreadLocalInputStream(InputStream defaultIn) {
		this.defaultIn = defaultIn;
	}
	
	@Override
	public int read() throws IOException {
		InputStream in = input.get();
		if (in == null) in = defaultIn;
		return in.read();
	}

	@Override
	public void close() throws IOException {
		InputStream in = input.get();
		if (in == null) in = defaultIn;
		in.close();
	}
	
	public ICloseable use(InputStream in) {
		InputStream current = input.get();
		input.set(in);
		return new InnerCloseable(current);
	}

	private class InnerCloseable implements ICloseable {

		private InputStream last;

		public InnerCloseable(InputStream last) {
			this.last = last;
		}

		@Override
		public void close() {
			input.set(last);
		}
		
	}
}
