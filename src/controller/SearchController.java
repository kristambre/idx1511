package controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import dao.EntityDAO;
import dao.JDBCDAO;
import entity.document.DocAttribute;
import entity.document.DocCatalog;
import entity.document.DocStatusType;
import entity.document.DocType;
import entity.document.Document;
import entity.document.DocumentDocCatalog;
import entity.document.DocumentDocType;
import entity.subject.Person;

@ManagedBean
@ViewScoped
public class SearchController {

	private static Document document;

	private static List<DocStatusType> allStatuses;
	private static List<DocType> allTypes;
	private static List<DocCatalog> allCatalogues;

	private static List<Document> results;

	@PostConstruct
	public void init() {
		EntityDAO dao = new EntityDAO();
		allStatuses = dao.findAllDocumentStatuses();
		allTypes = dao.findAllDocumentTypes();
		allCatalogues = dao.findAllDocumentCatalogues();

		results = new ArrayList<>();
	}

	public void search() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String doc_Id = params.get("searchForm:id");
		String name = params.get("searchForm:name");
		String description = params.get("searchForm:description");
		String lastUpdater = params.get("searchForm:lastUpdater");
		String catalog_Id = params.get("searchForm:catalog");
		String subject = params.get("searchForm:subject");
		String status_Id = params.get("searchForm:status");
		String attrVal = params.get("searchForm:attrVal");
		String type_Id = params.get("searchForm:type");

		BigDecimal docId = new BigDecimal(.1);
		BigDecimal catalogId = new BigDecimal(-1);
		BigDecimal statusId = new BigDecimal(-1);
		BigDecimal typeId = new BigDecimal(-1);

		try {
			docId = new BigDecimal(doc_Id);
		} catch (Exception e) {
			// id wrong or not given
		}

		try {
			catalogId = new BigDecimal(catalog_Id);
		} catch (Exception e) {
			// id wrong or not given
		}

		try {
			statusId = new BigDecimal(status_Id);
		} catch (Exception e) {
			// id wrong or not given
		}

		try {
			typeId = new BigDecimal(type_Id);
		} catch (Exception e) {
			// id wrong or not given
		}
		EntityDAO dao = new EntityDAO();

		List<Document> results = new ArrayList<>();

		List<Document> matchingId = getDocumentsWithId(docId);
		List<Document> matchingName = getDocumentsWithName(name);
		List<Document> matchingDescription = getDocumentsWithDescription(description);
		List<Document> matchingLastUpdater = getDocumentsWithLastUpdaterName(lastUpdater);
		List<Document> matchingAttributeValue = getDocumentsWithAttributeValue(attrVal);
		List<Document> matchingCatalogId = getDocumentsInCatalog(catalogId);
		List<Document> matchingStatusId = getDocumentsWithStatus(statusId);
		List<Document> matchingTypeId = getDocumentsWithType(typeId);

		results = intersection(matchingId, matchingName, matchingDescription, matchingLastUpdater, matchingAttributeValue, matchingCatalogId, matchingStatusId, matchingTypeId);

		SearchController.results = results;
	}

	private List<Document> getDocumentsWithAttributeValue(String val) {
		List<Document> match = new ArrayList<>();

		if (!val.isEmpty()) {
			EntityDAO dao = new EntityDAO();
			for (DocAttribute attr : dao.findAllDocumentAttributes()) {
				try {
					attr.setValueText(attr.getValueDate().toString());
				} catch (Exception e) {
					// empty
				}

				try {
					attr.setValueText(attr.getValueNumber().toString());
				} catch (Exception e) {
					// empty
				}

				try {
					if (attr.getValueText().toLowerCase().contains(val.toLowerCase())) {
						match.add(dao.findDocumentById(attr.getDocumentFk()));
					}
				} catch (Exception e) {
					// also empty
				}
			}
		}

		return match;
	}

	private List<Document> getDocumentsWithLastUpdaterName(String name) {
		List<Document> match = new ArrayList<>();

		if (!name.isEmpty()) {
			EntityDAO dao = new EntityDAO();
			for (Document d : new JDBCDAO().findAllDocuments()) {
				if (!(d.getUpdatedBy() == null)) {
					Person p = dao.findPersonById(d.getUpdatedBy());
					if (p.getLastName().toLowerCase().contains(name.toLowerCase())) {
						match.add(d);
					}
				}
			}
		}

		return match;
	}

	private List<Document> getDocumentsWithId(BigDecimal docId) {
		List<Document> match = new ArrayList<>();
		EntityDAO dao = new EntityDAO();
		if (dao.findDocumentById(docId) != null) {
			match.add(dao.findDocumentById(docId));
		}

		return match;
	}

	private List<Document> getDocumentsWithName(String name) {
		List<Document> match = new ArrayList<>();

		if (!name.isEmpty()) {
			for (Document d : new JDBCDAO().findAllDocuments()) {
				if (d.getName().toLowerCase().contains(name.toLowerCase())) {
					match.add(d);
				}
			}
		}

		return match;
	}

	private List<Document> getDocumentsWithDescription(String desc) {
		List<Document> match = new ArrayList<>();

		if (!desc.isEmpty()) {
			for (Document d : new JDBCDAO().findAllDocuments()) {
				if (d.getDescription().toLowerCase().contains(desc.toLowerCase())) {
					match.add(d);
				}
			}
		}
		return match;
	}

	private List<Document> getDocumentsInCatalog(BigDecimal catId) {
		EntityDAO dao = new EntityDAO();
		List<Document> match = new ArrayList<>();
		for (DocCatalog cat : allCatalogues) {
			if (cat.getDocCatalog().equals(catId)) {
				List<DocumentDocCatalog> all = dao.findAllDocumentDocCatalogues();
				for (DocumentDocCatalog dcat : all) {
					if (dcat.getDocCatalogFk().equals(cat.getDocCatalog())) {
						match.add(dao.findDocumentById(dcat.getDocumentFk()));
					}
				}
				break;
			}
		}

		return match;
	}

	private List<Document> getDocumentsWithStatus(BigDecimal statusId) {
		List<Document> match = new ArrayList<>();

		for (Document d : new JDBCDAO().findAllDocuments()) {
			if (d.getDocStatusTypeFk().equals(statusId)) {
				match.add(d);
			}
		}

		return match;
	}

	private List<Document> getDocumentsWithType(BigDecimal typeId) {
		EntityDAO dao = new EntityDAO();
		List<Document> match = new ArrayList<>();

		List<DocumentDocType> all = dao.findAllDocumentDocTypes();
		for (DocumentDocType d : all) {
			if (d.getDocTypeFk().equals(typeId)) {
				match.add(dao.findDocumentById(d.getDocumentFk()));
			}
		}

		return match;
	}

	private List<Document> intersection(List<Document>... lists) {
		List<Document> list = new JDBCDAO().findAllDocuments();
		int initialSize = list.size();

		for (List<Document> l : lists) {
			if (l.size() > 0) {
				list.retainAll(l);
			}
		}

		return list.size() == initialSize ? new ArrayList<>() : list;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public List<DocStatusType> getAllStatuses() {
		return allStatuses;
	}

	public void setAllStatuses(List<DocStatusType> allStatuses) {
		this.allStatuses = allStatuses;
	}

	public List<DocType> getAllTypes() {
		return allTypes;
	}

	public void setAllTypes(List<DocType> allTypes) {
		this.allTypes = allTypes;
	}

	public List<DocCatalog> getAllCatalogues() {
		return allCatalogues;
	}

	public void setAllCatalogues(List<DocCatalog> allCatalogues) {
		SearchController.allCatalogues = allCatalogues;
	}

	public List<Document> getResults() {
		return results;
	}

	public void setResults(List<Document> results) {
		SearchController.results = results;
	}

}
