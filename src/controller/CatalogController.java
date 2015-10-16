package controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.EntityDAO;
import entity.document.DocCatalog;
import entity.document.Document;
import entity.document.DocumentDocCatalog;

@ManagedBean
@SessionScoped
public class CatalogController {

	private static DocCatalog currentCatalog;
	private static List<DocCatalog> catalogues;
	private static List<DocumentDocCatalog> documentCatalogues;

	private static List<Document> buffer;

	private static Logger log = LogManager.getLogger(CatalogController.class);

	public CatalogController() {
		if (catalogues == null) {
			EntityDAO dao = new EntityDAO();
			catalogues = dao.findAllDocumentCatalogues();
		}

		if (documentCatalogues == null) {
			EntityDAO dao = new EntityDAO();
			documentCatalogues = dao.findAllDocumentDocCatalogues();
		}

		if (buffer == null) {
			buffer = new ArrayList<>();
		}

	}

	public List<DocCatalog> getSubcatalogsFor(BigDecimal catId) {
		List<DocCatalog> subs = new ArrayList<>();
		for (DocCatalog c : catalogues) {
			if (c.getUpperCatalogFk().equals(catId)) {
				subs.add(c);
			}
		}
		return subs;
	}

	public boolean isCurrentCatalogContainsDocument(Document doc) {
		// currentCatalog == null is root
		if (currentCatalog != null) {
			for (DocumentDocCatalog d : documentCatalogues) {
				if (d.getDocumentFk().equals(doc.getDocument()) && d.getDocCatalogFk().equals(currentCatalog.getDocCatalog())) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

	public void addToBuffer(BigDecimal docId) {
		EntityDAO dao = new EntityDAO();
		buffer.add(dao.findDocumentById(docId));
		FacesContext.getCurrentInstance().addMessage("serverMessages", new FacesMessage("Document successfully added to buffer!"));
		log.info("CatalogController - buffer addition ok");
	}

	public void removeFromBuffer(BigDecimal docId) {
		EntityDAO dao = new EntityDAO();
		Document d = dao.findDocumentById(docId);
		buffer.remove(d);
		FacesContext.getCurrentInstance().addMessage("serverMessages", new FacesMessage("Document successfully removed from buffer!"));
		log.info("CatalogController - buffer removal ok");
	}

	public String moveAllToCurrentFolder() {
		EntityDAO dao = new EntityDAO();
		for (Document d : buffer) {
			DocumentDocCatalog c = dao.findDocumentDocCatalog(d.getDocument());
			c.setDocCatalogFk(currentCatalog.getDocCatalog());
			dao.createUpdateDocumentCatalog(c);
		}
		buffer.clear();
		FacesContext.getCurrentInstance().addMessage("serverMessages", new FacesMessage("All documents in buffer successfully moved!"));
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		log.info("CatalogController - folder moving ok");
		refresh();
		new IndexController().refresh();
		return "index?faces-redirect=true";
	}

	public void clearBuffer() {
		buffer.clear();
	}

	protected void refresh() {
		documentCatalogues = new EntityDAO().findAllDocumentDocCatalogues();
	}

	public String getDocumentCatalogFor(BigDecimal docId) {
		EntityDAO dao = new EntityDAO();
		return dao.findDocumentCatalog(docId).getName();
	}

	public DocCatalog getCurrentCatalog() {
		return currentCatalog;
	}

	public void setCurrentCatalog(DocCatalog currentCatalog) {
		this.currentCatalog = currentCatalog;
	}

	public List<DocCatalog> getCatalogues() {
		return catalogues;
	}

	public void setCatalogues(List<DocCatalog> catalogues) {
		this.catalogues = catalogues;
	}

	public List<Document> getBuffer() {
		return buffer;
	}

	public void setBuffer(List<Document> buffer) {
		CatalogController.buffer = buffer;
	}

}
