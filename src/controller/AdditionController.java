package controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import util.Validator;
import dao.EntityDAO;
import entity.document.AtrTypeSelectionValue;
import entity.document.DocAttribute;
import entity.document.DocAttributeType;
import entity.document.DocCatalog;
import entity.document.DocStatus;
import entity.document.DocStatusType;
import entity.document.DocSubject;
import entity.document.DocSubjectRelationType;
import entity.document.DocSubjectType;
import entity.document.DocType;
import entity.document.Document;
import entity.document.DocumentDocCatalog;
import entity.document.DocumentDocType;
import entity.subject.Person;

@ManagedBean
@RequestScoped
public class AdditionController {

	private static List<DocStatusType> documentStatuses = null;
	private static List<DocType> documentTypes = null;
	private static List<DocCatalog> documentCatalogues = null;
	private static List<DocAttributeType> documentAttributes = null;
	private static List<DocAttribute> documentAttributesToAdd = new ArrayList<>();

	private static List<DocSubject> documentSubjectConnectionsToAdd = new ArrayList<>();
	private static List<DocSubjectRelationType> documentSubjectRelationTypes = null;
	private static List<DocSubjectType> documentSubjectTypes = null;
	private static List<AtrTypeSelectionValue> documentAttributesSelectionList = null;

	private static Logger log = LogManager.getLogger(AdditionController.class);

	public AdditionController() {
		if (documentStatuses == null || documentTypes == null || documentCatalogues == null || documentAttributes == null) {
			query();
		}
	}

	public String add() {
		log.info("AdditionController.add()");
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

		// kõigepealt valideerime atribuutide parameetrid:
		boolean paramsOk = fillDocumentAttributeToAddParameters();

		// kui kõik on ok:
		if (paramsOk) {

			EntityDAO dao = new EntityDAO();

			// praegune kuupäev, kuu, aasta ja kellaaeg
			Date now = new Date();

			// vormi parameetrid
			String name = params.get("add:name");
			String desc = params.get("add:desc");
			BigDecimal statusId = new BigDecimal(params.get("add:status"));
			BigDecimal typeId = new BigDecimal(params.get("add:type"));
			BigDecimal catalogId = new BigDecimal(params.get("add:catalog"));

			// sisselogitud kasutaja parameetrid
			Person creator = SessionController.loggedInUser();

			// sisestame vormi parameetrid dokumenti:
			Document d = new Document();
			d.setName(name);
			d.setDescription(desc);
			d.setCreatedBy(creator.getPerson());
			d.setCreated(now);
			d.setDocStatusTypeFk(statusId);

			// lisame andmebaasi ja saame vastuse, et saada dokumendi ID, mis on
			// postgreSQL-i poolt genereeritud:
			d = dao.createUpdateDocument(d);

			// lisame uue staatuse lisatud dokumendile:
			DocStatus status = new DocStatus();
			status.setCreatedBy(creator.getPerson());
			status.setDocStatusTypeFk(statusId);
			status.setDocumentFk(d.getDocument());
			status.setStatusBegin(now);

			// lisame staatuse andmebaasi:
			status = dao.createUpdateDocumentStatus(status);

			// lisame tüübi dokumendile:
			DocumentDocType type = new DocumentDocType();
			type.setDocTypeFk(typeId);
			type.setDocumentFk(d.getDocument());

			// lisame tüübi andmebaasi:
			type = dao.createUpdateDocumentType(type);

			// lisame kataloogi dokumendile:
			DocumentDocCatalog cat = new DocumentDocCatalog();
			cat.setDocumentFk(d.getDocument());
			cat.setDocCatalogFk(catalogId);
			cat.setCatalogTime(now);

			// lisame kataloogi andmebaasi:
			cat = dao.createUpdateDocumentCatalog(cat);

			// sisestame atribuutidele dokumendi fk:
			addDocumentFkToAttributes(d.getDocument());

			// lisame atribuudid andmebaasi:
			documentAttributesToAdd = dao.createUpdateDocumentAttributes(documentAttributesToAdd);
			// värskendame dokumentide listi:
			new IndexController().refresh();

			// saadame server message, et kõik on ok:
			FacesContext.getCurrentInstance().addMessage("serverMessages", new FacesMessage("Dokumendi sisestamine õnnestus!"));
			// hoiame message alles, et peale redirecti see ära ei kaoks:
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			return "index?faces-redirect=true";
		}
		return null;
	}

	private boolean fillDocumentAttributeToAddParameters() {
		// POST-requesti parameetrid:
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		// lisatud atribuutide väljade loendur:
		int i = 0;

		boolean ok = true;

		// tsükkel üle parameetrite:
		for (Map.Entry<String, String> e : params.entrySet()) {

			// teeme uue instantsi dokumendi atribuudist, kui selle index ei ole
			// suurem, kui nimekiri väljadest:
			DocAttribute d = null;
			if (i < documentAttributesToAdd.size()) {
				d = documentAttributesToAdd.get(i);
			}

			// teeme uue instantsi dokumendi atribuudi klassifikaatorist:
			DocAttributeType type = new DocAttributeType();

			// jsf-i renderduse tulemusena tuleb vahele üks random id, mistõttu
			// peame kontrollima, kas antud parameetri ID sisaldab :N:attrId ja
			// :N:attrVal väärtuseid, kus
			// N on number, mis suureneb vastavalt leitud väärtustele
			if (e.getKey().contains(i + ":attrId")) {

				// dokumendi atribuudile lisame viite dokumendi atribuudi
				// klassifikaatorite tabelile:
				d.setDocAttributeTypeFk(new BigDecimal(e.getValue()));

				// leiame andmebaasist vormilt valitud dokumendi atribuudi
				// klassifikaatorväärtuse:
				for (DocAttributeType t : documentAttributes) {
					if (t.getDocAttributeType().equals(new BigDecimal(e.getValue()))) {
						type = t;
						break;
					}
				}

				// lisame dokumendi atribuudile klassifikaatori väärtused, mis
				// on atribuudi enda tabelis vajalikud:
				d.setTypeName(type.getTypeName());
			}

			// lisaks Id-le peame leidma ka vastava input-i väärtuse:
			if (e.getKey().contains(i + ":attrVal")) {

				// vastavalt atribuudi klassifikaatori DataType väärtusele
				// üritame parse-ida inputi väärtust õigesse formaati:
				BigDecimal dataType = d.getDataType();
				// andmebaasist:
				// 1 - string
				// 2 - number
				// 3 - date
				// 4 - list

				// Stringiks sobib iga arv:
				if (dataType.equals(new BigDecimal("1"))) {
					if (e.getValue().isEmpty()) {
						FacesContext.getCurrentInstance().addMessage(e.getKey(), new FacesMessage("Ei tohi olla tühi!"));
					} else {
						d.setValueText(e.getValue());
						log.info("AdditionController - Empty data type detected");
					}
				}

				// proovime numbrit kõigepealt:
				if (dataType.equals(new BigDecimal("2"))) {
					try {
						if (e.getValue().isEmpty()) {
							throw new Exception();
						}
						d.setValueNumber(new BigDecimal(e.getValue()));
					} catch (Exception ex) {
						log.info("AdditionController - Entered value not numeric: " + ex.getMessage());
						FacesContext.getCurrentInstance().addMessage(e.getKey(), new FacesMessage("Ei õnnestunud sisestada! Ootasin numbrit, aga ei olnud!"));
						ok = false;
					}
				}

				// seejärel Date:
				if (dataType.equals(new BigDecimal("3"))) {

					try {
						if (e.getValue().isEmpty()) {
							throw new Exception();
						}
						DateFormat format = new SimpleDateFormat("d/MM/yyyy", Locale.ENGLISH);
						d.setValueDate(format.parse(e.getValue()));

						// vaatame, üritatakse panna sisse sellist kuupäeva,
						// mis
						// on väiksem, kui praegune:
						if (!Validator.validateDate(format.parse(e.getValue()))) {
							log.info("AdditionController - Entered date is before today");
							FacesContext.getCurrentInstance().addMessage(e.getKey(), new FacesMessage("Ei õnnestunud sisestada! Ei saa sisestada kuupäeva, mis on enne tänast!"));
							ok = false;
						}
					} catch (Exception ex) {
						log.info("AdditionController - Entered date in wrong format: " + ex.getMessage());
						FacesContext.getCurrentInstance().addMessage(e.getKey(), new FacesMessage("Ei õnnestunud sisestada! Kuupäev vales formaadis! Õige formaat on dd/MM/yyyy!"));
						ok = false;
					}

				}
				// kui on olemas valik nimekirjast, siis on selle id
				// :attrValSel
				// ning selle value on valiku primary key, mistõttu saame
				// selle
				// lihtsalt andmebaasi panna, aga siiski igaks juhuks
				// kontrollime
				// kas midagi juhtub
				if (dataType.equals(new BigDecimal("4"))) {
					try {
						if (e.getValue().isEmpty()) {
							throw new Exception();
						}
						d.setAtrTypeSelectionValueFk(new BigDecimal(e.getValue()));
					} catch (Exception ex) {
						log.info("AdditionController - Error: " + ex.getMessage());
						FacesContext.getCurrentInstance().addMessage(e.getKey(), new FacesMessage("Ei õnnestunud sisestada!" + ex.getMessage()));
						ok = false;
					}
				}

				i++;
			}

		}

		// nüüd kontrollime nime:
		String name = params.get("add:name");
		if (name.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage("add:name", new FacesMessage("Ei tohi olla tühi!"));
			ok = false;
		}

		return ok;
	}

	private void addDocumentFkToAttributes(BigDecimal documentId) {
		for (DocAttribute d : documentAttributesToAdd) {
			d.setDocumentFk(documentId);
		}
	}

	public void selectMenuListener(AjaxBehaviorEvent evt) {
		UIComponent uic = (UIComponent) evt.getSource();
		HtmlSelectOneMenu menu = (HtmlSelectOneMenu) evt.getSource();
		DocAttribute param = (DocAttribute) uic.getAttributes().get("attribute");
		BigDecimal val = new BigDecimal(String.valueOf(menu.getValue()));

		DocAttributeType type = null;
		for (DocAttributeType t : documentAttributes) {
			if (t.getDocAttributeType().equals(val)) {
				type = t;
				break;
			}
		}

		for (DocAttribute attr : documentAttributesToAdd) {
			if (param.equals(attr)) {
				attr.setDocAttributeTypeFk(val);
				attr.setDataType(type.getDataTypeFk());
				break;
			}
		}
	}

	/**
	 * Sisetab uue tühja parameetri listi.
	 */
	public void addAttribute() {
		DocAttribute d = new DocAttribute();
		d.setDocAttributeTypeFk(new BigDecimal("1"));
		d.setDataType(new BigDecimal("1"));
		documentAttributesToAdd.add(d);
	}

	public void addSubjectConnection() {
		DocSubject s = new DocSubject();
		documentSubjectConnectionsToAdd.add(s);
	}

	public void removeSubjectConnection(DocSubject d) {
		documentSubjectConnectionsToAdd.remove(d);
	}

	public void addConnection() {
		System.out.println("Adding connection");
		BigDecimal docId = new UpdateController().getDocument().getDocument();
		for (DocSubject s : documentSubjectConnectionsToAdd) {
			s.setDocumentFk(docId);
			System.out.println(s.getNote());
			System.out.println(s.getDocSubject());
			System.out.println(s.getDocSubjectRelationTypeFk());
			System.out.println(s.getDocSubjectTypeFk());
			System.out.println(s.getDocumentFk());
			System.out.println(s.getSubjectFk());
		}

		EntityDAO dao = new EntityDAO();
		System.out.println("dao");
		dao.createUpdateDocumentSubjects(documentSubjectConnectionsToAdd);
	}

	private void query() {
		EntityDAO dao = new EntityDAO();
		documentStatuses = dao.findAllDocumentStatuses();
		documentTypes = dao.findAllDocumentTypes();
		documentCatalogues = dao.findAllDocumentCatalogues();
		documentAttributes = dao.findAllDocumentAttributeTypes();
		documentAttributesSelectionList = dao.findAllDocumentAttributeSelectionValues();
		documentSubjectRelationTypes = dao.findAllDocumentSubjectRelationTypes();
		documentSubjectTypes = dao.findAllDocumentSubjectTypes();
	}

	public List<DocStatusType> getDocumentStatuses() {
		if (documentStatuses == null) {
			query();
		}
		return documentStatuses;
	}

	public void setDocumentStatuses(List<DocStatusType> documentStatuses) {
		this.documentStatuses = documentStatuses;
	}

	public List<DocType> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(List<DocType> documentTypes) {
		this.documentTypes = documentTypes;
	}

	public List<DocCatalog> getDocumentCatalogues() {
		return documentCatalogues;
	}

	public void setDocumentCatalogues(List<DocCatalog> documentCatalogues) {
		this.documentCatalogues = documentCatalogues;
	}

	public List<DocAttributeType> getDocumentAttributes() {
		return documentAttributes;
	}

	public void setDocumentAttributes(List<DocAttributeType> documentAttributes) {
		this.documentAttributes = documentAttributes;
	}

	public List<DocAttribute> getDocumentAttributesToAdd() {
		return documentAttributesToAdd;
	}

	public void setDocumentAttributesToAdd(List<DocAttribute> documentAttributesToAdd) {
		AdditionController.documentAttributesToAdd = documentAttributesToAdd;
	}

	public List<AtrTypeSelectionValue> getDocumentAttributesSelectionList() {
		return documentAttributesSelectionList;
	}

	public void setDocumentAttributesSelectionList(List<AtrTypeSelectionValue> documentAttributesSelectionList) {
		AdditionController.documentAttributesSelectionList = documentAttributesSelectionList;
	}

	public List<DocSubject> getDocumentSubjectConnectionsToAdd() {
		return documentSubjectConnectionsToAdd;
	}

	public void setDocumentSubjectConnectionsToAdd(List<DocSubject> documentSubjectConnectionsToAdd) {
		AdditionController.documentSubjectConnectionsToAdd = documentSubjectConnectionsToAdd;
	}

	public List<DocSubjectRelationType> getDocumentSubjectRelationTypes() {
		return documentSubjectRelationTypes;
	}

	public void setDocumentSubjectRelationTypes(List<DocSubjectRelationType> documentSubjectRelationTypes) {
		AdditionController.documentSubjectRelationTypes = documentSubjectRelationTypes;
	}

	public List<DocSubjectType> getDocumentSubjectTypes() {
		return documentSubjectTypes;
	}

	public void setDocumentSubjectTypes(List<DocSubjectType> documentSubjectTypes) {
		AdditionController.documentSubjectTypes = documentSubjectTypes;
	}

}
