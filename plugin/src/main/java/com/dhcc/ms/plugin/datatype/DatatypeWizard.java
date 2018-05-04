package com.dhcc.ms.plugin.datatype;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;

public class DatatypeWizard extends NewElementWizard {

	private NewClassWizardPage fPage;
	private boolean fOpenEditorOnFinish;

	private DatatypeWizardPage datatypePage;

	private DatatypeWizard(NewClassWizardPage page, boolean openEditorOnFinish) {
		setDefaultPageImageDescriptor(JavaPluginImages.DESC_WIZBAN_NEWCLASS);
		setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
		setWindowTitle(NewWizardMessages.NewClassCreationWizard_title);

		fPage = page;
		fOpenEditorOnFinish = openEditorOnFinish;
	}

	public DatatypeWizard() {
		this(null, true);
	}

	/*
	 * @see Wizard#createPages
	 */
	@Override
	public void addPages() {
		super.addPages();
		if (fPage == null) {
			fPage = new NewClassWizardPage() {
				@Override
				protected void createTypeMembers(IType type, ImportsManager imports, IProgressMonitor monitor)
						throws CoreException {
					new TypeMembersCreator().createFields(datatypePage.selectedElements(), type, imports, monitor);

					super.createTypeMembers(type, imports, monitor);
				}
			};
			fPage.setWizard(this);
			fPage.init(getSelection());
		}
		addPage(fPage);

		if (datatypePage == null) {
			datatypePage = new DatatypeWizardPage(getSelection());
		}
		addPage(datatypePage);
	}

	@Override
	protected boolean canRunForked() {
		return !fPage.isEnclosingTypeSelected();
	}

	@Override
	protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
		fPage.createType(monitor); // use the full progress monitor
	}

	@Override
	public boolean performFinish() {
		warnAboutTypeCommentDeprecation();
		boolean res = super.performFinish();
		if (res) {
			IResource resource = fPage.getModifiedResource();
			if (resource != null) {
				selectAndReveal(resource);
				if (fOpenEditorOnFinish) {
					openResource((IFile) resource);
				}
			}
		}
		return res;
	}

	@Override
	public IJavaElement getCreatedElement() {
		return fPage.getCreatedType();
	}

}
