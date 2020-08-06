
package io.cuillgln.toys.geojson;

public class MultiPoint extends Geometry {

	private double[][] coordinates;

	public double[][] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(double[][] coordinates) {
		this.coordinates = coordinates;
	}

	public MultiPoint() {
		super("MultiPoint");
	}

}
