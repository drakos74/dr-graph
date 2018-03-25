package dr.graph.vm.parser.util.xml;

import dr.common.struct.tree.Node;
import dr.common.struct.tree.Tree;

import java.util.Set;

import dr.common.struct.tree.HashMapTree;

public class XmlEntity {

	private final String xml;
	
	// TODO : populate the tree as we get the objects for more efficient retrieval
	private final Tree xmlTree = new HashMapTree(null,Node.Index.HEAD() );
	
	public XmlEntity(String xml) {
		this.xml = xml;
	}
	
	public String getXml() {
		return xml;
	}
	
	public String getOnly(String tag) {
		return XmlTag.get(tag).getOnly(xml);
	}
	
	public String getFirst(String tag) {
		return XmlTag.get(tag).getOne(xml);
	}
	
	public Set<String> getAll(String tag) {
		return XmlTag.get(tag).getMany(xml);
	}
	
}
