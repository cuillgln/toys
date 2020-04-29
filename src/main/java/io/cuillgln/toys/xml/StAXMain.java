
package io.cuillgln.toys.xml;

import java.io.FileInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

public class StAXMain {

	public static void main(String[] args) throws Exception {
		// 默认实现com.sun.xml.internal.stream.XMLInputFactoryImpl
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream("NciicServices.xml"));
		int event = reader.getEventType();
		while(true) {
			System.out.println(event);
			
			if (!reader.hasNext())
				break;
		 
			event = reader.next();
		}
		
	}
}
