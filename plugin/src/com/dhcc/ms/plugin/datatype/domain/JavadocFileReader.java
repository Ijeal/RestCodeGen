package com.dhcc.ms.plugin.datatype.domain;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JavadocFileReader {
	private String classFilePath;
	private URL classFileUrl;
	private String charset;
	private Document document;

	public JavadocFileReader(String classFilePath, String charset) throws MalformedURLException {
		this.classFilePath = classFilePath;
		this.classFileUrl = new URL(classFilePath);
		this.charset = charset;
	}

	private Document jarFileDocument() throws IOException {
		if (document != null) {
			return document;
		}

		document = Jsoup.parse(classFileUrl.openStream(), charset, classFilePath);
		return document;
	}

	public Set<String> inheritanceClassFileAbsolutePaths() throws IOException {
		Elements allDatatypeDocPathElements = jarFileDocument().getElementsByClass("description").get(0)
				.getElementsContainingOwnText("所有已知实现类").first().parent().getElementsByTag("a");

		Set<String> readers = new HashSet<String>(allDatatypeDocPathElements.size());

		for (int i = 0; i < allDatatypeDocPathElements.size(); i++) {
			readers.add(allDatatypeDocPathElements.get(i).absUrl("href").replaceFirst("jar:/file", "jar:file"));
		}

		return readers;
	}

	public String className() throws IOException {
		return jarFileDocument().getElementsByClass("contentContainer").first().getElementsByClass("inheritance").last()
				.getElementsByTag("li").first().text();
	}

	public String simpleClassName() throws IOException {
		return jarFileDocument().getElementsByClass("typeNameLabel").first().text();
	}

	public String classDescription() throws IOException {
		return jarFileDocument().getElementsByClass("contentContainer").first().getElementsByClass("block").first()
				.text();
	}
}