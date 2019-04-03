package org.patentprovider.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.patentprovider.dao.PatentDAO;
import org.patentprovider.model.Patent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.GZIPContentEncodingFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class PatentServiceImpl implements PatentService {

	public final String STRING_URL = "https://developer.uspto.gov/ibd-api/v1/patent/application?";

	@Autowired
	private PatentDAO patentDAO;

	public List<Patent> getAPIDataService(String start, String rows) {

		MultivaluedMap<String, String> queryParam = new MultivaluedMapImpl();
		queryParam.add("start", start);
		queryParam.add("rows", rows);
		List<Patent> patentAPIDataList = getAPIData(queryParam);

		return patentAPIDataList;
	}

	public List<Patent> getAPIData(MultivaluedMap<String, String> queryParam) {
		try {
			Client client = Client.create();
			client.addFilter(new GZIPContentEncodingFilter());
			WebResource webResource = client.resource(STRING_URL);
			ClientResponse response = webResource.queryParams(queryParam).get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed HTTP Operation:" + response.getStatus());
			}
			String textOutput = response.getEntity(String.class);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(textOutput);
			JsonNode responseNode = rootNode.path("response");
			JsonNode responseDocs = responseNode.path("docs");
			Iterator<JsonNode> elements = responseDocs.elements();
			List<Patent> patentRecords = new ArrayList<Patent>();
			while (elements.hasNext()) {
				JsonNode patentDocs = elements.next();
				Patent patentObj = mapper.treeToValue(patentDocs, Patent.class);
				patentRecords.add(patentObj);
			}
			System.out.println(patentRecords);
			addPatents(patentRecords);
			return patentRecords;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Transactional
	public void addPatentService(Patent patent) {
		patentDAO.insertRecord(patent);
	}

	@Transactional
	public void addPatents(List<Patent> patentList) {
		try {
			for (Patent patent : patentList) {
				addPatentService(patent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Patent> getAllPatentRecordsService(Patent patent, String start, String rows) {
		// TODO Auto-generated method stub
		List<Patent> patentList = patentDAO.listAllRecords(patent, start, rows);
		return patentList;
	}

	@Override
	public List<Patent> searchForPatentService(Patent patent, String rows) {
		// TODO Auto-generated method stub

		MultivaluedMap<String, String> queryParam = new MultivaluedMapImpl();

		if (patent.getApplicationType() != null && !(patent.getApplicationType().isEmpty())) {
			queryParam.add("applicationType", patent.getApplicationType());
		}

		if (patent.getDocumentId() != null && !(patent.getDocumentId().isEmpty())) {
			queryParam.add("documentId", patent.getDocumentId());
		}

		if (patent.getApplicationNumber() != null && !(patent.getApplicationNumber().isEmpty())) {
			queryParam.add("applicationNumber", patent.getApplicationNumber());
		}

		if (patent.getDocumentType() != null && !(patent.getDocumentType().isEmpty())) {
			queryParam.add("documentType", patent.getDocumentType());
		}

		if (patent.getPatentNumber() != null) {
			queryParam.add("patentNumber", patent.getPatentNumber().toString());
		}

		if (patent.getPublicationDate() != null) {
			queryParam.add("publicationDate", patent.getPublicationDate().toString());
		}

		if (patent.getDocumentDate() != null) {
			queryParam.add("documentDate", patent.getDocumentDate().toString());
		}

		if (patent.getProductionDate() != null) {
			queryParam.add("productionDate", patent.getProductionDate().toString());
		}

		if (patent.getApplicationDate() != null) {
			queryParam.add("applicationDate", patent.getApplicationDate().toString());
		}

		if (patent.getTitle() != null && !(patent.getTitle().isEmpty())) {
			queryParam.add("title", patent.getTitle());
		}

		if (patent.getYear() != null) {
			queryParam.add("criteriaSearchText", "year:" + patent.getYear());
		}

		if (patent.getAssignee() != null && !(patent.getAssignee().isEmpty())) {
			queryParam.add("assignee", String.join(", ", patent.getAssignee()));
		}

		queryParam.add("rows", rows);

		List<Patent> searchPatentList = getAPIData(queryParam);

		return searchPatentList;
	}

	@Override
	public HashMap<String, List<Integer>> getPatentYearCountService() {
		// TODO Auto-generated method stub
		HashMap<String, List<Integer>> yearCount = patentDAO.getYearCountDAO();

		return yearCount;
	}

	@Override
	public HashMap<String, List<Patent>> getPatentDocTypeService() {
		// TODO Auto-generated method stub
		HashMap<String, List<Patent>> documentTypeMap = patentDAO.getPatentDocTypeDAO();
		return documentTypeMap;
	}

	public Patent setPatentParams(String applicationType, String documentId, String applicationNumber,
			String documentType, String patentNumber, Date publicationDate, Date documentDate, Date productionDate,
			Date applicationDate, String title, String year, ArrayList<String> assignee) {
		Patent patent = new Patent();

		if (applicationType != null && !(applicationType.isEmpty())) {
			patent.setApplicationType(applicationType);
		}

		if (documentId != null && !(documentId.isEmpty())) {
			patent.setDocumentId(documentId);
		}

		if (applicationNumber != null && !(applicationNumber.isEmpty())) {
			patent.setApplicationNumber(applicationNumber);
		}

		if (documentType != null && !(documentType.isEmpty())) {
			patent.setDocumentType(documentType);
		}

		if (patentNumber != null && !(patentNumber.isEmpty())) {
			patent.setPatentNumber(Integer.parseInt(patentNumber));
		}

		if (publicationDate != null) {
			patent.setPublicationDate(publicationDate);
		}

		if (documentDate != null) {
			patent.setDocumentDate(documentDate);
		}

		if (productionDate != null) {
			patent.setProductionDate(productionDate);
		}

		if (applicationDate != null) {
			patent.setApplicationDate(applicationDate);
		}

		if (title != null && !(title.isEmpty())) {
			patent.setTitle(title);
		}

		if (year != null && !(year.isEmpty())) {
			patent.setYear(Integer.parseInt(year));
		}

		if (assignee != null && !(assignee.isEmpty())) {
			patent.setAssignee(assignee);
		}
		
		return patent;
	}

}
