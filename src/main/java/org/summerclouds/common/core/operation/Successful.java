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
package org.summerclouds.common.core.operation;

import java.util.HashMap;
import java.util.Map;

import org.summerclouds.common.core.error.RC;

public class Successful extends MutableOperationResult {


    public static final String OK = "ok";

    public Successful() {
        super();
    }

    public Successful(Operation operation) {
        super(operation, RC.OK, "ok");
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
        this(operation, msg == null ? OK : msg, 0, result);
    }

    @SuppressWarnings("deprecation")
    public Successful(Operation operation, String msg, int rc, Map<?, ?> result) {
        setOperationPath(operation.getDescription().getPath());
        setMsg(msg == null ? OK : msg);
        setResult(result);
        setReturnCode(rc);
    }

    @SuppressWarnings("deprecation")
    public Successful(String path, String msg, int rc, Map<?, ?> result) {
        setOperationPath(path);
        setMsg(msg == null ? OK : msg);
        setResult(result);
        setReturnCode(rc);
    }

    public Successful(Operation operation, String msg, String result) {
        this(operation, msg, 0, result);
    }

    @SuppressWarnings("deprecation")
    public Successful(Operation operation, String msg, int rc, String result) {
        setOperationPath(operation.getDescription().getPath());
        setMsg(msg == null ? OK : msg);
        setResult(result);
        setReturnCode(rc);
    }

    @SuppressWarnings("deprecation")
    public Successful(String path, String msg, int rc, String result) {
        setOperationPath(path);
        setMsg(msg == null ? OK : msg);
        setResult(result);
        setReturnCode(rc);
    }

    public Successful(Operation operation, String msg, String... keyValues) {
        this(operation.getDescription().getPath(), msg, 0, keyValues);
    }

    public Successful(String path) {
        this(path, OK, RC.OK);
    }

    @SuppressWarnings("deprecation")
    public Successful(String path, String msg, int rc, String... keyValues) {
        setOperationPath(path);
        setMsg(msg == null ? OK : msg);
        setReturnCode(rc);
        HashMap<Object, Object> r = new HashMap<>();
        if (keyValues != null) {
            for (int i = 0; i < keyValues.length - 1; i += 2)
                if (keyValues.length > i + 1) r.put(keyValues[i], keyValues[i + 1]);
            setResult(r);
        }
    }

    @Override
    public void setReturnCode(int returnCode) {
        if (returnCode < 0) {
            log().d("de.mhus.lib.core.operation.Successful: negative return code",returnCode);
            this.returnCode = RC.OK;
        } else
        if (returnCode > RC.RANGE_MAX_SUCCESSFUL) {
            log().d("de.mhus.lib.core.operation.Successful: wrong return code",returnCode);
            this.returnCode = RC.OK;
        } else
            this.returnCode = returnCode;
    }

}