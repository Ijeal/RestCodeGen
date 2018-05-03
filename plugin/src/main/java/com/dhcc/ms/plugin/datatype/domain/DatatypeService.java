package com.dhcc.ms.plugin.datatype.domain;

import java.util.Set;

import org.eclipse.core.resources.IProject;

public class DatatypeService {
	public static Set<Datatype> initialized(IProject project) throws Exception {
		JavadocDatatypeRepository repository = new JavadocDatatypeRepository(
				JavadocDownloaderFactory.createJavadocDownloder(project), JavadocReaderFactory.createJavadocReader());
		repository.init();
		return repository.allDatatypes();
	}

	public static Set<Datatype> forceRefresh(IProject project) throws Exception {
		JavadocDatatypeRepository repository = new JavadocDatatypeRepository(
				JavadocDownloaderFactory.createJavadocDownloder(project), JavadocReaderFactory.createJavadocReader());
		repository.refresh();
		return repository.allDatatypes();
	}
}
