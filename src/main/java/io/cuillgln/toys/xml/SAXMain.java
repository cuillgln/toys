
package io.cuillgln.toys.xml;

import java.io.FileInputStream;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * org.apache.xerces提供了JAXP/SAX/DOM(level2/level3)的实现
 * 
 * @author cuillgln
 *
 */
public class SAXMain {

	public static void main(String[] args) throws Exception {
		// 默认实现com.sun.org.apache.xerces.internal.parsers.SAXParser
		XMLReader reader = XMLReaderFactory.createXMLReader();
		MySAXHandler handler = new MySAXHandler();
		reader.setContentHandler(handler);
		reader.setErrorHandler(handler);
		reader.parse(new InputSource(new FileInputStream("NciicServices.xml")));
	}
}
