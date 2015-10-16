package controller;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.JDBCDAO;
import entity.document.Document;

@ManagedBean
@RequestScoped
public class IndexController {

	private JDBCDAO jdbcDAO = new JDBCDAO();

	private static List<Document> documents = null;

	private static Logger log = LogManager.getLogger(IndexController.class);

	@PostConstruct
	public void init() {
		if (documents == null) {
			documents = jdbcDAO.findAllDocuments();
		}
	}

	public void deleteAllForDocument(BigDecimal docId) {
		try {
			jdbcDAO.deleteAllDataForDocument(docId);
			refresh();
			FacesContext.getCurrentInstance().addMessage("serverMessages", new FacesMessage("Data deletion successful!"));
			log.info("IndexController - Data deletion ok");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("serverMessages", new FacesMessage("Data deletion unsuccessful! " + e.getMessage()));
			log.info("IndexController - Data deletion not ok: " + e.getMessage());
		}
	}

	protected void refresh() {
		documents = jdbcDAO.findAllDocuments();
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
}
