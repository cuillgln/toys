package io.cuillgln.toys.xml;

import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;


public class JaxpDOMMain {

	public static void main(String[] args) throws Exception {
		// 默认实现 com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new FileInputStream("NciicServices.xml"));
		System.out.println(doc.getDocumentElement());
	}
}
