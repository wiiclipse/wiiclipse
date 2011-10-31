package com.github.wiiclipse.launch;

import org.eclipse.cdt.debug.internal.ui.launch.CApplicationLaunchShortcut;
import org.eclipse.debug.core.ILaunchConfigurationType;

@SuppressWarnings("restriction")
public class WiiClipseLaunchShortcut extends CApplicationLaunchShortcut {
	/**
	 * Method getCLaunchConfigType.
	 * 
	 * @return ILaunchConfigurationType
	 */
	protected ILaunchConfigurationType getCLaunchConfigType() {
		return getLaunchManager().getLaunchConfigurationType(
				WiiClipseLaunchConfigurationConstants.ID_LAUNCH_C_APP);
	}

}
