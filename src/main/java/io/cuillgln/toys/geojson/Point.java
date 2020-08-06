
package io.cuillgln.toys.geojson;

public class Point extends Geometry {

	private double[] coordinates;

	public Point() {
		super("Point");
	}

	public double[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(double[] coordinates) {
		this.coordinates = coordinates;
	}

}
