package com.github.wiiclipse.core;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class WiiClipsePreferences extends AbstractPreferenceInitializer {

	public static final String DEVKITPPC_PATH = "devkitPPC.path";
	public static final String LIBOGC_PATH = "libogc.path";

	public WiiClipsePreferences() {
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore prefStore = WiiClipseCorePlugin.getDefault()
				.getPreferenceStore();

		IPath path = new Path(
				prefStore.getString(WiiClipsePreferences.DEVKITPPC_PATH));
		if (!checkDevkitPPCPath(path)) {
			path = WiiClipsePathResolver.getDevkitPPCPath();
			if (checkDevkitPPCPath(path)) {
				prefStore.setDefault(WiiClipsePreferences.DEVKITPPC_PATH,
						path.toOSString());
			}
		}

		path = new Path(prefStore.getString(WiiClipsePreferences.LIBOGC_PATH));
		if (!isValidLibOGCPath(path)) {
			path = WiiClipsePathResolver.getLibOGCPath();
			if (isValidLibOGCPath(path)) {
				prefStore.setDefault(WiiClipsePreferences.LIBOGC_PATH,
						path.toOSString());
			}
		}

	}

	private static boolean isDirectory(IPath path) {
		if (path == null)
			return false;

		if (path.isEmpty())
			return false;

		File f = path.toFile();
		return f.isDirectory();
	}

	public static boolean isValidLibOGCPath(IPath path) {
		if (!isDirectory(path))
			return false;
		return isDirectory(path.append("include"))
				&& isDirectory(path.append("lib/wii"));
	}

	public static boolean checkDevkitPPCPath(IPath path) {
		if (!isDirectory(path))
			return false;
		return isDirectory(path.append("bin"));
	}

	public static void validate() {
		IPreferenceStore prefStore = WiiClipseCorePlugin.getDefault()
				.getPreferenceStore();

		Path path = new Path(
				prefStore.getString(WiiClipsePreferences.DEVKITPPC_PATH));
		if (!checkDevkitPPCPath(path)) {

		}
		path = new Path(prefStore.getString(LIBOGC_PATH));
		if (!isValidLibOGCPath(path)) {

		}
	}
}
