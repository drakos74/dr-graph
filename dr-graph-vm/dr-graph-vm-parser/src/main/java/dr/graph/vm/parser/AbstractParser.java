package dr.graph.vm.parser;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import dr.graph.vm.parser.string.StringPattern;

// TODO : abstract even more ... at Pattern level
public abstract class AbstractParser implements Parser<Set<String>, String> {
	
	protected final Map<StringPattern.Type, StringPattern> patterns;

	protected Function<String,String> mapper = s -> s
			.replaceAll("[\n,\\s]","")
			.replaceAll("<!--(.*)-->","");
	
	public AbstractParser(StringPattern... patterns) {
		// TODO : select strategy based on the types ....
		this.patterns = Arrays.stream(patterns)
				.collect(Collectors.toMap(pattern -> pattern.type, pattern -> pattern, (p1, p2) -> {
					throw new IllegalArgumentException(String.format(" Duplicate type Error encountered for %s and %s",
							p1.toString(), p2.toString()));
				}));
	}
	
	public AbstractParser withMapper(Function<String,String> f){
		this.mapper = f;
		return this;
	}

}
