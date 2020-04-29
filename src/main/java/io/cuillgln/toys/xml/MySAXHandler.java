package io.cuillgln.toys.xml;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class MySAXHandler extends DefaultHandler {

	@Override
	public void startDocument() throws SAXException {
		System.out.println("Start document");
	}
}
