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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.summerclouds.common.core.operation.OperationComponent;
import org.summerclouds.common.core.operation.cmd.CmdOperation;
import org.summerclouds.common.core.tool.MSpring;

@OperationComponent(path = "core.config.print")
public class CmdConfigPrint extends CmdOperation {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected String executeCmd() throws Exception {

        Environment env = MSpring.getEnvironment();
        Map<String, Object> map = new HashMap();
        for (Iterator it = ((AbstractEnvironment) env).getPropertySources().iterator();
                it.hasNext(); ) {
            PropertySource propertySource = (PropertySource) it.next();
            if (propertySource instanceof MapPropertySource) {
                map.putAll(((MapPropertySource) propertySource).getSource());
            }
        }
        for (Entry<String, Object> entry : map.entrySet())
            System.out.println(entry.getKey() + "=" + entry.getValue());
        return null;
    }
}
