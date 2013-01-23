package plugin.editors;

import org.eclipse.jface.text.rules.IWordDetector;

public class MacroDetector implements IWordDetector {

	@Override
	public boolean isWordStart(char c) {
		return c == '@';
	}

	@Override
	public boolean isWordPart(char c) {
		return !(c < 'a' | c > 'z');
	}

}
