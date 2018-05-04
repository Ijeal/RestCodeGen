package com.dhcc.ms.plugin.datatype.preferences;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.dhcc.ms.plugin.datatype.Activator;

public class DownloaderPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public DownloaderPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	private ComboFieldEditor downloaderChecker;

	private StringFieldEditor groupIdEditor;
	private StringFieldEditor artifactIdEditor;
	private StringFieldEditor versionEditor;

	private StringFieldEditor urlEditor;

	@Override
	public void createFieldEditors() {
		// type checker
		downloaderChecker = new ComboFieldEditor(PreferenceConstants.DOWNLOADER_TYPE, "Downloader:",
				new String[][] { { "Maven", PreferenceConstants.MAVEN_DOWNLOADER_TYPE },
						{ "Url", PreferenceConstants.URL_DOWNLOADER_TYPE } },
				getFieldEditorParent());
		addField(downloaderChecker);

		// Maven
		groupIdEditor = new StringFieldEditor(PreferenceConstants.MAVEN_DOWNLOADER_GROUPID, "&GroupId:",
				getFieldEditorParent());
		addField(groupIdEditor);

		artifactIdEditor = new StringFieldEditor(PreferenceConstants.MAVEN_DOWNLOADER_ARTIFACTID, "&ArtifactId:",
				getFieldEditorParent());
		addField(artifactIdEditor);

		versionEditor = new StringFieldEditor(PreferenceConstants.MAVEN_DOWNLOADER_VERSION, "&Version:",
				getFieldEditorParent());
		addField(versionEditor);

		// Url
		urlEditor = new StringFieldEditor(PreferenceConstants.URL_DOWNLOADER_PATH, "&Url:", getFieldEditorParent());
		addField(urlEditor);

		urlEditor.setEnabled(false, getFieldEditorParent());
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);

		if (!event.getSource().equals(downloaderChecker)) {
			return;
		}

		if (PreferenceConstants.MAVEN_DOWNLOADER_TYPE.equals(event.getNewValue())) {
			groupIdEditor.setEnabled(true, getFieldEditorParent());
			artifactIdEditor.setEnabled(true, getFieldEditorParent());
			versionEditor.setEnabled(true, getFieldEditorParent());
			urlEditor.setEnabled(false, getFieldEditorParent());
		} else {
			groupIdEditor.setEnabled(false, getFieldEditorParent());
			artifactIdEditor.setEnabled(false, getFieldEditorParent());
			versionEditor.setEnabled(false, getFieldEditorParent());
			urlEditor.setEnabled(true, getFieldEditorParent());
		}
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}