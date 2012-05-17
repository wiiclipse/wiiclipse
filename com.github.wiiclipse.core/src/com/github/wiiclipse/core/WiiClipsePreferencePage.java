package com.github.wiiclipse.core;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class WiiClipsePreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {
	public WiiClipsePreferencePage() {
		super(FieldEditorPreferencePage.GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(WiiClipseCorePlugin.getDefault()
				.getPreferenceStore());
		setDescription("General WiiClipse preferences:");
	}

	@Override
	protected void createFieldEditors() {
		addField(new DirectoryFieldEditor("devkitPPC.path", "devkitPPC.path",
				getFieldEditorParent()));
		addField(new DirectoryFieldEditor("libogc.path", "libogc.path",
				getFieldEditorParent()));
	}

}
