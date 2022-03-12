package org.summerclouds.common.core.cmd;

import java.io.File;

import org.summerclouds.common.core.console.ConsoleTable;
import org.summerclouds.common.core.operation.OperationComponent;
import org.summerclouds.common.core.operation.cmd.CmdArgument;
import org.summerclouds.common.core.operation.cmd.CmdOperation;

@OperationComponent(path="core.ls")
public class CmdCoreLs extends CmdOperation {

	@CmdArgument(index = 0)
	private String path;
	
	@Override
	protected String executeCmd() throws Exception {
		File dir = new File(path);
		ConsoleTable t = new ConsoleTable();
		t.setHeaderValues("Name","Directory");
		for (File sub : dir.listFiles()) {
			t.addRowValues(sub.getName(),sub.isDirectory());
		}
		t.print();
		return null;
	}

}
