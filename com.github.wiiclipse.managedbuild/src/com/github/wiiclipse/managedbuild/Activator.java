package com.github.wiiclipse.managedbuild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.CProjectDescriptionEvent;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescriptionListener;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.github.wiiclipse.core.WiiClipsePlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements ICProjectDescriptionListener {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.github.wiiclipse.managedbuild"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		CoreModel.getDefault().addCProjectDescriptionListener(this,
				CProjectDescriptionEvent.ABOUT_TO_APPLY);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		CoreModel.getDefault().removeCProjectDescriptionListener(this);
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
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
				if (WiiClipsePlugin.isWiiClipseConfig(config)) {
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
}
