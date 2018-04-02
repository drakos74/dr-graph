package dr.graph.vm.maven;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import dr.common.Resolvable;
import dr.common.logger.LazyLogger;
import dr.common.struct.tree.Tree;
import dr.graph.vm.maven.resolver.MavenArtifactResolver;
import dr.graph.vm.maven.resolver.MavenDependenciesResolver;
import dr.graph.vm.parser.string.SingleMatchParser;
import dr.graph.vm.parser.util.xml.XmlEntity;
import dr.graph.vm.parser.util.xml.XmlTag;
import dr.graph.vm.source.GenericResolverException;
import dr.graph.vm.source.Resolver;

public class MavenDependency extends Tree<MavenDependency> implements Resolvable, LazyLogger {

	// TODO : create resolver chain
	// TODO : inject those 2 in the generic case of the abstract parent class
	private final Resolver<MavenDependencyKey, String> pomResolver;
	private final Resolver<String, List<MavenDependencyKey>> dependenciesResolver;

	private final MavenDependencyKey key;

	private volatile List<MavenDependency> children = null;
	private volatile MavenDependency parent = null;
	private volatile XmlEntity pom = null;

	private MavenDependency(MavenDependencyKey key, Resolver<MavenDependencyKey, String> pomResolver,
			Resolver<String, List<MavenDependencyKey>> dependenciesResolver) {
		this.pomResolver = pomResolver;
		this.dependenciesResolver = dependenciesResolver;
		this.key = key;
		try {
			pom = new XmlEntity(pomResolver.resolve(key));
			String parent_tag = pom.getFirst(XmlTag.parent.name());
			if (parent_tag.isEmpty() == false) {
				// inherit resolver logic to parent / children
				parent = new MavenDependency(MavenDependencyKey.fromPlainXml(parent_tag), pomResolver,
						dependenciesResolver);
				// BEWARE : this would cause a retrospective parent tree walk .. we dont wat
				// that ... We want only to get the parents of parents
				// parent.resolve();
			} else {
				log("reached end of parent chain ... we can return the constructed MavenDependency now ... ");
			}
		} catch (GenericResolverException e) {
			log("We could not resolve pom ... ", e);
		}

	}

	@Override
	public Key key() {
		return this.key;
	}

	@Override
	public MavenDependency parent() {
		// TODO : make proper Singleton
		// TODO : to resolve the parent we are resolving all the children ... do we really want this ?
		if (resolve()) {
			return parent;
		}
		throw new UnresolvedDependencyException();
	}

	public XmlEntity pom() {
		return pom;
	}
	
	@Override
	public List<Key> childrenKeys() {
		if (resolve()) {
			return children.stream().map(MavenDependency::key).collect(Collectors.toList());
		}
		throw new UnresolvedDependencyException();
	}

	@Override
	public List<MavenDependency> children() {
		if (resolve()) {
			return children;
		}
		throw new UnresolvedDependencyException();
	}

	@Override
	public boolean resolve() {
		// first we need to make sure that the key resolves ...
		if (!key.resolve()) {
			return false;
		}
		// check if we have already resolved this ...
		if (pom != null && parent != null && children != null) {
			return true;
		}

		// do the resolve now ...
		try {
			// TODO : make consistent ... e.g. dependencies in plugins ... e.g. parent-tags
			// aware parsers
			children = dependenciesResolver.resolve(pom.getXml()).stream().map(c_key -> {
				// fix the keys at least ...

				if (!c_key.resolve()) {

					log("Could not resolve ... " + c_key + " ... Will try with parent ");

					final String artifactId = c_key.getArtifactId();
					final String groupId = c_key.getGroupId();
					final String version = c_key.getVersion();

					MavenDependencyKey p_key = null;
					MavenDependency parent = this;

					while (p_key == null) {
						if(parent == null || parent.pom == null) {
							break;
						}
						// with property parsing ...
						if (c_key.getVersion() != null && c_key.getVersion().startsWith("${")
								&& c_key.getVersion().endsWith("}")) {
							// is property ...
							try {
								p_key = MavenDependencyKey.builder().withArtifactId(artifactId).withGroupId(groupId)
										.withVersion(XmlTag
												.get(new SingleMatchParser("${", "}").parse(version).iterator().next())
												.getOne(parent.pom.getXml()))
										.build();
								if (p_key == null || p_key.getVersion() == null || p_key.getVersion().isEmpty()) {
									p_key = null;
								}
							} catch (Exception e) {
								// nothing to log here ...
							}
						} else {
							// try with dependencymanagement ...
							p_key = p_key != null ? p_key
									: XmlTag.dependency.getMany(parent.pom.getXml()).stream()
											.map(MavenDependencyKey::fromPlainXml).filter(k -> {
												if (k.resolve()) {
													// TODO : make a method out of this ... 
													return k.getArtifactId().equals(artifactId) && k.getGroupId().equals(groupId);
												}
												return false;
											}).findFirst().orElseGet(() -> null);
						}
						parent = parent.parent;
					}

					if (p_key != null) {
						c_key = c_key.patch(p_key);
					}

				}
				return c_key;
			}).filter(Objects::nonNull).map(k -> {
				return new MavenDependency(k, this.pomResolver, this.dependenciesResolver);
			}).collect(Collectors.toList());
			return true;
		} catch (GenericResolverException e) {
			error("Could not resolve " + this, e);
		}

		return false;
	}

	@Override
	public String toString() {
		return "\n{ \n \t key = " + key + " , \n" + "\t children = " + (children == null ? "null" : children.size())
				+ " , \n" + "\t pom = " + (pom != null) + " , \n" + "\t parent = " + parent + " \n" + "}";
	}

//	public static MavenDependency create(MavenDependencyKey key) {
//		return new Builder().build(key);
//	}

	public static class Builder {

		private Resolver<MavenDependencyKey, String> pomResolver;
		private Resolver<String, List<MavenDependencyKey>> dependenciesResolver;

		public Builder() {

		}

		Builder withPomResolver(Resolver<MavenDependencyKey, String> pomResolver) {
			this.pomResolver = pomResolver;
			return this;
		}

		Builder withDependencyResolver(Resolver<String, List<MavenDependencyKey>> dependenciesResolver) {
			this.dependenciesResolver = dependenciesResolver;
			return this;
		}

		public MavenDependency build(MavenDependencyKey key) {
			return new MavenDependency(key, pomResolver,dependenciesResolver);
		}

	}

}
