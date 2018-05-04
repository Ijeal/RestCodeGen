package com.dhcc.ms.plugin.datatype;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class DatatypeWizardHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		DatatypeWizard wizard = new DatatypeWizard();
		wizard.init(window.getWorkbench(), HandlerUtil.getCurrentStructuredSelection(event));
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.open();

		return null;
	}
}
