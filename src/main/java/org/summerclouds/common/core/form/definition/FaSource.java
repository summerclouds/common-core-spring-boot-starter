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
package org.summerclouds.common.core.form.definition;

import org.summerclouds.common.core.error.MException;
import org.summerclouds.common.core.node.INode;
import org.summerclouds.common.core.node.MNode;

public class FaSource extends MNode implements IDefAttribute {

    private static final long serialVersionUID = 1L;
    private String tag;

    public FaSource(String tag, String name) {
        super(tag);
        this.tag = tag;
        setString("name", name);
    }

    @Override
    public void inject(INode root) throws MException {
        INode sources = root.getObject("sources");
        if (sources == null) {
            sources = root.createObject("sources");
        }
        sources.setObject(tag, this);
    }
}
