package com.dhcc.ms.plugin.datatype;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage.ImportsManager;

import com.dhcc.ms.plugin.datatype.domain.Datatype;

public class TypeMembersCreator {
	public void createFields(Datatype[] datatypes, IType type, ImportsManager imports, IProgressMonitor monitor)
			throws JavaModelException {
		for (Datatype datatype : datatypes) {
			imports.addImport("io.swagger.annotations");
			imports.addImport("com.fasterxml.jackson.annotation");
			imports.addImport(datatype.getClassName());

			String annotations = "@ApiModelProperty(value = \"" + datatype.getDescription() + "\")\n";
			annotations += "@JsonUnwrapped\n";

			String fieldName = Character.toLowerCase(datatype.getSimpleClassName().charAt(0))
					+ datatype.getSimpleClassName().substring(1);
			String field = "private " + datatype.getSimpleClassName() + " " + fieldName + " = null;";

			String contents = annotations + field;
			IField addField = type.createField(contents, null, true, monitor);

			ISourceRange range = addField.getSourceRange();
			IBuffer typeBuf = type.getCompilationUnit().getBuffer();

			typeBuf.replace(range.getOffset(), range.getLength(), contents + "\n");
		}
	}

}
