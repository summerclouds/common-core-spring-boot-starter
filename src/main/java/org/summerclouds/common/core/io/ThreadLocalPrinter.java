package org.summerclouds.common.core.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.summerclouds.common.core.lang.ICloseable;

public class ThreadLocalPrinter extends PrintStream {

	public ThreadLocalPrinter(OutputStream parent) {
		super(new InnerOutputStream(parent));
	}
	
	public ICloseable use(OutputStream stream) {
		OutputStream current = ((InnerOutputStream)out).output.get();
		((InnerOutputStream)out).output.set(stream);
		return new InnerCloseable(current);
	}
	
	private static class InnerOutputStream extends OutputStream {

		private OutputStream defaultOutput;
		private ThreadLocal<OutputStream> output = new ThreadLocal<>();

		public InnerOutputStream(OutputStream defaultOutput) {
			this.defaultOutput = defaultOutput;
		}

		@Override
		public void write(int b) throws IOException {
			OutputStream out = output.get();
			if (out == null) out = defaultOutput;
			out.write(b);
		}
		
		@Override
		public void flush() throws IOException {
			OutputStream out = output.get();
			if (out == null) out = defaultOutput;
			out.flush();
		}

		@Override
		public void close() throws IOException {
			OutputStream out = output.get();
			if (out == null) out = defaultOutput;
			out.close();
		}
		
	}
	
	private class InnerCloseable implements ICloseable {

		private OutputStream last;

		public InnerCloseable(OutputStream last) {
			this.last = last;
		}

		@Override
		public void close() {
			((InnerOutputStream)out).output.set(last);
		}
		
	}
}
