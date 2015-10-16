package util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

public class EMFUtil {

	@PersistenceUnit
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("documents");

	public static EntityManagerFactory getEmf() {
		return emf;
	}
}
