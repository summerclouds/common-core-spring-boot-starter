package org.summerclouds.common.core.test.operations;

import org.summerclouds.common.core.operation.OperationComponent;
import org.summerclouds.common.core.operation.cmd.CmdArgument;
import org.summerclouds.common.core.operation.cmd.CmdOperation;
import org.summerclouds.common.core.operation.cmd.CmdOption;

@OperationComponent
public class Cmd2 extends CmdOperation {

	@CmdArgument(index = 0)
	private String argString;

	@CmdArgument(index = 1)
	private long argLong;

	@CmdArgument(index = 2)
	private int argInt;

	@CmdArgument(index = 3)
	private double argDouble;

	@CmdOption
	private String optString;

	@CmdOption
	private long optLong;

	@CmdOption
	private int optInt;

	@CmdOption
	private double optDouble;

	@Override
	protected String executeCmd() throws Exception {
		System.out.println("argString: " + argString);
		System.out.println("argLong: " + argLong);
		System.out.println("argInt: " + argInt);
		System.out.println("argDouble: " + argDouble);

		System.out.println("optString: " + optString);
		System.out.println("optLong: " + optLong);
		System.out.println("optInt: " + optInt);
		System.out.println("optDouble: " + optDouble);

		return "Finish";
	}

}
