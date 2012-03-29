package com.github.wiiclipse.managedbuild;

import java.io.IOException;

import org.eclipse.cdt.core.AbstractCExtension;
import org.eclipse.cdt.core.IBinaryParser;
import org.eclipse.core.runtime.IPath;

public class DolBinaryParser extends AbstractCExtension implements
		IBinaryParser {

	@Override
	public IBinaryFile getBinary(byte[] hints, IPath path) throws IOException {
		return new DolBinaryObject(this, path);
	}

	@Override
	public IBinaryFile getBinary(IPath path) throws IOException {
		return getBinary(null, path);
	}

	@Override
	public String getFormat() {
		return "DOL";
	}

	@Override
	public boolean isBinary(byte[] hints, IPath path) {
		return path.toOSString().toLowerCase().endsWith(".dol");
	}

	@Override
	public int getHintBufferSize() {
		return 256;
	}

}
