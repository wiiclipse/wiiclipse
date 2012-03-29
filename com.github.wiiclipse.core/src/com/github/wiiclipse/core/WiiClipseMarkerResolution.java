package com.github.wiiclipse.core;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

public class WiiClipseMarkerResolution implements IMarkerResolution {

	@Override
	public String getLabel() {
		return "Change WiiClipse preferences";
	}

	@Override
	public void run(IMarker marker) {
		PreferenceDialog prefDialog = PreferencesUtil.createPreferenceDialogOn(
				PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				"com.github.wiiclipse.core.preferences", null, null);
		if (prefDialog != null)
			prefDialog.open();
	}
}
