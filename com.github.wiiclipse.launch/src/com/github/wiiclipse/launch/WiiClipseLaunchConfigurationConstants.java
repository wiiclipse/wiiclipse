package com.github.wiiclipse.launch;

public interface WiiClipseLaunchConfigurationConstants {
	public static final String ATTR_CONNECTION_MODE = "Connection.Mode";
	public static final int CONNECTION_MODE_TCP_IP = 0;
	public static final int CONNECTION_MODE_USB_GECKO = 1;
	public static final int CONNECTION_MODE_DEFAULT = CONNECTION_MODE_TCP_IP;

	public static final String ATTR_HOSTNAME = "Connection.Hostname";
	public static final String HOSTNAME_DEFAULT = "192.168.0.1";

	public static final String ATTR_DEVICE = "Connection.Device";
	public static final String DEVICE_DEFAULT = "/dev/ttyUSB0";
	public static final String ID_LAUNCH_C_APP = "com.github.wiiclipse.launch.config";
}
