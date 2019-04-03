package org.patentprovider.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.patentprovider.model.Patent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class PatentDAOImpl implements PatentDAO{

	private JdbcTemplate jdbcTemplate;
	
	public PatentDAOImpl(DataSource dataSource) {
		// TODO Auto-generated constructor stub
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void insertRecord(Patent patent) {
		// TODO Auto-generated method stub
		
		String sql = "SELECT COUNT(*) AS total FROM patentdatabase.patent WHERE applicationNumber=? AND documentId=?";
		
		int count = jdbcTemplate.queryForObject(sql, new Object[] { patent.getApplicationNumber(), patent.getDocumentId() }, Integer.class);
		
		//Use PreparedStatement 
		if(count > 0) {
			String deletePatent = "DELETE FROM patentdatabase.patent WHERE applicationNumber=? AND documentId=?";
			
			String deleteAssignee = "DELETE FROM patentdatabase.assignee WHERE applicationNumber=? AND documentId=?";
			
			String deleteInventor = "DELETE FROM patentdatabase.inventor WHERE applicationNumber=? AND documentId=?";
			
			String deleteApplicant = "DELETE FROM patentdatabase.applicant WHERE applicationNumber=? AND documentId=?";
			
			jdbcTemplate.update(deletePatent, patent.getApplicationNumber(), patent.getDocumentId());
			
			jdbcTemplate.update(deleteAssignee, patent.getApplicationNumber(), patent.getDocumentId());
			
			jdbcTemplate.update(deleteInventor, patent.getApplicationNumber(), patent.getDocumentId());
			
			jdbcTemplate.update(deleteApplicant, patent.getApplicationNumber(), patent.getDocumentId());
		}
		
		String patentSql = "INSERT INTO patentdatabase.patent (applicationType, documentId, applicationNumber,"
				+ " documentType, patentNumber, publicationDate, documentDate, productionDate, "
				+ "applicationDate, title, archiveUrl, pdfPath, year, version) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		jdbcTemplate.update(patentSql, 
				patent.getApplicationType(), 
				patent.getDocumentId(), 
				patent.getApplicationNumber(),
				patent.getDocumentType(), 
				patent.getPatentNumber(), 
				patent.getPublicationDate(), 
				patent.getDocumentDate(), 
				patent.getProductionDate(),
				patent.getApplicationDate(),
				patent.getTitle(), 
				patent.getArchiveUrl(), 
				patent.getPdfPath(), 
				patent.getYear(), 
				patent.get_version_());
			
		if (patent.getAssignee() != null && !(patent.getAssignee().isEmpty())) {
			String assigneeSql = "INSERT INTO patentdatabase.assignee (assigneeId, applicationNumber, documentId, assigneeName)"
					+ " VALUES (?, ?, ?, ?)";

			for (String assignee : patent.getAssignee()) {
				jdbcTemplate.update(assigneeSql, UUID.randomUUID().toString(), patent.getApplicationNumber(), patent.getDocumentId(), assignee);
			}
		}

		if (patent.getApplicant() != null && !(patent.getApplicant().isEmpty())) {
			String applicantSql = "INSERT INTO patentdatabase.applicant (applicantId, applicationNumber, documentId, applicantName)"
					+ " VALUES (?, ?, ?, ?)";

			for (String applicant : patent.getApplicant()) {
				jdbcTemplate.update(applicantSql, UUID.randomUUID().toString(), patent.getApplicationNumber(), patent.getDocumentId(), applicant);
			}
		}

		if (patent.getInventor() != null && !(patent.getApplicant().isEmpty())) {
			String inventorSql = "INSERT INTO patentdatabase.inventor (inventorId, applicationNumber, documentId, inventorName)"
					+ " VALUES (?, ?, ?, ?)";

			for (String inventor : patent.getInventor()) {
				jdbcTemplate.update(inventorSql, UUID.randomUUID().toString(), patent.getApplicationNumber(), patent.getDocumentId(), inventor);
			}
		}
	}

	@Override
	public List<Patent> listAllRecords(Patent patent, String start, String rows) {
		// TODO Auto-generated method stub
		String limitParam = "";
		
		if(rows != null) {
			limitParam += " LIMIT " + rows;
		}
		
		if(start != null) {
			limitParam += " OFFSET " + start;
		}
		
		String sql1 = "SELECT * FROM patentdatabase.patent WHERE (1=1) ";
		
		String queryParam = "";
		
		StringBuilder innerAssigneeParam = new StringBuilder();
		
		if (patent.getApplicationType() != null && !(patent.getApplicationType().isEmpty())) {
			queryParam += "AND applicationType='" + patent.getApplicationType() + "'";
		}

		if (patent.getDocumentId() != null && !(patent.getDocumentId().isEmpty())) {
			queryParam += "AND documentId='" + patent.getDocumentId() + "'";
		}

		if (patent.getApplicationNumber() != null && !(patent.getApplicationNumber().isEmpty())) {
			queryParam += "AND applicationNumber='" + patent.getApplicationNumber() + "'";
		}

		if (patent.getDocumentType() != null && !(patent.getDocumentType().isEmpty())) {
			queryParam += "AND documentType='" + patent.getDocumentType() + "'";
		}

		if (patent.getPatentNumber() != null) {
			queryParam += "AND patentNumber=" + patent.getPatentNumber();
		}

		if (patent.getPublicationDate() != null) {
			queryParam += "AND publicationDate=" + patent.getPublicationDate();
		}

		if (patent.getDocumentDate() != null) {
			queryParam += "AND documentDate=" + patent.getDocumentDate();
		}

		if (patent.getProductionDate() != null) {
			queryParam += "AND productionDate=" + patent.getProductionDate();
		}

		if (patent.getApplicationDate() != null) {
			queryParam += "AND applicationDate=" + patent.getApplicationDate();
		}

		if (patent.getTitle() != null && !(patent.getTitle().isEmpty())) {
			queryParam += "AND title='" + patent.getTitle() + "'";
		}

		if (patent.getYear() != null) {
			queryParam += "AND year=" + patent.getYear();
		}
		
		if (patent.getAssignee() != null && !(patent.getAssignee().isEmpty())) {
			innerAssigneeParam.append("AND (");
			for(String name : patent.getAssignee()) {
				innerAssigneeParam.append(" assigneeName='" + name + "' OR ");
			}
			innerAssigneeParam.delete(innerAssigneeParam.length()-3, innerAssigneeParam.length());
			innerAssigneeParam.append(")");
		}
		
		String sql = sql1 + queryParam + limitParam;
		
		System.out.println(sql);
		
		List<Patent> patentList = jdbcTemplate.query(sql, new RowMapper<Patent>() {

			@Override
			public Patent mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				Patent patent = new Patent();
				
				patent.setApplicationType(rs.getString("applicationType"));
				patent.setDocumentId(rs.getString("documentId"));
				String documentId = rs.getString("documentId");
				patent.setApplicationNumber(rs.getString("applicationNumber"));
				String applicationNumber = rs.getString("applicationNumber");
				patent.setDocumentType(rs.getString("documentType"));
				patent.setPatentNumber(rs.getInt("patentNumber"));
				patent.setPublicationDate(rs.getDate("publicationDate"));
				patent.setDocumentDate(rs.getDate("documentDate"));
				patent.setProductionDate(rs.getDate("productionDate"));
				patent.setApplicationDate(rs.getDate("applicationDate"));
				String applicantSql = "SELECT applicantName FROM patentdatabase.applicant WHERE applicationNumber='"+ applicationNumber
						+ "' AND documentId='" + documentId + "' ";

				List<String> applicantNameList = jdbcTemplate.query(applicantSql, new RowMapper<String>() {

					@Override
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						// TODO Auto-generated method stub
						return rs.getString("applicantName");
					}
				});
				patent.setApplicant(applicantNameList);
				String inventorSql = "SELECT inventorName FROM patentdatabase.inventor WHERE applicationNumber='"+ applicationNumber
						+ "' AND documentId='" + documentId + "' ";
				List<String> inventorNameList = jdbcTemplate.query(inventorSql, new RowMapper<String>() {

					@Override
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						// TODO Auto-generated method stub
						return rs.getString("inventorName");
					}
				});
				patent.setInventor(inventorNameList);
				String assigneeSql = "SELECT assigneeName FROM patentdatabase.assignee WHERE applicationNumber='"+ applicationNumber
						+ "' AND documentId='" + documentId + "' " + innerAssigneeParam;
				
				List<String> assigneeNameList = jdbcTemplate.query(assigneeSql, new RowMapper<String>() {

					@Override
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						// TODO Auto-generated method stub
						return rs.getString("assigneeName");
					}
				});
				patent.setAssignee(assigneeNameList);
				patent.setTitle(rs.getString("title"));
				patent.setArchiveUrl(rs.getString("archiveUrl"));
				patent.setPdfPath(rs.getString("pdfPath"));
				patent.setYear(rs.getInt("year"));
				patent.set_version_(rs.getLong("version"));
				
				return patent;
			}
		});
		return patentList;
	}

	@Override
	public HashMap<String, List<Integer>> getYearCountDAO() {
		// TODO Auto-generated method stub
		HashMap<String, List<Integer>> countMap = new HashMap<String, List<Integer>>();
		
		String sqlYear = "select year from patentdatabase.patent GROUP BY year ORDER BY year ASC";
		
		String sqlCount = "select count(*) AS count from patentdatabase.patent GROUP BY year ORDER BY year ASC";
		
		List<Integer> yearList = jdbcTemplate.query(sqlYear, new RowMapper<Integer>() {

			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				return rs.getInt("year");
			}
		});
		
		List<Integer> countList = jdbcTemplate.query(sqlCount, new RowMapper<Integer>() {

			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				return rs.getInt("count");
			}
		});
		
		countMap.put("year", yearList);
		countMap.put("count", countList);
		
		return countMap;
	}

	@Override
	public HashMap<String, List<Patent>> getPatentDocTypeDAO() {
		// TODO Auto-generated method stub
		String distinctDocType = "SELECT DISTINCT documentType FROM patentdatabase.patent";
		
		List<String> docTypeList = jdbcTemplate.query(distinctDocType, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				return rs.getString("documentType");
			}
			
		});
		
		HashMap<String, List<Patent>> documentTypeMap = new HashMap<String, List<Patent>>();
		
		for(String docType : docTypeList) {
			String sql = "SELECT * FROM patentdatabase.patent WHERE documentType='" + docType + "'";
			
			System.out.println(sql);
			
			List<Patent> docTypePatent = jdbcTemplate.query(sql, new RowMapper<Patent>() {

				@Override
				public Patent mapRow(ResultSet rs, int rowNum) throws SQLException {
					// TODO Auto-generated method stub
					Patent patent = new Patent();
					
					patent.setApplicationType(rs.getString("applicationType"));
					patent.setDocumentId(rs.getString("documentId"));
					String documentId = rs.getString("documentId");
					patent.setApplicationNumber(rs.getString("applicationNumber"));
					String applicationNumber = rs.getString("applicationNumber");
					patent.setDocumentType(rs.getString("documentType"));
					patent.setPatentNumber(rs.getInt("patentNumber"));
					patent.setPublicationDate(rs.getDate("publicationDate"));
					patent.setDocumentDate(rs.getDate("documentDate"));
					patent.setProductionDate(rs.getDate("productionDate"));
					patent.setApplicationDate(rs.getDate("applicationDate"));
					String applicantSql = "SELECT applicantName FROM patentdatabase.applicant WHERE applicationNumber='"+ applicationNumber
							+ "' AND documentId='" + documentId + "'";
					List<String> applicantNameList = jdbcTemplate.query(applicantSql, new RowMapper<String>() {

						@Override
						public String mapRow(ResultSet rs, int rowNum) throws SQLException {
							// TODO Auto-generated method stub
							return rs.getString("applicantName");
						}
					});
					patent.setApplicant(applicantNameList);
					String inventorSql = "SELECT inventorName FROM patentdatabase.inventor WHERE applicationNumber='"+ applicationNumber
							+ "' AND documentId='" + documentId + "'";
					List<String> inventorNameList = jdbcTemplate.query(inventorSql, new RowMapper<String>() {

						@Override
						public String mapRow(ResultSet rs, int rowNum) throws SQLException {
							// TODO Auto-generated method stub
							return rs.getString("inventorName");
						}
					});
					patent.setInventor(inventorNameList);
					String assigneeSql = "SELECT assigneeName FROM patentdatabase.assignee WHERE applicationNumber='"+ applicationNumber
							+ "' AND documentId='" + documentId + "'";
					List<String> assigneeNameList = jdbcTemplate.query(assigneeSql, new RowMapper<String>() {

						@Override
						public String mapRow(ResultSet rs, int rowNum) throws SQLException {
							// TODO Auto-generated method stub
							return rs.getString("assigneeName");
						}
					});
					patent.setAssignee(assigneeNameList);
					patent.setTitle(rs.getString("title"));
					patent.setArchiveUrl(rs.getString("archiveUrl"));
					patent.setPdfPath(rs.getString("pdfPath"));
					patent.setYear(rs.getInt("year"));
					patent.set_version_(rs.getLong("version"));
					
					return patent;
				}
				
			});
			
			documentTypeMap.put(docType, docTypePatent);
		}
		
		return documentTypeMap;
	}

}
