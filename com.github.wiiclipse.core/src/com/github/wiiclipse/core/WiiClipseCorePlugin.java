package com.github.wiiclipse.core;

import java.io.File;

import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICTargetPlatformSetting;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class WiiClipseCorePlugin extends AbstractUIPlugin {

	private static WiiClipseCorePlugin plugin;

	public static final String PLUGIN_ID = "com.github.wiiclipse.plugin";

	public static WiiClipseCorePlugin getDefault() {
		return plugin;
	}

	public WiiClipseCorePlugin() {
		super();
		WiiClipsePathResolver.resolvePaths();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		checkPreferences();
		IPreferenceStore prefStore = getPreferenceStore();
		prefStore.addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				checkPreferences();
			}
		});
	}

	private void checkPreferences() {
		IPreferenceStore prefStore = getPreferenceStore();
		// String devkitProPath = prefStore.getString("DEVKITPRO_PATH");
		IPath devkitProPath = new Path(prefStore.getString("DEVKITPRO_PATH"));
		if (!checkPath(devkitProPath)) {
			devkitProPath = WiiClipsePathResolver.getDevkitProPath();
			if (checkPath(devkitProPath)) {
				prefStore.setValue("DEVKITPRO_PATH", devkitProPath.toOSString());
			} else {
				addProblemMarker("Failed to locate devkitPro");
			}
		}
	}

	private boolean checkPath(IPath path) {
		if (path == null || path.isEmpty())
			return false;

		File file = path.toFile();
		if (!file.exists())
			return false;

		if (!file.isDirectory())
			return false;

		if (!file.isAbsolute())
			return false;

		return true;
	}

	private IMarker addProblemMarker(String message) {
		IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();
		IMarker marker = null;
		try {
			marker = wsRoot.createMarker("com.github.wiiclipse.core.marker");
			marker.setAttribute(IMarker.MESSAGE, message);
		} catch (CoreException e) {
		}
		return marker;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		IPreferenceStore prefStore = getPreferenceStore();
		super.stop(context);
	}
}
