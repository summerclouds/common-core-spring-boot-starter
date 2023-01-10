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
import org.summerclouds.common.core.error.MException;

public class FmCheckbox extends IFmElement {

    private static final long serialVersionUID = 1L;
    public static final String TYPE_CHECKBOX = "checkbox";

    public FmCheckbox(
            Identifier ident, String title, String description, IDefAttribute... definitions) {
        this(M.n(ident), title, description, definitions);
    }

    public FmCheckbox(String name, String title, String description, IDefAttribute... definitions) {
        this(name, new FaNls(title, description));
        addDefinition(definitions);
    }

    public FmCheckbox(String name, IDefAttribute... definitions) {
        super(name, definitions);
        setString(IFmElement.TYPE, TYPE_CHECKBOX);
    }

    public FmCheckbox defaultValue(boolean in) throws MException {
        setBoolean(IFmElement.DEFAULT, in);
        return this;
    }
}
