package com.github.wiiclipse.managedbuild.ui;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class WiiClipseLibrariesPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createFieldEditors() {
		addField(new StringListEditor("libraries.libs", "Libraries (-l)",
				getFieldEditorParent()));
		addField(new PathEditor("libraries.paths", "Library search paths (-L)", "Add",
				getFieldEditorParent()));

	}

}
