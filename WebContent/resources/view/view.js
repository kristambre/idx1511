/**
 * 
 */
function setData(doc, attr) {
	$("#attrTable").html(" ");
	$('#id').text(doc.document);
	$('#name').text(doc.name);
	$('#description').text(doc.description);
	$('#createdAt').text(doc.created);
	$('#createdBy').text(doc.createdBy);
	$('#updated').text(doc.updated);
	$('#type').text(doc.type);
	$('#status').text(doc.status);
	$('#catalog').text(doc.catalog);

	var html;
	for(var i in attr){
		html += "	<tr>";
		html += "	<td><span>"+attr[i].name+"</span></td>";
		html += "	<td><span>"+attr[i].value+"</span></td>";
		html += "	</tr>";
	}
	
	$("#attributes").show();
	$("#attrTable").html(html);
}

function closeTable(){
	$("#attributes").hide();
}