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
package org.summerclouds.common.core.condition;

import java.lang.annotation.Annotation;

import org.summerclouds.common.core.log.Log;
import org.summerclouds.common.core.matcher.Condition;
import org.summerclouds.common.core.tool.MSpring;
import org.summerclouds.common.core.util.IValuesProvider;

public class OnMatcherCondition implements SummerCondition {

    @Override
    public boolean matches(Annotation in, Object object) {
        SConditionalOnMatcher anno = (SConditionalOnMatcher) in;
        try {
            return new Condition(anno.value())
                    .matches(
                            new IValuesProvider() {

                                @Override
                                public Object get(String key) {
                                    return MSpring.getValue(key);
                                }
                            });
        } catch (Throwable t) {
            Log.getLog(OnMatcherCondition.class)
                    .e("failed to match condition {2} on {1}", object, anno.value());
        }
        return false;
    }
}
