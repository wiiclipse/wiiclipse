package com.github.wiiclipse.launch;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class WiiClipseLaunchPreferenceInitializer extends
		AbstractPreferenceInitializer {

	public WiiClipseLaunchPreferenceInitializer() {
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore prefStore = WiiClipseLaunchPlugin.getDefault()
				.getPreferenceStore();

		prefStore
				.setDefault(
						WiiClipseLaunchConfigurationConstants.ATTR_CONNECTION_MODE,
						String.valueOf(WiiClipseLaunchConfigurationConstants.CONNECTION_MODE_USB_GECKO));
	}

}
