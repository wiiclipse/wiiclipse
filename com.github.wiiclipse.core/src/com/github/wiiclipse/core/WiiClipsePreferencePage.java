package com.github.wiiclipse.core;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
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
		setDescription(Messages.WiiClipsePreferencePageDescription);
	}

	@Override
	protected void createFieldEditors() {
		addField(new DirectoryFieldEditor(WiiClipsePreferences.DEVKITPPC_PATH,
				Messages.WiiClipsePreferencePageDevkitPPCPathLabel,
				getFieldEditorParent()));
		addField(new DirectoryFieldEditor(WiiClipsePreferences.LIBOGC_PATH,
				Messages.WiiClipsePreferencePageLibogcPathLabel,
				getFieldEditorParent()));
	}

}
