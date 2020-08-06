
package io.cuillgln.toys.geojson;

public class MultiPolygon extends Geometry {

	private double[][][][] coordinates;

	public MultiPolygon() {
		super("MultiPolygon");
	}

	public double[][][][] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(double[][][][] coordinates) {
		this.coordinates = coordinates;
	}

}
