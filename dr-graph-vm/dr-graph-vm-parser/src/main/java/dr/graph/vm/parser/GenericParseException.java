package dr.graph.vm.parser;

public class GenericParseException extends Exception{

	private static final long serialVersionUID = 1L;

	public GenericParseException(String msg){
		super(msg);
	}
	
	public GenericParseException(String msg , Throwable e){
		super(msg,e);
	}
	
}
