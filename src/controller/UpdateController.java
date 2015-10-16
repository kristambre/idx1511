package controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import util.Validator;
import dao.EntityDAO;
import entity.document.AtrTypeSelectionValue;
import entity.document.DocAttribute;
import entity.document.DocCatalog;
import entity.document.DocStatus;
import entity.document.DocStatusType;
import entity.document.DocSubject;
import entity.document.DocType;
import entity.document.Document;
import entity.document.DocumentDocCatalog;

@ManagedBean
@RequestScoped
public class UpdateController {

	private static Document document;
	private static List<DocAttribute> documentAttributes;
	private static List<DocStatus> documentStatuses;
	private static DocStatusType documentStatusType;
	private static DocType documentType;
	private static DocCatalog documentCatalog;
	private static DocumentDocCatalog documentDocCatalog;

	private static List<DocCatalog> allCatalogues;
	private static List<DocStatusType> allTypes;
	private static List<AtrTypeSelectionValue> allSelectionValues;
	private static List<DocSubject> allConnections;

	private static Logger log = LogManager.getLogger(UpdateController.class);

	public UpdateController() {
		try {
			BigDecimal id = new BigDecimal(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
			updateDocumentData(id);
		} catch (NullPointerException e) {
			// nothing
		}
	}

	public String update() {
		// attributes first:
		boolean ok = true;
		int i = 0;
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

		for (DocAttribute d : documentAttributes) {

			Map.Entry<String, String> entry = null;
			for (Map.Entry<String, String> e : params.entrySet()) {
				if (e.getKey().contains(i + ":attr")) {
					entry = e;
					i++;
					break;
				}
			}

			if (d.getDataType().equals(new BigDecimal("2"))) {
				try {
					if (d.getValueText().isEmpty()) {
						throw new Exception();
					}
					d.setValueNumber(new BigDecimal(d.getValueText()));
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(entry.getKey(), new FacesMessage("Vale sisestus! Oodatud oli number!"));
					ok = false;
					log.info("UpdateController - numeric exception: " + e.getMessage());
				}
			}
			if (d.getDataType().equals(new BigDecimal("3"))) {
				try {
					if (d.getValueText().isEmpty()) {
						throw new Exception();
					}
					if (d.getValueText().length() > 10) {
						throw new Exception();
					}
					DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
					d.setValueDate(format.parse(d.getValueText()));

					if (!Validator.validateDate(format.parse(d.getValueText()))) {
						FacesContext.getCurrentInstance().addMessage(entry.getKey(), new FacesMessage("Vale sisestud! Ei saa olla enne tänast!"));
						ok = false;
						log.info("UpdateController - date before today detected");
					}
				} catch (Exception ex) {
					FacesContext.getCurrentInstance().addMessage(entry.getKey(), new FacesMessage("Ei õnnestunud sisestada! Kuupäev vales formaadis! Õige formaat on dd/MM/yyyy!"));
					ok = false;
					log.info("UpdateController - date in wrong format: " + ex.getMessage());
				}
			}
		}

		// now name:
		if (document.getName().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage("update:name", new FacesMessage("Ei õnnestunud sisestada! Ei tohi olla tühi!"));
			ok = false;
			log.info("UpdateController - Empty name detected.");
		}

		// now updating:
		if (ok) {
			EntityDAO dao = new EntityDAO();
			// attributes:
			dao.createUpdateDocumentAttributes(documentAttributes);

			// status:
			DocStatus lastStatus = documentStatuses.get(documentStatuses.size() - 1);
			lastStatus.setStatusEnd(new Date());
			dao.createUpdateDocumentStatus(lastStatus);

			DocStatus newStatus = new DocStatus();
			newStatus.setStatusBegin(new Date());
			newStatus.setCreatedBy(new SessionController().getLoggedInUser().getPerson());

			// right now select one menu selects a typeName of
			// documentStatusType, because a converter would have to be written
			// otherwise
			for (DocStatusType s : allTypes) {
				if (documentStatusType.getTypeName().equals(s.getTypeName())) {
					newStatus.setDocStatusTypeFk(s.getDocStatusType());
					break;
				}
			}
			newStatus.setDocumentFk(document.getDocument());
			dao.createUpdateDocumentStatus(newStatus);

			// catalog

			for (DocCatalog c : allCatalogues) {
				if (c.getName().equals(documentCatalog.getName())) {
					documentDocCatalog.setDocCatalogFk(c.getDocCatalog());
				}
			}
			dao.createUpdateDocumentCatalog(documentDocCatalog);

			// document:
			document.setDocStatusTypeFk(newStatus.getDocStatusTypeFk());
			document.setUpdated(new Date());
			document.setUpdatedBy(new SessionController().getLoggedInUser().getPerson());
			Document d = dao.createUpdateDocument(document);

			// refreshing index
			new IndexController().refresh();

			// saadame server message, et kõik on ok:
			FacesContext.getCurrentInstance().addMessage("serverMessages", new FacesMessage("Dokumendi uuendamine õnnestus!"));
			// hoiame message alles, et peale redirecti see ära ei kaoks:
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			return "index?faces-redirect=true";
		}
		return null;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public List<DocAttribute> getDocumentAttributes() {
		return documentAttributes;
	}

	public void setDocumentAttributes(List<DocAttribute> documentAttributes) {
		this.documentAttributes = documentAttributes;
	}

	public List<DocStatus> getDocumentStatus() {
		return documentStatuses;
	}

	public void setDocumentStatus(List<DocStatus> documentStatuses) {
		this.documentStatuses = documentStatuses;
	}

	public DocStatusType getDocumentStatusType() {
		return documentStatusType;
	}

	public void setDocumentStatusType(DocStatusType documentStatusType) {
		this.documentStatusType = documentStatusType;
	}

	public DocType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocType documentType) {
		this.documentType = documentType;
	}

	public DocCatalog getDocumentCatalog() {
		return documentCatalog;
	}

	public void setDocumentCatalog(DocCatalog documentCatalog) {
		this.documentCatalog = documentCatalog;
	}

	public List<DocStatus> getDocumentStatuses() {
		return documentStatuses;
	}

	public void setDocumentStatuses(List<DocStatus> documentStatuses) {
		UpdateController.documentStatuses = documentStatuses;
	}

	public List<DocCatalog> getAllCatalogues() {
		return allCatalogues;
	}

	public void setAllCatalogues(List<DocCatalog> allCatalogues) {
		UpdateController.allCatalogues = allCatalogues;
	}

	public List<DocStatusType> getAllTypes() {
		return allTypes;
	}

	public void setAllTypes(List<DocStatusType> allTypes) {
		UpdateController.allTypes = allTypes;
	}

	public List<AtrTypeSelectionValue> getAllSelectionValues() {
		return allSelectionValues;
	}

	public void setAllSelectionValues(List<AtrTypeSelectionValue> allSelectionValues) {
		UpdateController.allSelectionValues = allSelectionValues;
	}

	private void updateDocumentData(BigDecimal docId) {
		EntityDAO dao = new EntityDAO();

		document = dao.findDocumentById(docId);
		documentAttributes = dao.findDocumentAttributes(document.getDocument());
		documentStatuses = dao.findDocumentStatuses(document.getDocument());
		documentStatusType = dao.findDocumentStatusType(document.getDocStatusTypeFk());
		documentType = dao.findDocumentType(document.getDocument());
		documentCatalog = dao.findDocumentCatalog(document.getDocument());
		documentDocCatalog = dao.findDocumentDocCatalog(document.getDocument());

		allCatalogues = dao.findAllDocumentCatalogues();
		allTypes = dao.findAllDocumentStatuses();
		allSelectionValues = dao.findAllDocumentAttributeSelectionValues();

		// moving document attributes to text:
		for (DocAttribute d : documentAttributes) {
			if (d.getDataType().equals(new BigDecimal("2"))) {
				d.setValueText(d.getValueNumber().toString());
			}

			if (d.getDataType().equals(new BigDecimal("3"))) {
				DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				String date = format.format(d.getValueDate());
				d.setValueText(date);
			}
		}
	}
}
