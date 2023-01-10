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

import org.summerclouds.common.core.M;
import org.summerclouds.common.core.consts.Identifier;

public class FmAction extends IFmElement {

    private static final long serialVersionUID = 1L;

    public FmAction(
            Identifier ident,
            String action,
            String title,
            String description,
            IDefAttribute... definitions) {
        this(M.n(ident), action, title, description, definitions);
    }

    public FmAction(
            String name,
            String action,
            String title,
            String description,
            IDefAttribute... definitions) {
        this(name, new FaNls(name + ".title=" + title, name + ".description=" + description));
        setString("action", action);
        addDefinition(definitions);
    }

    public FmAction(String name, IDefAttribute... definitions) {
        super(name, definitions);
        setString("type", "action");
    }
}
