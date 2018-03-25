package dr.graph.vm.maven;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import dr.common.Patchable;
import dr.common.Resolvable;
import dr.common.logger.LazyLogger;
import dr.common.struct.tree.Node;
import dr.graph.vm.parser.util.xml.XmlTag;

public class MavenDependencyKey implements Node.Key, Resolvable, Patchable<MavenDependencyKey> , LazyLogger {

	// [groupId]:[artifactId]:[type]:[version]:[scope]
	private final String key;

	private final static String DELIMITER = ":";

	private static final Set<XmlTag> tags = new HashSet<>() {
		{
			add(XmlTag.artifactId);
			add(XmlTag.groupId);
			add(XmlTag.version);
			add(XmlTag.scope);
			add(XmlTag.type);
		}
	};

	private final String groupId;
	private final String artifactId;
	private final Type type;
	private final Scope scope;
	private final String version;

	private MavenDependencyKey(Builder builder) {
		// TODO : add safety checks
		groupId = builder.groupId;
		artifactId = builder.artifactId;
		type = builder.type == null ? Type.any : builder.type;
		version = builder.version;
		scope = builder.scope == null ? Scope.any : builder.scope;
		key = builder.toString();
	}

	private MavenDependencyKey(String key) {
		this(builder(key));
	}

	public String getKey() {
		return key;
	}

	public static Set<XmlTag> getTags() {
		return tags;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public Type getType() {
		return type;
	}

	public Scope getScope() {
		return scope;
	}

	public String getVersion() {
		return version;
	}
	
	@Override
	public String toString() {
		return key;
	}

	public static MavenDependencyKey fromString(String str) {
		return new MavenDependencyKey(str);
	}

	public static MavenDependencyKey fromPlainXml(String xmlSnippet) {
		return fromXml(XmlTag.dependency.wrap(xmlSnippet));
	}

	public static MavenDependencyKey fromXml(String xmlSnippet) {

		// TODO : make this check more sophisticated
		if (XmlTag.dependency.matches(xmlSnippet)) {

			final String dependencyString = XmlTag.dependency.getOne(xmlSnippet);

			return new MavenDependencyKey(String.join(":",
					IntStream.range(0, XmlTag.values().length).filter(i -> tags.contains(XmlTag.values()[i]))
							.mapToObj(i -> XmlTag.values()[i].getOne(dependencyString)).toArray(String[]::new)));

		}
		throw new IllegalArgumentException(
				"String '" + xmlSnippet + "' does not conform to maven dependency tag structure");
	}

	public static Builder builder() {
		return new Builder();
	}

	public static Builder builder(String address) {
		String[] index = address.split(DELIMITER);
		return new Builder().withGroupId(index[0]).withArtifactId(index[1])
				.withType(index.length > 2 && !index[2].isEmpty() ? Type.valueOf(index[2]) : Type.any)
				.withVersion(index.length > 3 ? index[3] : null)
				.withScope(index.length > 4 && !index[4].isEmpty() ? Scope.valueOf(index[4]) : Scope.any);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MavenDependencyKey other = (MavenDependencyKey) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	public enum Scope {
		compile,test,any,system;
	}

	public enum Type {
		pom, jar, any;
	}

	public static class Builder {

		private String groupId;
		private String artifactId;
		private Type type = Type.any;
		private Scope scope = Scope.any;
		private String version;

		private Builder() {
		}

		public Builder withGroupId(String groupId) {
			this.groupId = groupId;
			return this;
		}

		public Builder withArtifactId(String artifactId) {
			this.artifactId = artifactId;
			return this;
		}

		public Builder withType(Type type) {
			this.type = type;
			return this;
		}

		public Builder withScope(Scope scope) {
			this.scope = scope;
			return this;
		}

		public Builder withVersion(String version) {
			this.version = version;
			return this;
		};

		public MavenDependencyKey build() {
			return new MavenDependencyKey(this);
		}

		@Override
		public String toString() {
			return new StringBuilder().append(groupId).append(DELIMITER).append(artifactId).append(DELIMITER)
					.append(type).append(DELIMITER).append(version).append(DELIMITER).append(scope).toString();
		}

	}

	@Override
	public MavenDependencyKey patch(MavenDependencyKey patch) {
		log("trying to patch '"+this+"' with ... "+patch);
		if (patch.groupId.equals(this.groupId) && patch.artifactId.equals(this.artifactId)) {
			if (isInvalid(version) && !isInvalid(patch.version)) {
				log("patching ... ");
				return MavenDependencyKey.builder()
						.withGroupId(this.groupId)
						.withArtifactId(this.artifactId)
						.withVersion(patch.version)
						.withScope(this.scope == Scope.any && patch.scope != Scope.any ? patch.scope : this.scope )
						.withType(this.type == Type.any && patch.type != Type.any ? patch.type : this.type)
						.build();
			}
		}
		log("could not patch ... ["+
		patch.groupId.equals(this.groupId)+","+
				patch.artifactId.equals(this.artifactId)+","+
				isInvalid(version)+","+
				!isInvalid(patch.version)
				+"]");
		return null;
	}

	@Override
	public boolean resolve() {
		return resolve(null);
	}

	protected boolean resolve(MavenDependencyKey parentKey) {
		if( groupId != null && !groupId.isEmpty() && artifactId != null && !artifactId.isEmpty()
				&& !isInvalid(version) ) {
			return true;
		}else {
			return parentKey == null ? false : patch(parentKey) != null;
		}
	}
	
	private boolean isInvalid(String version) {
		return version == null || version.isEmpty() || version.startsWith("${");
	}

}
