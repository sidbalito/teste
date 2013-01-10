package plugin.editors;

import org.eclipse.jface.text.rules.IWordDetector;

public class VarDetector implements IWordDetector {

	@Override
	public boolean isWordStart(char c) {
		return c == '$';
	}

	@Override
	public boolean isWordPart(char c) {
		if(c == '_') return true;
		if(c < 'a' | c > 'z') return false;
		if(c < '0' | c > '9') return false;
		return true;
	}

}
