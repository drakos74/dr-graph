package dr.graph.vm.parser.util.xml;

import java.util.Set;

import dr.graph.vm.parser.AbstractParser;

public interface XmlTagIfc {

	String wrap(String value);
	
	boolean matches(String value);
	
	public String getOne(String xml);
	
	public Set<String> getMany(String xml);
	
	public AbstractParser getParser();
	
	public AbstractParser getMultiParser();

	AbstractParser getUniqueParser();

	String getOnly(String xml);
	
}
