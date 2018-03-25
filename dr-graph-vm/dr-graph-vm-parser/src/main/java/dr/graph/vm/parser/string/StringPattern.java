package dr.graph.vm.parser.string;

import java.util.Optional;

import dr.graph.vm.parser.Pattern;

public class StringPattern implements Pattern<Character> {

	private final String pattern;
	public final Type type;

	private int index;

	StringPattern(String pattern, Type type) {
		this.pattern = pattern;
		this.type = type;
		reset();
	}
	
	protected String get() {
		return pattern;
	}

	@Override
	public Optional<Boolean> test(Character ch) {

		if(pattern == null) {
			return Optional.empty();
		}
		
		if (isComplete()) {
			return Optional.of(true);
		}

		if (ch == pattern.charAt(this.index)) {
			this.index++;
			return Optional.of(isComplete());
		}
		reset();
		return Optional.empty();

	}

	@Override
	public void reset() {
		this.index = 0;
	}

	@Override
	public boolean isComplete() {
		boolean complete = index == pattern.length();
		if(complete) {
			reset();
		}
		return complete;
	}

	@Override
	public boolean isActive() {
		return !isComplete() && index > 0;
	}
	
	public static StringPattern createStart(String boundary) {
		return new StringPattern(boundary,Type.START);
	}
	
	public static StringPattern createEnd(String boundary) {
		return new StringPattern(boundary,Type.END);
	}
	
	public static StringPattern empty() {
		return new StringPattern(null,Type.START);
	}

}
