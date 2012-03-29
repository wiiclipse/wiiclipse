package com.github.wiiclipse.managedbuild;

import org.eclipse.cdt.core.IAddressFactory;
import org.eclipse.cdt.core.IBinaryParser;
import org.eclipse.cdt.core.IBinaryParser.IBinaryObject;
import org.eclipse.cdt.core.IBinaryParser.ISymbol;
import org.eclipse.cdt.utils.Addr32Factory;
import org.eclipse.cdt.utils.BinaryObjectAdapter;
import org.eclipse.cdt.utils.BinaryObjectAdapter.BinaryObjectInfo;
import org.eclipse.core.runtime.IPath;

public class DolBinaryObject extends BinaryObjectAdapter {

	public DolBinaryObject(IBinaryParser parser, IPath path) {
		super(parser, path, IBinaryObject.EXECUTABLE);
	}

	@Override
	public ISymbol[] getSymbols() {
		return NO_SYMBOLS;
	}

	@Override
	public IAddressFactory getAddressFactory() {
		// TODO Auto-generated method stub
		return new Addr32Factory();
	}

	@Override
	public boolean hasDebug() {
		return false;
	}

	@Override
	public boolean isLittleEndian() {
		return false;
	}

	@Override
	protected BinaryObjectInfo getBinaryObjectInfo() {
		BinaryObjectInfo info = new BinaryObjectInfo();
		info.cpu = "ppc";
		info.hasDebug = false;
		info.isLittleEndian = false;
		return info;
	}

}
