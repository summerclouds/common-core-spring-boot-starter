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

import java.lang.reflect.InvocationTargetException;

public interface IFormInformation extends IFormProvider {

    /**
     * Get Action Handler Class or null
     *
     * @return Action handler
     */
    Class<? extends ActionHandler> getActionHandler();

    /**
     * Get Form Control Class or null
     *
     * @return Form Control
     */
    Class<? extends FormControl> getFormControl();

    default FormControl createFormControl()
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
                    InvocationTargetException, NoSuchMethodException, SecurityException {
        return getFormControl().getConstructor().newInstance();
    }

    default ActionHandler createActionHandler()
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
                    InvocationTargetException, NoSuchMethodException, SecurityException {
        return getActionHandler().getConstructor().newInstance();
    }
}
