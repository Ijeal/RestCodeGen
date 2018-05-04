package com.dhcc.ms.plugin.datatype;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage.ImportsManager;

import com.dhcc.ms.plugin.datatype.domain.Datatype;

public class TypeMembersCreator {
	public void createFields(Datatype[] datatypes, IType type, ImportsManager imports, IProgressMonitor monitor)
			throws JavaModelException {
		if (datatypes == null || datatypes.length <= 0) {
			return;
		}

		imports.addImport("io.swagger.annotations");
		imports.addImport("com.fasterxml.jackson.annotation");

		for (Datatype datatype : datatypes) {
			imports.addImport(datatype.getClassName());

			StringBuffer buffer = new StringBuffer();
			buffer.append("@ApiModelProperty(value = \"").append(datatype.getDescription()).append("\")\r\n");
			buffer.append("@JsonUnwrapped\r\n");

			String fieldName = Character.toLowerCase(datatype.getSimpleClassName().charAt(0))
					+ datatype.getSimpleClassName().substring(1);
			buffer.append("private ").append(datatype.getSimpleClassName()).append(" ").append(fieldName)
					.append(" = null;");

			String contents = buffer.toString();
			type.createField(contents, null, true, monitor);
		}
	}

}
