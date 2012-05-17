package com.github.wiiclipse.launch.ui;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.github.wiiclipse.launch.WiiClipseLaunchPlugin;
import com.github.wiiclipse.launch.WiiClipseLaunchPreferences;

public class WiiClipseLaunchPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private Composite mTop;
	private DirectoryFieldEditor mPath;

	private Label mModeLbl;
	private Combo mMode;

	private Label mHostLbl;
	private Text mHost;
	private Label mDeviceLbl;
	private Text mDevice;

	@Override
	public void init(IWorkbench workbench) {

		IPreferenceStore prefStore = WiiClipseLaunchPlugin.getDefault()
				.getPreferenceStore();
		setPreferenceStore(prefStore);
		setDescription("Default launch settings:");

	}

	@Override
	protected Control createContents(Composite parent) {
		mTop = new Composite(parent, SWT.LEFT);
		mTop.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mTop.setLayout(new GridLayout());

		mPath = new DirectoryFieldEditor(
				WiiClipseLaunchPreferences.WIILOAD_PATH, "wiiload.path", mTop);
		mPath.setPage(this);
		mPath.setPreferenceStore(getPreferenceStore());
		mPath.load();

		createConnectionModeGroup(mTop, 3);
		return mTop;
	}

	private void createConnectionModeGroup(Composite parent, int colSpan) {
		Group group = new Group(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = colSpan;
		group.setLayoutData(gridData);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		group.setLayout(gridLayout);
		group.setText("Connection");

		mModeLbl = new Label(group, SWT.NONE);
		mModeLbl.setText("Mode:");
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		mModeLbl.setLayoutData(gridData);

		mMode = new Combo(group, SWT.READ_ONLY);

		mMode.add("TCP/IP", WiiClipseLaunchPreferences.CONNECTION_MODE_TCP_IP);
		mMode.add("USB Gecko",
				WiiClipseLaunchPreferences.CONNECTION_MODE_USB_GECKO);

		int connectionMode = getPreferenceStore().getInt(
				WiiClipseLaunchPreferences.CONNECTION_MODE);

		mMode.select(connectionMode);
		mMode.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateLControls();
			}
		});

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.END;
		mMode.setLayoutData(gridData);

		mHostLbl = new Label(group, SWT.NONE);
		mHostLbl.setText("Hostname/IP Address:");
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.BEGINNING;
		mHostLbl.setLayoutData(gridData);

		mHost = new Text(group, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.END;
		gridData.widthHint = 200;
		mHost.setLayoutData(gridData);
		mHost.setText(getPreferenceStore().getString(
				WiiClipseLaunchPreferences.HOSTNAME));

		mDeviceLbl = new Label(group, SWT.NONE);
		mDeviceLbl.setText("Device:");
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.BEGINNING;
		mDeviceLbl.setLayoutData(gridData);
		mDeviceLbl.setEnabled(false);

		mDevice = new Text(group, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.END;
		gridData.widthHint = 200;
		mDevice.setLayoutData(gridData);
		mDevice.setText(getPreferenceStore().getString(
				WiiClipseLaunchPreferences.DEVICE));
		mDevice.setEnabled(false);

	}
	private void updateLControls() {
		boolean isTCP = mMode.getSelectionIndex() == WiiClipseLaunchPreferences.CONNECTION_MODE_TCP_IP;
		mHostLbl.setEnabled(isTCP);
		mHost.setEnabled(isTCP);

		mDeviceLbl.setEnabled(!isTCP);
		mDevice.setEnabled(!isTCP);
	}

	/*
	 * The user has pressed "Restore defaults". Restore all default preferences.
	 */
	protected void performDefaults() {
		mPath.loadDefault();
		
		mHost.setText(getPreferenceStore().getString(
				WiiClipseLaunchPreferences.HOSTNAME));
		mDevice.setText(getPreferenceStore().getString(
				WiiClipseLaunchPreferences.DEVICE));
		mMode.select(getPreferenceStore().getInt(WiiClipseLaunchPreferences.CONNECTION_MODE));
		
		updateLControls();
		super.performDefaults();
	}

	/*
	 * The user has pressed Ok or Apply. Store/apply this page's values
	 * appropriately.
	 */
	public boolean performOk() {
		mPath.store();
		
		getPreferenceStore().setValue(WiiClipseLaunchPreferences.CONNECTION_MODE, mMode.getSelectionIndex());
		getPreferenceStore().setValue(
				WiiClipseLaunchPreferences.HOSTNAME, mHost.getText());
		getPreferenceStore().setValue(
				WiiClipseLaunchPreferences.DEVICE, mDevice.getText());

		return super.performOk();
	}
}
