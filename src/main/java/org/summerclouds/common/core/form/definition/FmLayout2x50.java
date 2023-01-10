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

import org.summerclouds.common.core.form.DefAttribute;

public class FmLayout2x50 extends IFmElement {

    private static final long serialVersionUID = 1L;

    public FmLayout2x50(IDefDefinition... definitions) {
        this("", "", "", definitions);
    }

    public FmLayout2x50(
            String name, String title, String description, IDefDefinition... definitions) {
        super(name, new FaNls(title, description));
        addDefinition(new DefAttribute("layout", "50x50"));
        setString("type", "layout50x50");
        addDefinition(definitions);
    }
}
