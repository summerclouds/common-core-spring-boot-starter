package org.summerclouds.common.core.cmd;

import java.util.Map.Entry;

import org.summerclouds.common.core.operation.OperationComponent;
import org.summerclouds.common.core.operation.cmd.CmdOperation;

@OperationComponent(path="core.properties.print")
public class CmdPropertiesPrint extends CmdOperation {

	@Override
	protected String executeCmd() throws Exception {
		for ( Entry<Object, Object> map : System.getProperties().entrySet())
			System.out.println(map.getKey() + "=" + map.getValue());
		return null;
	}

}
