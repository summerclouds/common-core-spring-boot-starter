package org.summerclouds.common.core.cmd;

import java.util.Map.Entry;

import org.summerclouds.common.core.operation.OperationComponent;
import org.summerclouds.common.core.operation.cmd.CmdOperation;

@OperationComponent(path="core.env.print")
public class CmdEnvPrint extends CmdOperation {

	@Override
	protected String executeCmd() throws Exception {
		for ( Entry<String, String> map : System.getenv().entrySet())
			System.out.println(map.getKey() + "=" + map.getValue());
		return null;
	}

}
