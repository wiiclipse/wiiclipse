package com.github.wiiclipse.managedbuild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.cdt.core.CProjectNature;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.CProjectDescriptionEvent;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescriptionListener;
import org.eclipse.cdt.core.settings.model.ICTargetPlatformSetting;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.github.wiiclipse.core.WiiClipseCorePlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class WiiClipseManagedBuildPlugin extends AbstractUIPlugin implements
		ICProjectDescriptionListener {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.github.wiiclipse.managedbuild"; //$NON-NLS-1$

	private static final String WIICLIPSE_TARGET_PLATFORM = "com.github.wiiclipse";

	// The shared instance
	private static WiiClipseManagedBuildPlugin plugin;

	/**
	 * The constructor
	 */
	public WiiClipseManagedBuildPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static WiiClipseManagedBuildPlugin getDefault() {
		return plugin;
	}

	@Override
	public void handleEvent(CProjectDescriptionEvent event) {
		ICProjectDescription projDesc = event.getNewCProjectDescription();
		ICConfigurationDescription[] configurations = projDesc
				.getConfigurations();
		for (ICConfigurationDescription config : configurations) {
			System.out.println(config);
			if (config.isValid()) {
				List<String> extSettingProviders = new ArrayList<String>(
						Arrays.asList(config.getExternalSettingsProviderIds()));
				if (isWiiClipseConfig(config)) {
					if (!extSettingProviders
							.contains(WiiClipseCExternalSettingsProvider.ID)) {
						extSettingProviders
								.add(WiiClipseCExternalSettingsProvider.ID);
						config.setExternalSettingsProviderIds(extSettingProviders
								.toArray(new String[0]));
						config.updateExternalSettingsProviders(extSettingProviders
								.toArray(new String[0]));
					}
				} else if (extSettingProviders
						.contains(WiiClipseCExternalSettingsProvider.ID)) {
					extSettingProviders
							.remove(WiiClipseCExternalSettingsProvider.ID);
					config.setExternalSettingsProviderIds(extSettingProviders
							.toArray(new String[0]));
					config.updateExternalSettingsProviders(extSettingProviders
							.toArray(new String[0]));
				}
			}
		}
	}

	public static boolean isWiiClipseConfig(ICConfigurationDescription config) {
		ICTargetPlatformSetting platform = config.getTargetPlatformSetting();
		if (platform == null)
			return false;

		return platform.getId().startsWith(WIICLIPSE_TARGET_PLATFORM);
	}
}
