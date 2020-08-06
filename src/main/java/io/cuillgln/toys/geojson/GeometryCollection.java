
package io.cuillgln.toys.geojson;

import java.util.List;

public class GeometryCollection extends Geometry {

	private List<Geometry> geometries;

	public GeometryCollection() {
		super("GeometryCollection");
	}

	public List<Geometry> getGeometries() {
		return geometries;
	}

	public void setGeometries(List<Geometry> geometries) {
		this.geometries = geometries;
	}

}
