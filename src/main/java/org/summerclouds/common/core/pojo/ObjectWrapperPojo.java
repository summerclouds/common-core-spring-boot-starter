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
package org.summerclouds.common.core.pojo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.summerclouds.common.core.M;
import org.summerclouds.common.core.activator.Activator;
import org.summerclouds.common.core.util.MObjectInputStream;

public class ObjectWrapperPojo<T> {

    private T object;
    private byte[] stream;

    public ObjectWrapperPojo() {}

    public ObjectWrapperPojo(T object) {
        this.object = object;
    }

    public T pojoGetObject() throws IOException, ClassNotFoundException {
        return pojoGetObject(M.l(Activator.class));
    }

    @SuppressWarnings("unchecked")
    public synchronized T pojoGetObject(ClassLoader classLoader)
            throws IOException, ClassNotFoundException {
        if (object == null && stream != null) {

            ByteArrayInputStream in = new ByteArrayInputStream(stream);
            MObjectInputStream ois = new MObjectInputStream(in);
            ois.setClassLoader(classLoader);
            object = (T) ois.readObject();
            ois.close();

            stream = null;
        }
        return object;
    }

    public void pojoSetObject(T object) {
        this.object = object;
    }

    public byte[] getContent() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(object);
        return out.toByteArray();
    }

    public void setContent(byte[] stream) {
        this.stream = stream;
        this.object = null;
    }
}
