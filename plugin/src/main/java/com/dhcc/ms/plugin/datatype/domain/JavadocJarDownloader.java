package com.dhcc.ms.plugin.datatype.domain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class JavadocJarDownloader {
	public static boolean isDownload() {
		return Configuration.javadocFile().exists();
	}

	public static void initJavadocFile() throws Exception {
		if (isDownload()) {
			return;
		}

		redownload();
	}

	public static void redownload() throws Exception {
		String fileUrl = Configuration.javadocUrl();
		if (fileUrl == null) {
			throw new Exception("not set datatype javadoc url at preference");
		}

		File file = Configuration.javadocFile();
		if (!file.exists()) {
			file.createNewFile();
		}

		URL url = new URL(fileUrl);
		InputStream in = null;
		FileOutputStream out = null;
		try {
			in = url.openStream();
			out = new FileOutputStream(file);

			byte[] data = new byte[128];
			while (in.read(data) > 0) {
				out.write(data);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}
}