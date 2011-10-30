package com.github.wiiclipse.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.CProjectDescriptionEvent;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescriptionListener;
import org.eclipse.cdt.core.settings.model.ICTargetPlatformSetting;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.github.wiiclipse.managedbuild.WiiClipseCExternalSettingsProvider;

public class WiiClipsePlugin extends AbstractUIPlugin implements
		ICProjectDescriptionListener {

	private static WiiClipsePlugin _plugin;

	public static final String ID = "com.github.wiiclipse.plugin";

	private static final String WIICLIPSE_TARGET_PLATFORM = "com.github.wiiclipse.toolchain.platform.base";

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
		_plugin = this;
		WiiClipsePathResolver.resolvePaths();
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

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		System.out.println("START");
		CoreModel.getDefault().addCProjectDescriptionListener(this,
				CProjectDescriptionEvent.ABOUT_TO_APPLY);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		System.out.println("STOP");
		CoreModel.getDefault().removeCProjectDescriptionListener(this);
		_plugin = null;
	}
}
