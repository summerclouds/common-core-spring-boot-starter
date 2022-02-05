package org.sommerclouds.common.core.log;

public interface IMutableParameterMapper {

    void clear();

    void put(String clazz, ParameterEntryMapper mapper);

}
