package com.github.wiiclipse.managedbuild;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.core.settings.model.CExternalSetting;
import org.eclipse.cdt.core.settings.model.CIncludePathEntry;
import org.eclipse.cdt.core.settings.model.CLibraryPathEntry;
import org.eclipse.cdt.core.settings.model.CMacroEntry;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICSettingEntry;
import org.eclipse.cdt.core.settings.model.extension.CExternalSettingProvider;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

public class WiiClipseCExternalSettingsProvider extends
		CExternalSettingProvider {

	private static CExternalSetting __includePathSetting;
	private static CExternalSetting __libraryPathSetting;
	private static CExternalSetting __macroSetting;

	public static final String ID = "com.github.wiiclipse.extSettings";

	static {
		{
			// include paths
			List<CIncludePathEntry> includePathEntries = new ArrayList<CIncludePathEntry>();
			IPath libOGCIncludePath = WiiClipsePathResolver
					.getLibOGCInlcudePath();
			if (libOGCIncludePath != null)
				includePathEntries.add(new CIncludePathEntry(libOGCIncludePath,
						0));

			__includePathSetting = new CExternalSetting(new String[] {
					"org.eclipse.cdt.core.gcc", "org.eclipse.cdt.core.g++" },
					new String[] { "org.eclipse.cdt.core.cSource",
							"org.eclipse.cdt.core.cxxSource" }, null,
					includePathEntries
							.toArray(new CIncludePathEntry[includePathEntries
									.size()]));
		}

		{
			List<CLibraryPathEntry> libraryPathEntries = new ArrayList<CLibraryPathEntry>();
			IPath libOGCLibPath = WiiClipsePathResolver.getLibOGCLibPath();
			if (libOGCLibPath != null)
				libraryPathEntries.add(new CLibraryPathEntry(libOGCLibPath, 0));

			__libraryPathSetting = new CExternalSetting(
					null,
					new String[] { "org.eclipse.cdt.managedbuilder.core.compiledObjectFile" },
					null, libraryPathEntries
							.toArray(new CLibraryPathEntry[libraryPathEntries
									.size()]));
		}

		{
			List<CMacroEntry> macroEntries = new ArrayList<CMacroEntry>();
			macroEntries.add(new CMacroEntry("GEKKO", "", CMacroEntry.MACRO));

			__macroSetting = new CExternalSetting(
					null,
					new String[] { "org.eclipse.cdt.core.cSource",
							"org.eclipse.cdt.core.cxxSource" },
					null,
					macroEntries.toArray(new CMacroEntry[macroEntries
							.size()]));
		}
	}

	@Override
	public CExternalSetting[] getSettings(IProject project,
			ICConfigurationDescription cfg) {
		System.out.println("Getting Settings for " + cfg);

		return new CExternalSetting[] { __includePathSetting,
				__libraryPathSetting, __macroSetting };
	}
}
