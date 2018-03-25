package dr.common.logger;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public interface LazyLogger {
	
	static String l = System.setProperty("java.util.logging.config.file", "logging.properties");
	
	static final Set<String> disabledClasses = new HashSet<String>() {{
		add("dr.graph.vm.parser.string.SingleMatchParser");
		add("dr.graph.vm.parser.string.FastStringParser");
		add("dr.graph.vm.node.dependency.MavenDependencyIndex");
		add("dr.graph.vm.maven.resolver.MavenArtifactResolver");
		add("dr.graph.vm.source.http.UrlResolver");
	}};

	// TODO : attach parser ... 
	final boolean debug = Boolean.parseBoolean(System.getProperty("debug"));
	
	final static Map<Class<?>,Logger> loggers = new ConcurrentHashMap<>();
	
	default void error(String msg , Throwable e) {
		getLogger().error(getMsg(msg),e);
	}
	
	default void log(String msg) {
		if(debug)getLogger().info(getMsg(msg));
	}
	
	default void log(String msg,Throwable e) {
		if(debug)getLogger().info(getMsg(msg),e);
	}
	
	default String getMsg(String msg) {
		return "["+this.getClass()+"] "+msg;
	}
	
	default Logger getLogger() {
		Class<?> this_class = this.getClass();
		if(disabledClasses.contains(this_class.getName())) {
			return noLogger;
		}
		return loggers.compute(this_class, (clazz,logger) -> {
			return logger == null ? LoggerFactory.getLogger(clazz) : logger;
		});
	}
	
	static final Logger noLogger = new Logger() {

		@Override
		public void debug(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void debug(String arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void debug(String arg0, Object... arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void debug(String arg0, Throwable arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void debug(Marker arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void debug(String arg0, Object arg1, Object arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void debug(Marker arg0, String arg1, Object arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void debug(Marker arg0, String arg1, Object... arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void debug(Marker arg0, String arg1, Throwable arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void debug(Marker arg0, String arg1, Object arg2, Object arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void error(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void error(String arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void error(String arg0, Object... arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void error(String arg0, Throwable arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void error(Marker arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void error(String arg0, Object arg1, Object arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void error(Marker arg0, String arg1, Object arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void error(Marker arg0, String arg1, Object... arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void error(Marker arg0, String arg1, Throwable arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void error(Marker arg0, String arg1, Object arg2, Object arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void info(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void info(String arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void info(String arg0, Object... arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void info(String arg0, Throwable arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void info(Marker arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void info(String arg0, Object arg1, Object arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void info(Marker arg0, String arg1, Object arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void info(Marker arg0, String arg1, Object... arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void info(Marker arg0, String arg1, Throwable arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void info(Marker arg0, String arg1, Object arg2, Object arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isDebugEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isDebugEnabled(Marker arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isErrorEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isErrorEnabled(Marker arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isInfoEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isInfoEnabled(Marker arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isTraceEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isTraceEnabled(Marker arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isWarnEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isWarnEnabled(Marker arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void trace(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void trace(String arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void trace(String arg0, Object... arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void trace(String arg0, Throwable arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void trace(Marker arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void trace(String arg0, Object arg1, Object arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void trace(Marker arg0, String arg1, Object arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void trace(Marker arg0, String arg1, Object... arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void trace(Marker arg0, String arg1, Throwable arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void trace(Marker arg0, String arg1, Object arg2, Object arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void warn(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void warn(String arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void warn(String arg0, Object... arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void warn(String arg0, Throwable arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void warn(Marker arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void warn(String arg0, Object arg1, Object arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void warn(Marker arg0, String arg1, Object arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void warn(Marker arg0, String arg1, Object... arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void warn(Marker arg0, String arg1, Throwable arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void warn(Marker arg0, String arg1, Object arg2, Object arg3) {
			// TODO Auto-generated method stub
			
		}};
	
}
