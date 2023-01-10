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
package org.summerclouds.common.core.operation;

import java.util.HashSet;

import org.summerclouds.common.core.error.IResult;
import org.summerclouds.common.core.form.DefRoot;
import org.summerclouds.common.core.form.IFormProvider;
import org.summerclouds.common.core.log.MLog;
import org.summerclouds.common.core.nls.MNls;
import org.summerclouds.common.core.node.IProperties;
import org.summerclouds.common.core.node.MProperties;
import org.summerclouds.common.core.tool.MSecurity;
import org.summerclouds.common.core.tool.MString;
import org.summerclouds.common.core.tool.MTracing;
import org.summerclouds.common.core.tracing.IScope;
import org.summerclouds.common.core.util.Version;

public abstract class AbstractOperation extends MLog implements Operation {

    protected boolean strictParameterCheck = false;
    private Object owner;
    private OperationDescription description;
    private MNls nls;

    @Override
    public boolean hasAccess(TaskContext context) {
        return MSecurity.hasPermission(
                Operation.class, MSecurity.EXECUTE, getClass().getCanonicalName());
    }

    @Override
    public final OperationResult doExecute(TaskContext context) throws Exception {
        OperationDescription desc = getDescription();
        String name = desc == null ? getClass().getCanonicalName() : desc.getPathVersion();
        try (IScope scope = MTracing.enter("operation " + name)) {
            try {
                log().d("execute operation {1} with {2}", name, context.getParameters());
                OperationResult ret = execute(context);
                if (ret != null && !ret.isSuccessful()) scope.getSpan().setError(ret.getMessage());
                log().d("result", ret);
                return ret;
            } catch (Throwable e) {
                scope.getSpan().setError(e);
                try {
                    onError(e);
                } catch (Throwable e2) {
                }
                if (e instanceof IResult) return new NotSuccessful(this, (IResult) e);
                throw e;
            }
        }
    }

    protected void onError(Throwable e) {
        log().e("error while executing operation", getDescription().getPath(), e);
    }

    protected abstract OperationResult execute(TaskContext context) throws Exception;

    @Override
    public boolean isBusy() {
        synchronized (this) {
            return owner != null;
        }
    }

    @Override
    public boolean setBusy(Object owner) {
        synchronized (this) {
            if (this.owner != null) return false;
            this.owner = owner;
        }
        return true;
    }

    @Override
    public boolean releaseBusy(Object owner) {
        synchronized (this) {
            if (this.owner == null) return true;
            //			if (!this.owner.equals(owner)) return false;
            if (this.owner != owner) return false;
            this.owner = null;
        }
        return true;
    }

    @Override
    public boolean canExecute(TaskContext context) {
        if (getDescription() == null) return true; // no definition, no check
        return validateParameters(getDescription().getParameterDefinitions(), context);
    }

    @Override
    public OperationDescription getDescription() {
        if (description == null) description = createDescription();
        return description;
    }

    /**
     * Create and return a operation definition. The method is called only one time.
     *
     * @return
     */
    protected OperationDescription createDescription() {

        Class<?> clazz = this.getClass();
        String title = clazz.getName();
        Version version = null;
        DefRoot form = null;

        String path = clazz.getCanonicalName();

        org.summerclouds.common.core.operation.OperationComponent desc =
                getClass()
                        .getAnnotation(
                                org.summerclouds.common.core.operation.OperationComponent.class);
        if (desc != null) {
            if (MString.isSet(desc.title())) title = desc.title();
            if (desc.clazz() != Object.class) {
                clazz = desc.clazz();
                path = clazz.getCanonicalName();
            }
            if (MString.isSet(desc.path())) {
                path = desc.path();
            }
            if (MString.isSet(desc.version())) {
                version = new Version(desc.version());
            }
            strictParameterCheck = desc.strictParameterCheck();
        }

        form = createDescriptionForm();

        MProperties labels = null;
        if (desc != null) {
            labels = IProperties.explodeToMProperties(desc.labels());
        }
        OperationDescription ret =
                new OperationDescription(path, version, this, title, labels, form);
        prepareCreatedDescription(ret);
        return ret;
    }

    protected DefRoot createDescriptionForm() {
        if (this instanceof IFormProvider) {
            return ((IFormProvider) this).getForm();
        }
        return null;
    }

    /**
     * Overwrite to manipulate created description.
     *
     * @param desc
     */
    protected void prepareCreatedDescription(OperationDescription desc) {}

    public boolean validateParameters(ParameterDefinitions definitions, TaskContext context) {
        if (definitions == null) return true;
        HashSet<String> sendKeys = null;
        if (strictParameterCheck) sendKeys = new HashSet<>(context.getParameters().keys());
        for (ParameterDefinition def : definitions.values()) {
            Object v = context.getParameters().get(def.getName());
            if (def.isMandatory() && v == null) {
                context.addErrorMessage("Mandatory: " + def.getName());
                return false;
            }
            if (!def.validate(v)) {
                context.addErrorMessage("Not valid: " + def.getName());
                return false;
            }
            if (strictParameterCheck) sendKeys.remove(def.getName());
        }
        if (strictParameterCheck && sendKeys.size() != 0) {
            context.addErrorMessage("Unknown parameters: " + sendKeys);
            return false;
        }
        return true;
    }

    @Override
    public MNls getNls() {
        if (nls == null) nls = MNls.lookup(this);
        return nls;
    }

    @Override
    public String nls(String text) {
        return MNls.find(this, text);
    }
}
