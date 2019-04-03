package org.patentprovider.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.patentprovider.model.Patent;

public interface PatentService {
	
	public List<Patent> getAPIDataService(String start, String rows);
	
	public void addPatentService(Patent patent);
	
	public void addPatents(List<Patent> patent);
	
	public List<Patent> getAllPatentRecordsService(Patent patent, String start, String rows);
	
	public List<Patent> searchForPatentService(Patent patent, String rows);

	public HashMap<String, List<Integer>> getPatentYearCountService();

	public HashMap<String, List<Patent>> getPatentDocTypeService();

	public Patent setPatentParams(String applicationType, String documentId, String applicationNumber,
			String documentType, String patentNumber, Date publicationDate, Date documentDate, Date productionDate,
			Date applicationDate, String title, String year, ArrayList<String> assignee);
}
