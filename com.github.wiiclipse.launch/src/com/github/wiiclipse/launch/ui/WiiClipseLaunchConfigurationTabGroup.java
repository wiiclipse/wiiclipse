package com.github.wiiclipse.launch.ui;

import org.eclipse.cdt.launch.ui.CArgumentsTab;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class WiiClipseLaunchConfigurationTabGroup extends
		AbstractLaunchConfigurationTabGroup {

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		// @formatter:off
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				new WiiClipseLaunchConfigurationTab(),
				new CArgumentsTab(),
				new CommonTab()
		};
		setTabs(tabs);
		// @formatter:on
	}

}
