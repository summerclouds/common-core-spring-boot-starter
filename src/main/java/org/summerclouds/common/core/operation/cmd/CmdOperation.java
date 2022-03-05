package org.summerclouds.common.core.operation.cmd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jline.reader.EndOfFileException;
import org.summerclouds.common.core.error.UsageException;
import org.summerclouds.common.core.form.DefAttribute;
import org.summerclouds.common.core.form.DefRoot;
import org.summerclouds.common.core.form.definition.FaMandatory;
import org.summerclouds.common.core.form.definition.FaMultiple;
import org.summerclouds.common.core.form.definition.FmCheckbox;
import org.summerclouds.common.core.form.definition.FmNumber;
import org.summerclouds.common.core.form.definition.FmOptions;
import org.summerclouds.common.core.form.definition.FmText;
import org.summerclouds.common.core.form.definition.IDefAttribute;
import org.summerclouds.common.core.form.definition.IDefDefinition;
import org.summerclouds.common.core.io.OutputStreamProxy;
import org.summerclouds.common.core.lang.ICloseable;
import org.summerclouds.common.core.operation.AbstractOperation;
import org.summerclouds.common.core.operation.OperationComponent;
import org.summerclouds.common.core.operation.OperationResult;
import org.summerclouds.common.core.operation.TaskContext;
import org.summerclouds.common.core.operation.util.SuccessfulMap;
import org.summerclouds.common.core.pojo.PojoAttribute;
import org.summerclouds.common.core.pojo.PojoModel;
import org.summerclouds.common.core.pojo.PojoParser;
import org.summerclouds.common.core.tool.MSecurity;
import org.summerclouds.common.core.tool.MSystem;

public abstract class CmdOperation extends AbstractOperation {

    public static final String PARAMETER_OUTPUT_STREAM = "_use_output_stream";
    public static final String PARAMETER_INPUT_STREAM = "_use_input_stream";
//    public static final String RESULT_THREAD = "_thread";
//    public static final String RESULT_OUTPUT_STERAM = "_output_stream";
	public static final String RESULT_OBJECT = "_result";
	public static final String RESULT_EXCEPTION = "_exception";
	
	protected TaskContext context;

	@Override
    public boolean hasAccess(TaskContext context) {
        return MSecurity.hasPermission(CmdOperation.class, MSecurity.EXECUTE, getClass().getCanonicalName());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected OperationResult execute(TaskContext context) throws Exception {
    	
    	this.context = context;
    	
		PojoModel model = new PojoParser().parse(getClass(), "_", CmdOption.class, CmdArgument.class).filter(true, true, false, true, false).getModel();
    	
    	for (PojoAttribute attr : model) {
    		CmdOption optionA = (CmdOption) attr.getAnnotation(CmdOption.class);
    		CmdArgument argumentA = (CmdArgument) attr.getAnnotation(CmdArgument.class);
    		if (optionA != null && argumentA != null)
    			throw new UsageException("attribute {1} can't be option and argument at the same time",attr);

    		if (optionA != null) {
    			String name = optionA.name().length() == 0 ? attr.getName() : optionA.name();
    			Object value = context.getParameters().get(name);
    			if (value != null)
    				attr.set(this, value, true);
    		} else {
    			String name = "" + argumentA.index();
    			Object value = context.getParameters().get(name);
    			if (value != null)
    				attr.set(this, value, true);
    		}
    	}
    	
    	OutputStream os = (OutputStream)context.getParameters().get(PARAMETER_OUTPUT_STREAM);
    	if (os == null) {
    		os = new OutputStreamProxy(System.out);
    		((OutputStreamProxy)os).setIgnoreClose(true);
    	} else
    		context.getParameters().remove(PARAMETER_OUTPUT_STREAM);
    	
    	InputStream is = (InputStream) context.getParameters().get(PARAMETER_INPUT_STREAM);
    	if (is == null) {
    		is = new InputStream() {
	
				@Override
				public int read() throws IOException {
					throw new EndOfFileException();
				}
	    		
	    	};
    	} else
    		context.getParameters().remove(PARAMETER_INPUT_STREAM);
    	
    	final SuccessfulMap ret = new SuccessfulMap(this, "ok");

		try (ICloseable ioEnv = MSystem.useIO(os, os, is)) {
			try {
				Object res = CmdOperation.this.executeCmd();
				ret.put(RESULT_OBJECT, res);
			} catch (Throwable t) {
				t.printStackTrace();
				ret.put(RESULT_EXCEPTION, t);
				throw t;
			}
		}

    	return ret;
    }

    protected void onError(Throwable e) {
	}

	protected abstract Object executeCmd() throws Exception;

	protected DefRoot createDescriptionForm() {
    	
		OperationComponent cmdDesc = getClass().getAnnotation(OperationComponent.class);
		String cmdName = cmdDesc.path();
		
		PojoModel model = new PojoParser().parse(getClass(), "_", CmdOption.class, CmdArgument.class).filter(true, true, false, true, false).getModel();
    	
    	ArrayList<IDefDefinition> definitions = new ArrayList<>();
    	Set<String> names = new HashSet<>();
    	for (PojoAttribute<?> attr : model) {
    		CmdOption optionA = attr.getAnnotation(CmdOption.class);
    		CmdArgument argumentA = attr.getAnnotation(CmdArgument.class);
    		if (optionA != null && argumentA != null)
    			throw new UsageException("attribute {1} can't be option and argument at the same time",attr);
    		
    		IDefDefinition def = null;
    		String name = null;
    		String title = null;
    		String description = null;
    		ArrayList<IDefAttribute> options = new ArrayList<>();
    		if (optionA != null) {
    			name = optionA.name().length() == 0 ? attr.getName() : optionA.name();
    			title = "option " + name;
    			description = optionA.description();
    			if (optionA.mandatory())
    				options.add(new FaMandatory());
    		} else {
    			name = "" + argumentA.index();
    			title = "argument " + name;
    			description = argumentA.description();
    			if (argumentA.mandatory())
    				options.add(new FaMandatory());
    		}
    		if (attr.getManagedClass().isArray() /*|| Collection.class.isAssignableFrom(attr.getManagedClass())*/ ) {
    				options.add(new FaMultiple());
    		}
    		if (attr.getManagedClass().isEnum()) {
    			options.add(new DefAttribute("values", Arrays.toString(attr.getManagedClass().getEnumConstants()) ));
    		}
    		IDefAttribute[] optionsArray = options.toArray(new IDefAttribute[options.size()]);
    		if (attr.getManagedClass() == Long.class || attr.getManagedClass() == long.class)
    			def = new FmNumber(name, FmNumber.TYPES.LONG, title , description, optionsArray );
    		else
    		if (attr.getManagedClass() == Integer.class || attr.getManagedClass() == int.class)
    			def = new FmNumber(name, FmNumber.TYPES.INTEGER, title , description, optionsArray );
    		else
    		if (attr.getManagedClass() == Double.class || attr.getManagedClass() == double.class)
    			def = new FmNumber(name, FmNumber.TYPES.INTEGER, title , description, optionsArray );
    		else
    		if (attr.getManagedClass() == Float.class || attr.getManagedClass() == float.class)
    			def = new FmNumber(name, FmNumber.TYPES.FLOAT, title , description, optionsArray );
    		else
    		if (attr.getManagedClass() == Boolean.class || attr.getManagedClass() == boolean.class)
    			def = new FmCheckbox(name, title , description, optionsArray );
    		else
    		if (attr.getManagedClass().isEnum())
    			def = new FmOptions(name, title , description, optionsArray );
    		else
    			def = new FmText(name, title , description, optionsArray );
    		
    		if (def != null) {
    			if (!names.add(name)) {
    				throw new UsageException("name {1} already defined",name);
    			}
    			definitions.add(def);
    		}
    	}
    	return new DefRoot(cmdName, definitions.toArray(new IDefDefinition[definitions.size()]));
    }

}
