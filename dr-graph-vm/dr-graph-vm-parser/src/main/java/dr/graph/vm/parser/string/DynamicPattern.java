package dr.graph.vm.parser.string;

import java.util.Optional;
import java.util.Set;

import dr.graph.vm.parser.AbstractParser;
import dr.graph.vm.parser.GenericParseException;
import dr.graph.vm.parser.Pattern;

/**
 * TODO : make more abstract ...
 * starts when first condition is fullfilled, and keeps being active until
 * second condition is met
 */
public class DynamicPattern implements Pattern<Character> {

	private Character openTagOpen = '<';
	private Character openTagClose = '>';
	
	private String closingTagOpen = "</";
	private String closingTagClose = ">";

	StringBuilder name = null;
	StringPattern closingPattern = null;
	
	public DynamicPattern() {
		
	}
	
	@Override
	public Optional<Boolean> test(Character ch) {
		// know where we are ... 
		
		if(name == null) {
			
			if(ch.equals(openTagOpen)) {
				
				name = new StringBuilder();
				return Optional.of(false);
				
			}
			
		} else {
			
			if(closingPattern == null) {
				
				if(ch.equals(openTagClose)) {
					
					closingPattern = new StringPattern(closingTagOpen + name.toString() + closingTagClose, Type.END);
					
				} else {
					
					name.append(ch);
					
				}
				
			} else {
				
				Optional<Boolean> status = closingPattern.test(ch);
				
				if(status.isPresent()) {
					if(status.get()) {
						reset();
					}
					return status;
				} else {
					return Optional.of(false);
				}
				
			}
			
			return Optional.of(false);

		}
		
		return Optional.empty();
	}

	@Override
	public void reset() {
		name = null;
		closingPattern = null;		
	}

	@Override
	public boolean isComplete() {
		return closingPattern.isComplete();
	}

	@Override
	public boolean isActive() {
		return name != null && !isComplete();
	}

	


}
