
package io.cuillgln.toys.xml;

import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;

/**
 * apache Xerces
 * @author cuillgln
 *
 */
public class DOMMain {

	public static void main(String[] args) throws Exception {
		// 默认实现com.sun.org.apache.xerces.internal.dom.DOMXSImplementationSourceImpl
		DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
		DOMImplementation impl = registry.getDOMImplementation("XML 3.0");
		Document doc = impl.createDocument(null, "person", null);
		Element first = doc.createElement("firstname");
		Element last = doc.createElement("lastname");
		// doc.getDocumentElement()得到根元素
		doc.getDocumentElement().appendChild(first);
		doc.getDocumentElement().appendChild(last);
		first.appendChild(doc.createTextNode("Genglin"));
		last.appendChild(doc.createTextNode("Lee"));

		System.out.println(doc.getDocumentElement().getTextContent());
		System.out.println(doc.getDocumentElement().getChildNodes().getLength());
	}
}
