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

import java.util.LinkedList;
import java.util.Properties;

import org.summerclouds.common.core.error.MException;
import org.summerclouds.common.core.form.definition.IDefDefinition;
import org.summerclouds.common.core.node.INode;
import org.summerclouds.common.core.node.MNode;

public class DefComponent extends MNode implements IDefDefinition {

    private static final long serialVersionUID = 1L;
    protected LinkedList<IDefDefinition> definitions = new LinkedList<IDefDefinition>();

    public DefComponent(String tag, IDefDefinition... definitions) {
        super(tag);
        addDefinition(definitions);
    }

    public DefComponent addAttribute(String name, Object value) {
        return addDefinition(new DefAttribute(name, value));
    }

    public DefComponent addDefinition(IDefDefinition... def) {
        if (def == null) return this;
        for (IDefDefinition d : def) if (d != null) definitions.add(d);
        return this;
    }

    public LinkedList<IDefDefinition> definitions() {
        return definitions;
    }

    @Override
    public void inject(INode parent) throws MException {
        if (parent != null) {
            parent.getArrayOrCreate(getName()).add(this);
            // parent.setObject(tag, this);
        }
        for (IDefDefinition d : definitions) {
            d.inject(this);
        }
    }

    public void fillNls(Properties p) throws MException {

        String nls = getString("nls", null);
        if (nls == null) nls = getString("name", null);
        if (nls != null && isProperty("title")) {
            p.setProperty(nls + "_title", getString("title", null));
        }
        if (nls != null && isProperty("description")) {
            p.setProperty(nls + "_description", getString("description", null));
        }

        fill(this, p);
    }

    private void fill(INode config, Properties p) throws MException {
        for (INode c : config.getObjects()) {
            if (c instanceof DefComponent) ((DefComponent) c).fillNls(p);
            else fill(c, p);
        }
    }
}
