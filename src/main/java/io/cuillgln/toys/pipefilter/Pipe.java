
package io.cuillgln.toys.pipefilter;

import java.util.Iterator;
import java.util.List;

public class Pipe {

	private List<Filter> filters;
	private Iterator<Filter> iterator;

	public void doFilter(Object target) {
		if (iterator == null) {
			iterator = filters.iterator();
		}
		if (iterator.hasNext()) {
			Filter nextFilter = iterator.next();
			nextFilter.doFilter(target, this);
		}
	}

}
