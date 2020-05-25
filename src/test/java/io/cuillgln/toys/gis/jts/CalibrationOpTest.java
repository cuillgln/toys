
package io.cuillgln.toys.gis.jts;

import static org.junit.Assert.assertTrue;
import io.cuillgln.toys.gis.jts.CalibrateOp;

import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXYM;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

public class CalibrationOpTest {

	@Test
	public void test() {
		Coordinate[] line = new Coordinate[] {
						new Coordinate(0, 0),
						new Coordinate(1, 0),
						new Coordinate(2, 0),
						new Coordinate(3, 0),
						new Coordinate(4, 0),
						new Coordinate(5, 0),
						new Coordinate(10, 0)

		};

		CoordinateXYM[] calibrationPoints = new CoordinateXYM[] {
						new CoordinateXYM(1, 0, 50),
						new CoordinateXYM(2.01, 0, 100),
						new CoordinateXYM(5, 0, 200),
						new CoordinateXYM(11, 0, 400)

		};
		Geometry calibratedLine = CalibrateOp.calibrate(new GeometryFactory(), line, calibrationPoints);
		Geometry expected = new GeometryFactory().createLineString(new Coordinate[] {
						new CoordinateXYM(1, 0, 50),
						new CoordinateXYM(2, 0, 100),
						new CoordinateXYM(3, 0, Double.NaN),
						new CoordinateXYM(4, 0, Double.NaN),
						new CoordinateXYM(5, 0, 200),
						new CoordinateXYM(10, 0, 400)

		});
		assertTrue(calibratedLine.equalsTopo(expected));
	}
}
