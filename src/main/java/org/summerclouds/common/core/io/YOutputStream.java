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
package org.summerclouds.common.core.io;

import java.io.IOException;
import java.io.OutputStream;

public class YOutputStream extends OutputStream {

    private OutputStream[] streams;

    public YOutputStream(OutputStream... pStreams) {
        streams = pStreams;
    }

    @Override
    public void write(int b) throws IOException {
        for (int i = 0; i < streams.length; i++) streams[i].write(b);
    }

    @Override
    public void write(byte b[]) throws IOException {
        for (int i = 0; i < streams.length; i++) streams[i].write(b);
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        for (int i = 0; i < streams.length; i++) streams[i].write(b, off, len);
    }

    @Override
    public void close() throws IOException {
        for (int i = 0; i < streams.length; i++) streams[i].close();
    }
}
