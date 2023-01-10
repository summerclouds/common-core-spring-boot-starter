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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.summerclouds.common.core.nls.MNls;
import org.summerclouds.common.core.nls.MNlsProvider;
import org.summerclouds.common.core.tool.MSystem;

public class Item implements Externalizable {

    private static final long serialVersionUID = 1L;
    private String key;
    private String caption;
    private MNlsProvider provider;
    private String parent;

    public Item() {}

    public Item(String parent, String key, String caption) {
        this.key = key;
        this.caption = caption;
        this.parent = parent;
    }

    public Item(String key, String caption) {
        this.key = key;
        this.caption = caption;
    }

    public void setNlsProvider(MNlsProvider provider) {
        this.provider = provider;
    }

    @Override
    public String toString() {
        return MNls.find(provider, caption);
    }

    public String getKey() {
        return key;
    }

    public String getParent() {
        return parent;
    }

    public String getCaption() {
        return caption;
    }

    @Override
    public boolean equals(Object in) {
        if (in instanceof Item) return MSystem.equals(((Item) in).getKey(), key);
        return key.equals(in);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(key);
        out.writeObject(caption);
        out.writeObject(parent);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        key = (String) in.readObject();
        caption = (String) in.readObject();
        parent = (String) in.readObject();
    }
}
