package org.summerclouds.common.core.tracing;

import org.summerclouds.common.core.lang.ICloseable;

public interface IScope extends ICloseable {

	ISpan getSpan();
	
}
