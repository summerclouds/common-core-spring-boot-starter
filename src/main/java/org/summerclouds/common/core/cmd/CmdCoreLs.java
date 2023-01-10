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
package org.summerclouds.common.core.cmd;

import java.io.File;

import org.summerclouds.common.core.console.ConsoleTable;
import org.summerclouds.common.core.operation.OperationComponent;
import org.summerclouds.common.core.operation.cmd.CmdArgument;
import org.summerclouds.common.core.operation.cmd.CmdOperation;

@OperationComponent(path = "core.ls")
public class CmdCoreLs extends CmdOperation {

    @CmdArgument(index = 0)
    private String path;

    @Override
    protected String executeCmd() throws Exception {
        File dir = new File(path);
        ConsoleTable t = new ConsoleTable();
        t.setHeaderValues("Name", "Directory");
        for (File sub : dir.listFiles()) {
            t.addRowValues(sub.getName(), sub.isDirectory());
        }
        t.print();
        return null;
    }
}
