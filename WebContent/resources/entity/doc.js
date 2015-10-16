/**
 * 
 */
function Doc(id, name, desc, created, createdBy, updated, status, type, catalog){
	this.document = id;
	this.name = name;
	this.description = desc;
	this.created = created;
	this.createdBy = createdBy;
	this.updated = updated;
	this.status = status;
	this.type = type;
	this.catalog = catalog;
}