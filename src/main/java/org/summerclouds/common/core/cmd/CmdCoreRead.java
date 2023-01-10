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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.summerclouds.common.core.operation.OperationComponent;
import org.summerclouds.common.core.operation.cmd.CmdArgument;
import org.summerclouds.common.core.operation.cmd.CmdOperation;
import org.summerclouds.common.core.tool.MAscii;
import org.summerclouds.common.core.tool.MCast;

@OperationComponent(path = "core.read")
public class CmdCoreRead extends CmdOperation {

    @CmdArgument(index = 0)
    private String path;

    @Override
    protected String executeCmd() throws Exception {
        File f = new File(path);

        if (f.isFile()) {
            System.out.print(MAscii.NUL);
            System.out.print('f');
            System.out.println(f.getName());
            System.out.print(MAscii.NUL);
            try (InputStream is = new FileInputStream(f)) {
                while (true) {
                    int c = is.read();
                    if (c < 0) break;
                    System.out.print(MCast.toHex2LowerString(c));
                }
            } catch (IOException e) {
            }
            System.out.print(MAscii.NUL);
        }
        return null;
    }
}
