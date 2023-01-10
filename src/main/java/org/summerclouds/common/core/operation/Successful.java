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

import java.util.Map;

import org.summerclouds.common.core.error.RC;
import org.summerclouds.common.core.log.Log;

public class Successful extends MutableOperationResult {

    public static final String OK = "ok";

    public Successful(Operation operation, String msg) {
        super(operation, RC.OK, msg);
    }

    public Successful(Operation operation) {
        super(operation, RC.OK, OK);
    }

    public Successful(Operation operation, int rc, String msg, Object... parameters) {
        super(operation, rc, msg == null ? OK : msg, parameters);
    }

    public Successful(OperationDescription description) {
        super(description);
    }

    public Successful(String path, int rc, String msg, Object... parameters) {
        super(path, rc, msg == null ? OK : msg, parameters);
    }

    public Successful(Operation operation, String msg, Map<?, ?> result) {
        this(operation, RC.OK, msg == null ? OK : msg, result);
    }

    @SuppressWarnings("deprecation")
    public Successful(Operation operation, int rc, String msg, Map<?, ?> result) {
        setOperationPath(operation.getDescription().getPath());
        setMsg(msg == null ? OK : msg);
        setResult(result);
        setReturnCode(rc);
    }

    @SuppressWarnings("deprecation")
    public Successful(String path, int rc, String msg, Map<?, ?> result) {
        setOperationPath(path);
        setMsg(msg == null ? OK : msg);
        setResult(result);
        setReturnCode(rc);
    }

    public Successful(Operation operation, String msg, String result) {
        this(operation, RC.OK, msg, result);
    }

    @SuppressWarnings("deprecation")
    public Successful(Operation operation, int rc, String msg, String result) {
        setOperationPath(operation.getDescription().getPath());
        setMsg(msg == null ? OK : msg);
        setResult(result);
        setReturnCode(rc);
    }

    @SuppressWarnings("deprecation")
    public Successful(String path, int rc, String msg, String result) {
        setOperationPath(path);
        setMsg(msg == null ? OK : msg);
        setResult(result);
        setReturnCode(rc);
    }

    public Successful(String path) {
        this(path, RC.OK, OK, (String) null);
    }

    public Successful(String path, int rc, String msg) {
        this(path, rc, msg, (String) null);
    }

    @Override
    public void setReturnCode(int returnCode) {
        if (returnCode < 0) {
            Log.getLog(getClass())
                    .d("de.mhus.lib.core.operation.Successful: negative return code", returnCode);
            this.returnCode = RC.OK;
        } else if (returnCode > RC.RANGE_MAX_SUCCESSFUL) {
            Log.getLog(getClass())
                    .d("de.mhus.lib.core.operation.Successful: wrong return code", returnCode);
            this.returnCode = RC.OK;
        } else this.returnCode = returnCode;
    }
}
