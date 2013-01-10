package plugin.editors;

import org.eclipse.jface.text.rules.IWordDetector;

public class WordDetector implements IWordDetector {

	@Override
	public boolean isWordStart(char c) {
		c = Character.toLowerCase(c);
		return !(c < 'a' | c > 'z');
	}

	@Override
	public boolean isWordPart(char c) {
		c = Character.toLowerCase(c);
		return !(c < 'a' | c > 'z');
	}

}
