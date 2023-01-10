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

import org.summerclouds.common.core.nls.MNlsProvider;
import org.summerclouds.common.core.nls.Nls;

public interface Operation extends MNlsProvider, Nls {

    boolean hasAccess(TaskContext context);

    boolean canExecute(TaskContext context);

    OperationDescription getDescription();

    OperationResult doExecute(TaskContext context) throws Exception;

    boolean isBusy();

    boolean setBusy(Object owner);

    boolean releaseBusy(Object owner);
}
