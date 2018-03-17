package dr.graph.vm.parser.string;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import dr.graph.vm.parser.Parser;

public abstract class StringParser implements Parser<Set<String>, String> {
	
	protected final Map<Pattern.Type, Pattern> patterns;

	protected Function<String,String> mapper = s -> s
			.replaceAll("[\n,\\s]","")
			.replaceAll("<!--(.*)-->","");
	
	public StringParser(Pattern... patterns) {
		// TODO : select strategy based on the types ....
		this.patterns = Arrays.stream(patterns)
				.collect(Collectors.toMap(pattern -> pattern.type, pattern -> pattern, (p1, p2) -> {
					throw new IllegalArgumentException(String.format(" Duplicate type Error encountered for %s and %s",
							p1.toString(), p2.toString()));
				}));
	}
	
	public StringParser withMapper(Function<String,String> f){
		this.mapper = f;
		return this;
	}

	public static class Pattern {

		private final String pattern;
		private final Type type;

		private int index;

		private Pattern(String pattern, Type type) {
			this.pattern = pattern;
			this.type = type;
			reset();
		}
		
		protected String get() {
			return pattern;
		}

		protected Optional<Boolean> test(char ch) {

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

		/**
		 * make the object re-usable
		 */
		private void reset() {
			this.index = 0;
		}

		/**
		 * check if the pattern is complete / found
		 */
		protected boolean isComplete() {
			boolean complete = index == pattern.length();
			if(complete) {
				reset();
			}
			return complete;
		}

		/**
		 * check if pattern is currently in
		 */
		protected boolean isActive() {
			return !isComplete() && index > 0;
		}
		
		public static Pattern createStart(String boundary) {
			return new Pattern(boundary,Type.START);
		}
		
		public static Pattern createEnd(String boundary) {
			return new Pattern(boundary,Type.END);
		}

		enum Type {
			START, END;
		}

	}

}
