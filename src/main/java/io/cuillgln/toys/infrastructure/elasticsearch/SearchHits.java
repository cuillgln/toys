
package io.cuillgln.toys.infrastructure.elasticsearch;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchHits<T> {

	private Total total;
	private Float max_score;
	private Collection<Hit<T>> hits;

	public Total getTotal() {
		return total;
	}

	public Float getMax_score() {
		return max_score;
	}

	public Collection<Hit<T>> getHits() {
		return hits;
	}

	public void setHits(Collection<Hit<T>> hits) {
		this.hits = hits;
	}

	public void setTotal(Total total) {
		this.total = total;
	}

	public void setMax_score(Float max_score) {
		this.max_score = max_score;
	}

	static class Total {

		private long value;
		private String relation;

		public long getValue() {
			return value;
		}

		public void setValue(long value) {
			this.value = value;
		}

		public String getRelation() {
			return relation;
		}

		public void setRelation(String relation) {
			this.relation = relation;
		}

	}

	static class Hit<S> {

		@JsonProperty("_index")
		private String index;
		@JsonProperty("_type")
		private String type;
		@JsonProperty("_id")
		private String id;
		@JsonProperty("_score")
		private Float score;
		@JsonProperty("_source")
		private S source;
		@JsonProperty("sort")
		private List<Object> sort;

		public String getIndex() {
			return index;
		}

		public String getType() {
			return type;
		}

		public String getId() {
			return id;
		}

		public Float getScore() {
			return score;
		}

		public S getSource() {
			return source;
		}

		public List<Object> getSort() {
			return sort;
		}

	}
}
