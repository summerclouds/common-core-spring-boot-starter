package org.summerclouds.common.core.tool;

import org.summerclouds.common.core.M;
import org.summerclouds.common.core.tracing.ITracing;

public class MTracing {

	public static ITracing get() {
		return M.l(ITracing.class);
	}

}
