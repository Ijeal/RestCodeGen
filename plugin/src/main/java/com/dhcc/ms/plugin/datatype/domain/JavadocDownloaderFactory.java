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
		return PreferenceConstants.MAVEN_DOWNLOADER_TYPE
				.equals(Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.DOWNLOADER_TYPE));
	}

	private static String groupId() {
		return Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.MAVEN_DOWNLOADER_GROUPID);
	}

	private static String artifactId() {
		return Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.MAVEN_DOWNLOADER_ARTIFACTID);
	}

	private static String version() {
		return Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.MAVEN_DOWNLOADER_VERSION);
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