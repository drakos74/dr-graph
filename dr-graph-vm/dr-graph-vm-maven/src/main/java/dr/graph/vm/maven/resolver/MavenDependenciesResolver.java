package dr.graph.vm.maven.resolver;

import java.util.List;
import java.util.stream.Collectors;

import dr.graph.vm.maven.MavenDependency;
import dr.graph.vm.maven.MavenDependencyKey;
import dr.graph.vm.parser.util.xml.XmlTag;
import dr.graph.vm.source.GenericResolverException;
import dr.graph.vm.source.Resolver;

public class MavenDependenciesResolver implements Resolver<String, List<MavenDependencyKey>> {

	@Override
	public List<MavenDependencyKey> resolve(String tag) throws GenericResolverException {

		String dependencies = XmlTag.dependencies.getOne(tag);

		// parse this with the multi parser

		return XmlTag.dependency.getMany(dependencies).stream()
				// we need to re-apply boundaries ... :( <= : TODO
				.map(xml -> MavenDependencyKey.fromXml(XmlTag.dependency.wrap(xml)))
//				.map(key -> new MavenDependency(key))
				.collect(Collectors.toList());

	}

}
