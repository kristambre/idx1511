package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.google.gson.Gson;

import dao.EntityDAO;
import entity.document.DocAttribute;
import entity.document.Document;

@ManagedBean
@RequestScoped
public class JavascriptController {

	public String getBufferData(BigDecimal id) {
		Gson g = new Gson();

		EntityDAO dao = new EntityDAO();
		List<DocAttribute> attributes = dao.findDocumentAttributes(id);
		JSDoc doc = new JSDoc(dao.findDocumentById(id));

		doc.setStatus(dao.findDocumentStatusType(doc.getDocStatusTypeFk()).getTypeName());
		doc.setType(dao.findDocumentType(doc.getDocument()).getTypeName());
		doc.setCatalog(dao.findDocumentCatalog(doc.getDocument()).getName());
		doc.setCreatedByStr(dao.findPersonById(doc.getCreatedBy()).getFirstName() + " " + dao.findPersonById(doc.getCreatedBy()).getLastName());

		Map<String, String> attr = new HashMap<>();
		for (DocAttribute a : attributes) {
			String val = new String();
			// if the value we add to attributes is a date, we make it to
			// string:
			try {
				val = a.getValueDate().toString();
			} catch (NullPointerException e) {
				// ignored
			}

			// same with number:
			try {
				val = a.getValueNumber().toString();
			} catch (NullPointerException e) {
				// ignored
			}

			// if the value is still empty here, means it wasn't either of the
			// upper ones
			if (val.isEmpty()) {
				try {
					// so we find out if its a selection from the database:
					val = dao.findAttributeTypeSelectionValueById(a.getAtrTypeSelectionValueFk()).getValueText();
				} catch (Exception e) {
					// if it isn't, means its just the text value:
					val = a.getValueText();
				}
			}

			// if value is still empty here, means there was no value provided
			// for this attribute and we add a space, so it gets added to the
			// hashmap:
			if (val == null || val.isEmpty()) {
				val = " ";
			}

			// finally, we check if the existing attribute value is already in
			// this map:
			if (attr.get(a.getTypeName()) != null) {
				// if it is, we simply take the old one and add the new one and
				// seperate it with a comma:
				String original = attr.get(a.getTypeName());
				attr.replace(a.getTypeName(), original + ", " + val);
			} else {
				// if it isnt, we add a new value:
				attr.put(a.getTypeName(), val);
			}
		}

		doc.setAttributes(attr);

		String json = g.toJson(doc);
		System.out.println(json);
		return json;
	}

	public void renderJson() throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		BigDecimal id = new BigDecimal(externalContext.getRequestParameterMap().get("id"));
		externalContext.setResponseContentType("application/json");
		externalContext.setResponseCharacterEncoding("UTF-8");
		externalContext.getResponseOutputWriter().write(getBufferData(id));
		facesContext.responseComplete();
	}

	private class JSDoc extends Document {
		private static final long serialVersionUID = -3730193171110381452L;
		private String createdByStr = "";
		private String status = "";
		private String catalog = "";
		private String type = "";
		private Map<String, String> attributes;

		public JSDoc(Document d) {
			this.setCreatedBy(d.getCreatedBy());
			this.setCreated(d.getCreated());
			this.setUpdated(d.getUpdated());
			this.setUpdatedBy(d.getUpdatedBy());

			this.setDocument(d.getDocument());
			this.setName(d.getName());
			this.setDescription(d.getDescription());
			this.setDocNr(d.getDocNr());
			this.setDocStatusTypeFk(d.getDocStatusTypeFk());
			this.setFilename(d.getFilename());
		}

		public String getCreatedByStr() {
			return createdByStr;
		}

		public void setCreatedByStr(String createdByStr) {
			this.createdByStr = createdByStr;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getCatalog() {
			return catalog;
		}

		public void setCatalog(String catalog) {
			this.catalog = catalog;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Map<String, String> getAttributes() {
			return attributes;
		}

		public void setAttributes(Map<String, String> attributes) {
			this.attributes = attributes;
		}

	}
}
