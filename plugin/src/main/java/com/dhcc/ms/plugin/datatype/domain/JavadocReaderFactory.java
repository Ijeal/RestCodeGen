package com.dhcc.ms.plugin.datatype.domain;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.preferences.ConfigurationScope;

import com.dhcc.ms.plugin.datatype.Activator;
import com.dhcc.ms.plugin.datatype.domain.reader.CacheJavadocReader;
import com.dhcc.ms.plugin.datatype.domain.reader.JavadocFileReader;
import com.dhcc.ms.plugin.datatype.preferences.PreferenceConstants;

public class JavadocReaderFactory {
	private JavadocReaderFactory() {
	}

	private final static IPath folder;
	static {
		folder = initFolder();
	}

	private static IPath initFolder() {
		IPath path = ConfigurationScope.INSTANCE.getLocation().append("com.dhcc.ms.plugin").append("datatype");

		File folder = path.toFile();
		if (!folder.exists()) {
			folder.mkdirs();
		}
		if (!folder.isDirectory()) {
			folder.delete();
			folder.mkdirs();
		}
		return path;
	}

	private static String rootDatatypePagePath() {
		// return "com/dhcc/ms/datatype/BaseDataType.html";
		return Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ROOT_DATATYPE_PATH);
	}

	private static String javadocCharset() {
		return Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.JAVADOC_CHARSET);
	}

	private static File cacheFile() {
		return folder.append("cache").toFile();
	}

	public static JavadocReader createJavadocReader() throws Exception {
		JavadocFileReader reader = new JavadocFileReader(rootDatatypePagePath(), javadocCharset());
		return new CacheJavadocReader(reader, cacheFile());
	}
}