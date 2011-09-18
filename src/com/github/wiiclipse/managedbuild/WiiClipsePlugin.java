package com.github.wiiclipse.managedbuild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.CProjectDescriptionEvent;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICLanguageSetting;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescriptionListener;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class WiiClipsePlugin extends Plugin implements
		ICProjectDescriptionListener {

	private static WiiClipsePlugin __plugin;

	public WiiClipsePlugin() {
		super();
		WiiClipsePathResolver.resolvePaths();
		__plugin = this;
	}

	WiiClipsePlugin getDefault() {
		return __plugin;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		CoreModel.getDefault().addCProjectDescriptionListener(this,
				CProjectDescriptionEvent.ABOUT_TO_APPLY);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		CoreModel.getDefault().removeCProjectDescriptionListener(this);
	}

	@Override
	public void handleEvent(CProjectDescriptionEvent event) {

		ICProjectDescription cProjDesc = event.getNewCProjectDescription();
		ICConfigurationDescription[] configurations = cProjDesc
				.getConfigurations();
		for (ICConfigurationDescription config : configurations) {
			List<String> extSettingProviders = new ArrayList<String>(
					Arrays.asList(config.getExternalSettingsProviderIds()));
			if (!extSettingProviders
					.contains(WiiClipseCExternalSettingsProvider.ID)) {
				extSettingProviders.add(WiiClipseCExternalSettingsProvider.ID);
				config.setExternalSettingsProviderIds(extSettingProviders
						.toArray(new String[0]));

			}

		}

		// for (ICConfigurationDescription config : configurations) {
		// ICFolderDescription folderDesc = config.getRootFolderDescription();
		// ICLanguageSetting[] languages = folderDesc.getLanguageSettings();
		// for (ICLanguageSetting lang : languages) {
		// List<ICLanguageSettingEntry> includePaths = lang
		// .getSettingEntriesList(ICSettingEntry.INCLUDE_PATH);
		// includePaths.add(new CIncludePathEntry(WiiClipsePathResolver
		// .getLibOGCInlcudePath(), ICSettingEntry.INCLUDE_PATH
		// | ICSettingEntry.LOCAL));
		// lang.setSettingEntries(
		// ICSettingEntry.INCLUDE_PATH,
		// includePaths
		// .toArray(new ICLanguageSettingEntry[includePaths
		// .size()]));
		//
		// List<ICLanguageSettingEntry> libraryPaths = lang
		// .getSettingEntriesList(ICSettingEntry.LIBRARY_PATH);
		// libraryPaths.add(new CLibraryPathEntry(WiiClipsePathResolver
		// .getLibOGCLibPath(), ICSettingEntry.LIBRARY_PATH
		// | ICSettingEntry.LOCAL));
		// lang.setSettingEntries(
		// ICSettingEntry.LIBRARY_PATH,
		// libraryPaths
		// .toArray(new ICLanguageSettingEntry[libraryPaths
		// .size()]));
		//
		// // List<ICLanguageSettingEntry> libraryFiles = lang
		// // .getSettingEntriesList(ICSettingEntry.LIBRARY_FILE);
		// // libraryFiles.add(new CLibraryFileEntry("ogc",
		// // ICSettingEntry.LIBRARY_FILE | ICSettingEntry.RESOLVED));
		// // lang.setSettingEntries(
		// // ICSettingEntry.LIBRARY_FILE,
		// // libraryFiles
		// // .toArray(new ICLanguageSettingEntry[libraryFiles
		// // .size()]));
		//
		// LinkedHashSet<ICLanguageSettingEntry> macros = new
		// LinkedHashSet<ICLanguageSettingEntry>(
		// lang.getSettingEntriesList(ICSettingEntry.LIBRARY_FILE));
		// macros.add(new CMacroEntry("GEKKO", "", ICSettingEntry.MACRO));
		// lang.setSettingEntries(ICSettingEntry.MACRO,
		// macros.toArray(new ICLanguageSettingEntry[0]));
		// }
		// }

	}

	private ICLanguageSetting getLanguageByID(ICLanguageSetting[] languages,
			String id) {
		for (ICLanguageSetting lang : languages) {
			if (lang.getLanguageId().equals(id))
				return lang;
		}
		return null;
	}
}
