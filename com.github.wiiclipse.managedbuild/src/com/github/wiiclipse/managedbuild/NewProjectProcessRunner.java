package com.github.wiiclipse.managedbuild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.CIncludePathEntry;
import org.eclipse.cdt.core.settings.model.CLibraryPathEntry;
import org.eclipse.cdt.core.settings.model.CMacroEntry;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICFolderDescription;
import org.eclipse.cdt.core.settings.model.ICLanguageSetting;
import org.eclipse.cdt.core.settings.model.ICLanguageSettingEntry;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICSettingEntry;
import org.eclipse.cdt.core.templateengine.process.ProcessFailureException;
import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.wiiclipse.core.WiiClipseCorePlugin;
import com.github.wiiclipse.core.WiiClipsePathResolver;

public class NewProjectProcessRunner extends ProcessRunnerHelper {
	IPreferenceStore prefStore;

	public NewProjectProcessRunner() {
		prefStore = WiiClipseCorePlugin.getDefault().getPreferenceStore();
	}

	@Override
	protected void doProcess() throws ProcessFailureException {
		super.doProcess();
		try {
			String projName = getSimple("projName");
//			String projType = getSimple("projType");

			String artefactBuildType = "org.eclipse.cdt.build.core.buildArtefactType.exe";
//
//			if (projType != null && projType.equals("library")) {
//				artefactBuildType = "org.eclipse.cdt.build.core.buildArtefactType.staticLib";
//			}

			IProject project = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(projName);
			if (!project.exists())
				throw new ProcessFailureException("Project does not exist.");

			ICProjectDescription projDesc = CoreModel.getDefault()
					.getProjectDescription(project);

			IManagedBuildInfo buildInfo = ManagedBuildManager
					.getBuildInfo(project);

			for (ICConfigurationDescription confDesc : projDesc
					.getConfigurations()) {
				ICFolderDescription folderDescription = confDesc
						.getRootFolderDescription();
				ICLanguageSetting[] languageSettings = folderDescription
						.getLanguageSettings();
				for (ICLanguageSetting lang : languageSettings) {
					ICLanguageSettingEntry[] includePathSettings = lang
							.getSettingEntries(ICSettingEntry.INCLUDE_PATH);
					List<ICLanguageSettingEntry> includePaths = new ArrayList<ICLanguageSettingEntry>(
							Arrays.asList(includePathSettings));
					includePaths.add(new CIncludePathEntry(
							WiiClipsePathResolver.getLibOGCPath(),
							CIncludePathEntry.INCLUDE_PATH));
					lang.setSettingEntries(ICSettingEntry.INCLUDE_PATH,
							includePaths.toArray(new ICLanguageSettingEntry[0]));

					ICLanguageSettingEntry[] libPathSettings = lang
							.getSettingEntries(ICSettingEntry.LIBRARY_PATH);
					List<ICLanguageSettingEntry> libPaths = new ArrayList<ICLanguageSettingEntry>(
							Arrays.asList(libPathSettings));
					libPaths.add(new CLibraryPathEntry(WiiClipsePathResolver
							.getLibOGCPath(), ICSettingEntry.LIBRARY_PATH
							| ICSettingEntry.RESOLVED));
					lang.setSettingEntries(ICSettingEntry.LIBRARY_PATH,
							libPaths.toArray(new ICLanguageSettingEntry[0]));

					ICLanguageSettingEntry[] macroSettings = lang
							.getSettingEntries(ICSettingEntry.MACRO);
					List<ICLanguageSettingEntry> macros = new ArrayList<ICLanguageSettingEntry>(
							Arrays.asList(macroSettings));
					macros.add(new CMacroEntry("GEKKO", "",
							ICSettingEntry.MACRO));
					lang.setSettingEntries(ICSettingEntry.MACRO,
							macros.toArray(new ICLanguageSettingEntry[0]));
				}

				IConfiguration conf = ManagedBuildManager
						.getConfigurationForDescription(confDesc);
				conf.setBuildArtefactType(artefactBuildType);
			}

			CoreModel.getDefault().setProjectDescription(project, projDesc);

		} catch (CoreException e) {
			throw new ProcessFailureException(e);
		} catch (BuildException e) {
			throw new ProcessFailureException(e);
		}
	}
}
