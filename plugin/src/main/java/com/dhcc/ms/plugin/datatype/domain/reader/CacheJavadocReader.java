package com.dhcc.ms.plugin.datatype.domain.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import com.dhcc.ms.plugin.datatype.domain.Datatype;
import com.dhcc.ms.plugin.datatype.domain.JavadocReader;

public class CacheJavadocReader implements JavadocReader {
	private JavadocReader reader;
	private DatatypeCache cache;

	public CacheJavadocReader(JavadocReader reader, File cacheFile) {
		this.reader = reader;
		this.cache = new DatatypeCache(cacheFile);
	}

	@Override
	public Set<Datatype> allDatatypes(File javadocFile) throws Exception {
		if (cache.hasSave()) {
			return cache.getSave();
		}

		Set<Datatype> datatypes = reader.allDatatypes(javadocFile);

		cache.resave(datatypes);

		return datatypes;
	}

	@Override
	public void clean() {
		cache.clean();
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