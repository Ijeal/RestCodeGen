package com.dhcc.ms.plugin.datatype.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.dhcc.ms.plugin.datatype.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.JAVADOC_CHARSET, "UTF-8");
		store.setDefault(PreferenceConstants.ROOT_DATATYPE_PATH, "com/dhcc/ms/datatype/BaseDataType.html");
	}

}
