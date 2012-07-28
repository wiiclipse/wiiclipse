package com.github.wiiclipse.launch;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.wiiclipse.core.WiiClipseCorePlugin;
import com.github.wiiclipse.core.WiiClipsePreferences;

public class WiiClipseLaunchPreferences extends AbstractPreferenceInitializer {

	public static final String CONNECTION_MODE = "Connection.Mode";
	public static final int CONNECTION_MODE_TCP_IP = 0;
	public static final int CONNECTION_MODE_USB_GECKO = 1;
	public static final int CONNECTION_MODE_DEFAULT = CONNECTION_MODE_TCP_IP;

	public static final String HOSTNAME = "Connection.Hostname";
	public static final String HOSTNAME_DEFAULT = "192.168.0.1";

	public static final String DEVICE = "Connection.Device";
	public static final String DEVICE_DEFAULT = "/dev/ttyUSB0";

	public static final String DEST_PATH = "Connection.DestPath";
	public static final String DEST_PATH_DEFAULT = "";
	
	public static final String WIILOAD_PATH = "wiiload.path";
	public static final String ID_LAUNCH_C_APP = "com.github.wiiclipse.launch.config";

	public WiiClipseLaunchPreferences() {
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore prefStore = WiiClipseLaunchPlugin.getDefault()
				.getPreferenceStore();

		int mode = WiiClipseLaunchPreferences.CONNECTION_MODE_TCP_IP;
		String hostname = WiiClipseLaunchPreferences.HOSTNAME_DEFAULT;
		String device = WiiClipseLaunchPreferences.DEVICE_DEFAULT;

		String envVar = System.getenv("WIILOAD");
		if (envVar != null && envVar.length() != 0) {
			if (envVar.toLowerCase().startsWith("tcp:")) {
				mode = WiiClipseLaunchPreferences.CONNECTION_MODE_TCP_IP;
				hostname = envVar.substring(4);
			} else {
				mode = WiiClipseLaunchPreferences.CONNECTION_MODE_USB_GECKO;
				device = envVar;
			}
		}

		prefStore.setDefault(WiiClipseLaunchPreferences.CONNECTION_MODE, mode);
		prefStore.setDefault(WiiClipseLaunchPreferences.HOSTNAME, hostname);
		prefStore.setDefault(WiiClipseLaunchPreferences.DEVICE, device);

		IPreferenceStore corePrefs = WiiClipseCorePlugin.getDefault()
				.getPreferenceStore();
		String devkitPPCPath = corePrefs
				.getString(WiiClipsePreferences.DEVKITPPC_PATH);
		if (devkitPPCPath != null) {
			IPath p = new Path(devkitPPCPath).append("bin");
			if (p.toFile().exists()) {
				prefStore.setDefault(WiiClipseLaunchPreferences.WIILOAD_PATH,
						p.toOSString());
			}
		}

	}
}
