package com.github.wiiclipse.managedbuild;

import org.eclipse.cdt.make.internal.core.scannerconfig2.GCCSpecsRunSIProvider;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

import com.github.wiiclipse.core.WiiClipsePathResolver;


@SuppressWarnings("restriction")
public class WiiClipseSpecsRunScannerInfoProvider extends GCCSpecsRunSIProvider {
	@Override
	protected boolean initialize() {
		boolean rc = super.initialize();
		if (rc) {
			IPath devkitPPCBinPath = WiiClipsePathResolver.getDevkitPPCBinPath();
			if(devkitPPCBinPath != null) {
				this.fCompileCommand = devkitPPCBinPath.append(this.fCompileCommand);
			}
		}
		return rc;
	}
}
