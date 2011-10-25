package com.github.wiiclipse.core;

import java.io.File;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

public class WiiClipsePathResolver {
	private static boolean __checked = false;

	private static IPath __devkitProPath = null;
	private static IPath __devkitPPCPath = null;
	private static IPath __devkitPPCBinPath = null;

	private static IPath __libOGCPath = null;
	private static IPath __libOGCInlcudePath = null;
	private static IPath __libOGCLibPath = null;


	private static final String WIN_DEVKITPRO_DEFAULT_PATH = "c:/devkitPro/";

	private static final String DEVKITPRO_DEFAULT_PATH = "/opt/devkitPro/";
	
	private static final String DEVKITPPC_DEFAULT_REL_PATH = "devkitPPC/";
	private static final String LIBOGC_DEFAULT_REL_PATH = "libogc/";

	public static void resolvePaths() {
		if (__checked)
			return;

		__checked = true;

		boolean isWindows = Platform.OS_WIN32.equals(Platform.getOS());

		String devkitProPath = System.getenv("DEVKITPRO");
		if (isWindows) {
			devkitProPath = fixWindowsPath(devkitProPath);
		}
		devkitProPath = fixPath(devkitProPath);
		if (devkitProPath == null) {
			if (isWindows)
				devkitProPath = WIN_DEVKITPRO_DEFAULT_PATH;
			else
				devkitProPath = DEVKITPRO_DEFAULT_PATH;
		}
		if (!new File(devkitProPath).exists())
			return;
		__devkitProPath = new Path(devkitProPath);

		String devkitPPCPath = System.getenv("DEVKITPPC");
		if (isWindows) {
			devkitPPCPath = fixWindowsPath(devkitPPCPath);
		}
		devkitPPCPath = fixPath(devkitPPCPath);
		if (devkitPPCPath == null) {
			devkitPPCPath = devkitProPath + DEVKITPPC_DEFAULT_REL_PATH;
		}

		if (new File(devkitPPCPath).exists()) {
			__devkitPPCPath = new Path(devkitPPCPath);
			String devkitPPCBinPath = devkitPPCPath + "bin/";
			if (new File(devkitPPCBinPath).exists()) {
				__devkitPPCBinPath = new Path(devkitPPCBinPath);
			}
		}

		String libOGCPath = devkitProPath + LIBOGC_DEFAULT_REL_PATH;
		if (new File(libOGCPath).exists()) {
			__libOGCPath = new Path(libOGCPath);

			String libOGCIncludePath = libOGCPath + "include/";
			if (new File(libOGCIncludePath).exists()) {
				__libOGCInlcudePath = new Path(libOGCIncludePath);
			}

			String libOGCLibPath = libOGCPath + "lib/wii/";
			if (new File(libOGCLibPath).exists()) {
				__libOGCLibPath = new Path(libOGCLibPath);
			}

		}
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
		if (!__checked)
			resolvePaths();
		return __devkitPPCBinPath;
	}

	public static IPath getDevkitPPCPath() {
		if (!__checked)
			resolvePaths();
		return __devkitPPCPath;
	}

	public static IPath getDevkitProPath() {
		if (!__checked)
			resolvePaths();
		return __devkitProPath;
	}

	public static IPath getLibOGCInlcudePath() {
		if (!__checked)
			resolvePaths();
		return __libOGCInlcudePath;
	}

	public static IPath getLibOGCLibPath() {
		if (!__checked)
			resolvePaths();
		return __libOGCLibPath;
	}

	public static IPath getLibOGCPath() {
		if (!__checked)
			resolvePaths();
		return __libOGCPath;
	}

}
