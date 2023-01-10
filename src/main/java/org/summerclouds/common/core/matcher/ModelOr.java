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
package org.summerclouds.common.core.matcher;

import org.summerclouds.common.core.util.IValuesProvider;

public class ModelOr extends ModelComposit {

    @Override
    public boolean matches(ModelPart model, IValuesProvider map, String str) {
        for (ModelPart part : components) {
            if (part.m(map, str)) return true;
        }
        return false;
    }

    @Override
    public String getOperatorName() {
        return "or";
    }

    @Override
    protected boolean matches(IValuesProvider map) {
        for (ModelPart part : components) {
            if (part.m(map)) return true;
        }
        return false;
    }
}
