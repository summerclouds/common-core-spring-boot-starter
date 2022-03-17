package org.summerclouds.common.core.conditions;

import java.lang.annotation.Annotation;

public interface SummerCondition {

	boolean matches(Annotation anno);
	
}
