package com.dhcc.ms.plugin.datatype.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.preferences.ConfigurationScope;

import com.dhcc.ms.plugin.datatype.Activator;
import com.dhcc.ms.plugin.datatype.preferences.PreferenceConstants;

public abstract class Configuration {
	private final static IPath folder;
	static {
		folder = initFolder();
	}

	static IPath initFolder() {
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

	static File javadocFile() {
		return folder.append("validator-javadoc.jar").toFile();
	}

	static String rootDatatypeDocPath() {
		// return "com/dhcc/ms/datatype/BaseDataType.html";
		return Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ROOT_DATATYPE_PATH);
	}

	static String javadocCharset() {
		return Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.JAVADOC_CHARSET);
	}

	static String javadocUrl() {
		return Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.JAVADOC_URL);
	}

	static DatatypeCache datatypeCache() {
		return new DatatypeCache(folder.append("cache").toFile());
	}
}

class DatatypeCache {
	private File cacheFile;

	DatatypeCache(File cacheFile) {
		this.cacheFile = cacheFile;
	}

	public void resave(Set<Datatype> datatypes) throws IOException {
		if (!cacheFile.exists()) {
			cacheFile.createNewFile();
		}

		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(cacheFile));
		out.writeObject(datatypes);
	}

	public boolean hasSave() {
		return cacheFile.exists();
	}

	public Set<Datatype> getSave() throws IOException {
		if (!hasSave()) {
			return null;
		}

		ObjectInputStream in = new ObjectInputStream(new FileInputStream(cacheFile));
		try {
			return (Set<Datatype>) in.readObject();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void clean() {
		cacheFile.delete();
	}

}