package com.github.wiiclipse.core;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class Library {
	private final String name;

	private IPath incPath;
	private IPath libPath;

	public Library(String name) {
		this.name = name;
	}

	private String getFileName() {
		return "lib" + name + ".a";
	}

	public boolean isValid() {
		if (incPath == null || libPath == null)
			return false;

		if (!incPath.toFile().isDirectory())
			return false;

		if (!libPath.toFile().isDirectory())
			return false;

		if (!libPath.append(getFileName()).toFile().exists())
			return false;

		return true;
	}

//	private void resolvePaths(String basePath) {
//		IPath path = new Path(basePath);
//		if (!path.toFile().isDirectory())
//			return;
//
//		path = new Path(basePath).append("include");
//		if (!path.toFile().isDirectory())
//			return;
//
//		incPath = path;
//
//		path = new Path(basePath).append("lib").append("wii");
//		if (!path.toFile().isDirectory()) {
//			path = new Path(basePath).append("lib");
//			if (!path.toFile().isDirectory())
//				return;
//		}
//
//		libPath = path;
//	}
}
