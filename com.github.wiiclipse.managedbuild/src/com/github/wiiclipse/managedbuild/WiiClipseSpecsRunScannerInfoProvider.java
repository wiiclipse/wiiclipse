package com.github.wiiclipse.managedbuild;

import org.eclipse.cdt.make.internal.core.scannerconfig2.GCCSpecsRunSIProvider;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.wiiclipse.core.WiiClipseCorePlugin;
import com.github.wiiclipse.core.WiiClipsePathResolver;
import com.github.wiiclipse.core.WiiClipsePreferences;

@SuppressWarnings("restriction")
public class WiiClipseSpecsRunScannerInfoProvider extends GCCSpecsRunSIProvider {
	@Override
	protected boolean initialize() {
		boolean rc = super.initialize();
		if (rc) {
			IPreferenceStore prefStore = WiiClipseCorePlugin.getDefault()
					.getPreferenceStore();

			String pathStr = prefStore
					.getString(WiiClipsePreferences.DEVKITPPC_PATH);
			if(pathStr == null)
				return false;
			
			IPath devkitPPCBinPath = WiiClipsePathResolver.getBinPath(new Path(pathStr));
			if (devkitPPCBinPath != null) {
				this.fCompileCommand = devkitPPCBinPath
						.append(this.fCompileCommand);
			}
		}
		return rc;
	}
}
