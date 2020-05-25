
package io.cuillgln.toys.infrastructure.elasticsearch;

public class ElasticsearchException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ElasticsearchException() {
		super();
	}

	public ElasticsearchException(String message, Throwable cause) {
		super(message, cause);
	}

	public ElasticsearchException(String message) {
		super(message);
	}

	public ElasticsearchException(Throwable cause) {
		super(cause);
	}

}
