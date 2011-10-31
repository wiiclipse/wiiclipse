package com.github.wiiclipse.core;

import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICTargetPlatformSetting;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class WiiClipsePlugin extends AbstractUIPlugin {

	private static WiiClipsePlugin _plugin;

	public static final String ID = "com.github.wiiclipse.plugin";

	private static final String WIICLIPSE_TARGET_PLATFORM = "com.github.wiiclipse";

	public static WiiClipsePlugin getDefault() {
		return _plugin;
	}

	public static boolean isWiiClipseConfig(ICConfigurationDescription config) {
		ICTargetPlatformSetting platform = config.getTargetPlatformSetting();
		if (platform == null)
			return false;

		return platform.getId().startsWith(WIICLIPSE_TARGET_PLATFORM);
	}

	public WiiClipsePlugin() {
		super();
		WiiClipsePathResolver.resolvePaths();
	}
	

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		_plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		_plugin = null;
		super.stop(context);
	}
}
