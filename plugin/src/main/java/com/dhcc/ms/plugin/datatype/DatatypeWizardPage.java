package com.dhcc.ms.plugin.datatype;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.dhcc.ms.plugin.datatype.domain.Datatype;
import com.dhcc.ms.plugin.datatype.domain.DatatypeService;
import com.dhcc.ms.plugin.datatype.utils.Utils;

public class DatatypeWizardPage extends WizardPage {
	public DatatypeWizardPage(IStructuredSelection selection) {
		super("Select Datatypes");
		setTitle("Select Datatypes");
		setDescription("Select the data type as the service field.");
		this.selection = selection;
	}

	private IStructuredSelection selection;
	private Composite pageComposite;
	private CheckboxTableViewer checkboxTableViewer;
	private DatatypeChecker checker;

	public Datatype[] selectedElements() {
		return checker.checkedDatatypes();
	}

	@Override
	public boolean isPageComplete() {
		return isCurrentPage();
	}

	@Override
	public void createControl(Composite parent) {
		try {
			createPageComposite(parent);

			createCheckView();

			createButton();

			createInput();
		} catch (Exception e) {
			MessageDialog.openError(getShell(), "Datatype page open error",
					"Datatype page open error : " + e.getMessage());
		}
	}

	private void createPageComposite(Composite parent) {
		pageComposite = new Composite(parent, SWT.NONE);
		pageComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		GridLayout gridLayout = new GridLayout(2, false);
		pageComposite.setLayout(gridLayout);
		setControl(pageComposite);
	}

	private void createCheckView() {
		Composite composite = new Composite(pageComposite, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gridLayout = new GridLayout(2, true);
		composite.setLayout(gridLayout);

		// 过滤
		Text typeText = new Text(composite, SWT.SINGLE | SWT.BORDER | SWT.SEARCH | SWT.ICON_CANCEL);
		typeText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		typeText.setMessage("type filter");

		Text descriptionText = new Text(composite, SWT.SINGLE | SWT.BORDER | SWT.SEARCH | SWT.ICON_CANCEL);
		descriptionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		descriptionText.setMessage("description filter");

		// 选择
		checkboxTableViewer = CheckboxTableViewer.newCheckList(composite,
				SWT.BORDER | SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.MULTI);
		checkboxTableViewer.setAllGrayed(true);
		checkboxTableViewer.setAllChecked(true);
		Table table = checkboxTableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		table.setHeaderVisible(true);

		TableViewerColumn typeColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumn = typeColumn.getColumn();
		tableColumn.setText("Type");
		tableColumn.setAlignment(SWT.CENTER);
		tableColumn.setWidth(225);

		TableViewerColumn descriptionColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		tableColumn = descriptionColumn.getColumn();
		tableColumn.setText("Description");
		tableColumn.setAlignment(SWT.CENTER);
		tableColumn.setWidth(225);

		// ----- add handler -----

		TypeFilter typeFilter = new TypeFilter(typeText, checkboxTableViewer);
		DescriptionFilter descriptionFilter = new DescriptionFilter(descriptionText, checkboxTableViewer);
		typeText.addModifyListener(typeFilter);
		descriptionText.addModifyListener(descriptionFilter);

		checkboxTableViewer.addFilter(typeFilter);
		checkboxTableViewer.addFilter(descriptionFilter);

		checkboxTableViewer.addCheckStateListener(checker = new DatatypeChecker(checkboxTableViewer));
		checkboxTableViewer.setCheckStateProvider(checker);

		checkboxTableViewer.setContentProvider(new DatatypeContentProvider());
		checkboxTableViewer.setLabelProvider(new DatatypeLabelProvider());

		DatatypeComparator comparator = new DatatypeComparator(checkboxTableViewer);
		checkboxTableViewer.setComparator(comparator);

		typeColumn.getColumn().addSelectionListener(comparator.createTypeSelectionAdapter());
		descriptionColumn.getColumn().addSelectionListener(comparator.createDescriptionSelectionAdapter());
	}

	private void createButton() {
		Composite composite = new Composite(pageComposite, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite.setLayout(new GridLayout(1, false));

		Button redownloadButton = new Button(composite, SWT.CENTER);
		redownloadButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		redownloadButton.setText("Redownload");

		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Button selectButton = new Button(composite, SWT.NONE);
		selectButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		selectButton.setText("Select All");

		Button deselectButton = new Button(composite, SWT.NONE);
		deselectButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		deselectButton.setText("Deselect All");

		// ----- add handler -----

		redownloadButton.addMouseListener(new DatatypesMouseAdapter());
		selectButton.addMouseListener(checker.selectAll());
		deselectButton.addMouseListener(checker.deselectAll());
	}

	private void createInput() throws Exception {
		Utils.job("init datatyps data", new Runnable() {
			@Override
			public void run() {
				try {
					Set<Datatype> datatypes = DatatypeService.initialized(Utils.selectedProject(selection));

					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							checkboxTableViewer.setInput(datatypes);
						}
					});
				} catch (Exception e) {
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							MessageDialog.openError(getShell(), "Datatype wizard page",
									"init datatype data error. " + e);
						}
					});

				}
			}
		}).schedule();
	}

	class DatatypesMouseAdapter extends MouseAdapter {
		@Override
		public void mouseDown(MouseEvent e) {
			checker.cleanCheckedDatatypes();

			Utils.job("init datatyps data", new Runnable() {
				@Override
				public void run() {
					try {
						Set<Datatype> datatypes = DatatypeService.forceRefresh(Utils.selectedProject(selection));

						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								checkboxTableViewer.setInput(datatypes);
							}
						});
					} catch (Exception e) {
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								MessageDialog.openError(getShell(), "Datatype wizard page",
										"redownload datatype javadoc error. " + e);
							}
						});
					}
				}
			}).schedule();
		}
	}
}

abstract class TextFilter extends ViewerFilter implements ModifyListener {
	private Text text;
	private CheckboxTableViewer viewer;
	private String filterValue;

	TextFilter(Text text, CheckboxTableViewer viewer) {
		this.text = text;
		this.viewer = viewer;
	}

	@Override
	public void modifyText(ModifyEvent e) {
		this.filterValue = text.getText();

		viewer.refresh(false);
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (!(element instanceof Datatype) || filterValue == null)
			return true;
		return doSelect((Datatype) element, filterValue);
	}

	abstract boolean doSelect(Datatype element, String filterValue);
}

class TypeFilter extends TextFilter {
	TypeFilter(Text text, CheckboxTableViewer viewer) {
		super(text, viewer);
	}

	@Override
	boolean doSelect(Datatype element, String filterValue) {
		return element.getSimpleClassName().toLowerCase().contains(filterValue.toLowerCase());
	}
}

class DescriptionFilter extends TextFilter {
	DescriptionFilter(Text text, CheckboxTableViewer viewer) {
		super(text, viewer);
	}

	@Override
	boolean doSelect(Datatype element, String filterValue) {
		return element.getDescription().toLowerCase().contains(filterValue.toLowerCase());
	}
}

class DatatypeComparator extends ViewerComparator {
	private CheckboxTableViewer viewer;
	private Comparator<Datatype> activeComparator;

	public DatatypeComparator(CheckboxTableViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		boolean isType = e1 instanceof Datatype && e2 instanceof Datatype;
		if (!isType) {
			return super.compare(viewer, e1, e2);
		}

		boolean isActive = activeComparator != null;
		if (!isActive) {
			return super.compare(viewer, e1, e2);
		}

		return activeComparator.compare((Datatype) e1, (Datatype) e2);
	}

	SelectionAdapter createTypeSelectionAdapter() {
		TypeColumnSelection selection = new TypeColumnSelection();
		boolean isActive = activeComparator != null;
		if (!isActive) {
			activeComparator = selection;
		}

		return selection;
	}

	SelectionAdapter createDescriptionSelectionAdapter() {
		return new DescriptionColumnSelection();
	}

	abstract class ColumnSelection extends SelectionAdapter implements Comparator<Datatype> {
		private boolean oddSelection;

		@Override
		public void widgetSelected(SelectionEvent e) {
			oddSelection = !oddSelection;

			activeComparator = this;
			viewer.refresh(false);
		}

		@Override
		public int compare(Datatype o1, Datatype o2) {
			return oddSelection ? doCompare(o1, o2) : -doCompare(o1, o2);
		}

		abstract int doCompare(Datatype o1, Datatype o2);
	}

	class TypeColumnSelection extends ColumnSelection {
		@Override
		int doCompare(Datatype o1, Datatype o2) {
			return o1.getSimpleClassName().compareTo(o2.getSimpleClassName());
		}
	}

	class DescriptionColumnSelection extends ColumnSelection {
		@Override
		int doCompare(Datatype o1, Datatype o2) {
			return o1.getDescription().compareTo(o2.getDescription());
		}
	}
}

class DatatypeContentProvider implements IStructuredContentProvider {
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Set) {
			return ((Set<?>) inputElement).toArray();
		}
		return new Object[0];
	}
}

class DatatypeLabelProvider extends LabelProvider implements ITableLabelProvider {
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return getImage(element);
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (!(element instanceof Datatype))
			return null;

		switch (columnIndex) {
		case 0:
			return ((Datatype) element).getSimpleClassName();
		case 1:
			return ((Datatype) element).getDescription();
		default:
			return null;
		}
	}
}

class DatatypeChecker implements ICheckStateListener, ICheckStateProvider {
	private CheckboxTableViewer viewer;

	private Set<Object> checkedElementSet;

	DatatypeChecker(CheckboxTableViewer viewer) {
		this.viewer = viewer;
		this.checkedElementSet = new HashSet<Object>();
	}

	class SelectAll extends MouseAdapter {
		@Override
		public void mouseDown(MouseEvent e) {
			for (TableItem item : viewer.getTable().getItems()) {
				checkedElementSet.add(item.getData());
			}

			viewer.refresh();
		}
	}

	class DeselectAll extends MouseAdapter {
		@Override
		public void mouseDown(MouseEvent e) {
			for (TableItem item : viewer.getTable().getItems()) {
				checkedElementSet.remove(item.getData());
			}

			viewer.refresh();
		}
	}

	public MouseAdapter selectAll() {
		return new SelectAll();
	}

	public MouseAdapter deselectAll() {
		return new DeselectAll();
	}

	@Override
	public void checkStateChanged(CheckStateChangedEvent event) {
		for (TableItem item : viewer.getTable().getItems()) {
			checkedElementSet.remove(item.getData());
		}

		for (Object element : viewer.getCheckedElements()) {
			checkedElementSet.add(element);
		}
	}

	Datatype[] checkedDatatypes() {
		return checkedElementSet.toArray(new Datatype[0]);
	}

	void cleanCheckedDatatypes() {
		checkedElementSet.clear();
	}

	@Override
	public boolean isChecked(Object element) {
		return checkedElementSet.contains(element);
	}

	@Override
	public boolean isGrayed(Object element) {
		return false;
	}
}
