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

public class FmNumber extends IFmElement {

    private static final long serialVersionUID = 1L;

    public enum TYPES {
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE
    }

    public enum FORMAT {
        PERCENTAGE,
        CURRENCY
    }

    public static final String TYPE_NUMBER = "number";
    public static final String ALLOW_NEGATIVE = "allow_negative";
    public static final String MINIMUM = "min";
    public static final String MAXIMUM = "max";
    public static final String FORMAT = "format";
    public static final String NUMBER_TYPE = "number";

    public FmNumber(
            Identifier ident,
            TYPES type,
            String title,
            String description,
            IDefAttribute... definitions) {
        this(M.n(ident), type, title, description, definitions);
    }

    public FmNumber(
            String name,
            TYPES type,
            String title,
            String description,
            IDefAttribute... definitions) {
        this(name, type, new FaNls(title, description));
        addDefinition(definitions);
    }

    public FmNumber(String name, TYPES type, IDefAttribute... definitions) {
        super(name, definitions);
        setString(IFmElement.TYPE, TYPE_NUMBER);
        setString(NUMBER_TYPE, type.name().toLowerCase());
    }

    public FmNumber allowNull(boolean in) throws MException {
        setBoolean(IFmElement.ALLOW_NULL, in);
        return this;
    }

    public FmNumber allowNegative(boolean in) throws MException {
        setBoolean(ALLOW_NEGATIVE, in);
        return this;
    }

    public FmNumber min(int min) throws MException {
        setInt(MINIMUM, min);
        return this;
    }

    public FmNumber max(int max) throws MException {
        setInt(MAXIMUM, max);
        return this;
    }

    public FmNumber format(FORMAT format) throws MException {
        setString(FORMAT, format.name());
        return this;
    }
}
