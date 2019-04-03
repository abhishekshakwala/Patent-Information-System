package org.patentprovider.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.patentprovider.model.Patent;
import org.patentprovider.service.PatentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PatentController {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private PatentService patentService;

	private static final Logger LOG = Logger.getLogger(PatentController.class.getName());

	@RequestMapping("/")
	public String applicationStarter(ModelMap m) {
		m.addAttribute("data", "Welcome to Patent Provider");
		return "jsonTemplate";
	}

	/**
	 * refreshDataJob is a scheduler method which will get executed at midnight on
	 * daily basis to update the database record by getting fresh data from the API.
	 * 
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void refreshDataJob() {
		Patent patent = new Patent();

		List<Patent> patentList = patentService.getAllPatentRecordsService(patent, null, null);
		
		for(Patent p : patentList) {
			patentService.searchForPatentService(p, null);
		}

		LOG.info("Logger Name: " + LOG.getName() + " Executed Successfully.");
	}

	/**
	 * getPatentAPIData calls the API URL for downloading the data from API and
	 * stores in the database
	 * 
	 */
	@RequestMapping(value = "/getPatentAPIData", method = { RequestMethod.GET })
	public String getPatentDataFromAPI(@RequestParam(value = "start", required = false) String start,
			@RequestParam(value = "rows", required = false) String rows, ModelMap m) {
		try {
			start = (start != null) ? start : "0";
			rows = (rows != null) ? rows : "100";
			List<Patent> patentList = patentService.getAPIDataService(start, rows);
			m.addAttribute("data", patentList);
			return "jsonTemplate";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * (Could have just taken the field name and value as the parameter)
	 * getPatentData makes calls to the database with the requested query parameters by
	 * the http GET Request. It gets the data from the database and returns the
	 * results.
	 * @param start variable takes the start record number
	 * @param rows variable takes the number of record from start
	 */
	@RequestMapping(value = "/getAllPatent", method = { RequestMethod.GET })
	public String getPatentData(@RequestParam(value = "applicationType", required = false) String applicationType,
			@RequestParam(value = "documentId", required = false) String documentId,
			@RequestParam(value = "applicationNumber", required = false) String applicationNumber,
			@RequestParam(value = "documentType", required = false) String documentType,
			@RequestParam(value = "patentNumber", required = false) String patentNumber,
			@RequestParam(value = "publicationDate", required = false) Date publicationDate,
			@RequestParam(value = "documentDate", required = false) Date documentDate,
			@RequestParam(value = "productionDate", required = false) Date productionDate,
			@RequestParam(value = "applicationDate", required = false) Date applicationDate,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "assignee", required = false) ArrayList<String> assignee,
			@RequestParam(value = "start", required = false) String start,
			@RequestParam(value = "rows", required = false) String rows, ModelMap m) {
		Patent patent = patentService.setPatentParams(applicationType, documentId, applicationNumber, documentType,
				patentNumber, publicationDate, documentDate, productionDate, applicationDate, title, year, assignee);

		List<Patent> patentList = patentService.getAllPatentRecordsService(patent, start, rows);

		m.addAttribute(patentList);

		return "jsonTemplate";
	}

	/**
	 * searchInPatentAPI calls the API URL with the requested query parameters by the
	 * http GET Request. It gets the data from API stores in the database and
	 * returns the search results.
	 * 
	 */
	@RequestMapping(value = "/searchForPatent", method = { RequestMethod.GET })
	public String searchInPatentAPI(@RequestParam(value = "applicationType", required = false) String applicationType,
			@RequestParam(value = "documentId", required = false) String documentId,
			@RequestParam(value = "applicationNumber", required = false) String applicationNumber,
			@RequestParam(value = "documentType", required = false) String documentType,
			@RequestParam(value = "patentNumber", required = false) String patentNumber,
			@RequestParam(value = "publicationDate", required = false) Date publicationDate,
			@RequestParam(value = "documentDate", required = false) Date documentDate,
			@RequestParam(value = "productionDate", required = false) Date productionDate,
			@RequestParam(value = "applicationDate", required = false) Date applicationDate,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "assignee", required = false) ArrayList<String> assignee,
			@RequestParam(value = "rows", required = false) String rows, ModelMap m) {
		Patent patent = patentService.setPatentParams(applicationType, documentId, applicationNumber, documentType,
				patentNumber, publicationDate, documentDate, productionDate, applicationDate, title, year, assignee);

		rows = (rows != null) ? rows : "100";

		// Retrieving only the new search results from the API & storing it to database
		List<Patent> searchPatentList = patentService.searchForPatentService(patent, rows);

		// Refreshing the Database for results: Retrieving all the patents records in
		// the database
		// after adding the search results into database
		// List<Patent> patentList = patentService.getAllPatentRecordsService();

		m.addAttribute("data", searchPatentList);

		return "jsonTemplate";

	}

	/**
	 * getPatentsYearCount method serves by returning the year and count of patent
	 * records in each year
	 * 
	 */
	@RequestMapping(value = "/getPatentsYearCount", method = { RequestMethod.GET })
	public String getPatentsYearCount(ModelMap m) {
		HashMap<String, List<Integer>> yearCount = patentService.getPatentYearCountService();
		m.addAttribute("data", yearCount);

		return "jsonTemplate";
	}

	/**
	 * getPatentsDocTypeData method serves by returning the each document type data
	 * 
	 */
	@RequestMapping(value = "/getPatentsDocumentType", method = { RequestMethod.GET })
	public String getPatentsDocTypeData(ModelMap m) {
		HashMap<String, List<Patent>> documentTypeMap = patentService.getPatentDocTypeService();
		m.addAttribute("data", documentTypeMap);

		return "jsonTemplate";
	}
}
