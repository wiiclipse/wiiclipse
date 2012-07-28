package com.github.wiiclipse.launch.ui;

import org.eclipse.cdt.launch.ui.CMainTab;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.github.wiiclipse.launch.WiiClipseLaunchPlugin;
import com.github.wiiclipse.launch.WiiClipseLaunchPreferences;

public class WiiClipseLaunchConfigurationTab extends CMainTab {
	private Label _connectionModeLabel;
	private Combo _connectionModeBox;

	private Label _hostNameLabel;
	private Text _hostNameText;
	private Label _deviceLabel;
	private Text _deviceText;
	private Text _destPathText;

	private final IPreferenceStore _prefStore;

	public WiiClipseLaunchConfigurationTab() {
		_prefStore = WiiClipseLaunchPlugin.getDefault().getPreferenceStore();
	}

	@Override
	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);

		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 1;
		comp.setLayout(topLayout);

		createVerticalSpacer(comp, 1);
		createProjectGroup(comp, 1);

		createApplicationGroup(comp, 1);
		createVerticalSpacer(comp, 1);

		createConnectionModeGroup(comp, 1);
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

		_connectionModeLabel = new Label(group, SWT.NONE);
		_connectionModeLabel.setText("Mode:");
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		_connectionModeLabel.setLayoutData(gridData);

		_connectionModeBox = new Combo(group, SWT.READ_ONLY);

		_connectionModeBox.add("TCP/IP",
				WiiClipseLaunchPreferences.CONNECTION_MODE_TCP_IP);
		_connectionModeBox.add("USB Gecko",
				WiiClipseLaunchPreferences.CONNECTION_MODE_USB_GECKO);

		int connectionMode = _prefStore
				.getInt(WiiClipseLaunchPreferences.CONNECTION_MODE);

		_connectionModeBox.select(connectionMode);
		_connectionModeBox.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateLaunchConfigurationDialog();
			}
		});

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.END;
		_connectionModeBox.setLayoutData(gridData);

		_hostNameLabel = new Label(group, SWT.NONE);
		_hostNameLabel.setText("Hostname/IP Address:");
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.BEGINNING;
		_hostNameLabel.setLayoutData(gridData);

		_hostNameText = new Text(group, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.END;
		gridData.widthHint = 300;
		_hostNameText.setLayoutData(gridData);
		_hostNameText.setText("192.168.0.1");
		_hostNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				updateLaunchConfigurationDialog();
			}
		});

		_deviceLabel = new Label(group, SWT.NONE);
		_deviceLabel.setText("Device:");
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.BEGINNING;
		_deviceLabel.setLayoutData(gridData);
		_deviceLabel.setEnabled(false);

		_deviceText = new Text(group, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.END;
		gridData.widthHint = 300;
		_deviceText.setLayoutData(gridData);
		_deviceText.setText("/dev/ttyUSB0");
		_deviceText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				updateLaunchConfigurationDialog();
			}
		});
		_deviceText.setEnabled(false);

		Label destLabel = new Label(group, SWT.NONE);
		destLabel.setText("Destination:");
		gridData = new GridData();
		destLabel.setLayoutData(gridData);

		_destPathText = new Text(group, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.END;
		gridData.widthHint = 300;
		_destPathText.setLayoutData(gridData);
		_destPathText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent evt) {
				updateLaunchConfigurationDialog();
			}
		});
	}

	private void createApplicationGroup(Composite parent, int colSpan) {
		Composite mainComp = new Composite(parent, SWT.NONE);
		GridLayout mainLayout = new GridLayout();
		mainLayout.numColumns = 3;
		mainLayout.marginHeight = 0;
		mainLayout.marginWidth = 0;
		mainComp.setLayout(mainLayout);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = colSpan;
		mainComp.setLayoutData(gd);
		fProgLabel = new Label(mainComp, SWT.NONE);
		fProgLabel.setText("Application:");
		gd = new GridData();
		gd.horizontalSpan = 3;
		fProgLabel.setLayoutData(gd);
		fProgText = new Text(mainComp, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fProgText.setLayoutData(gd);
		fProgText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				updateLaunchConfigurationDialog();
			}
		});

		fSearchButton = createPushButton(mainComp, "Search", null); //$NON-NLS-1$
		fSearchButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				handleSearchButtonSelected();
				updateLaunchConfigurationDialog();
			}
		});

		Button fBrowseForBinaryButton;
		fBrowseForBinaryButton = createPushButton(mainComp, "Browse", null); //$NON-NLS-1$
		fBrowseForBinaryButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				handleBrowseButtonSelected();
				updateLaunchConfigurationDialog();
			}
		});

	}

	@Override
	protected void updateLaunchConfigurationDialog() {
		super.updateLaunchConfigurationDialog();
		boolean isTCP = _connectionModeBox.getSelectionIndex() == WiiClipseLaunchPreferences.CONNECTION_MODE_TCP_IP;
		_hostNameLabel.setEnabled(isTCP);
		_hostNameText.setEnabled(isTCP);
		_deviceLabel.setEnabled(!isTCP);
		_deviceText.setEnabled(!isTCP);
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy config) {
		super.setDefaults(config);

		// initialise from preference store

		String hostname = _prefStore
				.getString(WiiClipseLaunchPreferences.HOSTNAME);
		String device = _prefStore.getString(WiiClipseLaunchPreferences.DEVICE);
		int mode = _prefStore
				.getInt(WiiClipseLaunchPreferences.CONNECTION_MODE);

		config.setAttribute(WiiClipseLaunchPreferences.CONNECTION_MODE, mode);
		config.setAttribute(WiiClipseLaunchPreferences.HOSTNAME, hostname);
		config.setAttribute(WiiClipseLaunchPreferences.DEVICE, device);
		config.setAttribute(WiiClipseLaunchPreferences.DEST_PATH,
				WiiClipseLaunchPreferences.DEST_PATH_DEFAULT);
	}

	@Override
	public void initializeFrom(ILaunchConfiguration config) {
		super.initializeFrom(config);

		String hostname = _prefStore
				.getString(WiiClipseLaunchPreferences.HOSTNAME);
		String device = _prefStore.getString(WiiClipseLaunchPreferences.DEVICE);
		int mode = _prefStore
				.getInt(WiiClipseLaunchPreferences.CONNECTION_MODE);

		String destPath = "";
		try {
			mode = config.getAttribute(
					WiiClipseLaunchPreferences.CONNECTION_MODE, mode);
			hostname = config.getAttribute(WiiClipseLaunchPreferences.HOSTNAME,
					hostname);
			device = config.getAttribute(WiiClipseLaunchPreferences.DEVICE,
					device);
			destPath = config.getAttribute(
					WiiClipseLaunchPreferences.DEST_PATH, destPath);

		} catch (CoreException ce) {
			// WiiClipse.log(ce);
		}
		_connectionModeBox.select(mode);
		_hostNameText.setText(hostname);
		_deviceText.setText(device);
		_destPathText.setText(destPath);
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy config) {
		super.performApply(config);

		config.setAttribute(WiiClipseLaunchPreferences.CONNECTION_MODE,
				_connectionModeBox.getSelectionIndex());

		config.setAttribute(WiiClipseLaunchPreferences.HOSTNAME, _hostNameText
				.getText().trim());

		config.setAttribute(WiiClipseLaunchPreferences.DEVICE, _deviceText
				.getText().trim());

		config.setAttribute(WiiClipseLaunchPreferences.DEST_PATH, _destPathText
				.getText().trim());

	}

	@Override
	public boolean isValid(ILaunchConfiguration config) {
		if (!super.isValid(config))
			return false;

		setErrorMessage(null);
		setMessage(null);

		if (_connectionModeBox.getSelectionIndex() == WiiClipseLaunchPreferences.CONNECTION_MODE_TCP_IP) {
			String hostname = _hostNameText.getText().trim();
			if (hostname.isEmpty())
				return false;
		} else {
			String device = _deviceText.getText().trim();
			if (device.isEmpty())
				return false;
		}

		return true;
	}
}
