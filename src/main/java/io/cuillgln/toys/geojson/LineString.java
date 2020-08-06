
package io.cuillgln.toys.geojson;

public class LineString extends Geometry {

	private double[][] coordinates;

	public LineString() {
		super("LineString");
	}

	public double[][] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(double[][] coordinates) {
		this.coordinates = coordinates;
	}

}
