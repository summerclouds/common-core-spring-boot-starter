/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
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
import org.summerclouds.common.core.form.DefAttribute;
import org.summerclouds.common.core.node.INode;

public class FaNls extends DefAttribute {

    private String description;

    public FaNls(String title, String description) {
        super("title", title);
        this.description = description;
    }

    @Override
    public void inject(INode root) throws MException {
        super.inject(root);
        if (description != null) root.setString("description", description);
    }

    @Override
    public String toString() {
        return getName() + "->[" + getValue() + "," + description + "]";
    }
}
