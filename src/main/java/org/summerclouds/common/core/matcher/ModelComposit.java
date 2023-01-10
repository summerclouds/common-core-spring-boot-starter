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

import java.util.LinkedList;

import org.summerclouds.common.core.tool.MString;
import org.summerclouds.common.core.util.IValuesProvider;

public abstract class ModelComposit extends ModelPart {

    protected LinkedList<ModelPart> components = new LinkedList<>();

    public void add(ModelPart part) {
        components.add(part);
    }

    public int size() {
        return components.size();
    }

    @Override
    protected abstract boolean matches(IValuesProvider map);

    @Override
    public String toString() {
        return (isNot() ? "!" : "")
                + "("
                + MString.join(components, " " + getOperatorName() + " ")
                + ")";
    }

    public abstract String getOperatorName();
}
