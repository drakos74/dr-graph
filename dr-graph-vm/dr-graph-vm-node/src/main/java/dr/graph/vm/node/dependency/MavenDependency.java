package dr.graph.vm.node.dependency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import dr.common.Patchable;
import dr.common.Resolvable;
import dr.graph.vm.node.Index;
import dr.graph.vm.node.Node;
import dr.graph.vm.node.dependency.source.MavenArtifactResolver;
import dr.graph.vm.node.dependency.source.MavenDependenciesResolver;
import dr.graph.vm.parser.GenericParseException;
import dr.graph.vm.parser.string.SingleMatchParser;
import dr.graph.vm.parser.util.xml.XmlTag;
import dr.graph.vm.source.GenericResolverException;
import dr.graph.vm.source.Resolver;

public class MavenDependency implements Node<MavenDependency>, Resolvable, Patchable {

	private MavenDependencyIndex index;
	private MavenDependency parent;
	private List<MavenDependency> children = new ArrayList<>();
	private AtomicReference<String> pom = new AtomicReference<String>("");

	// TODO : create resolver chain
	private Resolver<MavenDependencyIndex, String> pomResolver = new MavenArtifactResolver();
	private Resolver<String, List<MavenDependency>> dependenciesResolver = new MavenDependenciesResolver();

	public MavenDependency(MavenDependencyIndex index) {
		this.index = index;
	}

	public Index getIndex() {
		return index;
	}

	@Override
	public List<MavenDependency> getChildren() {
		// TODO : add synchronization handle
		if (children == null || children.size() == 0) {
			// resolve the children from artifactory
			try {
				pom.set(pomResolver.resolve(index));
			} catch (GenericResolverException e) {
				throw new IllegalArgumentException("Could not resolve index\n[" + index + "]", e);
			}

			try {
				// TODO : make consistent ... e.g. dependencies in plugins ... e.g.  parent-tags aware parsers
				children = dependenciesResolver.resolve(pom.get())
						.stream().map(child -> child.withParent(this)).filter(dependency -> {
							if (!dependency.resolves()) {
								return dependency.patch(this);
							}
							return true;
						}).collect(Collectors.toList());
			} catch (GenericResolverException e) {
				throw new IllegalArgumentException("Could not resolve pom\n[" + pom.get() + "]", e);
			}

		}
		return children;
	}

	@Override
	public MavenDependency getParent() {
		return parent;
	}

	@Override
	public <P extends Patchable> boolean patch(P object) {

		if (object.getClass() != this.getClass())
			return false;

		MavenDependency parent = (MavenDependency) object;

		String parentIndex = XmlTag.parent.getOne(parent.pom.get());
		
		if(parentIndex == null || parentIndex.isEmpty()) {
			return false;
		}

		// TODO : find a way to avoid doing the same work over and over ... again
		try {
			String parentXml = pomResolver.resolve(MavenDependencyIndex.fromPlainXml(parentIndex));
			return 
					// find in dependencyManagement of parent
					XmlTag.dependency.getMany(parentXml).stream().map(MavenDependencyIndex::fromPlainXml)
					.filter(index -> this.index.patch(index)).findFirst().isPresent() || 
					// find in properties of parent
					this.index.patch(MavenDependencyIndex.fromString(
							this.index.getGroupId()+":"+
									this.index.getArtifactId()+"::"+
							XmlTag.get(new SingleMatchParser("${","}").parse(this.index.getVersion()).iterator().next()).getOne(parentXml))) ||
					// find in properties of self TODO : test!!!
					this.index.patch(MavenDependencyIndex.fromString(
							this.index.getGroupId()+":"+
									this.index.getArtifactId()+"::"+
							XmlTag.get(new SingleMatchParser("${","}").parse(this.index.getVersion()).iterator().next()).getOne(pom.get())));
		} catch (GenericResolverException | GenericParseException e) {
			return false;
		}

	}

	@Override
	public boolean resolves() {
		return index.resolves();
	}

	public MavenDependency withParent(MavenDependency node) {
		this.parent = node;
		return this;
	}

}
