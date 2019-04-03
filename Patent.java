package org.patentprovider.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Patent {

	private String applicationType;
	private String documentId;
	private String applicationNumber;
	private String documentType;
	private Integer patentNumber;
	private Date publicationDate;
	private Date documentDate;
	private Date productionDate;
	private Date applicationDate;
	private ArrayList<String> applicant;
	private ArrayList<String> inventor;
	private ArrayList<String> assignee;
	private String title;
	private String archiveUrl;
	private String pdfPath;
	private Integer year;
	private long _version_;
	
	public Patent() {
		
	}
	
	public Patent(String applicationType, String documentId, String applicationNumber, String documentType, 
			Integer patentNumber, String title, String archiveUrl, Integer year) {
		this.applicationType = applicationType;
		this.documentId = documentId;
		this.applicationNumber = applicationNumber;
		this.documentType = documentType;
		this.patentNumber = patentNumber;
		this.title = title;
		this.archiveUrl = archiveUrl;
		this.year = year;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public Integer getPatentNumber() {
		return patentNumber;
	}

	public void setPatentNumber(Integer patentNumber) {
		this.patentNumber = patentNumber;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public Date getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}

	public ArrayList<String> getApplicant() {
		if(this.applicant != null) {
			return new ArrayList<String>(this.applicant);
		} else {
			return new ArrayList<String>();
		}
	}

	public void setApplicant(List<String> applicant) {
		this.applicant = new ArrayList<String>(applicant);
	}

	public ArrayList<String> getInventor() {
		if(this.inventor != null) {
			return new ArrayList<String>(this.inventor);
		} else {
			return new ArrayList<String>();
		}
	}

	public void setInventor(List<String> inventor) {
		this.inventor = new ArrayList<String>(inventor);
	}

	public ArrayList<String> getAssignee() {
		if(this.assignee != null) {
			return new ArrayList<String>(this.assignee);
		} else {
			return new ArrayList<String>();
		}
	}

	public void setAssignee(List<String> assignee) {
		this.assignee = new ArrayList<String>(assignee);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArchiveUrl() {
		return archiveUrl;
	}

	public void setArchiveUrl(String archiveUrl) {
		this.archiveUrl = archiveUrl;
	}

	public String getPdfPath() {
		return pdfPath;
	}

	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public long get_version_() {
		return _version_;
	}

	public void set_version_(long _version_) {
		this._version_ = _version_;
	}
	
	@Override
	public String toString() {
		return "Patent [applicationType=" + applicationType + ", documentId=" + documentId + ", applicationNumber="
				+ applicationNumber + ", documentType=" + documentType + ", patentNumber=" + patentNumber
				+ ", publicationDate=" + publicationDate + ", documentDate=" + documentDate + ", productionDate="
				+ productionDate + ", applicationDate=" + applicationDate + ", applicant=" + applicant + ", inventor="
				+ inventor + ", assignee=" + assignee + ", title=" + title + ", archiveUrl=" + archiveUrl + ", pdfPath="
				+ pdfPath + ", year=" + year + ", _VERSION_=" + _version_ + "]";
	}
	
}
