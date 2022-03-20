package org.summerclouds.common.core.tracing;

public interface Getter<T> {
    T get(String key);
}