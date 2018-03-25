package dr.graph.vm.parser.util.xml;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dr.graph.vm.parser.AbstractParser;
import dr.graph.vm.parser.string.DynamicPattern;
import dr.graph.vm.parser.string.FastStringParser;
import dr.graph.vm.parser.string.SingleMatchParser;

public enum XmlTag implements XmlTagIfc {
	parent , dependencyManagement, dependencies , dependency, groupId, artifactId, type, version, scope;
	
	private static final Logger logger = LoggerFactory.getLogger(XmlTag.class);

	private final Helper helper;

	private XmlTag() {
		helper = new Helper(this.name());
	}

	@Override
	public String getOne(String xml) {
		return helper.getOne(xml);
	}
	
	@Override
	public Set<String> getMany(String xml) {
		return helper.getMany(xml);
	}
	
	@Override
	public String getOnly(String xml) {
		return helper.getOnly(xml);
	}
	
	@Override
	public AbstractParser getParser() {
		return helper.singleMatchParser;
	}
	
	@Override
	public AbstractParser getMultiParser() {
		return helper.multiMatchParser;
	}
	
	@Override
	public AbstractParser getUniqueParser() {
		return helper.getUniqueParser();
	}
	
	@Override
	public String wrap(String value) {
		return helper.wrap(value);
	}
	
	@Override
	public boolean matches(String value) {
		String _value = value.trim();
		return _value.startsWith(helper.open) && _value.endsWith(helper.close);
	}
	
	public static XmlTagIfc get(String name) {
		Optional<XmlTag> knownXmlTag = Arrays.stream(XmlTag.values())
		.filter( tag -> tag.name().equals(name) )
		.findFirst();
		return knownXmlTag.isPresent() ? knownXmlTag.get() : new Helper(name);
	}
	
	public static class Helper implements XmlTagIfc {
		
		private final String name;
		
		public final String open;
		public final String close;
		
		private final SingleMatchParser singleMatchParser;
		private final FastStringParser uniqueMatchParser;
		private final FastStringParser multiMatchParser;

		private Helper(String name) {
			this.name = name;
			this.open = "<" + name + ">";
			this.close = "</" + name + ">";
			singleMatchParser = new SingleMatchParser(this.open, this.close);
			multiMatchParser = new FastStringParser(this.open, this.close);
			uniqueMatchParser = new FastStringParser(this.open, this.close,new DynamicPattern());
		}
		
		@Override
		public String getOnly(String xml) {
			Set<String> matches = Collections.emptySet();
			try {
				matches = getParser().parse(xml);
			} catch (Exception e) {
				logger.warn("Could not parse " + this.name + " from " + xml);
			}
			return matches.size() > 0 ? matches.iterator().next() : "";
		}
		
		@Override
		public String getOne(String xml) {
			Set<String> matches = Collections.emptySet();
			try {
				matches = getParser().parse(xml);
			} catch (Exception e) {
				logger.warn("Could not parse " + this.name + " from " + xml);
			}
			return matches.size() > 0 ? matches.iterator().next() : "";
		}
		
		@Override
		public Set<String> getMany(String xml) {
			Set<String> matches = Collections.emptySet();
			try {
				matches = getMultiParser().parse(xml);
			} catch (Exception e) {
				logger.warn("Could not parse " + this.name + " from " + xml);
			}
			return matches.size() > 0 ? matches : Collections.emptySet();
		}

		@Override
		public AbstractParser getParser() {
			return singleMatchParser;
		}
		
		@Override
		public AbstractParser getUniqueParser() {
			return uniqueMatchParser;
		}
		
		@Override
		public AbstractParser getMultiParser() {
			return multiMatchParser;
		}

		@Override
		public String wrap(String value) {
			return open+value+close;
		}
		
		@Override
		public boolean matches(String value) {
			String _value = value.trim();
			return _value.startsWith(open) && _value.endsWith(close);
		}
	}

}


