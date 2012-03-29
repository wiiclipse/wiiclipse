package com.github.wiiclipse.managedbuild.ui;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class WiiClipseIncludesPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createFieldEditors() {
		addField(new PathEditor("includes.path", "Include paths (-I)", "Add", getFieldEditorParent()));
		addField(new PathEditor("includes.files", "Include files (-include)", "Add", getFieldEditorParent()));

	}

}
