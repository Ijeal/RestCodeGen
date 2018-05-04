package com.dhcc.ms.plugin.datatype.domain.downloader;

import java.io.File;
import java.lang.reflect.Method;

import org.apache.maven.artifact.Artifact;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.ArtifactKey;
import org.eclipse.m2e.jdt.MavenJdtPlugin;
import org.eclipse.m2e.jdt.internal.BuildPathManager;

import com.dhcc.ms.plugin.datatype.domain.JavadocDownloader;
import com.dhcc.ms.plugin.datatype.utils.Utils;

public class MavenJavadocDownloader implements JavadocDownloader {
	private static final String JAVADOC_CALSSIFIER = "javadoc";
	private static Method downloadJobMethod;
	private static BuildPathManager manager;

	private IProject project;
	private ArtifactKey key;

	public MavenJavadocDownloader(IProject project, String groupId, String artifactId, String version) {
		this.project = project;
		this.key = new ArtifactKey(groupId, artifactId, version, null);
	}

	private Method downloadJobMethod() {
		if (downloadJobMethod != null) {
			return downloadJobMethod;
		}

		try {
			Class a = getClass().forName("org.eclipse.m2e.jdt.internal.DownloadSourcesJob");
			Method m = a.getMethod("scheduleDownload", IProject.class, ArtifactKey.class, boolean.class, boolean.class);
			m.setAccessible(true);
			downloadJobMethod = m;
			return downloadJobMethod;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Job downloadJob() {
		if (manager != null) {
			return manager.getDownloadSourcesJob();
		}

		BuildPathManager manager = (BuildPathManager) MavenJdtPlugin.getDefault().getBuildpathManager();
		this.manager = manager;

		return this.manager.getDownloadSourcesJob();
	}

	@Override
	public boolean isDownload() {
		try {
			return file().exists();
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void download() throws Exception {
		asyncDownload();
	}

	public void asyncDownload() throws Exception {
		Job job = downloadJob();

		downloadJobMethod().invoke(job, project, key, false, true);
	}

	@Override
	public File file() throws Exception {
		Artifact artifact = MavenPlugin.getMaven().resolve(key.getGroupId(), key.getArtifactId(), key.getVersion(),
				"jar", JAVADOC_CALSSIFIER, Utils.projectAllArtifactRepositorys(project), null);
		return artifact.getFile();
	}

	@Override
	public void clean() {
	}
}
