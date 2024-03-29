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
package org.summerclouds.common.core.resource;

import java.io.InputStream;
import java.net.URL;

import org.summerclouds.common.core.node.INode;

public interface IResourceProvider {

    /**
     * Return a requested resource.
     *
     * @param path The name or path to the resource.
     * @return The resource or null if not found.
     */
    INode getResourceByPath(String path);

    INode getResourceById(String id);

    String getName();

    InputStream getInputStream(String key);

    URL getUrl(String key);

    boolean isValid();
}
