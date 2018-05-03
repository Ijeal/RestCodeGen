package com.dhcc.ms.plugin.datatype.domain;

import java.util.Set;

public class DatatypeService {
	public static Set<Datatype> initialized() throws Exception {
		JavadocDatatypeRepository repository = new JavadocDatatypeRepository();
		repository.init();
		return repository.allDatatypes();
	}

	public static Set<Datatype> forceRefresh() throws Exception {
		JavadocDatatypeRepository repository = new JavadocDatatypeRepository();
		repository.refresh();
		return repository.allDatatypes();
	}
}
