package dr.graph.vm.parser.string;

import java.util.HashSet;
import java.util.Set;

import dr.common.struct.LinkedListStack;
import dr.common.struct.Stack;
import dr.graph.vm.parser.GenericParseException;

public class FastStringParser extends StringParser {
	
	private Stack<StringBuilder> stack = new LinkedListStack<>();
	
	protected Set<String> matches = new HashSet<>();

	protected Pattern startPattern;
	protected Pattern endPattern;
	
	public FastStringParser(String start , String end) {
		super(Pattern.createStart(start),Pattern.createEnd(end));
		startPattern = patterns.get(Pattern.Type.START);
		endPattern = patterns.get(Pattern.Type.END);
	}
	
	protected boolean done() {
		return false;
	}
	
	private void reset() {
		stack.clear();
		matches.clear();
	}
	
	@Override
	public Set<String> parse(String input) throws GenericParseException {
		
		// reset before each usage ... !!!
		reset();
		
		log("parse ... "+input);

		for(int i = 0 ; i < input.length() ; i++) {
			
			char ch = input.charAt(i);
			
			log("checking ... "+ch);
			
			startPattern.test(ch)
				.ifPresent( b -> {
				if(b) {
					log("matched starting pattern ... "+startPattern.get());
					stack.add(new StringBuilder());
				}
			});
			
			if(stack.size() > 0) {
				log("stack size "+stack.size()+" for "+ch);
				//TODO : make this condition prettier ... 
				if(stack.get().length() == 0) {
					stack.get().append(startPattern.get());
				} else {
					stack.get().append(ch);
				}
				log("current latest match has "+stack.get().length()+" length");
				endPattern.test(ch)
				.ifPresent(b -> {
					if(b) {
						log("matched ending pattern ... "+endPattern.get());
						StringBuilder result = stack.remove();
						log("found ... {"+result.length()+"}");
						matches.add(mapper.apply(result.substring(startPattern.get().length() , result.length() - endPattern.get().length())));
					}
				});
			}
			
			if(done()) {
				return matches;
			}
			
		}
		
		return matches;
	}

}
