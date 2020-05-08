
package io.cuillgln.toys.domain.model;

public interface Repository<T, ID> {

	T save(T data);

}
