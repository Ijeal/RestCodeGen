package com.dhcc.ms.plugin.datatype.preferences;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.dhcc.ms.plugin.datatype.Activator;

public class DownloaderPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public DownloaderPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	private ComboFieldEditor downloaderChecker;

	private RefreshableStringFieldEditor groupIdEditor;
	private RefreshableStringFieldEditor artifactIdEditor;
	private RefreshableStringFieldEditor versionEditor;

	private RefreshableStringFieldEditor urlEditor;

	@Override
	public void createFieldEditors() {
		// type checker
		downloaderChecker = new ComboFieldEditor(PreferenceConstants.DOWNLOADER_TYPE, "Downloader:",
				new String[][] { { "Maven", PreferenceConstants.MAVEN_DOWNLOADER_TYPE },
						{ "Url", PreferenceConstants.URL_DOWNLOADER_TYPE } },
				getFieldEditorParent());
		addField(downloaderChecker);

		// Maven
		groupIdEditor = new RefreshableStringFieldEditor(PreferenceConstants.MAVEN_DOWNLOADER_GROUPID, "&GroupId *:",
				getFieldEditorParent());
		addField(groupIdEditor);

		artifactIdEditor = new RefreshableStringFieldEditor(PreferenceConstants.MAVEN_DOWNLOADER_ARTIFACTID,
				"&ArtifactId *:", getFieldEditorParent());
		addField(artifactIdEditor);

		versionEditor = new RefreshableStringFieldEditor(PreferenceConstants.MAVEN_DOWNLOADER_VERSION, "&Version *:",
				getFieldEditorParent());
		addField(versionEditor);

		// Url
		urlEditor = new RefreshableStringFieldEditor(PreferenceConstants.URL_DOWNLOADER_PATH, "&Url *:",
				getFieldEditorParent());
		addField(urlEditor);

		initFieldEditorsEnableStatus(
				Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.DOWNLOADER_TYPE));
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		boolean isTypeChanged = event.getSource().equals(downloaderChecker)
				&& !event.getNewValue().equals(event.getOldValue());
		if (isTypeChanged) {
			setValid(true);
			initFieldEditorsEnableStatus(event.getNewValue());
			checkFieldEditorsState();
		}

		super.propertyChange(event);
	}

	private void defaultFieldEditorEnableStatus() {
		mavenFieldEditorsEnableStatus();
	}

	private void initFieldEditorsEnableStatus(Object downloaderType) {
		if (PreferenceConstants.MAVEN_DOWNLOADER_TYPE.equals(downloaderType)) {
			mavenFieldEditorsEnableStatus();
		} else {
			urlFieldEditorsEnableStatus();
		}
	}

	private void mavenFieldEditorsEnableStatus() {
		groupIdEditor.setEnabled(true, getFieldEditorParent());
		artifactIdEditor.setEnabled(true, getFieldEditorParent());
		versionEditor.setEnabled(true, getFieldEditorParent());
		urlEditor.setEnabled(false, getFieldEditorParent());

		groupIdEditor.setEmptyStringAllowed(false);
		artifactIdEditor.setEmptyStringAllowed(false);
		versionEditor.setEmptyStringAllowed(false);
		urlEditor.setEmptyStringAllowed(true);
	}

	private void urlFieldEditorsEnableStatus() {
		groupIdEditor.setEnabled(false, getFieldEditorParent());
		artifactIdEditor.setEnabled(false, getFieldEditorParent());
		versionEditor.setEnabled(false, getFieldEditorParent());
		urlEditor.setEnabled(true, getFieldEditorParent());

		groupIdEditor.setEmptyStringAllowed(true);
		artifactIdEditor.setEmptyStringAllowed(true);
		versionEditor.setEmptyStringAllowed(true);
		urlEditor.setEmptyStringAllowed(false);
	}

	private void checkFieldEditorsState() {
		@SuppressWarnings("unused")
		boolean shortValidState = groupIdEditor.getRefreshValidState() && artifactIdEditor.getRefreshValidState()
				&& versionEditor.getRefreshValidState() && urlEditor.getRefreshValidState();

		checkState();
	}

	@Override
	protected void performDefaults() {
		defaultFieldEditorEnableStatus();
		super.performDefaults();
	}

	@Override
	public void init(IWorkbench workbench) {
	}
}

class RefreshableStringFieldEditor extends StringFieldEditor {
	public RefreshableStringFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}

	public boolean getRefreshValidState() {
		refreshValidState();
		return isValid();
	}
}