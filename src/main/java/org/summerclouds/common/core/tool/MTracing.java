package org.summerclouds.common.core.tool;

import org.summerclouds.common.core.cfg.BeanRef;
import org.summerclouds.common.core.error.ConflictRuntimeException;
import org.summerclouds.common.core.tracing.IScope;
import org.summerclouds.common.core.tracing.ISpan;
import org.summerclouds.common.core.tracing.ITracing;

public class MTracing {

	private static BeanRef<ITracing> instance = new BeanRef<>(ITracing.class);

	public static ITracing get() {
		return instance.bean();
	}

	public static ISpan current() {
		ITracing inst = get();
		if (inst == null) throw new ConflictRuntimeException("tracing bean not found");
		return inst.current();
	}

	public static IScope enter(ISpan span, String name, Object ... keyValue) {
		ITracing inst = get();
		if (inst == null) throw new ConflictRuntimeException("tracing bean not found");
		return inst.enter(span, name, keyValue);
	}

	public static IScope enter(ISpan span, String name) {
		ITracing inst = get();
		if (inst == null) throw new ConflictRuntimeException("tracing bean not found");
		return inst.enter(span, name);
	}

	public static IScope enter(String name, Object ... keyValue) {
		ITracing inst = get();
		if (inst == null) throw new ConflictRuntimeException("tracing bean not found");
		return inst.enter(name, keyValue);
	}

	public static String getTraceId() {
		ITracing inst = get();
		if (inst == null) return "";
		return inst.getTraceId();
	}

	public static String getSpanId() {
		ITracing inst = get();
		if (inst == null) return "";
		return inst.getSpanId();
	}
	
}
