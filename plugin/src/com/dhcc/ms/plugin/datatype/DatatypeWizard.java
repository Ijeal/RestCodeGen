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

import com.dhcc.ms.plugin.datatype.domain.Datatype;

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

		datatypePage = new DatatypeWizardPage();
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
					super.createTypeMembers(type, imports, monitor);

					for (Datatype datatype : datatypePage.selectedElements()) {
						imports.addImport(datatype.getClassName());

						String comment = "/** \n * " + datatype.getDescription() + "\n */\n";
						String fieldName = Character.toLowerCase(datatype.getSimpleClassName().charAt(0))
								+ datatype.getSimpleClassName().substring(1);
						String field = "private " + datatype.getSimpleClassName() + " " + fieldName + " = null;";
						String contents = comment + field;
						type.createField(contents, null, true, monitor);
					}
				}
			};
			fPage.setWizard(this);
			fPage.init(getSelection());
		}
		addPage(fPage);
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
