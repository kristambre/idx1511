package dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import util.EMFUtil;
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
import entity.subject.Employee;
import entity.subject.Person;

public class EntityDAO {

	@PersistenceContext
	private EntityManager em = EMFUtil.getEmf().createEntityManager();

	public Document findDocumentById(BigDecimal id) {
		return em.find(Document.class, id);
	}

	public Person findPersonById(BigDecimal id) {
		return em.find(Person.class, id);
	}

	public List<DocStatusType> findAllDocumentStatuses() {
		return em.createQuery("from DocStatusType").getResultList();
	}

	public List<DocType> findAllDocumentTypes() {
		return em.createQuery("from DocType").getResultList();
	}

	public List<DocCatalog> findAllDocumentCatalogues() {
		return em.createQuery("from DocCatalog").getResultList();
	}

	public List<DocumentDocCatalog> findAllDocumentDocCatalogues() {
		return em.createQuery("from DocumentDocCatalog").getResultList();
	}

	public List<DocAttributeType> findAllDocumentAttributeTypes() {
		return em.createQuery("from DocAttributeType").getResultList();
	}

	public List<DocAttribute> findAllDocumentAttributes() {
		return em.createQuery("from DocAttribute").getResultList();
	}

	public List<AtrTypeSelectionValue> findAllDocumentAttributeSelectionValues() {
		return em.createQuery("from AtrTypeSelectionValue").getResultList();
	}

	public List<DocumentDocType> findAllDocumentDocTypes() {
		return em.createQuery("from DocumentDocType").getResultList();
	}

	public List<DocSubjectRelationType> findAllDocumentSubjectRelationTypes() {
		return em.createQuery("from DocSubjectRelationType").getResultList();
	}

	public List<DocSubjectType> findAllDocumentSubjectTypes() {
		return em.createQuery("from DocSubjectType").getResultList();
	}

	public DocSubjectRelationType findDocumentSubjectRelationTypeById(BigDecimal id) {
		return em.find(DocSubjectRelationType.class, id);
	}

	public DocSubjectType findDocumentSubjectTypeById(BigDecimal id) {
		return em.find(DocSubjectType.class, id);
	}

	public Person findEmployeeByName(String firstName, String lastName) {
		Person p = (Person) em.createQuery("from Person p where p.firstName=:firstName and p.lastName=:lastName").setParameter("firstName", firstName).setParameter("lastName", lastName)
				.getSingleResult();
		List<Employee> employees = em.createQuery("from Employee").getResultList();

		for (Employee e : employees) {
			if (p.getPerson().equals(e.getPersonFk())) {
				return p;
			}
		}

		return null;
	}

	public DocStatusType findDocumentStatusType(BigDecimal typeId) {
		return em.find(DocStatusType.class, typeId);
	}

	public List<DocStatus> findDocumentStatuses(BigDecimal docId) {
		return em.createQuery("from DocStatus d where d.documentFk=:documentFk").setParameter("documentFk", docId).getResultList();
	}

	public DocType findDocumentType(BigDecimal docId) {
		DocumentDocType d = (DocumentDocType) em.createQuery("from DocumentDocType d where d.documentFk=:documentFk").setParameter("documentFk", docId).getSingleResult();
		return (DocType) em.createQuery("from DocType d where d.docType=:docType").setParameter("docType", d.getDocTypeFk()).getSingleResult();
	}

	public DocCatalog findDocumentCatalog(BigDecimal docId) {
		DocumentDocCatalog c = (DocumentDocCatalog) em.createQuery("from DocumentDocCatalog d where d.documentFk=:documentFk").setParameter("documentFk", docId).getSingleResult();
		return (DocCatalog) em.createQuery("from DocCatalog d where d.docCatalog=:docCatalog").setParameter("docCatalog", c.getDocCatalogFk()).getSingleResult();
	}

	public DocumentDocCatalog findDocumentDocCatalog(BigDecimal docId) {
		return (DocumentDocCatalog) em.createQuery("from DocumentDocCatalog d where d.documentFk=:documentFk").setParameter("documentFk", docId).getSingleResult();
	}

	public List<DocAttribute> findDocumentAttributes(BigDecimal docId) {
		return em.createQuery("from DocAttribute d where d.documentFk=:documentFk").setParameter("documentFk", docId).getResultList();
	}

	public AtrTypeSelectionValue findAttributeTypeSelectionValueById(BigDecimal attrId) {
		return em.find(AtrTypeSelectionValue.class, attrId);
	}

	public Document createUpdateDocument(Document d) {
		em.getTransaction().begin();
		Document doc = em.merge(d);
		em.getTransaction().commit();
		return doc;
	}

	public DocStatus createUpdateDocumentStatus(DocStatus d) {
		em.getTransaction().begin();
		DocStatus s = em.merge(d);
		em.getTransaction().commit();
		return s;
	}

	public DocumentDocType createUpdateDocumentType(DocumentDocType d) {
		em.getTransaction().begin();
		DocumentDocType t = em.merge(d);
		em.getTransaction().commit();
		return t;
	}

	public DocumentDocCatalog createUpdateDocumentCatalog(DocumentDocCatalog d) {
		em.getTransaction().begin();
		DocumentDocCatalog c = em.merge(d);
		em.getTransaction().commit();
		return c;
	}

	public DocAttribute createUpdateDocumentAttribute(DocAttribute d) {
		em.getTransaction().begin();
		DocAttribute c = em.merge(d);
		em.getTransaction().commit();
		return c;
	}

	public List<DocAttribute> createUpdateDocumentAttributes(List<DocAttribute> list) {
		List<DocAttribute> ret = new ArrayList<>();
		for (DocAttribute d : list) {
			ret.add(createUpdateDocumentAttribute(d));
		}
		return ret;
	}

	public List<DocSubject> createUpdateDocumentSubjects(List<DocSubject> list) {
		List<DocSubject> s = new ArrayList<>();
		for (DocSubject d : list) {
			em.getTransaction().begin();
			s.add(em.merge(d));
			em.getTransaction().commit();
		}
		return s;
	}
}
