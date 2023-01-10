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
package org.summerclouds.common.core.json;

import org.summerclouds.common.core.error.NotSupportedException;
import org.summerclouds.common.core.tool.MJson;

import com.fasterxml.jackson.databind.JsonNode;

public class JacksonTransformer extends TransformStrategy {

    @Override
    public Object jsonToPojo(JsonNode node, Class<?> type, TransformHelper helper)
            throws NotSupportedException {

        try {
            return MJson.getMapper().readerFor(type).readValue(node);
        } catch (Exception e) {
            throw new NotSupportedException(e);
        }
    }

    @Override
    public JsonNode pojoToJson(Object obj, TransformHelper helper) throws NotSupportedException {
        JsonNode x;
        try {
            x = MJson.load(MJson.getMapper().writeValueAsString(obj));
        } catch (Exception e) {
            throw new NotSupportedException(e);
        }
        return x;
    }
}
