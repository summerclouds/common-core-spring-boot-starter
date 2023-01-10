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

import org.summerclouds.common.core.form.DefComponent;

public class IFmElement extends DefComponent {

    private static final long serialVersionUID = 1L;
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String TYPE = "type";
    public static final String NAME = "name";
    public static final String ENABLED = "enabled";
    public static final String SOURCES = "sources";
    public static final String FULLWIDTH = "fullwidth";
    public static final String TITLEINSIDE = "titleinside";
    public static final String DEFAULT = "default";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String ALLOW_NULL = "allow_null";
    public static final String HEIGHT = "height";
    public static final String WIZARD = "wizard";

    public static final String ELEMENT_NODE = "element";

    public IFmElement(String name, IDefAttribute... definitions) {
        super(ELEMENT_NODE, definitions);
        setString(NAME, name);
    }
}
