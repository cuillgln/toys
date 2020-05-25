
package io.cuillgln.toys.gis.jts;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.linearref.LengthIndexedLine;

public class LengthIndexedLineLearningTest {

	private GeometryFactory factory = new GeometryFactory();
	private LineString linearGeom = factory.createLineString(new Coordinate[] {
					new Coordinate(0, 0),
					new Coordinate(1, 1),
					new Coordinate(2, 2),
					new Coordinate(3, 3),
					new Coordinate(4, 4),
					new Coordinate(5, 5),
					new Coordinate(10, 10)
	});

	@Test
	public void testStartAndEndIndex() {
		LengthIndexedLine line = new LengthIndexedLine(linearGeom);
		assertEquals(0.0, line.getStartIndex(), 0);
		assertEquals(Math.sqrt(2) * 10, line.getEndIndex(), 0);
	}
	
	@Test
	public void testProjection() {
		LengthIndexedLine line = new LengthIndexedLine(linearGeom);
		double index = line.project(new Coordinate(6, 0));
		assertEquals(Math.sqrt(2) * 3.0, index, 0);
	}
	
	@Test
	public void testOutRangeProjection() {
		LengthIndexedLine line = new LengthIndexedLine(linearGeom);
		double index = line.project(new Coordinate(100, 0));
		assertEquals(Math.sqrt(2) * 10.0, index, 0);
		
		index = line.project(new Coordinate(-1, -1));
		assertEquals(0, index, 0);
	}
}
