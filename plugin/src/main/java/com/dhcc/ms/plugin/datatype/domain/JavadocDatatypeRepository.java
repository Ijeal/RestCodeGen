package com.dhcc.ms.plugin.datatype.domain;

import java.util.Set;

public class JavadocDatatypeRepository {
	private JavadocDownloader downloder;
	private JavadocReader reader;

	public JavadocDatatypeRepository(JavadocDownloader downloder, JavadocReader reader) {
		this.downloder = downloder;
		this.reader = reader;
	}

	public void init() throws Exception {
		if (!downloder.isDownload()) {
			downloder.download();
		}
	}

	public void clean() {
		downloder.clean();
		reader.clean();
	}

	public void refresh() throws Exception {
		clean();
		init();
	}

	public Set<Datatype> allDatatypes() throws Exception {
		return reader.allDatatypes(downloder.file());
	}
}