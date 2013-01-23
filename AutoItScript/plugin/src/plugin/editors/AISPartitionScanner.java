package plugin.editors;

import org.eclipse.jface.text.rules.*;

public class AISPartitionScanner extends RuleBasedPartitionScanner {
	public final static String AIS_COMMENT = "__ais_comment";
	public final static String AIS_TAG = "__ais_tag";

	public AISPartitionScanner() {

		IToken xmlComment = new Token(AIS_COMMENT);
		IToken tag = new Token(AIS_TAG);

		IPredicateRule[] rules = new IPredicateRule[2];

		rules[0] = new MultiLineRule("<!--", "-->", xmlComment);
		rules[1] = new TagRule(tag);

		setPredicateRules(rules);
	}
}
