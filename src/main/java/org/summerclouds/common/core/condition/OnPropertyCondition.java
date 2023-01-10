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
