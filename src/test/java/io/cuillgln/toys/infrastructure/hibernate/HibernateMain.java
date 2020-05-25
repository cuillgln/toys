
package io.cuillgln.toys.infrastructure.hibernate;

import java.sql.Timestamp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

public class HibernateMain {

	public static void main(String[] args) {
		BootstrapServiceRegistry bootstrapRegistry =
						new BootstrapServiceRegistryBuilder().build();
		StandardServiceRegistryBuilder standardRegistryBuilder =
						new StandardServiceRegistryBuilder(bootstrapRegistry);
		StandardServiceRegistry standardRegistry = standardRegistryBuilder.build();
		MetadataSources sources = new MetadataSources(standardRegistry);
		Metadata metadata = sources.buildMetadata();
		SessionFactory sessionFactory = metadata.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Query<Timestamp> now = session.createNativeQuery("select now()", Timestamp.class);
		System.out.println(now.getSingleResult());
		session.close();
		sessionFactory.close();
	}

}
