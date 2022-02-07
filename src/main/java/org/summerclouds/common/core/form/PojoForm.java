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
package org.summerclouds.common.core.form;

import org.summerclouds.common.core.nls.MNlsBundle;
import org.summerclouds.common.core.node.INode;
import org.summerclouds.common.core.pojo.DefaultFilter;
import org.summerclouds.common.core.pojo.PojoAction;
import org.summerclouds.common.core.pojo.PojoModel;
import org.summerclouds.common.core.pojo.PojoParser;

public class PojoForm extends MutableMForm {

    public PojoForm(PojoProvider pojo) throws Exception {
        this(pojo, "");
        // setNlsBundle(new MNlsFactory().setOwner(pojo.getPojo()));
    }

    public PojoForm(PojoProvider pojo, String modelName) throws Exception {
        model = createModel(pojo.getPojo(), modelName);

        if (pojo instanceof FormControl) setControl((FormControl) pojo);

        if (pojo instanceof DataSource) setDataSource((DataSource) pojo);
        else setDataSource(new ModelDataSource(new PojoDataSource(pojo)));
    }

    protected INode createModel(Object pojo, String modelName) throws Exception {

        PojoModel pojoModel =
                new PojoParser()
                        .parse(pojo)
                        .filter(new DefaultFilter(true, false, true, false, false))
                        .getModel();

        DefRoot definition = null;
        MNlsBundle nls = null;
        FormControl control = null;
        // looking for models
        for (String actionName : pojoModel.getActionNames()) {
            PojoAction action = pojoModel.getAction(actionName);
            if (action.getAnnotation(ALayoutModel.class) != null) {
                if (((ALayoutModel) action.getAnnotation(ALayoutModel.class))
                                .value()
                                .equals(modelName)
                        && action.getReturnType().equals(DefRoot.class)) {
                    definition = ((DefRoot) action.doExecute(pojo));
                } else if (((ALayoutModel) action.getAnnotation(ALayoutModel.class))
                                .value()
                                .equals(modelName)
                        && action.getReturnType() == MNlsBundle.class) {
                    nls = (MNlsBundle) action.doExecute(pojo);
                } else if (((ALayoutModel) action.getAnnotation(ALayoutModel.class))
                                .value()
                                .equals(modelName)
                        && action.getReturnType() == FormControl.class) {
                    control = (FormControl) action.doExecute(pojo);
                }
            }
        }
        if (nls != null) setNlsBundle(nls);
        if (control != null) setControl(control);
        if (definition != null) definition.build();
        return definition;
    }
}
