package org.summerclouds.common.core.condition;

import java.lang.annotation.Annotation;

import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.summerclouds.common.core.tool.MSpring;
import org.summerclouds.common.core.tool.MSystem;

public class OnPropertyCondition implements SummerCondition {

	@Override
	public boolean matches(Annotation in, Object object) {
		SConditionalOnProperty anno = (SConditionalOnProperty) in;

		String prefix = anno.prefix();
		if (StringUtils.hasText(prefix) && !prefix.endsWith(".")) {
			prefix = prefix + ".";
		}
		String havingValue = anno.havingValue();
		String[] names = anno.name().length == 0 ? anno.value() : anno.name();
		boolean matchIfMissing = anno.matchIfMissing();
		Environment env = MSpring.getEnvironment();

		for (String name : names) {
			String key = prefix + name;
			if (env.containsProperty(key)) {
				if (!MSystem.equals(env.getProperty(key), havingValue)) {
					return false;
				}
			} else {
				if (!matchIfMissing) {
					return false;
				}
			}
		}

		return true;
	}

}
