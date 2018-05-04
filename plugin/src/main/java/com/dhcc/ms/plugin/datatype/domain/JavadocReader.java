package com.dhcc.ms.plugin.datatype.domain;

import java.io.File;
import java.util.Set;

public interface JavadocReader {

	Set<Datatype> allDatatypes(File javadocFile) throws Exception;

	void clean();

}
