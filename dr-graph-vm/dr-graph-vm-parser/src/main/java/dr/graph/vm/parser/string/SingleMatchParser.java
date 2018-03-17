package dr.graph.vm.parser.string;

public class SingleMatchParser extends FastStringParser {

	public SingleMatchParser(String start, String end) {
		super(start, end);
	}

	@Override
	protected boolean done() {
		if (matches.size() > 0) {
			return true;
		}
		return false;
	}

}
