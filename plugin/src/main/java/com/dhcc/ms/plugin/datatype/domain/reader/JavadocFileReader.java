package com.dhcc.ms.plugin.datatype.domain.reader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.dhcc.ms.plugin.datatype.domain.Datatype;
import com.dhcc.ms.plugin.datatype.domain.JavadocReader;

public class JavadocFileReader implements JavadocReader {
	private String rootDatatypePagePath;
	private String charset;

	public JavadocFileReader(String rootDatatypePagePath, String charset) throws MalformedURLException {
		this.rootDatatypePagePath = rootDatatypePagePath;
		this.charset = charset;
	}

	private String rootDatatypePageAbsolutePath(File javadocFile) throws MalformedURLException {
		return "jar:file:/" + javadocFile.getAbsolutePath() + "!/" + rootDatatypePagePath;
	}

	@Override
	public Set<Datatype> allDatatypes(File javadocFile) throws Exception {
		String rootDatatypePageAbsolutePath = rootDatatypePageAbsolutePath(javadocFile);

		Set<String> paths = null;

		Reader reader = new Reader(rootDatatypePageAbsolutePath, charset);
		try {
			paths = reader.inheritanceClassFileAbsolutePaths();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		return readToDatatypes(paths);

	}

	private Set<Datatype> readToDatatypes(Set<String> allClassPageAbsolutePaths) throws Exception {
		Set<Datatype> datatypes = new HashSet<Datatype>(allClassPageAbsolutePaths.size());
		for (String path : allClassPageAbsolutePaths) {
			datatypes.add(readToDatatype(path));
		}
		return datatypes;
	}

	private Datatype readToDatatype(String classPageAbsolutePath) throws Exception {
		Reader inReader = new Reader(classPageAbsolutePath, charset);
		try {
			return new Datatype(inReader.className(), inReader.simpleClassName(), inReader.classDescription());
		} finally {
			if (inReader != null) {
				inReader.close();
			}
		}
	}

	@Override
	public void clean() {
	}

}

class Reader {
	private String classFilePath;
	private URL classFileUrl;
	private String charset;

	private InputStream in;
	private Document document;

	Reader(String classFilePath, String charset) throws MalformedURLException {
		this.classFilePath = classFilePath;
		this.classFileUrl = new URL(classFilePath);
		this.charset = charset;
	}

	private Document jarFileDocument() throws IOException {
		if (document != null) {
			return document;
		}

		in = classFileUrl.openStream();
		document = Jsoup.parse(in, charset, classFilePath);
		return document;
	}

	Set<String> inheritanceClassFileAbsolutePaths() throws IOException {
		Elements allDatatypeDocPathElements = jarFileDocument().getElementsByClass("description").get(0)
				.getElementsContainingOwnText("所有已知实现类").first().parent().getElementsByTag("a");

		Set<String> readers = new HashSet<String>(allDatatypeDocPathElements.size());

		for (int i = 0; i < allDatatypeDocPathElements.size(); i++) {
			readers.add(allDatatypeDocPathElements.get(i).absUrl("href").replaceFirst("jar:/file", "jar:file"));
		}

		return readers;
	}

	String className() throws IOException {
		return jarFileDocument().getElementsByClass("contentContainer").first().getElementsByClass("inheritance").last()
				.getElementsByTag("li").first().text();
	}

	String simpleClassName() throws IOException {
		return jarFileDocument().getElementsByClass("typeNameLabel").first().text();
	}

	String classDescription() throws IOException {
		return jarFileDocument().getElementsByClass("contentContainer").first().getElementsByClass("block").first()
				.text();
	}

	void close() {
		try {
			if (in != null) {
				in.close();
			}
		} catch (IOException e) {
		}
	}
}
