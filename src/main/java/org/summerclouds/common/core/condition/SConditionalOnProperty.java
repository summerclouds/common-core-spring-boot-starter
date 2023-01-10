/**
 * Copyright (C) 2022 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// NO AUTO LICENSE
package org.summerclouds.common.core.condition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SummerConditional(OnPropertyCondition.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface SConditionalOnProperty {

    /**
     * Alias for {@link #name()}.
     *
     * @return the names
     */
    String[] value() default {};

    /**
     * A prefix that should be applied to each property. The prefix automatically ends with a dot if
     * not specified. A valid prefix is defined by one or more words separated with dots (e.g.
     * {@code "acme.system.feature"}).
     *
     * @return the prefix
     */
    String prefix() default "";

    /**
     * The name of the properties to test. If a prefix has been defined, it is applied to compute
     * the full key of each property. For instance if the prefix is {@code app.config} and one value
     * is {@code my-value}, the full key would be {@code app.config.my-value}
     *
     * <p>Use the dashed notation to specify each property, that is all lower case with a "-" to
     * separate words (e.g. {@code my-long-property}).
     *
     * @return the names
     */
    String[] name() default {};

    /**
     * The string representation of the expected value for the properties. If not specified, the
     * property must <strong>not</strong> be equal to {@code false}.
     *
     * @return the expected value
     */
    String havingValue() default "";

    /**
     * Specify if the condition should match if the property is not set. Defaults to {@code false}.
     *
     * @return if the condition should match if the property is missing
     */
    boolean matchIfMissing() default false;
}
