package org.summerclouds.common.core.log;

public interface IMutableParameterMapper {

    void clear();

    void put(String clazz, ParameterEntryMapper mapper);

}
