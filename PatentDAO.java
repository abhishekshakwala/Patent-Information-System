package org.patentprovider.dao;

import java.util.HashMap;
import java.util.List;

import org.patentprovider.model.Patent;

public interface PatentDAO {
	
	public void insertRecord(Patent patent);
	
	public List<Patent> listAllRecords(Patent patent, String start, String rows);

	public HashMap<String, List<Integer>> getYearCountDAO();

	public HashMap<String, List<Patent>> getPatentDocTypeDAO();
}
