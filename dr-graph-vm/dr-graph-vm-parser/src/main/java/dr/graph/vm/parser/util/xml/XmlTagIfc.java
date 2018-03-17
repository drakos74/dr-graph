package dr.graph.vm.parser.util.xml;

import java.util.Set;

import dr.graph.vm.parser.string.StringParser;

public interface XmlTagIfc {

	String wrap(String value);
	
	boolean matches(String value);
	
	public String getOne(String xml);
	
	public Set<String> getMany(String xml);
	
	public StringParser getParser();
	
	public StringParser getMultiParser();
	
}
