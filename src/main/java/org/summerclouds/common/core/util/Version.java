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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.summerclouds.common.core.error.MRuntimeException;
import org.summerclouds.common.core.error.RC;
import org.summerclouds.common.core.lang.Versioned;
import org.summerclouds.common.core.tool.MCast;
import org.summerclouds.common.core.tool.MString;

public class Version implements Comparable<Version>, Externalizable {

    public static final Version V_0_0_0 = new Version("0.0.0");
    public static final Version V_1_0_0 = new Version("1.0.0");
    public static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";

    private String original;
    private long[] versions;
    private VersionRange range;

    public Version(String in) {
        if (MString.isEmpty(in)) in = Versioned.DEFAULT_VERSION;

        this.original = in;
        // parse in
        // crop
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if (!(c == '.' || (c >= '0' && c <= '9'))) {
                in = in.substring(0, i);
                break;
            }
        }
        String[] parts = in.split("\\.");
        versions = new long[parts.length];
        for (int i = 0; i < parts.length; i++) versions[i] = MCast.tolong(parts[i], 0);
    }

    public int size() {
        if (versions == null) return 0;
        return versions.length;
    }

    public long getVersion(int index) {
        return versions[index];
    }

    @Override
    public String toString() {
        return MString.join(versions, '.');
    }

    @Override
    public boolean equals(Object in) {
        if (in == null) return false;
        if (in instanceof String) in = new Version((String) in);
        if (in instanceof Version) {
            Version v = (Version) in;
            if (v.size() != size()) return false;
            for (int i = 0; i < size(); i++) if (v.getVersion(i) != getVersion(i)) return false;
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Version o) {
        int len = Math.min(size(), o.size());
        for (int i = 0; i < len; i++) {
            long ov = o.getVersion(i);
            long v = getVersion(i);
            if (ov < v) return 1;
            if (ov > v) return -1;
        }
        if (size() > len) return 1;
        if (o.size() > len) return -1;
        return 0;
    }

    public VersionRange toRange() {
        if (range == null) range = new VersionRange(toString());
        return range;
    }

    public String getOriginal() {
        return original;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(1);
        out.writeObject(original);
        out.writeObject(versions);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readInt(); // 1
        original = (String) in.readObject();
        versions = (long[]) in.readObject();
    }

    public boolean isSnapshot() {
        return original.toUpperCase().endsWith(SNAPSHOT_SUFFIX);
    }

    public Version nextMajor() {
        if (versions.length < 1)
            throw new MRuntimeException(
                    RC.SYNTAX_ERROR,
                    "malformed version, can't create next major version",
                    original);
        long[] v2 = new long[versions.length];
        v2[0] = versions[0] + 1;
        return new Version(MString.join(v2, '.'));
    }

    public Version nextMinor() {
        if (versions.length < 2)
            throw new MRuntimeException(
                    RC.SYNTAX_ERROR,
                    "malformed version, can't create next minor version",
                    original);
        long[] v2 = new long[versions.length];
        v2[0] = versions[0];
        v2[1] = versions[1] + 1;
        return new Version(MString.join(v2, '.'));
    }

    public Version previousMajor() {
        if (versions.length < 1 || versions[0] <= 0)
            throw new MRuntimeException(
                    RC.SYNTAX_ERROR,
                    "malformed version, can't create previous major version",
                    original);
        long[] v2 = new long[versions.length];
        v2[0] = versions[0] - 1;
        return new Version(MString.join(v2, '.'));
    }

    public Version previousMinor() {
        if (versions.length < 2 || versions[1] <= 0)
            throw new MRuntimeException(
                    RC.SYNTAX_ERROR,
                    "malformed version, can't create previous minor version",
                    original);
        long[] v2 = new long[versions.length];
        v2[0] = versions[0];
        v2[1] = versions[1] - 1;
        return new Version(MString.join(v2, '.'));
    }

    public Version withoutSuffix() {
        return new Version(MString.join(versions, '.'));
    }
}

/*
Alternative:

aQute.libg.version

public class Version implements Comparable<Version> {
    final int                   major;
    final int                   minor;
    final int                   micro;
    final String                qualifier;
    public final static String  VERSION_STRING = "(\\d+)(\\.(\\d+)(\\.(\\d+)(\\.([-_\\da-zA-Z]+))?)?)?";
    public final static Pattern VERSION        = Pattern
                                                       .compile(VERSION_STRING);
    public final static Version LOWEST         = new Version();
    public final static Version HIGHEST        = new Version(Integer.MAX_VALUE,
                                                       Integer.MAX_VALUE,
                                                       Integer.MAX_VALUE,
                                                       "\uFFFF");

    public static final Version	emptyVersion	= LOWEST;

    public Version() {
        this(0);
    }

    public Version(int major, int minor, int micro, String qualifier) {
        this.major = major;
        this.minor = minor;
        this.micro = micro;
        this.qualifier = qualifier;
    }

    public Version(int major, int minor, int micro) {
        this(major, minor, micro, null);
    }

    public Version(int major, int minor) {
        this(major, minor, 0, null);
    }

    public Version(int major) {
        this(major, 0, 0, null);
    }

    public Version(String version) {
        Matcher m = VERSION.matcher(version);
        if (!m.matches())
            throw new IllegalArgumentException("Invalid syntax for version: "
                    + version);

        major = Integer.parseInt(m.group(1));
        if (m.group(3) != null)
            minor = Integer.parseInt(m.group(3));
        else
            minor = 0;

        if (m.group(5) != null)
            micro = Integer.parseInt(m.group(5));
        else
            micro = 0;

        qualifier = m.group(7);
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getMicro() {
        return micro;
    }

    public String getQualifier() {
        return qualifier;
    }

    public int compareTo(Version other) {
        if (other == this)
            return 0;

        if (!(other instanceof Version))
            throw new IllegalArgumentException(
                    "Can only compare versions to versions");

        Version o = (Version) other;
        if (major != o.major)
            return major - o.major;

        if (minor != o.minor)
            return minor - o.minor;

        if (micro != o.micro)
            return micro - o.micro;

        int c = 0;
        if (qualifier != null)
            c = 1;
        if (o.qualifier != null)
            c += 2;

        switch (c) {
        case 0:
            return 0;
        case 1:
            return 1;
        case 2:
            return -1;
        }
        return qualifier.compareTo(o.qualifier);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(major);
        sb.append(".");
        sb.append(minor);
        sb.append(".");
        sb.append(micro);
        if (qualifier != null) {
            sb.append(".");
            sb.append(qualifier);
        }
        return sb.toString();
    }

    public boolean equals(Object ot) {
        if ( ! (ot instanceof Version))
            return false;

        return compareTo((Version)ot) == 0;
    }

    public int hashCode() {
        return major * 97 ^ minor * 13 ^ micro
                + (qualifier == null ? 97 : qualifier.hashCode());
    }

    public int get(int i) {
        switch(i) {
        case 0 : return major;
        case 1 : return minor;
        case 2 : return micro;
        default:
            throw new IllegalArgumentException("Version can only get 0 (major), 1 (minor), or 2 (micro)");
        }
    }

    public static Version parseVersion(String version) {
		if (version == null) {
			return LOWEST;
		}

		version = version.trim();
		if (version.length() == 0) {
			return LOWEST;
		}

		return new Version(version);

    }
}

*/
