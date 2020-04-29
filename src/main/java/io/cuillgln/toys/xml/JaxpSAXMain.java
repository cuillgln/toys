
package io.cuillgln.toys.xml;

import java.io.FileInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class JaxpSAXMain {

	public static void main(String[] args) throws Exception {
		// 默认实现 com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(new FileInputStream("NciicServices.xml"), new MySAXHandler());
		
	}
}
