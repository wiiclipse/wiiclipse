package com.github.wiiclipse.launch;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	@Override
	public void init(IWorkbench workbench) {

		IPreferenceStore prefStore = WiiClipseLaunchPlugin.getDefault()
				.getPreferenceStore();
		setPreferenceStore(prefStore);

	}

	@Override
	protected void createFieldEditors() {
		addField(new RadioGroupFieldEditor(
				WiiClipseLaunchConfigurationConstants.ATTR_CONNECTION_MODE,
				"Connection mode", 1, new String[][] { { "TCP/IP", "tcp_ip" },
						{ "USB Gecko", String.valueOf(WiiClipseLaunchConfigurationConstants.CONNECTION_MODE_USB_GECKO) } }, getFieldEditorParent(), true));
	}
}
