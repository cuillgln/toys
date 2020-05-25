
package io.cuillgln.toys.gis.jts;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXYM;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.linearref.LinearGeometryBuilder;
import org.locationtech.jts.linearref.LinearLocation;
import org.locationtech.jts.linearref.LocationIndexedLine;

public class CalibrateOp {

	public static final double TOLERATE_DISTANCE = 1e-2;

	public static Geometry calibrate(GeometryFactory geomFact, Coordinate[] line, CoordinateXYM[] calibrationPoints) {
		Geometry linearGeom = geomFact.createLineString(line);
		LocationIndexedLine tempLine = new LocationIndexedLine(linearGeom);
		LinearGeometryBuilder calibratedLine = new LinearGeometryBuilder(geomFact);
		LinearLocation preLoc = null;
		for (CoordinateXYM point : calibrationPoints) {
			LinearLocation loc = tempLine.project(point);
			loc.snapToVertex(linearGeom, TOLERATE_DISTANCE);
			if (preLoc != null) {
				addExtractLine(calibratedLine, tempLine.extractLine(preLoc, loc).getCoordinates());
			}
			// 结尾点
			Coordinate calibratedPoint = tempLine.extractPoint(loc);
			calibratedLine.add(new CoordinateXYM(calibratedPoint.x, calibratedPoint.y, point.getM()), false);
			preLoc = loc;
		}
		return calibratedLine.getGeometry();
	}

	static void addExtractLine(LinearGeometryBuilder calibratedLine, Coordinate[] subLine) {
		// 去头去尾
		for (int i = 1; i < subLine.length - 1; i++) {
			calibratedLine.add(new CoordinateXYM(subLine[i].x, subLine[i].y, Coordinate.NULL_ORDINATE), false);
		}
	}
}
