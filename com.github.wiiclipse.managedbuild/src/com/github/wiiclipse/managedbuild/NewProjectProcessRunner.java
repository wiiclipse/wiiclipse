package com.github.wiiclipse.managedbuild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.CIncludePathEntry;
import org.eclipse.cdt.core.settings.model.CLibraryPathEntry;
import org.eclipse.cdt.core.settings.model.CMacroEntry;
import org.eclipse.cdt.core.settings.model.CSourceEntry;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICFolderDescription;
import org.eclipse.cdt.core.settings.model.ICLanguageSetting;
import org.eclipse.cdt.core.settings.model.ICLanguageSettingEntry;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICSettingEntry;
import org.eclipse.cdt.core.settings.model.ICSourceEntry;
import org.eclipse.cdt.core.templateengine.process.ProcessFailureException;
import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.wiiclipse.core.WiiClipseCorePlugin;
import com.github.wiiclipse.core.WiiClipsePreferences;

public class NewProjectProcessRunner extends ProcessRunnerHelper {
	IPreferenceStore prefStore;

	private static final String EXECUTABLE_ARTEFACT_ID = "org.eclipse.cdt.build.core.buildArtefactType.exe";
	private static final String LIBRARY_ARTEFACT_ID = "org.eclipse.cdt.build.core.buildArtefactType.staticLib";

	public NewProjectProcessRunner() {
		prefStore = WiiClipseCorePlugin.getDefault().getPreferenceStore();
	}

	@Override
	protected void doProcess() throws ProcessFailureException {
		super.doProcess();
		try {
			String projName = getSimple("projName");
			String projType = getSimple("projType");

			String buildArtefactType = EXECUTABLE_ARTEFACT_ID;
			if (projType != null && projType.equals("library")) {
				buildArtefactType = LIBRARY_ARTEFACT_ID;
			}

			IProject project = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(projName);
			if (!project.exists())
				throw new ProcessFailureException("Project does not exist.");

			createSourceFolder(project);
			applyDefaultSettings(project, buildArtefactType);
		} catch (CoreException e) {
			throw new ProcessFailureException(e);
		} catch (BuildException e) {
			throw new ProcessFailureException(e);
		}
	}

	private void createSourceFolder(IProject project) throws CoreException {
		ICProjectDescription projDesc = CoreModel.getDefault()
				.getProjectDescription(project);
		for (ICConfigurationDescription confDesc : projDesc.getConfigurations()) {
			IConfiguration conf = ManagedBuildManager
					.getConfigurationForDescription(confDesc);
			List<ICSourceEntry> sourceEntries = new ArrayList<ICSourceEntry>(
					Arrays.asList(conf.getSourceEntries()));

			sourceEntries.add(new CSourceEntry("source", null,
					ICSettingEntry.RESOLVED));
			conf.setSourceEntries(sourceEntries.toArray(new ICSourceEntry[] {}));

		}

		CoreModel.getDefault().setProjectDescription(project, projDesc);
	}

	private void applyDefaultSettings(IProject project, String buildArtefactType)
			throws CoreException, BuildException {
		ICProjectDescription projDesc = CoreModel.getDefault()
				.getProjectDescription(project);
		for (ICConfigurationDescription confDesc : projDesc.getConfigurations()) {
			applyDefaultIncludeSettings(confDesc);
			if (buildArtefactType == EXECUTABLE_ARTEFACT_ID) {
				applyDefaultLibrarySettings(confDesc);
			}
			applyDefaultMacroSettings(confDesc);

			IConfiguration conf = ManagedBuildManager
					.getConfigurationForDescription(confDesc);
			conf.setBuildArtefactType(buildArtefactType);
		}

		CoreModel.getDefault().setProjectDescription(project, projDesc);
	}

	private void applyDefaultMacroSettings(ICConfigurationDescription confDesc) {
		ICFolderDescription folderDescription = confDesc
				.getRootFolderDescription();
		ICLanguageSetting[] languageSettings = folderDescription
				.getLanguageSettings();

		for (ICLanguageSetting lang : languageSettings) {
			ICLanguageSettingEntry[] macroSettings = lang
					.getSettingEntries(ICSettingEntry.MACRO);
			List<ICLanguageSettingEntry> macros = new ArrayList<ICLanguageSettingEntry>(
					Arrays.asList(macroSettings));

			macros.add(new CMacroEntry("GEKKO", "", ICSettingEntry.MACRO));
			lang.setSettingEntries(ICSettingEntry.MACRO,
					macros.toArray(new ICLanguageSettingEntry[0]));
		}

	}

	private void applyDefaultLibrarySettings(ICConfigurationDescription confDesc) {
		ICFolderDescription folderDescription = confDesc
				.getRootFolderDescription();
		ICLanguageSetting[] languageSettings = folderDescription
				.getLanguageSettings();

		IPreferenceStore prefStore = WiiClipseCorePlugin.getDefault()
				.getPreferenceStore();
		IPath path = new Path(
				prefStore.getString(WiiClipsePreferences.LIBOGC_PATH));
		if (WiiClipsePreferences.isValidLibOGCPath(path)) {
			for (ICLanguageSetting lang : languageSettings) {
				ICLanguageSettingEntry[] libPathSettings = lang
						.getSettingEntries(ICSettingEntry.LIBRARY_PATH);
				List<ICLanguageSettingEntry> libPaths = new ArrayList<ICLanguageSettingEntry>(
						Arrays.asList(libPathSettings));

				libPaths.add(new CLibraryPathEntry(path,
						ICSettingEntry.LIBRARY_PATH | ICSettingEntry.RESOLVED));
				lang.setSettingEntries(ICSettingEntry.LIBRARY_PATH,
						libPaths.toArray(new ICLanguageSettingEntry[0]));
			}
		}
	}

	private void applyDefaultIncludeSettings(ICConfigurationDescription confDesc) {
		ICFolderDescription folderDescription = confDesc
				.getRootFolderDescription();
		ICLanguageSetting[] languageSettings = folderDescription
				.getLanguageSettings();

		for (ICLanguageSetting lang : languageSettings) {
			// libogc
			IPreferenceStore prefStore = WiiClipseCorePlugin.getDefault()
					.getPreferenceStore();
			String libogcPath = prefStore
					.getString(WiiClipsePreferences.LIBOGC_PATH);
			if (libogcPath != null && !libogcPath.trim().isEmpty()) {
				ICLanguageSettingEntry[] includePathSettings = lang
						.getSettingEntries(ICSettingEntry.INCLUDE_PATH);
				List<ICLanguageSettingEntry> includePaths = new ArrayList<ICLanguageSettingEntry>(
						Arrays.asList(includePathSettings));

				includePaths.add(new CIncludePathEntry(libogcPath,
						CIncludePathEntry.INCLUDE_PATH));
				lang.setSettingEntries(ICSettingEntry.INCLUDE_PATH,
						includePaths.toArray(new ICLanguageSettingEntry[0]));

			}

			// GEKKO compiler symbol
			{
				ICLanguageSettingEntry[] macros = lang
						.getSettingEntries(ICSettingEntry.MACRO);
				List<ICLanguageSettingEntry> macroEntries = new ArrayList<ICLanguageSettingEntry>(
						Arrays.asList(macros));
				macroEntries
						.add(new CMacroEntry("GEKKO", "", CMacroEntry.MACRO));
				lang.setSettingEntries(ICSettingEntry.MACRO,
						macroEntries.toArray(new ICLanguageSettingEntry[0]));
			}
		}
	}
}
