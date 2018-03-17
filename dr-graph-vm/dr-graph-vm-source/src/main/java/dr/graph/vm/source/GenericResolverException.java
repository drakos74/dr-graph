package dr.graph.vm.source;

public class GenericResolverException extends Exception{

	private static final long serialVersionUID = 1L;

	public GenericResolverException(String msg){
		super(msg);
	}
	
	public GenericResolverException(String msg , Throwable e){
		super(msg,e);
	}
	
}
