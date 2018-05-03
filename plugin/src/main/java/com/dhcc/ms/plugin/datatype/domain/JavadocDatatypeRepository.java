package com.dhcc.ms.plugin.datatype.domain;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

public class JavadocDatatypeRepository {
	private static String classFilePath(File jarFile, String classFilePath) throws MalformedURLException {
		return "jar:file:/" + jarFile.getAbsolutePath() + "!/" + classFilePath;
	}

	public void init() throws Exception {
		if (!JavadocJarDownloader.isDownload()) {
			JavadocJarDownloader.redownload();
		}
	}

	public void clean() {
		Configuration.datatypeCache().clean();
		Configuration.javadocFile().delete();
	}

	public void refresh() throws Exception {
		clean();
		init();
	}

	public Set<Datatype> allDatatypes() throws IOException {
		DatatypeCache cache = Configuration.datatypeCache();
		if (cache.hasSave()) {
			return cache.getSave();
		}

		Set<Datatype> datatypes = allDatatypesFromLocalJarFile();

		cache.resave(datatypes);

		return datatypes;
	}

	private Set<Datatype> allDatatypesFromLocalJarFile() throws IOException {
		JavadocFileReader reader = new JavadocFileReader(
				classFilePath(Configuration.javadocFile(), Configuration.rootDatatypeDocPath()),
				Configuration.javadocCharset());
		Set<String> paths = reader.inheritanceClassFileAbsolutePaths();

		Set<Datatype> datatypes = new HashSet<Datatype>(paths.size());
		for (String path : paths) {
			JavadocFileReader inReader = new JavadocFileReader(path, Configuration.javadocCharset());
			datatypes.add(new Datatype(inReader.className(), inReader.simpleClassName(), inReader.classDescription()));
		}

		return datatypes;
	}
}