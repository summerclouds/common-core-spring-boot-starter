package org.summerclouds.common.core.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.summerclouds.common.core.operation.OperationComponent;
import org.summerclouds.common.core.operation.cmd.CmdArgument;
import org.summerclouds.common.core.operation.cmd.CmdOperation;
import org.summerclouds.common.core.tool.MAscii;
import org.summerclouds.common.core.tool.MFile;

@OperationComponent(path="core.cat")
public class CmdCoreCat extends CmdOperation {

	@CmdArgument(index = 0)
	private String path;

	@Override
	protected String executeCmd() throws Exception {
		File f = new File(path);

		if (f.isFile()) {
			try (InputStream is = new FileInputStream(f)) {
				MFile.copyFile(is, System.out);
			} catch (IOException e) {}
			System.out.print(MAscii.NUL);
		}
		return null;
	}

}
