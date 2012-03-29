package com.github.wiiclipse.core;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class WiiClipsePreferenceInitializer extends
		AbstractPreferenceInitializer {

	public WiiClipsePreferenceInitializer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore prefStore = WiiClipseCorePlugin.getDefault()
				.getPreferenceStore();

		IPath path = new Path(
				prefStore.getString(WiiClipsePreferences.devkitPPC_Path));
		if (!checkDevkitPPCPath(path)) {
			prefStore.setDefault(WiiClipsePreferences.devkitPPC_Path,
					WiiClipsePathResolver.getDevkitPPCPath().toOSString());
		}

		path = new Path(prefStore.getString(WiiClipsePreferences.libogc_path));
		if (!checkDevkitPPCPath(path)) {
			prefStore.setDefault(WiiClipsePreferences.libogc_path,
					WiiClipsePathResolver.getLibOGCPath().toOSString());
		}

	}

	public static boolean checkDevkitPPCPath(IPath path) {
		if (path.isEmpty())
			return false;

		File f = path.toFile();
		if (!f.isDirectory())
			return false;

		return true;
	}
}
