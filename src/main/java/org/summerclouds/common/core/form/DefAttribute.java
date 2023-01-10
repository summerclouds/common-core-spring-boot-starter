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
package org.summerclouds.common.core.form;

import org.summerclouds.common.core.error.MException;
import org.summerclouds.common.core.form.definition.IDefAttribute;
import org.summerclouds.common.core.node.INode;
import org.summerclouds.common.core.tool.MCast;

public class DefAttribute implements IDefAttribute {

    private String name;
    private Object value;

    public DefAttribute(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public void inject(INode parent) throws MException {
        parent.setString(name, MCast.objectToString(value));
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }
}
