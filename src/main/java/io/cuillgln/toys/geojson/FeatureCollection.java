
package io.cuillgln.toys.geojson;

import java.util.List;

public class FeatureCollection {

	private final String type = "FeatureCollection";

	private List<Feature> features;

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public String getType() {
		return type;
	}

}
