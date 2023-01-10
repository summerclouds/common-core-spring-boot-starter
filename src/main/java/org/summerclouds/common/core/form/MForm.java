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

import java.util.Locale;

import org.summerclouds.common.core.M;
import org.summerclouds.common.core.error.MException;
import org.summerclouds.common.core.log.MLog;
import org.summerclouds.common.core.nls.MNls;
import org.summerclouds.common.core.nls.MNlsBundle;
import org.summerclouds.common.core.nls.MNlsProvider;
import org.summerclouds.common.core.node.INode;

/**
 * Represent a read only Form object. If you wan to modify the form cast it to MutableMForm if
 * possible. Not all forms are mutable.
 *
 * @author mikehummel
 */
public class MForm extends MLog implements MNlsProvider {

    protected Locale locale = Locale.getDefault();
    protected ComponentAdapterProvider adapterProvider;
    protected INode model;
    protected DataSource dataSource;
    protected ActionHandler actionHandler;
    protected MNlsBundle nlsBundle;
    protected FormControl control;
    protected UiInformation informationPane;
    protected IUiBuilder builder;

    public MForm() {}

    public MForm(Locale locale, ComponentAdapterProvider adapterProvider, INode model) {
        if (locale != null) this.locale = locale;
        this.adapterProvider = adapterProvider;
        if (model == null) new NullPointerException("model could not be null");
        this.model = model;
    }

    public MForm(DefRoot model) throws MException {
        this.model = model;
        model.build();
    }

    public Locale getLocale() {
        return locale;
    }

    public ComponentAdapterProvider getAdapterProvider() {
        return adapterProvider;
    }

    public INode getModel() {
        return model;
    }

    public DataSource getDataSource() {
        if (dataSource == null) dataSource = M.l(DataSource.class);
        return dataSource;
    }

    @Override
    public synchronized MNls getNls() {
        if (nlsBundle == null) {
            //			nlsBundle = base(MNlsBundle.class);
            return null;
        }
        return nlsBundle.getNls(locale);
    }

    public FormControl getControl() {
        if (control == null) control = M.l(FormControl.class);
        return control;
    }

    public UiInformation getInformationPane() {
        return informationPane;
    }

    public MNlsBundle getNlsBundle() {
        return nlsBundle;
    }

    public ActionHandler getActionHandler() {
        return actionHandler;
    }

    public IUiBuilder getBuilder() {
        return builder;
    }
}
