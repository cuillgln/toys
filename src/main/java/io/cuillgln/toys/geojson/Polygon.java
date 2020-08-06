
package io.cuillgln.toys.geojson;

public class Polygon extends Geometry {

	private double[][][] coordinates;

	public Polygon() {
		super("Polygon");
	}

	public double[][][] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(double[][][] coordinates) {
		this.coordinates = coordinates;
	}

}
