# Patent-Information-System

1. Build a process to download Few hundred Patents from USPTO site in the database. USPTO provides various APIs to perform search. Download the data from: 

	a.	https://developer.uspto.gov/ibd-api-docs/
  
	b.	https://developer.uspto.gov/ibd-api/v1/patent/application?applicationNumber=US14568694

2. Build a Java Rest API which can take various parameters (Title, Application Number, Application Type, Assignee etc.) to fetch the live data. Refresh the Database and Return the data on request of the API.

3. Build a front end application using Angular 6.

	a. Create a Page which will list all the patents in the Grid. Grid should support Pagination, filtering etc.
  
	b. Grid should also show filter charts (Pie chart or Bar chart) on the top. E.g. Pie charts to show Patents status distribution. Clicking on the chart should filter down Grid data.

4. Build an asynchronous Job to Refresh the downloaded patent status every night using USPTO API.
