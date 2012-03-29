package com.github.wiiclipse.managedbuild;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.core.templateengine.TemplateCore;
import org.eclipse.cdt.core.templateengine.process.ProcessArgument;
import org.eclipse.cdt.core.templateengine.process.ProcessFailureException;
import org.eclipse.cdt.core.templateengine.process.ProcessRunner;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

public abstract class ProcessRunnerHelper extends ProcessRunner {

	private Map<String, ProcessArgument> arguments = new HashMap<String, ProcessArgument>();
	private TemplateCore template;
	private IProgressMonitor monitor;
	private String processId;

	public ProcessRunnerHelper() {
		super();
	}

	@Override
	public final void process(TemplateCore template, ProcessArgument[] args,
			String processId, IProgressMonitor monitor)
			throws ProcessFailureException {

		for (ProcessArgument processArgument : args) {
			arguments.put(processArgument.getName(), processArgument);
		}

		this.monitor = monitor;
		if (this.monitor == null)
			this.monitor = new NullProgressMonitor();

		this.template = template;
		this.processId = processId;

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceDescription workspaceDesc = workspace.getDescription();
		boolean autoBuilding = workspaceDesc.isAutoBuilding();
		workspaceDesc.setAutoBuilding(false);
		try {
			workspace.setDescription(workspaceDesc);
		} catch (CoreException e) {// ignore
		}

		doProcess();

		workspaceDesc.setAutoBuilding(autoBuilding);
		try {
			workspace.setDescription(workspaceDesc);
		} catch (CoreException e) {// ignore
		}
		System.out.println();
	}

	protected void doProcess() throws ProcessFailureException {
	}

	protected String getSimple(String argName) {
		ProcessArgument arg = arguments.get(argName);
		return arg == null ? null : arg.getSimpleValue();
	}

	protected String[] getSimpleArray(String argName) {
		ProcessArgument arg = arguments.get(argName);
		return arg == null ? null : arg.getSimpleArrayValue();
	}

	protected ProcessArgument[] getComplex(String argName) {
		ProcessArgument arg = arguments.get(argName);
		return arg == null ? null : arg.getComplexValue();
	}

	protected ProcessArgument[][] getComplexArray(String argName) {
		ProcessArgument arg = arguments.get(argName);
		return arg == null ? null : arg.getComplexArrayValue();
	}

	protected TemplateCore getTemplate() {
		return template;
	}

	protected IProgressMonitor getMonitor() {
		return monitor;
	}

	protected String getProcessId() {
		return processId;
	}

}