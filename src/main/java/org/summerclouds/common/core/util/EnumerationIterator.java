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
package org.summerclouds.common.core.util;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.Enumeration;
import java.util.Iterator;

/**
 * An Iterator wrapper for an Enumeration.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: EnumerationIterator.java 463298 2006-10-12 16:10:32Z henning $
 * @param <T>
 */
public class EnumerationIterator<T> implements Iterator<T>, Iterable<T> {
    /** The enumeration to iterate. */
    @SuppressWarnings("rawtypes")
    private Enumeration enumeration = null;

    /**
     * Creates a new iteratorwrapper instance for the specified Enumeration.
     *
     * @param enumeration The Enumeration to wrap.
     */
    @SuppressWarnings("rawtypes")
    public EnumerationIterator(Enumeration enumeration) {
        this.enumeration = enumeration;
    }

    /**
     * Move to next element in the array.
     *
     * @return The next object in the array.
     */
    @Override
    @SuppressWarnings("unchecked")
    public T next() {
        return (T) enumeration.nextElement();
    }

    /**
     * Check to see if there is another element in the array.
     *
     * @return Whether there is another element.
     */
    @Override
    public boolean hasNext() {
        return enumeration.hasMoreElements();
    }

    /** Unimplemented. No analogy in Enumeration */
    @Override
    public void remove() {
        // not implemented
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }
}
