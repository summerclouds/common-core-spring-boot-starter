package org.summerclouds.common.core.condition;

import java.lang.annotation.Annotation;

public interface SummerCondition {

	boolean matches(Annotation anno, Object object);
	
}
