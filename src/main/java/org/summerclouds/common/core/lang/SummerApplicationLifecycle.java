package org.summerclouds.common.core.lang;

public interface SummerApplicationLifecycle {

	void onSummerApplicationStart() throws Exception;
	
	void onSummerApplicationStop() throws Exception;
	
}
