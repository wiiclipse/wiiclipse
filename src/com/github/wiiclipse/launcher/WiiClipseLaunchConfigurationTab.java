package com.github.wiiclipse.launcher;

import org.eclipse.cdt.launch.ui.CAbstractMainTab;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class WiiClipseLaunchConfigurationTab extends CAbstractMainTab {
	private Label _connectionModeLabel;
	private Combo _connectionModeBox;

	private Label _connectionDetailLabel;
	private Text _connectionDetailText;

	@Override
	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);

		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 2;
		comp.setLayout(topLayout);

		createVerticalSpacer(comp, 2);
		createProjectGroup(comp, 2);

		createApplicationGroup(comp, 2);
		createVerticalSpacer(comp, 2);

		createConnectionModeGroup(comp, 1);
		createConnectionDetailsGroup(comp, 1);
	}

	private void createConnectionModeGroup(Composite parent, int colSpan) {
		Composite mainComp = new Composite(parent, SWT.NONE);
		GridLayout mainLayout = new GridLayout();

		mainLayout.numColumns = 1;
		mainLayout.marginHeight = 0;
		mainLayout.marginWidth = 0;
		mainComp.setLayout(mainLayout);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = colSpan;
		mainComp.setLayoutData(gd);

		_connectionModeLabel = new Label(mainComp, SWT.NONE);
		_connectionModeLabel.setText("Connection Mode:");
		_connectionModeBox = new Combo(mainComp, SWT.READ_ONLY);

		_connectionModeBox.add("TCP/IP",
				WiiClipseLaunchConfigurationConstants.CONNECTION_MODE_TCP_IP);
		_connectionModeBox
				.add("USB Gecko",
						WiiClipseLaunchConfigurationConstants.CONNECTION_MODE_USB_GECKO);
		_connectionModeBox.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				updateLaunchConfigurationDialog();
			}
		});

		gd = new GridData();
		gd.horizontalAlignment = SWT.END;
		_connectionModeBox.setLayoutData(gd);
	}

	private void createConnectionDetailsGroup(Composite parent, int colSpan) {
		Composite mainComp = new Composite(parent, SWT.NONE);
		GridLayout mainLayout = new GridLayout();

		mainLayout.numColumns = 1;
		mainLayout.marginHeight = 0;
		mainLayout.marginWidth = 0;
		mainComp.setLayout(mainLayout);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = colSpan;
		mainComp.setLayoutData(gd);

		_connectionDetailLabel = new Label(mainComp, SWT.NONE);
		_connectionDetailLabel.setText("Hostname/IP Adress:");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = SWT.BEGINNING;
		_connectionDetailLabel.setLayoutData(gd);

		_connectionDetailText = new Text(mainComp, SWT.SINGLE | SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = SWT.END;
		_connectionDetailText.setLayoutData(gd);
		_connectionDetailText.addModifyListener(new ModifyListener() {
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
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void handleSearchButtonSelected() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateLaunchConfigurationDialog() {
		super.updateLaunchConfigurationDialog();
		switch (_connectionModeBox.getSelectionIndex()) {
		case WiiClipseLaunchConfigurationConstants.CONNECTION_MODE_TCP_IP:
			_connectionDetailLabel.setText("Hostname/IP Adress:");
			break;
		case WiiClipseLaunchConfigurationConstants.CONNECTION_MODE_USB_GECKO:
			_connectionDetailLabel.setText("Device:");
		default:
			break;
		}
	}
}
