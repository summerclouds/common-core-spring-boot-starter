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
import org.summerclouds.common.core.log.MLog;
import org.summerclouds.common.core.node.INode;

public abstract class UiComponent extends MLog {

    public static final String FULL_SIZE = "fullSize";
    public static final String FULL_SIZE_DEFAULT = "fullSizeDefault";
    private static final String WIZARD = "wizard";

    private MForm form;
    private INode config;

    public void doInit(MForm form, INode config) {
        this.form = form;
        this.config = config;
    }

    public MForm getForm() {
        return form;
    }

    public INode getConfig() {
        return config;
    }

    public abstract void doRevert() throws MException;

    public abstract void doUpdateValue() throws MException;

    public abstract void doUpdateMetadata() throws MException;

    public abstract void setVisible(boolean visible) throws MException;

    public abstract boolean isVisible() throws MException;

    public abstract void setEnabled(boolean enabled) throws MException;

    public abstract void setEditable(boolean editable) throws MException;

    public abstract boolean isEnabled() throws MException;

    public boolean isFullSize() {
        return config.getBoolean(FULL_SIZE, config.getBoolean(FULL_SIZE_DEFAULT, false));
    }

    public UiWizard getWizard() {
        Object obj = config.getProperty(WIZARD);
        if (obj == null) return null;
        if (obj instanceof UiWizard) return (UiWizard) obj;
        try {
            if (obj instanceof String)
                return getForm().getAdapterProvider().createWizard((String) obj);
        } catch (Exception e) {
            log().d(e);
        }
        return null; // TODO
    }

    public String getName() {
        return config.getString("name", "");
    }

    public abstract void setError(String error);

    public abstract void clearError();

    public String getConfigString(String name, String def) {
        INode c = getConfig();
        if (c == null) return def;
        return c.getString(name, def);
    }
}
