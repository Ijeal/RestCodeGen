package com.dhcc.ms.plugin.datatype.utils;

import java.util.List;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.project.MavenProject;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.IMavenProjectRegistry;
import org.eclipse.ui.handlers.HandlerUtil;

public class Utils {

	public static IProject selectedProject(ExecutionEvent event) {
		return selectedProject(HandlerUtil.getCurrentStructuredSelection(event));
	}

	public static IProject selectedProject(IStructuredSelection selection) {
		Object element = selection.getFirstElement();

		if (element instanceof IResource) {
			return ((IResource) element).getProject();
		}

		if (element instanceof PackageFragmentRootContainer) {
			IJavaProject jProject = ((PackageFragmentRootContainer) element).getJavaProject();
			return jProject.getProject();
		}

		if (element instanceof IJavaElement) {
			IJavaProject jProject = ((IJavaElement) element).getJavaProject();
			return jProject.getProject();
		}

		return null;
	}

	public static List<ArtifactRepository> projectAllArtifactRepositorys(IProject project) throws CoreException {
		IMavenProjectRegistry projectManager = MavenPlugin.getMavenProjectRegistry();
		IMavenProjectFacade ex = projectManager.create(project, null);

		if (ex != null) {
			MavenProject mavenProject = ex.getMavenProject(null);
			return mavenProject.getRemoteArtifactRepositories();
		} else {
			return MavenPlugin.getMaven().getArtifactRepositories();
		}
	}

	public static Job job(String name, Runnable run) {
		return new Job(name) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				run.run();
				return Status.OK_STATUS;
			}
		};
	}
}
