package com.dhcc.ms.plugin.datatype.domain.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import com.dhcc.ms.plugin.datatype.domain.JavadocDownloader;

public class UrlJavadocDownloader implements JavadocDownloader {
	private String urlPath;
	private String localPath;

	public UrlJavadocDownloader(String url, String localPath) {
		this.urlPath = url;
		this.localPath = localPath;
	}

	@Override
	public boolean isDownload() {
		return new File(localPath).exists();
	}

	@Override
	public void download() throws Exception {
		if (urlPath == null) {
			throw new Exception("not set datatype javadoc url at preference");
		}

		File file = new File(localPath);
		if (!file.exists()) {
			file.createNewFile();
		}

		URL url = new URL(urlPath);
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

	@Override
	public File file() throws Exception {
		return new File(localPath);
	}

	@Override
	public void clean() {
		new File(localPath).delete();
	}
}