package plugin.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class AISConfiguration extends SourceViewerConfiguration {
	private static final Display CURR_DISP = Display.getCurrent();
	private static final TextAttribute NUMBER = new TextAttribute(new Color(CURR_DISP, IAISColorConstants.NUMBER));
	private static final TextAttribute MACRO = new TextAttribute(new Color(CURR_DISP, IAISColorConstants.MACRO));
	private static final TextAttribute VAR = new TextAttribute(new Color(CURR_DISP, IAISColorConstants.MACRO));
	private static final TextAttribute STRING = new TextAttribute(new Color(CURR_DISP, IAISColorConstants.STRING));
	private AISDoubleClickStrategy doubleClickStrategy;
	private AISTagScanner tagScanner;
	private AISScanner scanner;
	private ColorManager colorManager;

	public AISConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			AISPartitionScanner.AIS_COMMENT,
			AISPartitionScanner.AIS_TAG };
	}
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new AISDoubleClickStrategy();
		return doubleClickStrategy;
	}

	protected AISScanner getXMLScanner() {
		if (scanner == null) {
			scanner = new AISScanner(colorManager);
			scanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IAISColorConstants.DEFAULT))));
		}
		return scanner;
	}
	protected AISTagScanner getXMLTagScanner() {
		if (tagScanner == null) {
			tagScanner = new AISTagScanner(colorManager);
			tagScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IAISColorConstants.TAG))));
		}
		return tagScanner;
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr;
		dr = new DefaultDamagerRepairer(getXMLScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
		dr = new DefaultDamagerRepairer(getXMLTagScanner());
		reconciler.setDamager(dr, AISPartitionScanner.AIS_TAG);
		reconciler.setRepairer(dr, AISPartitionScanner.AIS_TAG);

		RuleBasedScanner word = new RuleBasedScanner();
		
		word.setRules(new IRule[]{ 
				//new WordRule(new WordDetector(), new Token(WORD), true),
				// Add rule for double quotes
				new SingleLineRule("\"", "\"", new Token(STRING), '\\'),
				// Add a rule for single quotes
				new SingleLineRule("'", "'", new Token(STRING), '\\'),
				
				new WordRule(new NumberDetector(), new Token(NUMBER), true), 
				new WordRule(new MacroDetector(), new Token(MACRO), true), 
				new WordRule(new VarDetector(), new Token(VAR), true) });

		dr = new DefaultDamagerRepairer(word );
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
		

		NonRuleBasedDamagerRepairer ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(IAISColorConstants.XML_COMMENT)));
		reconciler.setDamager(ndr, AISPartitionScanner.AIS_COMMENT);
		reconciler.setRepairer(ndr, AISPartitionScanner.AIS_COMMENT);

		return reconciler;
	}

}