package dr.graph.vm.node.dependency.source;

import java.util.List;
import java.util.stream.Collectors;

import dr.graph.vm.node.dependency.MavenDependency;
import dr.graph.vm.node.dependency.MavenDependencyIndex;
import dr.graph.vm.parser.util.xml.XmlTag;
import dr.graph.vm.source.GenericResolverException;
import dr.graph.vm.source.Resolver;

public class MavenDependenciesResolver implements Resolver<String, List<MavenDependency>> {

	@Override
	public List<MavenDependency> resolve(String id) throws GenericResolverException {

		String dependencies = XmlTag.dependencies.getOne(id);

		// parse this with the multi parser

		return XmlTag.dependency.getMany(dependencies).stream()
				// we need to re-apply boundaries ... :( <= : TODO
				.map(xml -> MavenDependencyIndex.fromXml(XmlTag.dependency.wrap(xml)))
				.map(index -> new MavenDependency(index)).collect(Collectors.toList());

	}

}
