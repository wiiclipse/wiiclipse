package com.github.wiiclipse.core;

import java.io.File;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

public class WiiClipsePathResolver {
	private static boolean checked = false;

	private static IPath __devkitProPath = null;
	private static IPath __devkitPPCPath = null;
	private static IPath __devkitPPCBinPath = null;

	private static IPath __libOGCPath = null;
	private static IPath __libOGCInlcudePath = null;
	private static IPath __libOGCLibPath = null;

	private static final String WIN_DEVKITPRO_DEFAULT_PATH = "c:/devkitPro/";
	private static final String DEVKITPRO_DEFAULT_PATH = "/opt/devkitPro/";
	private static final String DEVKITPPC_DEFAULT_REL_PATH = "devkitPPC/";

	private static final boolean isWindows = Platform.OS_WIN32.equals(Platform
			.getOS());

	public static void resolvePaths() {
		if (checked)
			return;

		// locate devkitppc

		// check env
		IPath devkitPPCBasePath = getPathFromEnv("DEVKITPPC");
		IPath devkitProBasePath = getPathFromEnv("DEVKITPRO");

		if (devkitPPCBasePath != null) {
			if (getBinPath(devkitPPCBasePath) == null) {
				devkitPPCBasePath = null;
			}
		}

		if (devkitPPCBasePath == null) {
			// check for devkitpro variable
			if (devkitProBasePath == null) {
				// use default location
				if (isWindows) {
					devkitProBasePath = new Path(WIN_DEVKITPRO_DEFAULT_PATH);
				} else {
					devkitProBasePath = new Path(DEVKITPRO_DEFAULT_PATH);
				}
			}
			devkitPPCBasePath = devkitProBasePath
					.append(DEVKITPPC_DEFAULT_REL_PATH);
		}
		if (getBinPath(devkitPPCBasePath) != null) {
			__devkitPPCPath = devkitPPCBasePath;
		}

		if (devkitProBasePath == null) {
			File devkitPPCBase = devkitPPCBasePath.toFile();
			File parent = devkitPPCBase.getParentFile();
			if (parent.exists() && parent.isDirectory()) {
				devkitProBasePath = new Path(parent.getAbsolutePath());
			}
		}

		// libogc
		if (devkitProBasePath != null) {
			// assume libogc is installed into devkitpro folder
			IPath libOGCBasePath = devkitProBasePath.append("libogc");
			IPath libOGCLibPath = getLibPath(libOGCBasePath);
			IPath libOGCIncPath = getIncludePath(libOGCBasePath);

			if (getIncludePath(libOGCBasePath) != null
					&& getLibPath(libOGCBasePath) != null) {
				__libOGCPath = libOGCBasePath;
				__libOGCLibPath = libOGCLibPath;
				__libOGCInlcudePath = libOGCIncPath;
			}
		}

		checked = true;
	}

	private static String fixPath(String path) {
		if (path != null && !path.isEmpty() && !path.endsWith("/")) {
			path = path + "/";
		}
		return path;
	}

	private static String fixWindowsPath(String path) {
		if (path != null && path.length() >= 3 && path.startsWith("/")) {
			char volume = path.charAt(1);
			path = volume + ":" + path.substring(2);
			path.replaceAll("\\", "/");
		}
		return path;
	}

	public static IPath getDevkitPPCBinPath() {
		if (!checked)
			resolvePaths();
		return __devkitPPCBinPath;
	}

	public static IPath getDevkitPPCPath() {
		if (!checked)
			resolvePaths();
		return __devkitPPCPath;
	}

	public static IPath getDevkitProPath() {
		if (!checked)
			resolvePaths();
		return __devkitProPath;
	}

	public static IPath getLibOGCInlcudePath() {
		if (!checked)
			resolvePaths();
		return __libOGCInlcudePath;
	}

	public static IPath getLibOGCLibPath() {
		if (!checked)
			resolvePaths();
		return __libOGCLibPath;
	}

	public static IPath getLibOGCPath() {
		if (!checked)
			resolvePaths();
		return __libOGCPath;
	}

	public static IPath getLibPath(IPath basePath) {
		IPath libPath = null;
		File f = basePath.toFile();
		if (f.exists() && f.isDirectory()) {
			File p = f.getParentFile();
			String name = f.getName();
			if (name.equalsIgnoreCase("wii")) {
				if (p != null && p.exists() && p.getName().equals("lib")) {
					libPath = basePath;
				}
			} else if (name.equalsIgnoreCase("lib")) {
				IPath childPath = basePath.append("wii");
				File cFile = childPath.toFile();
				if (cFile.exists() && cFile.isDirectory()) {
					libPath = childPath;
				} else {
					libPath = basePath;
				}
			} else {
				libPath = getLibPath(basePath.append("lib"));
			}
		}
		return libPath;
	}

	public static IPath getBinPath(IPath basePath) {
		IPath binPath = null;
		File f = basePath.toFile();
		if (f.exists() && f.isDirectory()) {
			String name = f.getName();
			if (name.equalsIgnoreCase("bin")) {
				binPath = basePath;
			} else {
				binPath = getBinPath(basePath.append("bin"));
			}
		}
		return binPath;
	}

	public static IPath getIncludePath(IPath basePath) {
		IPath incPath = null;
		File f = basePath.toFile();
		if (f.exists() && f.isDirectory()) {
			String name = f.getName();
			if (name.equalsIgnoreCase("include")) {
				incPath = basePath;
			} else {
				incPath = getIncludePath(basePath.append("include"));
			}
		}
		return incPath;
	}

	static IPath getPathFromEnv(String envVar) {
		IPath path = null;
		String value = System.getenv(envVar);
		if (value != null) {
			if (isWindows) {
				value = fixWindowsPath(value);
			} else {
				value = fixPath(value);
			}
			path = new Path(value);
		}
		return path;
	}

}
