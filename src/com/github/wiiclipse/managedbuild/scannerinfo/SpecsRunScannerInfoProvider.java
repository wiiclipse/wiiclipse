package com.github.wiiclipse.managedbuild.scannerinfo;

import org.eclipse.cdt.make.internal.core.scannerconfig2.GCCSpecsRunSIProvider;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

@SuppressWarnings("restriction")
public class SpecsRunScannerInfoProvider extends GCCSpecsRunSIProvider {
	@Override
	protected boolean initialize() {
		boolean rc = super.initialize();
		if (rc) {
			String devkitppc = java.lang.System.getenv("DEVKITPPC");
			if (Platform.getOS().equals(Platform.OS_WIN32)) {
				// default for windows
				if (devkitppc == null)
					devkitppc = "c:/devkitPro/devkitPPC/bin";
				if ((devkitppc.length() >= 3) && devkitppc.startsWith("/")) {
					char volume = devkitppc.charAt(1);
					devkitppc = volume + ":" + devkitppc.substring(2);
				}
				if (new Path(devkitppc).append(this.fCompileCommand).toFile()
						.exists()) {
					this.fCompileCommand = new Path(devkitppc)
							.append(this.fCompileCommand);
				}
			} else if (Platform.getOS().equals(Platform.OS_LINUX)) {
				// default for linux
				if (devkitppc == null)
					devkitppc = "/opt/devkitPro/devkitPPC/bin";
				else
					devkitppc += "/bin";
				IPath command = new Path(devkitppc)
						.append(this.fCompileCommand);
				if (command.toFile().exists()) {
					this.fCompileCommand = command;
				}
			} else if (Platform.getOS().equals(Platform.OS_MACOSX)) {
				// default for mac
				if (devkitppc == null)
					devkitppc = "/opt/devkitPro/devkitPPC/bin";
				IPath command = new Path(devkitppc)
						.append(this.fCompileCommand);
				if (command.toFile().exists()) {
					this.fCompileCommand = command;
				}
			}
		}
		return rc;

	}
}
