package com.dhcc.ms.plugin.datatype.domain;

import org.eclipse.core.resources.IProject;

import com.dhcc.ms.plugin.datatype.Activator;
import com.dhcc.ms.plugin.datatype.domain.downloader.MavenJavadocDownloader;
import com.dhcc.ms.plugin.datatype.domain.downloader.UrlJavadocDownloader;
import com.dhcc.ms.plugin.datatype.preferences.PreferenceConstants;

public class JavadocDownloaderFactory {
	private JavadocDownloaderFactory() {
	}

	private static boolean isMavenDownloader() {
		return true;
	}

	private static String groupId() {
		return "com.dhcc.ms";
	}

	private static String artifactId() {
		return "validator";
	}

	private static String version() {
		return "1.0.0-SNAPSHOT";
	}

	private static String url() {
		return Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.URL_DOWNLOADER_PATH);
	}

	private static String localPath() {
		return null;
	}

	public static JavadocDownloader createJavadocDownloder(IProject project) {
		if (isMavenDownloader()) {
			return createMavenJavadocDownloder(project);
		} else {
			return createUrlJavadocDownloder();
		}
	}

	private static MavenJavadocDownloader createMavenJavadocDownloder(IProject project) {
		return new MavenJavadocDownloader(project, groupId(), artifactId(), version());
	}

	private static UrlJavadocDownloader createUrlJavadocDownloder() {
		return new UrlJavadocDownloader(url(), localPath());
	}

}