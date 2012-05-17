package com.github.wiiclipse.managedbuild;

import java.io.File;
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
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.wiiclipse.core.WiiClipseCorePlugin;
import com.github.wiiclipse.core.WiiClipsePathResolver;
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

	private void createSourceFolder(IProject project) throws CoreException,
			BuildException {
		ICProjectDescription projDesc = CoreModel.getDefault()
				.getProjectDescription(project);

		IPath projectPath = project.getLocation();
		IPath sourcePath = projectPath.append("source");
		File srcPathFile = sourcePath.toFile();
		if (!srcPathFile.exists()) {
			if (!srcPathFile.mkdir()) {
				return;
			}
		}
		for (ICConfigurationDescription confDesc : projDesc.getConfigurations()) {
			IConfiguration conf = ManagedBuildManager
					.getConfigurationForDescription(confDesc);

			IPath libogcLibPath = WiiClipsePathResolver.getLibPath(new Path(
					prefStore.getString(WiiClipsePreferences.LIBOGC_PATH)));
			if (libogcLibPath != null) {
				ITool[] tools = conf
						.getToolsBySuperClassId("com.github.wiiclipse.managedbuild.linker.base");

				for (ITool tool : tools) {
					IOption option = tool
							.getOptionById("com.github.wiiclipse.managedbuild.linker.option.libsearch");
					if (option != null) {
						conf.setOption(tool, option,
								new String[] { libogcLibPath.toString() });
					}
					
					option = tool.getOptionById("com.github.wiiclipse.managedbuild.linker.option.libs");
					if (option != null) {
						conf.setOption(tool, option,
								new String[] { "ogc" });
					}
					
				}
			}
			conf.setSourceEntries(new ICSourceEntry[] { new CSourceEntry(
					"source", null, ICSettingEntry.RESOLVED) });
		}

		CoreModel.getDefault().setProjectDescription(project, projDesc);
	}

	private void applyDefaultSettings(IProject project, String buildArtefactType)
			throws CoreException, BuildException {
		ICProjectDescription projDesc = CoreModel.getDefault()
				.getProjectDescription(project);
		for (ICConfigurationDescription confDesc : projDesc.getConfigurations()) {
			ICFolderDescription folderDescription = confDesc
					.getRootFolderDescription();
			ICLanguageSetting[] languageSettings = folderDescription
					.getLanguageSettings();
			for (ICLanguageSetting lang : languageSettings) {
				applyDefaultIncludeSettings(lang);
				if (buildArtefactType == EXECUTABLE_ARTEFACT_ID) {
					applyDefaultLibrarySettings(lang);
				}
				applyDefaultMacroSettings(lang);
			}
			IConfiguration conf = ManagedBuildManager
					.getConfigurationForDescription(confDesc);
			conf.setBuildArtefactType(buildArtefactType);
		}

		CoreModel.getDefault().setProjectDescription(project, projDesc);
	}

	private void applyDefaultMacroSettings(ICLanguageSetting lang) {
		addSettingEntry(lang,
				new CMacroEntry("GEKKO", "", ICSettingEntry.MACRO));
	}

	private void applyDefaultLibrarySettings(ICLanguageSetting lang) {
		IPath path = WiiClipsePathResolver.getLibPath(new Path(prefStore
				.getString(WiiClipsePreferences.LIBOGC_PATH)));
		if (path != null) {
			addSettingEntry(lang, new CLibraryPathEntry(path.toOSString(),
					ICSettingEntry.RESOLVED));
		}
	}

	private void applyDefaultIncludeSettings(ICLanguageSetting lang) {
		IPath incPath = WiiClipsePathResolver.getIncludePath(new Path(prefStore
				.getString(WiiClipsePreferences.LIBOGC_PATH)));
		if (incPath != null) {
			addSettingEntry(lang, new CIncludePathEntry(incPath.toOSString(),
					CIncludePathEntry.INCLUDE_PATH));

		}
	}

	private void addSettingEntry(ICLanguageSetting setting,
			ICLanguageSettingEntry entry) {
		int kind = entry.getKind();
		List<ICLanguageSettingEntry> entries = new ArrayList<ICLanguageSettingEntry>(
				Arrays.asList(setting.getSettingEntries(kind)));
		entries.add(entry);
		setting.setSettingEntries(kind,
				entries.toArray(new ICLanguageSettingEntry[] {}));

	}
}
