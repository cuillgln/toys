
package io.cuillgln.file;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;

public class PropertiesTest {

	public static void main(String[] args) throws InterruptedException, MalformedURLException, URISyntaxException {
		// System.out.println(Paths.get("F:\\奥尔夫\\上册图谱\\jars.rar").toUri().toASCIIString());
		Object o = new Object();
	    // 默认的构造函数，会使用ReferenceQueue.NULL 作为queue
//	    WeakReference<Object> wr = new WeakReference<Object>(o);
//	    System.out.println(wr.get() == null);
//	    o = null;
//	     System.gc();
//	    System.out.println(wr.get() == null);
//	    System.gc();
//	    System.out.println(wr.get() == null);
		
		URL url = new URL("classpath:/com/haiyisoft/Main");
		System.out.println(url.getProtocol());
		System.out.println(url.toURI().toString());
		
		
		/*Map<Object, Object> props = System.getProperties();
		for (Object key : props.keySet()) {
			System.out.format("%s=%s\n", key, props.get(key));
		}

		Thread[] ths = new Thread[] { new Thread(new Full()), new Thread(new Full()), new Thread(new Full()),
				new Thread(new Full()) };

		for (int i = 0; i < 4; i++) {
			ths[i].start();
			Thread.sleep(2000);
		}

		for (int i = 0; i < 4; i++) {
			ths[i].interrupt();
			Thread.sleep(2000);
		} */
	}

	static class Full implements Runnable {

		@Override
		public void run() {
			int i = 0;
			while (true) {
				if (Thread.interrupted()) {
					break;
				}
				i = i + 1 % 100;
			}
		}

	}
}
