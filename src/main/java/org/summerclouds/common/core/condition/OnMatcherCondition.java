package org.summerclouds.common.core.condition;

import java.lang.annotation.Annotation;

import org.summerclouds.common.core.log.Log;
import org.summerclouds.common.core.matcher.Condition;
import org.summerclouds.common.core.tool.MSpring;
import org.summerclouds.common.core.util.IValuesProvider;

public class OnMatcherCondition implements SummerCondition {

	@Override
	public boolean matches(Annotation in, Object object) {
		SConditionalOnMatcher anno = (SConditionalOnMatcher)in;
		try {
			return new Condition(anno.value()).matches(new IValuesProvider() {
	
				@Override
				public Object get(String key) {
					return MSpring.getValue(key);
				}
				
			});
		} catch (Throwable t) {
			Log.getLog(OnMatcherCondition.class).e("failed to match condition {2} on {1}", object, anno.value());
		}
		return false;
	}

}
