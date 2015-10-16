/**
 * 
 */
function getData(id) {
	var doc;
	var attr = [];
	$.ajax({
		async : "false",
		url : "data.xhtml?id="+id,
		dataType : "json",
		type : "GET",
		success : function(data) {
			//getting a new document from server:
			doc = new Doc(data.document, data.name, data.description,
					data.created, data.createdByStr, data.updated, data.status,
					data.type, data.catalog);
			
			//also getting its' attributes:
			var attributes = data.attributes;
			
			//adding the attributes to Attribute.js array:
			var i = 0;
			for ( var key in attributes) {
				if (attributes.hasOwnProperty(key)) {
					attr[i] = new Attribute(key, attributes[key]);
					i++;
				}
			}
			
			//going to view
			setData(doc, attr);
		}
	})
};