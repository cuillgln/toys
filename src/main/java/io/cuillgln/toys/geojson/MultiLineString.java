
package io.cuillgln.toys.geojson;

public class MultiLineString extends Geometry {

	private double[][][] coordinates;

	public double[][][] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(double[][][] coordinates) {
		this.coordinates = coordinates;
	}

	public MultiLineString() {
		super("MultiLineString");
	}

}
