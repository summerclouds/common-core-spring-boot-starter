package org.summerclouds.common.core.lang;

import java.util.Map;

public interface IThreadControl {

	void prepareNewThread(Map<String, Object> context);

	void cleanup();

	void initNewThread(Map<String, Object> context);

	void releaseThread(Map<String, Object> context);

}
