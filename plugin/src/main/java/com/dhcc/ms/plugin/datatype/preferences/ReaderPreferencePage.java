package com.dhcc.ms.plugin.datatype.preferences;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.dhcc.ms.plugin.datatype.Activator;

public class ReaderPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public ReaderPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public void createFieldEditors() {
		addField(new ComboFieldEditor(PreferenceConstants.JAVADOC_CHARSET, "A Javadoc jar &Charset:",
				new String[][] { { "UTF-8", "UTF-8" }, { "GBK", "GBK" } }, getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.ROOT_DATATYPE_PATH, "A &Root datatype file path:",
				getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}