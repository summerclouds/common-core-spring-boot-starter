package org.summerclouds.common.core.cmd;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.summerclouds.common.core.operation.OperationComponent;
import org.summerclouds.common.core.operation.cmd.CmdOperation;
import org.summerclouds.common.core.tool.MSpring;

@OperationComponent(path="core.config.print")
public class CmdConfigPrint extends CmdOperation {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected String executeCmd() throws Exception {

		Environment env = MSpring.getEnvironment();
		Map<String, Object> map = new HashMap();
        for(Iterator it = ((AbstractEnvironment) env).getPropertySources().iterator(); it.hasNext(); ) {
            PropertySource propertySource = (PropertySource) it.next();
            if (propertySource instanceof MapPropertySource) {
                map.putAll(((MapPropertySource) propertySource).getSource());
            }
        }
		for ( Entry<String, Object> entry : map.entrySet())
			System.out.println(entry.getKey() + "=" + entry.getValue());
		return null;
	}

}
