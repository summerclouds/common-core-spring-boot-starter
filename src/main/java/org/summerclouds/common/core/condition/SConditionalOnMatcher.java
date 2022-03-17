// NO AUTO LICENSE
package org.summerclouds.common.core.condition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Write a condition in summer condition style and match it against spring environent.
 * 
 * @author mikehummel
 *
 */
@SummerConditional(OnMatcherCondition.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface SConditionalOnMatcher {

	/**
	 * The mathcer condition.
	 * @return the condition
	 */
	String value();

}