package com.github.wiiclipse.core;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.github.wiiclipse.core.messages"; //$NON-NLS-1$
	
	public static String WiiClipsePreferencePageLibogcPathLabel;
	public static String WiiClipsePreferencePageDevkitPPCPathLabel;
	public static String WiiClipsePreferencePageDescription;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
