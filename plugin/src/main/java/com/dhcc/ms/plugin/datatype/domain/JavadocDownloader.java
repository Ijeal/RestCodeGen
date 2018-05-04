package com.dhcc.ms.plugin.datatype.domain;

import java.io.File;

public interface JavadocDownloader {

	boolean isDownload();

	void download() throws Exception;

	File file() throws Exception;

	void clean();

}
