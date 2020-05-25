
package io.cuillgln.toys.gis.jts;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.linearref.LinearLocation;
import org.locationtech.jts.linearref.LocationIndexedLine;

/**
 * endIndex 比较trick
 * @author cuillgln
 *
 */
public class LocationIndexedLineLearningTest {

	private GeometryFactory factory = new GeometryFactory();
	private LineString linearGeom = factory.createLineString(new Coordinate[] {
					new Coordinate(0, 0),
					new Coordinate(1, 0),
					new Coordinate(2, 0),
					new Coordinate(3, 0),
					new Coordinate(4, 0),
					new Coordinate(5, 0),
					new Coordinate(10, 0)
	});

	@Test
	public void testStartAndEndIndex() {
		LocationIndexedLine line = new LocationIndexedLine(linearGeom);
		assertLinearLocationEquals(0, 0, 0, line.getStartIndex());
		assertLinearLocationEquals(0, 6, 1.0, line.getEndIndex());
		Coordinate end = line.extractPoint(line.getEndIndex());
		assertEquals(10, end.x, 0);
		assertEquals(0, end.y, 0);
	}
	
	@Test
	public void testProjection() {
		LocationIndexedLine line = new LocationIndexedLine(linearGeom);
		LinearLocation loc = line.project(new Coordinate(1.5, 0));
		assertLinearLocationEquals(0, 1, 0.5, loc);
	}

	@Test
	public void testProjectToEndIndex() {
		LocationIndexedLine line = new LocationIndexedLine(linearGeom);
		LinearLocation loc = line.project(new Coordinate(10, 0));
		assertLinearLocationEquals(0, 6, 0, loc);
		Coordinate end = line.extractPoint(loc);
		assertEquals(10, end.x, 0);
		assertEquals(0, end.y, 0);
	}

	@Test
	public void testOutRangeProjection() {
		LocationIndexedLine line = new LocationIndexedLine(linearGeom);
		LinearLocation loc = line.project(new Coordinate(100, 0));
		assertLinearLocationEquals(0, 6, 0, loc);
		Coordinate projection = line.extractPoint(loc);
		assertEquals(10, projection.x, 0);
		assertEquals(0, projection.y, 0);

		loc = line.project(new Coordinate(-10, 1));
		assertLinearLocationEquals(0, 0, 0, loc);
		projection = line.extractPoint(loc);
		assertEquals(0, projection.x, 0);
		assertEquals(0, projection.y, 0);
	}

	public void assertLinearLocationEquals(int componentIndex, int segmentIndex, double segmentFraction,
					LinearLocation actual) {
		assertEquals(componentIndex, actual.getComponentIndex());
		assertEquals(segmentIndex, actual.getSegmentIndex());
		assertEquals(segmentFraction, actual.getSegmentFraction(), 1e-9);
	}
}
