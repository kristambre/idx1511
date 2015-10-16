package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import entity.document.Document;

public class JDBCDAO {

	private final String DB_URL = ResourceBundle.getBundle("util.db").getString("url");
	private final String USER = ResourceBundle.getBundle("util.db").getString("user");
	private final String PASS = ResourceBundle.getBundle("util.db").getString("pass");

	private Statement stmt = null;

	public JDBCDAO() {
		Connection con = null;

		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = con.createStatement();

		} catch (Exception e) {
			System.out.println("Connection failed: " + e.getMessage());
		}
	}

	public List<Document> findAllDocuments() {
		List<Document> documents = new ArrayList<>();
		String sql = "SELECT * FROM Document d";

		try {
			ResultSet results = stmt.executeQuery(sql);

			while (results.next()) {
				Document d = new Document();
				d.setCreated(results.getDate("created"));
				d.setCreatedBy(results.getBigDecimal("created_by"));
				d.setDescription(results.getString("description"));
				d.setDocNr(results.getString("doc_nr"));
				d.setDocStatusTypeFk(results.getBigDecimal("doc_status_type_fk"));
				d.setDocument(results.getBigDecimal("document"));
				d.setFilename(results.getString("filename"));
				d.setName(results.getString("name"));
				d.setUpdated(results.getDate("updated"));
				d.setUpdatedBy(results.getBigDecimal("updated_by"));
				documents.add(d);
			}

		} catch (Exception e) {
			System.out.println("Unable to get results: " + e.getMessage());
		}

		return documents;
	}

	public void deleteAllDataForDocument(BigDecimal docId) throws SQLException {
		String sql = "SELECT deleteDocument(" + docId.intValue() + ");";
		stmt.executeQuery(sql);
	}
}
