package plugin.editors;

import org.eclipse.ui.editors.text.TextEditor;

public class AISEditor extends TextEditor {

	private ColorManager colorManager;

	public AISEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new AISConfiguration(colorManager));
		setDocumentProvider(new AISDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
