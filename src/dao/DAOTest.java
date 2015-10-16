package dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import entity.document.Document;

public class DAOTest {

	@Test
	public void test() {
		JDBCDAO dao = new JDBCDAO();
		List<Document> documents = dao.findAllDocuments();

		assertEquals(2, documents.size());
	}

}
