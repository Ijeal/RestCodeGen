package com.dhcc.ms.plugin.datatype.domain;

import java.util.Set;

public class DatatypeService {
	public static Set<Datatype> initialized() throws Exception {
		if (!JavadocJarDownloader.isDownload()) {
			JavadocJarDownloader.redownload();
		}

		JavadocDatatypeRepository repository = new JavadocDatatypeRepository();
		return repository.allDatatypes();
	}

	public static Set<Datatype> forceRefresh() throws Exception {
		JavadocDatatypeRepository repository = new JavadocDatatypeRepository();
		repository.clean();
		JavadocJarDownloader.redownload();
		return repository.allDatatypes();
	}
}
