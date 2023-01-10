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
package org.summerclouds.common.core.test.operations;

import org.summerclouds.common.core.operation.OperationComponent;
import org.summerclouds.common.core.operation.cmd.CmdArgument;
import org.summerclouds.common.core.operation.cmd.CmdOperation;
import org.summerclouds.common.core.operation.cmd.CmdOption;

@OperationComponent
public class Cmd2 extends CmdOperation {

    @CmdArgument(index = 0)
    private String argString;

    @CmdArgument(index = 1)
    private long argLong;

    @CmdArgument(index = 2)
    private int argInt;

    @CmdArgument(index = 3)
    private double argDouble;

    @CmdOption private String optString;

    @CmdOption private long optLong;

    @CmdOption private int optInt;

    @CmdOption private double optDouble;

    @Override
    protected String executeCmd() throws Exception {
        System.out.println("argString: " + argString);
        System.out.println("argLong: " + argLong);
        System.out.println("argInt: " + argInt);
        System.out.println("argDouble: " + argDouble);

        System.out.println("optString: " + optString);
        System.out.println("optLong: " + optLong);
        System.out.println("optInt: " + optInt);
        System.out.println("optDouble: " + optDouble);

        return "Finish";
    }
}
