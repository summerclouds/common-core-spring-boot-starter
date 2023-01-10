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
package org.summerclouds.common.core.pojo;

public class DefaultFilter implements PojoFilter {

    private boolean removeHidden;
    private boolean removeWriteOnly;
    private boolean removeReadOnly;
    private boolean removeEmbedded;
    private boolean removeNoActions;

    public DefaultFilter() {
        this(true, false, true, false, true);
    }

    public DefaultFilter(
            boolean removeHidden,
            boolean removeEmbedded,
            boolean removeWriteOnly,
            boolean removeReadOnly,
            boolean removeNoActions) {
        this.removeHidden = removeHidden;
        this.removeEmbedded = removeEmbedded;
        this.removeWriteOnly = removeWriteOnly;
        this.removeReadOnly = removeReadOnly;
        this.removeNoActions = removeNoActions;
    }

    @Override
    public void filter(PojoModelImpl model) {
        for (String name : model.getAttributeNames()) {
            PojoAttribute<?> attr = model.getAttribute(name);
            if (removeHidden && attr.getAnnotation(Hidden.class) != null) {
                model.removeAttribute(name);
            } else if (removeEmbedded && attr.getAnnotation(Embedded.class) != null) {
                model.removeAttribute(name);
            } else if (removeWriteOnly && !attr.canRead()) {
                model.removeAttribute(name);
            } else if (removeReadOnly && !attr.canWrite()) {
                model.removeAttribute(name);
            }
        }

        for (String name : model.getActionNames()) {
            PojoAction action = model.getAction(name);
            Action anno = action.getAnnotation(Action.class);
            if (removeNoActions && anno == null) model.removeAction(name);
            if (anno != null && anno.value().length() > 0) {
                // rename
                model.removeAction(name);
                model.addAction(anno.value(), action);
            }
        }
    }
}
