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

	public static final String ID = "com.github.wiiclipse.extSettings";

	@Override
	public CExternalSetting[] getSettings(IProject project,
			ICConfigurationDescription cfg) {
		System.out.println("Getting Settings for " + cfg);

		CExternalSetting includePathSetting;
		CExternalSetting libraryPathSetting;
		CExternalSetting macroSetting;
		
		{
			// include paths
			List<CIncludePathEntry> includePathEntries = new ArrayList<CIncludePathEntry>();
			IPath libOGCIncludePath = WiiClipsePathResolver
					.getLibOGCInlcudePath();
			if (libOGCIncludePath != null)
				includePathEntries.add(new CIncludePathEntry(libOGCIncludePath,
						0));

			includePathSetting = new CExternalSetting(new String[] {
					"org.eclipse.cdt.core.gcc", "org.eclipse.cdt.core.g++", "org.eclipse.cdt.core.assembly" },
					new String[] { "org.eclipse.cdt.core.cSource",
							"org.eclipse.cdt.core.cxxSource", "org.eclipse.cdt.core.asmSource" }, null,
					includePathEntries
							.toArray(new CIncludePathEntry[includePathEntries
									.size()]));
		}

		{
			List<CLibraryPathEntry> libraryPathEntries = new ArrayList<CLibraryPathEntry>();
			IPath libOGCLibPath = WiiClipsePathResolver.getLibOGCLibPath();
			if (libOGCLibPath != null)
				libraryPathEntries.add(new CLibraryPathEntry(libOGCLibPath, 0));

			libraryPathSetting = new CExternalSetting(
					null,
					new String[] { "org.eclipse.cdt.managedbuilder.core.compiledObjectFile" },
					null, libraryPathEntries
							.toArray(new CLibraryPathEntry[libraryPathEntries
									.size()]));
		}

		{
			List<CMacroEntry> macroEntries = new ArrayList<CMacroEntry>();
			macroEntries.add(new CMacroEntry("GEKKO", "", CMacroEntry.MACRO));

			macroSetting = new CExternalSetting(null, new String[] {
					"org.eclipse.cdt.core.cSource",
					"org.eclipse.cdt.core.cxxSource" }, null,
					macroEntries.toArray(new CMacroEntry[macroEntries.size()]));
		}

		return new CExternalSetting[] { includePathSetting,
				libraryPathSetting, macroSetting };
	}
}
