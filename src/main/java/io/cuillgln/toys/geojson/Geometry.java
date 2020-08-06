
package io.cuillgln.toys.geojson;

public abstract class Geometry {

	private final String type;
	protected double[] bbox;

	public Geometry(String type) {
		this.type = type;
	}

	public double[] getBbox() {
		return bbox;
	}

	public void setBbox(double[] bbox) {
		this.bbox = bbox;
	}

	public String getType() {
		return type;
	}

}
