package com.github.wiiclipse.managedbuild.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;

public class StringListEditor extends ListEditor {

	public StringListEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}

	@Override
	protected String createList(String[] items) {
		StringBuffer list = new StringBuffer("");//$NON-NLS-1$

		for (int i = 0; i < items.length; i++) {
			list.append(items[i]);
			list.append(File.pathSeparator);
		}
		return list.toString();
	}

	@Override
	protected String getNewInputObject() {
		FileDialog dialog = new FileDialog(getShell(), SWT.SHEET);
		return dialog.open();
	}

	@Override
	protected String[] parseString(String stringList) {
		StringTokenizer st = new StringTokenizer(stringList, File.pathSeparator
				+ "\n\r");//$NON-NLS-1$
		ArrayList v = new ArrayList();
		while (st.hasMoreElements()) {
			v.add(st.nextElement());
		}
		return (String[]) v.toArray(new String[v.size()]);
	}

}
