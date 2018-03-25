package dr.graph.vm.parser.string;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import dr.common.struct.stack.LinkedListStack;
import dr.common.struct.stack.Stack;
import dr.graph.vm.parser.AbstractParser;
import dr.graph.vm.parser.GenericParseException;
import dr.graph.vm.parser.Pattern;

public class FastStringParser extends AbstractParser {
	
	private Stack<StringBuilder> stack = new LinkedListStack<>();
	
	protected Set<String> matches = new HashSet<>();

	protected final StringPattern startPattern;
	protected final StringPattern endPattern;
	protected final Pattern<Character> blocker;
	
	public FastStringParser(String start , String end) {
		super(StringPattern.createStart(start),StringPattern.createEnd(end));
		startPattern = patterns.get(StringPattern.Type.START);
		endPattern = patterns.get(StringPattern.Type.END);
		blocker = StringPattern.empty();
	}
	
	public FastStringParser(String start , String end , Pattern<Character> blocker) {
		super(StringPattern.createStart(start),StringPattern.createEnd(end));
		startPattern = patterns.get(StringPattern.Type.START);
		endPattern = patterns.get(StringPattern.Type.END);
		this.blocker = blocker;
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
			
			final AtomicReference<Optional<Boolean>> block = new AtomicReference<Optional<Boolean>>(blocker.test(ch));
			
			log("checking ... "+ch);
			
			startPattern.test(ch)
				.ifPresent( b -> {
				if(b) {
					log("matched starting pattern ... "+startPattern.get());
					stack.add(new StringBuilder());
				}
				// anyway we are in the game to find the opening pattern , so disable the blocking
				block.set(Optional.empty());
			});
			
			if(block.get().isPresent()){
				break;
			}
			
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
