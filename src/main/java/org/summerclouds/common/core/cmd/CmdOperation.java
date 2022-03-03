package org.summerclouds.common.core.cmd;

import org.summerclouds.common.core.form.DefRoot;
import org.summerclouds.common.core.form.IFormProvider;
import org.summerclouds.common.core.log.MLog;
import org.summerclouds.common.core.nls.MNls;
import org.summerclouds.common.core.node.IProperties;
import org.summerclouds.common.core.node.MProperties;
import org.summerclouds.common.core.operation.Operation;
import org.summerclouds.common.core.operation.OperationDescription;
import org.summerclouds.common.core.operation.OperationResult;
import org.summerclouds.common.core.operation.TaskContext;
import org.summerclouds.common.core.tool.MSecurity;
import org.summerclouds.common.core.tool.MString;
import org.summerclouds.common.core.util.Version;

public abstract class CmdOperation extends MLog implements Operation {

	protected MNls nls;

	@Override
	public MNls getNls() {
        if (nls == null) nls = MNls.lookup(this);
        return nls;
	}

	@Override
	public String nls(String text) {
        return MNls.find(this, text);
	}

    @Override
    public boolean hasAccess(TaskContext context) {
        return MSecurity.hasPermission(CmdOperation.class, MSecurity.EXECUTE, getClass().getCanonicalName());
    }

	@Override
	public boolean canExecute(TaskContext context) {
		return false;
	}

	@Override
	public OperationDescription getDescription() {

        Class<?> clazz = this.getClass();
        String title = clazz.getName();
        Version version = null;
        DefRoot form = null;

        String path = clazz.getCanonicalName();

        org.summerclouds.common.core.operation.OperationComponent desc =
                getClass()
                        .getAnnotation(org.summerclouds.common.core.operation.OperationComponent.class);
        if (desc != null) {
            if (MString.isSet(desc.title())) title = desc.title();
            if (desc.clazz() != Object.class) {
                clazz = desc.clazz();
                path = clazz.getCanonicalName();
            }
            if (MString.isSet(desc.path())) {
                path = desc.path();
            }
            if (MString.isSet(desc.version())) {
                version = new Version(desc.version());
            }
        }

        if (this instanceof IFormProvider) {
            form = ((IFormProvider) this).getForm();
        }

        MProperties labels = null;
        if (desc != null) {
            labels = IProperties.explodeToMProperties(desc.labels());
        }
        OperationDescription ret =
                new OperationDescription(path, version, this, title, labels, form);
        return ret;
    }
	
	
	@Override
	public OperationResult doExecute(TaskContext context) throws Exception {

		return null;
	}

    public abstract Object execute() throws Exception;

	@Override
	public boolean isBusy() {
		return false;
	}

	@Override
	public boolean setBusy(Object owner) {
		return false;
	}

	@Override
	public boolean releaseBusy(Object owner) {
		return false;
	}

}
