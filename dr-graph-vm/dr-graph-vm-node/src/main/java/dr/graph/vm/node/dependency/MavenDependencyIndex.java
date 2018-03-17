package dr.graph.vm.node.dependency;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dr.common.Patchable;
import dr.common.Resolvable;
import dr.graph.vm.node.Index;
import dr.graph.vm.parser.util.xml.XmlTag;

public class MavenDependencyIndex implements Index , Resolvable , Patchable {

	private static final Logger logger = LoggerFactory.getLogger(MavenDependencyIndex.class);

	// [groupId]:[artifactId]:[type]:[version]

	private static final Set<XmlTag> tags = new HashSet<>() {{
		add(XmlTag.artifactId);
		add(XmlTag.groupId);
		add(XmlTag.version);
		add(XmlTag.scope);
		add(XmlTag.type);
	}};
	
	private final String groupId;
	private final String artifactId;
	private Type type;
	private Scope scope;
	private String version;

	private MavenDependencyIndex(String dependency) {
		// TODO : add safety checks
		String[] index = dependency.split(":");
		
		log("parsing ... "+dependency);
		
		groupId = index[0];
		artifactId = index[1];
		type = index.length > 2 && !index[2].isEmpty() ? Type.valueOf(index[2]) : Type.any;
		version = index.length > 3 ? index[3] : null;
		scope = index.length > 4 && !index[4].isEmpty() ? Scope.valueOf(index[4]) : Scope.any;
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

	public String getVersion() {
		return version;
	}
	
	public Scope getScope() {
		return scope;
	}

	public enum Scope {
		compile, test, any;
	}

	public enum Type {
		pom, jar, any;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		MavenDependencyIndex other = (MavenDependencyIndex) obj;
		if (artifactId == null) {
			if (other.artifactId != null)
				return false;
		} else if (!artifactId.equals(other.artifactId))
			return false;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (type != other.type)
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		if (scope == null) {
			if (other.scope != null)
				return false;
		} else if (!scope.equals(other.scope))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return groupId + ":" + artifactId + ":" + type + ":" + version + ":" + scope;
	}
	
	public static MavenDependencyIndex fromString(String str) {
		return new MavenDependencyIndex(str);
	}

	public static MavenDependencyIndex fromPlainXml(String xmlSnippet) {
		return fromXml(XmlTag.dependency.wrap(xmlSnippet));
	}
	
	public static MavenDependencyIndex fromXml(String xmlSnippet) {
		
		// TODO : make this check more sophisticated
		if (XmlTag.dependency.matches(xmlSnippet)) {

			final String dependencyString = XmlTag.dependency.getOne(xmlSnippet);
			
			return new MavenDependencyIndex(String.join(":", IntStream.range(0, XmlTag.values().length)
					.filter(i -> tags.contains(XmlTag.values()[i]))
					.mapToObj(i -> XmlTag.values()[i].getOne(dependencyString)).toArray(String[]::new)));

		}
		throw new IllegalArgumentException(
				"String '" + xmlSnippet + "' does not conform to maven dependency tag structure");
	}

	@Override
	public boolean resolves() {
		return groupId != null && !groupId.isEmpty() && 
				artifactId != null && !artifactId.isEmpty() && 
				!isInvalid(version);
	}
	
	private boolean isInvalid(String version) {
		return version == null || version.isEmpty() || version.startsWith("${");
	}

	@Override
	public <P extends Patchable> boolean patch(P object) {
		if (getClass() != object.getClass())
			return false;
		MavenDependencyIndex patch = (MavenDependencyIndex) object;
		
		if(patch.groupId.equals(this.groupId) && 
				patch.artifactId.equals(this.artifactId)) {
			
			if(isInvalid(version) && !isInvalid(patch.version)) {
				this.version = patch.version;
				return true;
			}
		}
		return false;
	}

}
