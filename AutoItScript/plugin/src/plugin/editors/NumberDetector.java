package plugin.editors;

import org.eclipse.jface.text.rules.IWordDetector;

public class NumberDetector implements IWordDetector {

	private char firstChar, secChar;
	private boolean isHEx;
	private boolean isSciNotation;

	@Override
	public boolean isWordStart(char c) {
		secChar = 0;
		firstChar = c;
		if(Character.isDigit(c)) return true;
		if(c == '.') return true;
		return false;
	}

	@Override
	public boolean isWordPart(char c) {
		if(firstChar == '0' & secChar == 0){
			secChar = c;			
			if(c == 'x') {
				isHEx = true;
				return true;
			}
		}
		if(isHEx & !isSciNotation & !(c < 'a' | c > 'f')) return true;
		if(!isSciNotation & !isHEx & c == 'e'){
			isSciNotation = true;
			return true;
		}
		return false;
	}

}
