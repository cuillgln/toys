
package io.cuillgln.toys.gis.jts;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.linearref.LinearGeometryBuilder;
import org.springframework.util.StringUtils;

public class CoordUtil {

	public static final double RADIUS_EARTH = 6378137.0;

	/**
	 * (lon,lat)格式的坐标串
	 * 
	 * @param coords
	 * @return
	 */
	public static Coordinate[] constructLine(GeometryFactory geomFact, String[] coords) {
		LinearGeometryBuilder builder = new LinearGeometryBuilder(geomFact);
		for (String coord : coords) {
			if (StringUtils.hasText(coord) && !"0,0".equals(coord)) {
				String[] point = coord.split(",");
				double x = Double.parseDouble(point[0]);
				double y = Double.parseDouble(point[1]);
				builder.add(new Coordinate(x, y));
			}
		}
		builder.endLine();
		return builder.getGeometry().getCoordinates();
	}

	/**
	 * (lon,lat)格式的坐标
	 * 
	 * @param srcCoords
	 * @return
	 */
	public static Coordinate[] lonlatToWebMercator(Coordinate[] srcCoords) {
		Coordinate[] target = new Coordinate[srcCoords.length];
		int index = 0;
		for (Coordinate src : srcCoords) {
			target[index++] = lonlatToWebMercator(src.x, src.y);
		}
		return target;
	}

	/*
	 * WGS1984坐标系(EPSG:4326)转WEB Mercator坐标系(EPSG:3857)
	 */
	public static Coordinate lonlatToWebMercator(double lon, double lat) {
		double x = lon * Math.PI / 180 * RADIUS_EARTH;
		double y = Math.log(Math.tan((90 + lat) * Math.PI / 360)) * RADIUS_EARTH;
		return new Coordinate(x, y);
	}
}
