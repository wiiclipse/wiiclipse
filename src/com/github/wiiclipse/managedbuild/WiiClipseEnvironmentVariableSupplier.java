package com.github.wiiclipse.managedbuild;

import java.io.File;

import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.envvar.IBuildEnvironmentVariable;
import org.eclipse.cdt.managedbuilder.envvar.IConfigurationEnvironmentVariableSupplier;
import org.eclipse.cdt.managedbuilder.envvar.IEnvironmentVariableProvider;
import org.eclipse.core.runtime.IPath;

import com.github.wiiclipse.core.WiiClipsePathResolver;

public class WiiClipseEnvironmentVariableSupplier implements
		IConfigurationEnvironmentVariableSupplier {

	private static class EnvironentVariable implements
			IBuildEnvironmentVariable {

		private String _name;
		private String _value;
		private int _operation;
		private String _delimiter;

		public EnvironentVariable(String name, String value, int operation,
				String delimiter) {
			_name = name;
			_value = value;
			_operation = operation;
			_delimiter = delimiter;
		}

		@Override
		public String getName() {
			return _name;
		}

		@Override
		public String getValue() {
			return _value;
		}

		@Override
		public int getOperation() {
			return _operation;
		}

		@Override
		public String getDelimiter() {
			return _delimiter;
		}

	}

	private static final String PATH_VARIABLE = "PATH";

	@Override
	public IBuildEnvironmentVariable getVariable(String variableName,
			IConfiguration configuration, IEnvironmentVariableProvider provider) {
		if (variableName == null)
			return null;
		if (!PATH_VARIABLE.equalsIgnoreCase(variableName))
			return null;

		IPath binPath = WiiClipsePathResolver.getDevkitPPCBinPath();
		if (binPath == null)
			return null;

		return new EnvironentVariable(PATH_VARIABLE,
				binPath.toFile().getPath(),
				IBuildEnvironmentVariable.ENVVAR_PREPEND, File.pathSeparator);
	}

	@Override
	public IBuildEnvironmentVariable[] getVariables(
			IConfiguration configuration, IEnvironmentVariableProvider provider) {
		IBuildEnvironmentVariable[] tmp = new IBuildEnvironmentVariable[1];
		tmp[0] = getVariable(PATH_VARIABLE, configuration, provider);
		if (tmp[0] != null)
			return tmp;
		return null;
	}

}
