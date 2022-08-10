# Auto-generated file based on standard validation CSVW tests from https://w3c.github.io/csvw/tests/manifest-validation.jsonld

Feature: CSVW Validation Tests

	# manifest-validation#test001
	# The simplest possible table without metadata
	Scenario: manifest-validation#test001 Simple table
		Given I have a CSV file called "csvw/test001.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test001.csv"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test001.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test005
	# A table with entity identifiers and references to other entities without metadata
	Scenario: manifest-validation#test005 Identifier references
		Given I have a CSV file called "csvw/test005.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test005.csv"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test005.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test006
	# Records contain two entities with relationships which are duplicated without metadata
	Scenario: manifest-validation#test006 No identifiers
		Given I have a CSV file called "csvw/test006.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test006.csv"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test006.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test007
	# Joined data with identified records without metadata
	Scenario: manifest-validation#test007 Joined table with unique identifiers
		Given I have a CSV file called "csvw/test007.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test007.csv"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test007.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test008
	# One field has comma-separated values without metadata
	Scenario: manifest-validation#test008 Microsyntax - internal field separator
		Given I have a CSV file called "csvw/test008.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test008.csv"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test008.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test009
	# Field with parseable human formatted time without metadata
	Scenario: manifest-validation#test009 Microsyntax - formatted time
		Given I have a CSV file called "csvw/test009.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test009.csv"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test009.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test010
	# Country-codes-and-names example
	Scenario: manifest-validation#test010 Country-codes-and-names example
		Given I have a CSV file called "csvw/test010.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test010.csv"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test010.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test011
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration.
	Scenario: manifest-validation#test011 tree-ops example with metadata
		Given I have a CSV file called "csvw/test011/tree-ops.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test011/tree-ops.csv"
		And I have a file called "csvw/test011/tree-ops.csv-metadata.json" at the url "https://w3c.github.io/csvw/tests/test011/tree-ops.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test011/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test012
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration.
	Scenario: manifest-validation#test012 tree-ops example with directory metadata
		Given I have a CSV file called "csvw/test012/tree-ops.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test012/tree-ops.csv"
		And I have a file called "csvw/test012/csv-metadata.json" at the url "https://w3c.github.io/csvw/tests/test012/csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test012/tree-ops.csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test013
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration.
	Scenario: manifest-validation#test013 tree-ops example from user metadata
		Given I have a CSV file called "csvw/tree-ops.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		And I have a metadata file called "csvw/test013-user-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test013-user-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/tree-ops.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test014
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration.
	Scenario: manifest-validation#test014 tree-ops example with linked metadata
		Given I have a CSV file called "csvw/test014/tree-ops.csv"
		And it has a Link header holding "<linked-metadata.json>; rel="describedby"; type="application/csvm+json""
		And it is stored at the url "https://w3c.github.io/csvw/tests/test014/tree-ops.csv"
		And I have a file called "csvw/test014/linked-metadata.json" at the url "https://w3c.github.io/csvw/tests/test014/linked-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test014/tree-ops.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test014/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test015
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration.
	Scenario: manifest-validation#test015 tree-ops example with user and directory metadata
		Given I have a CSV file called "csvw/test015/tree-ops.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test015/tree-ops.csv"
		And I have a metadata file called "csvw/test015/user-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test015/user-metadata.json"
		And I have a file called "csvw/test015/csv-metadata.json" at the url "https://w3c.github.io/csvw/tests/test015/csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test015/tree-ops.csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test016
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration.
	Scenario: manifest-validation#test016 tree-ops example with linked and directory metadata
		Given I have a CSV file called "csvw/test016/tree-ops.csv"
		And it has a Link header holding "<linked-metadata.json>; rel="describedby"; type="application/csvm+json""
		And it is stored at the url "https://w3c.github.io/csvw/tests/test016/tree-ops.csv"
		And I have a file called "csvw/test016/csv-metadata.json" at the url "https://w3c.github.io/csvw/tests/test016/csv-metadata.json"
		And I have a file called "csvw/test016/linked-metadata.json" at the url "https://w3c.github.io/csvw/tests/test016/linked-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test016/tree-ops.csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test017
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration.
	Scenario: manifest-validation#test017 tree-ops example with file and directory metadata
		Given I have a CSV file called "csvw/test017/tree-ops.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test017/tree-ops.csv"
		And I have a file called "csvw/test017/tree-ops.csv-metadata.json" at the url "https://w3c.github.io/csvw/tests/test017/tree-ops.csv-metadata.json"
		And I have a file called "csvw/test017/csv-metadata.json" at the url "https://w3c.github.io/csvw/tests/test017/csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test018
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration.
	Scenario: manifest-validation#test018 tree-ops example with user, file and directory metadata
		Given I have a CSV file called "csvw/test018/tree-ops.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test018/tree-ops.csv"
		And I have a metadata file called "csvw/test018/user-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test018/user-metadata.json"
		And I have a file called "csvw/test018/tree-ops.csv-metadata.json" at the url "https://w3c.github.io/csvw/tests/test018/tree-ops.csv-metadata.json"
		And I have a file called "csvw/test018/csv-metadata.json" at the url "https://w3c.github.io/csvw/tests/test018/csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test023
	# If `true`, sets the `header row count` flag to 1, and if `false` to 0, unless `headerRowCount` is provided, in which case the value provided for the `header` property is ignored.
	@ignore
	Scenario: manifest-validation#test023 dialect: header=false
		Given I have a CSV file called "csvw/tree-ops.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		And I have a metadata file called "csvw/test023-user-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test023-user-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/tree-ops.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test027
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration.
	Scenario: manifest-validation#test027 tree-ops minimal output
		Given I have a CSV file called "csvw/tree-ops.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		And I have a metadata file called "csvw/test027-user-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test027-user-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/tree-ops.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test028
	# If no metadata is supplied or found, processors MUST use embedded metadata.
	Scenario: manifest-validation#test028 countries.csv example
		Given I have a CSV file called "csvw/countries.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/countries.csv"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/countries.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test029
	# If no metadata is supplied or found, processors MUST use embedded metadata.
	Scenario: manifest-validation#test029 countries.csv minimal
		Given I have a CSV file called "csvw/countries.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/countries.csv"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/countries.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test030
	# countries.json from metadata
	Scenario: manifest-validation#test030 countries.json example
		Given I have a metadata file called "csvw/countries.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/countries.json"
		And I have a file called "csvw/countries.csv" at the url "https://w3c.github.io/csvw/tests/countries.csv"
		And I have a file called "csvw/country_slice.csv" at the url "https://w3c.github.io/csvw/tests/country_slice.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test031
	# countries.json from metadata minimal output
	Scenario: manifest-validation#test031 countries.json example minimal output
		Given I have a metadata file called "csvw/countries.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/countries.json"
		And I have a file called "csvw/countries.csv" at the url "https://w3c.github.io/csvw/tests/countries.csv"
		And I have a file called "csvw/country_slice.csv" at the url "https://w3c.github.io/csvw/tests/country_slice.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test032
	# events-listing example from metadata, virtual columns and multiple subjects per row
	Scenario: manifest-validation#test032 events-listing.csv example
		Given I have a metadata file called "csvw/test032/csv-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test032/csv-metadata.json"
		And I have a file called "csvw/test032/events-listing.csv" at the url "https://w3c.github.io/csvw/tests/test032/events-listing.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test033
	# events-listing example from metadata, virtual columns and multiple subjects per row; minimal output
	Scenario: manifest-validation#test033 events-listing.csv minimal output
		Given I have a metadata file called "csvw/test033/csv-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test033/csv-metadata.json"
		And I have a file called "csvw/test033/events-listing.csv" at the url "https://w3c.github.io/csvw/tests/test033/events-listing.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test034
	# Public Sector Roles example with referenced schemas. Validation fails because organization.csv intentionally contains an invalid reference.
	Scenario: manifest-validation#test034 roles example
		Given I have a metadata file called "csvw/test034/csv-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test034/csv-metadata.json"
		And I have a file called "csvw/test034/senior-roles.csv" at the url "https://w3c.github.io/csvw/tests/test034/senior-roles.csv"
		And I have a file called "csvw/test034/junior-roles.csv" at the url "https://w3c.github.io/csvw/tests/test034/junior-roles.csv"
		And I have a file called "csvw/test034/gov.uk/data/organizations.csv" at the url "https://w3c.github.io/csvw/tests/test034/gov.uk/data/organizations.csv"
		And I have a file called "csvw/test034/gov.uk/data/professions.csv" at the url "https://w3c.github.io/csvw/tests/test034/gov.uk/data/professions.csv"
		And I have a file called "csvw/test034/gov.uk/schema/junior-roles.json" at the url "https://w3c.github.io/csvw/tests/test034/gov.uk/schema/junior-roles.json"
		And I have a file called "csvw/test034/gov.uk/schema/senior-roles.json" at the url "https://w3c.github.io/csvw/tests/test034/gov.uk/schema/senior-roles.json"
		And I have a file called "csvw/test034/gov.uk/schema/organizations.json" at the url "https://w3c.github.io/csvw/tests/test034/gov.uk/schema/organizations.json"
		And I have a file called "csvw/test034/gov.uk/schema/professions.json" at the url "https://w3c.github.io/csvw/tests/test034/gov.uk/schema/professions.json"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test035
	# Public Sector Roles example with referenced schemas; minimal output. Validation fails because organization.csv intentionally contains an invalid reference.
	Scenario: manifest-validation#test035 roles minimal
		Given I have a metadata file called "csvw/test035/csv-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test035/csv-metadata.json"
		And I have a file called "csvw/test035/senior-roles.csv" at the url "https://w3c.github.io/csvw/tests/test035/senior-roles.csv"
		And I have a file called "csvw/test035/junior-roles.csv" at the url "https://w3c.github.io/csvw/tests/test035/junior-roles.csv"
		And I have a file called "csvw/test035/gov.uk/data/organizations.csv" at the url "https://w3c.github.io/csvw/tests/test035/gov.uk/data/organizations.csv"
		And I have a file called "csvw/test035/gov.uk/data/professions.csv" at the url "https://w3c.github.io/csvw/tests/test035/gov.uk/data/professions.csv"
		And I have a file called "csvw/test035/gov.uk/schema/junior-roles.json" at the url "https://w3c.github.io/csvw/tests/test035/gov.uk/schema/junior-roles.json"
		And I have a file called "csvw/test035/gov.uk/schema/senior-roles.json" at the url "https://w3c.github.io/csvw/tests/test035/gov.uk/schema/senior-roles.json"
		And I have a file called "csvw/test035/gov.uk/schema/organizations.json" at the url "https://w3c.github.io/csvw/tests/test035/gov.uk/schema/organizations.json"
		And I have a file called "csvw/test035/gov.uk/schema/professions.json" at the url "https://w3c.github.io/csvw/tests/test035/gov.uk/schema/professions.json"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test036
	# tree-ops extended example
	Scenario: manifest-validation#test036 tree-ops-ext example
		Given I have a CSV file called "csvw/test036/tree-ops-ext.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test036/tree-ops-ext.csv"
		And I have a file called "csvw/test036/tree-ops-ext.csv-metadata.json" at the url "https://w3c.github.io/csvw/tests/test036/tree-ops-ext.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test036/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test037
	# tree-ops extended example; minimal output
	Scenario: manifest-validation#test037 tree-ops-ext minimal
		Given I have a CSV file called "csvw/test037/tree-ops-ext.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test037/tree-ops-ext.csv"
		And I have a file called "csvw/test037/tree-ops-ext.csv-metadata.json" at the url "https://w3c.github.io/csvw/tests/test037/tree-ops-ext.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test037/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test038
	# Setting inherited properties at different levels inherit to cell
	Scenario: manifest-validation#test038 inherited properties propagation
		Given I have a metadata file called "csvw/test038-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test038-metadata.json"
		And I have a file called "csvw/test038.csv" at the url "https://w3c.github.io/csvw/tests/test038.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test039
	# Different combinations of valid inherited properties
	Scenario: manifest-validation#test039 valid inherited properties
		Given I have a metadata file called "csvw/test039-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test039-metadata.json"
		And I have a file called "csvw/test039.csv" at the url "https://w3c.github.io/csvw/tests/test039.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test040
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test040 invalid null
		Given I have a metadata file called "csvw/test040-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test040-metadata.json"
		And I have a file called "csvw/test040.csv" at the url "https://w3c.github.io/csvw/tests/test040.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test041
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test041 invalid lang
		Given I have a metadata file called "csvw/test041-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test041-metadata.json"
		And I have a file called "csvw/test041.csv" at the url "https://w3c.github.io/csvw/tests/test041.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test042
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test042 invalid textDirection
		Given I have a metadata file called "csvw/test042-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test042-metadata.json"
		And I have a file called "csvw/test042.csv" at the url "https://w3c.github.io/csvw/tests/test042.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test043
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test043 invalid separator
		Given I have a metadata file called "csvw/test043-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test043-metadata.json"
		And I have a file called "csvw/test043.csv" at the url "https://w3c.github.io/csvw/tests/test043.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test044
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test044 invalid ordered
		Given I have a metadata file called "csvw/test044-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test044-metadata.json"
		And I have a file called "csvw/test044.csv" at the url "https://w3c.github.io/csvw/tests/test044.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test045
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test045 invalid default
		Given I have a metadata file called "csvw/test045-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test045-metadata.json"
		And I have a file called "csvw/test045.csv" at the url "https://w3c.github.io/csvw/tests/test045.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test046
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test046 invalid dataype
		Given I have a metadata file called "csvw/test046-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test046-metadata.json"
		And I have a file called "csvw/test046.csv" at the url "https://w3c.github.io/csvw/tests/test046.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test047
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test047 invalid aboutUrl
		Given I have a metadata file called "csvw/test047-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test047-metadata.json"
		And I have a file called "csvw/test047.csv" at the url "https://w3c.github.io/csvw/tests/test047.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test048
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test048 invalid propertyUrl
		Given I have a metadata file called "csvw/test048-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test048-metadata.json"
		And I have a file called "csvw/test048.csv" at the url "https://w3c.github.io/csvw/tests/test048.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test049
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test049 invalid valueUrl
		Given I have a metadata file called "csvw/test049-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test049-metadata.json"
		And I have a file called "csvw/test049.csv" at the url "https://w3c.github.io/csvw/tests/test049.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test059
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test059 dialect: invalid commentPrefix
		Given I have a metadata file called "csvw/test059-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test059-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test060
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test060 dialect: invalid delimiter
		Given I have a metadata file called "csvw/test060-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test060-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test061
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test061 dialect: invalid doubleQuote
		Given I have a metadata file called "csvw/test061-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test061-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test062
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test062 dialect: invalid encoding
		Given I have a metadata file called "csvw/test062-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test062-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test063
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test063 dialect: invalid header
		Given I have a metadata file called "csvw/test063-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test063-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test065
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test065 dialect: invalid headerRowCount
		Given I have a metadata file called "csvw/test065-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test065-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test066
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test066 dialect: invalid lineTerminators
		Given I have a metadata file called "csvw/test066-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test066-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test067
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test067 dialect: invalid quoteChar
		Given I have a metadata file called "csvw/test067-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test067-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test068
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test068 dialect: invalid skipBlankRows
		Given I have a metadata file called "csvw/test068-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test068-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test069
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test069 dialect: invalid skipColumns
		Given I have a metadata file called "csvw/test069-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test069-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test070
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test070 dialect: invalid skipInitialSpace
		Given I have a metadata file called "csvw/test070-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test070-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test071
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test071 dialect: invalid skipRows
		Given I have a metadata file called "csvw/test071-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test071-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test072
	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified.
	Scenario: manifest-validation#test072 dialect: invalid trim
		Given I have a metadata file called "csvw/test072-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test072-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test073
	# The value of `@language` MUST be a valid `BCP47` language code
	Scenario: manifest-validation#test073 invalid @language
		Given I have a metadata file called "csvw/test073-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test073-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test074
	# Compliant application MUST raise an error if this array does not contain one or more `table descriptions`.
	Scenario: manifest-validation#test074 empty tables
		Given I have a metadata file called "csvw/test074-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test074-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test075
	# An atomic property that MUST have a single string value that is one of "rtl", "ltr" or "auto".
	Scenario: manifest-validation#test075 invalid tableGroup tableDirection
		Given I have a metadata file called "csvw/test075-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test075-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test076
	# An atomic property that MUST have a single string value that is one of "rtl", "ltr" or "auto".
	Scenario: manifest-validation#test076 invalid table tableDirection
		Given I have a metadata file called "csvw/test076-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test076-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test077
	# It MUST NOT start with `_:`.
	Scenario: manifest-validation#test077 invalid tableGroup @id
		Given I have a metadata file called "csvw/test077-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test077-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test078
	# It MUST NOT start with `_:`.
	Scenario: manifest-validation#test078 invalid table @id
		Given I have a metadata file called "csvw/test078-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test078-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test079
	# It MUST NOT start with `_:`.
	Scenario: manifest-validation#test079 invalid schema @id
		Given I have a metadata file called "csvw/test079-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test079-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test080
	# It MUST NOT start with `_:`.
	Scenario: manifest-validation#test080 invalid column @id
		Given I have a metadata file called "csvw/test080-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test080-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test081
	# It MUST NOT start with `_:`.
	Scenario: manifest-validation#test081 invalid dialect @id
		Given I have a metadata file called "csvw/test081-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test081-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test082
	# It MUST NOT start with `_:`.
	Scenario: manifest-validation#test082 invalid template @id
		Given I have a metadata file called "csvw/test082-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test082-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test083
	# If included `@type` MUST be `TableGroup`
	Scenario: manifest-validation#test083 invalid tableGroup @type
		Given I have a metadata file called "csvw/test083-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test083-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test084
	# If included `@type` MUST be `TableGroup`
	Scenario: manifest-validation#test084 invalid table @type
		Given I have a metadata file called "csvw/test084-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test084-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test085
	# If included `@type` MUST be `TableGroup`
	Scenario: manifest-validation#test085 invalid schema @type
		Given I have a metadata file called "csvw/test085-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test085-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test086
	# If included `@type` MUST be `TableGroup`
	Scenario: manifest-validation#test086 invalid column @type
		Given I have a metadata file called "csvw/test086-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test086-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test087
	# If included `@type` MUST be `Dialect`
	Scenario: manifest-validation#test087 invalid dialect @type
		Given I have a metadata file called "csvw/test087-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test087-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test088
	# If included `@type` MUST be `Template`
	Scenario: manifest-validation#test088 invalid transformation @type
		Given I have a metadata file called "csvw/test088-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test088-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test089
	# The `tables` property is required in a `TableGroup`
	Scenario: manifest-validation#test089 missing tables in TableGroup
		Given I have a metadata file called "csvw/test089-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test089-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test090
	# The `url` property is required in a `Table`
	Scenario: manifest-validation#test090 missing url in Table
		Given I have a metadata file called "csvw/test090-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test090-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test092
	# All compliant applications MUST generate errors and stop processing if a metadata document does not use valid JSON syntax
	Scenario: manifest-validation#test092 invalid JSON
		Given I have a metadata file called "csvw/test092-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test092-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test093
	# Compliant applications MUST ignore properties (aside from _common properties_) which are not defined in this specification and MUST generate a warning when they are encoutered
	Scenario: manifest-validation#test093 undefined properties
		Given I have a metadata file called "csvw/test093-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test093-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test094
	# Any items within an array that are not valid objects of the type expected are ignored
	Scenario: manifest-validation#test094 inconsistent array values: tables
		Given I have a metadata file called "csvw/test094-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test094-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test095
	# Any items within an array that are not valid objects of the type expected are ignored
	Scenario: manifest-validation#test095 inconsistent array values: transformations
		Given I have a metadata file called "csvw/test095-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test095-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test096
	# Any items within an array that are not valid objects of the type expected are ignored
	Scenario: manifest-validation#test096 inconsistent array values: columns
		Given I have a metadata file called "csvw/test096-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test096-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test097
	# Any items within an array that are not valid objects of the type expected are ignored
	Scenario: manifest-validation#test097 inconsistent array values: foreignKeys
		Given I have a metadata file called "csvw/test097-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test097-metadata.json"
		And I have a file called "csvw/countries.csv" at the url "https://w3c.github.io/csvw/tests/countries.csv"
		And I have a file called "csvw/country_slice.csv" at the url "https://w3c.github.io/csvw/tests/country_slice.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test098
	# If the supplied value of an array property is not an array (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been supplied with an empty array. Compliant application MUST raise an error if this array does not contain one or more table descriptions.
	Scenario: manifest-validation#test098 inconsistent array values: tables
		Given I have a metadata file called "csvw/test098-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test098-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test099
	# If the supplied value of an array property is not an array (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been supplied with an empty array
	Scenario: manifest-validation#test099 inconsistent array values: transformations
		Given I have a metadata file called "csvw/test099-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test099-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test100
	# If the supplied value of an array property is not an array (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been supplied with an empty array
	Scenario: manifest-validation#test100 inconsistent array values: columns
		Given I have a metadata file called "csvw/test100-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test100-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test101
	# If the supplied value of an array property is not an array (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been supplied with an empty array
	Scenario: manifest-validation#test101 inconsistent array values: foreignKeys
		Given I have a metadata file called "csvw/test101-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test101-metadata.json"
		And I have a file called "csvw/countries.csv" at the url "https://w3c.github.io/csvw/tests/countries.csv"
		And I have a file called "csvw/country_slice.csv" at the url "https://w3c.github.io/csvw/tests/country_slice.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test102
	# If the supplied value of an array property is not an array (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been supplied with an empty array
	Scenario: manifest-validation#test102 inconsistent link values: @id
		Given I have a metadata file called "csvw/test102-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test102-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test103
	# If the supplied value of an array property is not an array (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been supplied with an empty array
	Scenario: manifest-validation#test103 inconsistent link values: url
		Given I have a metadata file called "csvw/test103-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test103-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test104
	# The referenced description object MUST have a name property
	Scenario: manifest-validation#test104 invalid columnReference
		Given I have a metadata file called "csvw/test104-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test104-metadata.json"
		And I have a file called "csvw/countries.csv" at the url "https://w3c.github.io/csvw/tests/countries.csv"
		And I have a file called "csvw/country_slice.csv" at the url "https://w3c.github.io/csvw/tests/country_slice.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test105
	# The referenced description object MUST have a name property
	Scenario: manifest-validation#test105 invalid primaryKey
		Given I have a metadata file called "csvw/test105-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test105-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test106
	# If the supplied value of an object property is not a string or object (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been specified as an object with no properties.
	Scenario: manifest-validation#test106 invalid dialect
		Given I have a metadata file called "csvw/test106-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test106-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test107
	# If the supplied value of an object property is not a string or object (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been specified as an object with no properties.
	Scenario: manifest-validation#test107 invalid tableSchema
		Given I have a metadata file called "csvw/test107-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test107-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test108
	# If the supplied value of an object property is not a string or object (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been specified as an object with no properties.
	Scenario: manifest-validation#test108 invalid reference
		Given I have a metadata file called "csvw/test108-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test108-metadata.json"
		And I have a file called "csvw/countries.csv" at the url "https://w3c.github.io/csvw/tests/countries.csv"
		And I have a file called "csvw/country_slice.csv" at the url "https://w3c.github.io/csvw/tests/country_slice.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test109
	# Natural Language properties may be objects whose properties MUST be language codes as defined by [BCP47] and whose values are either strings or arrays, providing natural language strings in that language. Validation fails because without a title, the metadata is incompatible with the CSV, which isn't a problem when not validating.
	Scenario: manifest-validation#test109 titles with invalid language
		Given I have a metadata file called "csvw/test109-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test109-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test110
	# Natural Language properties may be objects whose properties MUST be language codes as defined by [BCP47] and whose values are either strings or arrays, providing natural language strings in that language
	Scenario: manifest-validation#test110 titles with non-string values
		Given I have a metadata file called "csvw/test110-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test110-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test111
	# If the supplied value of a natural language property is not a string, array or object (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been specified as an empty array. Validation fails because without a title, the metadata is incompatible with the CSV, which isn't a problem when not validating.
	Scenario: manifest-validation#test111 titles with invalid value
		Given I have a metadata file called "csvw/test111-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test111-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test112
	# If the supplied value is an array, any items in that array that are not strings MUST be ignored
	Scenario: manifest-validation#test112 titles with non-string array values
		Given I have a metadata file called "csvw/test112-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test112-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test113
	# Atomic properties: Processors MUST issue a warning if a property is set to an invalid value type
	Scenario: manifest-validation#test113 invalid suppressOutput
		Given I have a metadata file called "csvw/test113-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test113-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test114
	# Atomic properties: Processors MUST issue a warning if a property is set to an invalid value type
	Scenario: manifest-validation#test114 invalid name
		Given I have a metadata file called "csvw/test114-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test114-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test115
	# Atomic properties: Processors MUST issue a warning if a property is set to an invalid value type
	Scenario: manifest-validation#test115 invalid virtual
		Given I have a metadata file called "csvw/test115-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test115-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test116
	# processors MUST attempt to locate a metadata documents through site-wide configuration.
	Scenario: manifest-validation#test116 file-metadata with query component not found
		Given I have a CSV file called "csvw/test116.csv?query"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test116.csv?query"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test116.csv?query-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test117
	# If the metadata file found at this location does not explicitly include a reference to the requested tabular data file then it MUST be ignored.
	Scenario: manifest-validation#test117 file-metadata not referencing file
		Given I have a CSV file called "csvw/test117.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test117.csv"
		And I have a file called "csvw/test117.csv-metadata.json" at the url "https://w3c.github.io/csvw/tests/test117.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test118
	# processors MUST attempt to locate a metadata documents through site-wide configuration. component.
	Scenario: manifest-validation#test118 directory-metadata with query component
		Given I have a CSV file called "csvw/test118/action.csv?query"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test118/action.csv?query"
		And I have a file called "csvw/test118/csv-metadata.json" at the url "https://w3c.github.io/csvw/tests/test118/csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test118/action.csv?query-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test119
	# If the metadata file found at this location does not explicitly include a reference to the requested tabular data file then it MUST be ignored.
	Scenario: manifest-validation#test119 directory-metadata not referencing file
		Given I have a CSV file called "csvw/test119/action.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test119/action.csv"
		And I have a file called "csvw/test119/csv-metadata.json" at the url "https://w3c.github.io/csvw/tests/test119/csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test119/action.csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test120
	# If the metadata file found at this location does not explicitly include a reference to the requested tabular data file then it MUST be ignored.
	@ignore
	Scenario: manifest-validation#test120 link-metadata not referencing file
		Given I have a CSV file called "csvw/test120.csv"
		And it has a Link header holding "<test120-linked-metadata.json>; rel="describedby"; type="application/csvm+json""
		And it is stored at the url "https://w3c.github.io/csvw/tests/test120.csv"
		And I have a file called "csvw/test120-linked-metadata.json" at the url "https://w3c.github.io/csvw/tests/test120-linked-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test120.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test121
	# User-specified metadata does not need to reference the starting CSV
	Scenario: manifest-validation#test121 user-metadata not referencing file
		Given I have a CSV file called "csvw/test121.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test121.csv"
		And I have a metadata file called "csvw/test121-user-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test121-user-metadata.json"
		And I have a file called "csvw/test121-ref.csv" at the url "https://w3c.github.io/csvw/tests/test121-ref.csv"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test121.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test122
	# If the metadata file found at this location does not explicitly include a reference to the requested tabular data file then it MUST be ignored.
	@ignore
	Scenario: manifest-validation#test122 link-metadata not describing file uses file-metadata
		Given I have a CSV file called "csvw/test122.csv"
		And it has a Link header holding "<test122-linked-metadata.json>; rel="describedby"; type="application/csvm+json""
		And it is stored at the url "https://w3c.github.io/csvw/tests/test122.csv"
		And I have a file called "csvw/test122.csv-metadata.json" at the url "https://w3c.github.io/csvw/tests/test122.csv-metadata.json"
		And I have a file called "csvw/test122-linked-metadata.json" at the url "https://w3c.github.io/csvw/tests/test122-linked-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test123
	# If the metadata file found at this location does not explicitly include a reference to the requested tabular data file then it MUST be ignored.
	Scenario: manifest-validation#test123 file-metadata not describing file uses directory-metadata
		Given I have a CSV file called "csvw/test123/action.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test123/action.csv"
		And I have a file called "csvw/test123/action.csv-metadata.json" at the url "https://w3c.github.io/csvw/tests/test123/action.csv-metadata.json"
		And I have a file called "csvw/test123/csv-metadata.json" at the url "https://w3c.github.io/csvw/tests/test123/csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test124
	# If not validating, and one schema has a name property but not a titles property, and the other has a titles property but not a name property.
	Scenario: manifest-validation#test124 metadata with columns not matching csv titles
		Given I have a CSV file called "csvw/tree-ops.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		And I have a metadata file called "csvw/test124-user-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test124-user-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/tree-ops.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test125
	# If the column required annotation is true, add an error to the list of errors for the cell.
	Scenario: manifest-validation#test125 required column with empty cell
		Given I have a metadata file called "csvw/test125-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test125-metadata.json"
		And I have a file called "csvw/test125.csv" at the url "https://w3c.github.io/csvw/tests/test125.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test126
	# if the string is the same as any one of the values of the column null annotation, then the resulting value is null. If the column separator annotation is null and the column required annotation is true, add an error to the list of errors for the cell.
	Scenario: manifest-validation#test126 required column with cell matching null
		Given I have a metadata file called "csvw/test126-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test126-metadata.json"
		And I have a file called "csvw/test126.csv" at the url "https://w3c.github.io/csvw/tests/test126.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test127
	# if TM is not compatible with EM validators MUST raise an error, other processors MUST generate a warning and continue processing
	Scenario: manifest-validation#test127 incompatible table
		Given I have a metadata file called "csvw/test127-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test127-metadata.json"
		And I have a file called "csvw/test127.csv" at the url "https://w3c.github.io/csvw/tests/test127.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test128
	# The name properties of the column descriptions MUST be unique within a given table description.
	Scenario: manifest-validation#test128 duplicate column names
		Given I have a metadata file called "csvw/test128-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test128-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test129
	# This (name) MUST be a string and this property has no default value, which means it MUST be ignored if the supplied value is not a string.
	Scenario: manifest-validation#test129 columnn name as integer
		Given I have a metadata file called "csvw/test129-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test129-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test130
	# column names are restricted as defined in Variables in [URI-TEMPLATE]
	Scenario: manifest-validation#test130 invalid column name
		Given I have a metadata file called "csvw/test130-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test130-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test131
	# column names are restricted ... names beginning with '_' are reserved by this specification and MUST NOT be used within metadata documents.
	Scenario: manifest-validation#test131 invalid column name
		Given I have a metadata file called "csvw/test131-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test131-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test132
	# If there is no name property defined on this column, the first titles value having the same language tag as default language, or und or if no default language is specified, becomes the name annotation for the described column. This annotation MUST be percent-encoded as necessary to conform to the syntactic requirements defined in [RFC3986]
	Scenario: manifest-validation#test132 name annotation from title percent encoded
		Given I have a metadata file called "csvw/test132-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test132-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test133
	# If present, a virtual column MUST appear after all other non-virtual column definitions.
	Scenario: manifest-validation#test133 virtual before non-virtual
		Given I have a metadata file called "csvw/test133-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test133-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test134
	# A metadata document MUST NOT add a new context
	Scenario: manifest-validation#test134 context in common property
		Given I have a metadata file called "csvw/test134-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test134-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test135
	# Values MUST NOT use list objects or set objects.
	Scenario: manifest-validation#test135 @list value
		Given I have a metadata file called "csvw/test135-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test135-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test136
	# Values MUST NOT use list objects or set objects.
	Scenario: manifest-validation#test136 @set value
		Given I have a metadata file called "csvw/test136-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test136-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test137
	# The value of any @id or @type contained within a metadata document MUST NOT be a blank node.
	Scenario: manifest-validation#test137 @type out of range (as datatype)
		Given I have a metadata file called "csvw/test137-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test137-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test138
	# The value of any @id or @type contained within a metadata document MUST NOT be a blank node.
	Scenario: manifest-validation#test138 @type out of range (as node type)
		Given I have a metadata file called "csvw/test138-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test138-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test139
	# The value of any member of @type MUST be either a term defined in [csvw-context], a prefixed name where the prefix is a term defined in [csvw-context], or an absolute URL.
	Scenario: manifest-validation#test139 @type out of range (as node type) - string
		Given I have a metadata file called "csvw/test139-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test139-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test140
	# The value of any member of @type MUST be either a term defined in [csvw-context], a prefixed name where the prefix is a term defined in [csvw-context], or an absolute URL.
	Scenario: manifest-validation#test140 @type out of range (as node type) - integer
		Given I have a metadata file called "csvw/test140-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test140-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test141
	# The value of any @id or @type contained within a metadata document MUST NOT be a blank node.
	Scenario: manifest-validation#test141 @id out of range (as node type) - bnode
		Given I have a metadata file called "csvw/test141-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test141-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test142
	# If a @value property is used on an object, that object MUST NOT have any other properties aside from either @type or @language, and MUST NOT have both @type and @language as properties. The value of the @value property MUST be a string, number, or boolean value.
	Scenario: manifest-validation#test142 @value with @language and @type
		Given I have a metadata file called "csvw/test142-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test142-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test143
	# If a @value property is used on an object, that object MUST NOT have any other properties aside from either @type or @language, and MUST NOT have both @type and @language as properties. The value of the @value property MUST be a string, number, or boolean value.
	Scenario: manifest-validation#test143 @value with extra properties
		Given I have a metadata file called "csvw/test143-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test143-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test144
	# A @language property MUST NOT be used on an object unless it also has a @value property.
	Scenario: manifest-validation#test144 @language outside of @value
		Given I have a metadata file called "csvw/test144-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test144-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test145
	# If a @language property is used, it MUST have a string value that adheres to the syntax defined in [BCP47], or be null.
	Scenario: manifest-validation#test145 @value with invalid @language
		Given I have a metadata file called "csvw/test145-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test145-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test146
	# Aside from @value, @type, @language, and @id, the properties used on an object MUST NOT start with @.
	Scenario: manifest-validation#test146 Invalid faux-keyword
		Given I have a metadata file called "csvw/test146-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test146-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test147
	# If there is a non-empty case-sensitive intersection between the titles values, where matches MUST have a matching language; `und` matches any language, and languages match if they are equal when truncated, as defined in [BCP47], to the length of the shortest language tag.
	Scenario: manifest-validation#test147 title incompatible with title on case
		Given I have a metadata file called "csvw/test147-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test147-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test148
	# If there is a non-empty case-sensitive intersection between the titles values, where matches MUST have a matching language; `und` matches any language, and languages match if they are equal when truncated, as defined in [BCP47], to the length of the shortest language tag.
	Scenario: manifest-validation#test148 title incompatible with title on language
		Given I have a metadata file called "csvw/test148-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test148-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test149
	# If there is a non-empty case-sensitive intersection between the titles values, where matches MUST have a matching language; `und` matches any language, and languages match if they are equal when truncated, as defined in [BCP47], to the length of the shortest language tag.
	Scenario: manifest-validation#test149 title compatible with title on less specific language
		Given I have a metadata file called "csvw/test149-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test149-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test150
	# If the value of this property is a string, it MUST be one of the built-in datatypes defined in section 5.11.1 Built-in Datatypes or an absolute URL
	Scenario: manifest-validation#test150 non-builtin datatype (datatype value)
		Given I have a metadata file called "csvw/test150-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test150-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test151
	# If the value of this property is a string, it MUST be one of the built-in datatypes
	Scenario: manifest-validation#test151 non-builtin datatype (base value)
		Given I have a metadata file called "csvw/test151-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test151-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test152
	# If the datatype base is not numeric, boolean, a date/time type, or a duration type, the datatype format annotation provides a regular expression for the string values
	Scenario: manifest-validation#test152 string format (valid combinations)
		Given I have a metadata file called "csvw/test152-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test152-metadata.json"
		And I have a file called "csvw/test152.csv" at the url "https://w3c.github.io/csvw/tests/test152.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test153
	# If the datatype base is not numeric, boolean, a date/time type, or a duration type, the datatype format annotation provides a regular expression for the string values
	Scenario: manifest-validation#test153 string format (bad format string)
		Given I have a metadata file called "csvw/test153-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test153-metadata.json"
		And I have a file called "csvw/test153.csv" at the url "https://w3c.github.io/csvw/tests/test153.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test154
	# If the datatype base is not numeric, boolean, a date/time type, or a duration type, the datatype format annotation provides a regular expression for the string values
	Scenario: manifest-validation#test154 string format (value not matching format)
		Given I have a metadata file called "csvw/test154-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test154-metadata.json"
		And I have a file called "csvw/test154.csv" at the url "https://w3c.github.io/csvw/tests/test154.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test155
	# If the datatype format annotation is a single string, this is interpreted in the same way as if it were an object with a pattern property whose value is that string
	@ignore
	Scenario: manifest-validation#test155 number format (valid combinations)
		Given I have a metadata file called "csvw/test155-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test155-metadata.json"
		And I have a file called "csvw/test155.csv" at the url "https://w3c.github.io/csvw/tests/test155.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test156
	# If the datatype format annotation is a single string, this is interpreted in the same way as if it were an object with a pattern property whose value is that string
	Scenario: manifest-validation#test156 number format (bad format string)
		Given I have a metadata file called "csvw/test156-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test156-metadata.json"
		And I have a file called "csvw/test156.csv" at the url "https://w3c.github.io/csvw/tests/test156.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test157
	# If the datatype format annotation is a single string, this is interpreted in the same way as if it were an object with a pattern property whose value is that string
	Scenario: manifest-validation#test157 number format (value not matching format)
		Given I have a metadata file called "csvw/test157-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test157-metadata.json"
		And I have a file called "csvw/test157.csv" at the url "https://w3c.github.io/csvw/tests/test157.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test158
	# Numeric dataype with object format
	@ignore
	Scenario: manifest-validation#test158 number format (valid combinations)
		Given I have a metadata file called "csvw/test158-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test158-metadata.json"
		And I have a file called "csvw/test158.csv" at the url "https://w3c.github.io/csvw/tests/test158.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test159
	# If the datatype format annotation is a single string, this is interpreted in the same way as if it were an object with a pattern property whose value is that string
	Scenario: manifest-validation#test159 number format (bad pattern format string)
		Given I have a metadata file called "csvw/test159-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test159-metadata.json"
		And I have a file called "csvw/test159.csv" at the url "https://w3c.github.io/csvw/tests/test159.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test160
	# Implementations MUST add a validation error to the errors annotation for the cell if the string being parsed
	Scenario: manifest-validation#test160 number format (not matching values with pattern)
		Given I have a metadata file called "csvw/test160-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test160-metadata.json"
		And I have a file called "csvw/test160.csv" at the url "https://w3c.github.io/csvw/tests/test160.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test161
	# Implementations MUST add a validation error to the errors annotation for the cell if the string being parsed
	Scenario: manifest-validation#test161 number format (not matching values without pattern)
		Given I have a metadata file called "csvw/test161-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test161-metadata.json"
		And I have a file called "csvw/test161.csv" at the url "https://w3c.github.io/csvw/tests/test161.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test162
	# Implementations MUST add a validation error to the errors annotation for the cell if the string being parsed contains two consecutive groupChar strings
	Scenario: manifest-validation#test162 numeric format (consecutive groupChar)
		Given I have a metadata file called "csvw/test162-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test162-metadata.json"
		And I have a file called "csvw/test162.csv" at the url "https://w3c.github.io/csvw/tests/test162.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test163
	# Implementations MUST add a validation error to the errors annotation for the cell if the string being parsed contains the decimalChar, if the datatype base is integer or one of its sub-values
	Scenario: manifest-validation#test163 integer datatype with decimalChar
		Given I have a metadata file called "csvw/test163-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test163-metadata.json"
		And I have a file called "csvw/test163.csv" at the url "https://w3c.github.io/csvw/tests/test163.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test164
	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, if the datatype base is decimal or one of its sub-values
	Scenario: manifest-validation#test164 decimal datatype with exponent
		Given I have a metadata file called "csvw/test164-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test164-metadata.json"
		And I have a file called "csvw/test164.csv" at the url "https://w3c.github.io/csvw/tests/test164.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test165
	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, is one of the special values NaN, INF, or -INF, if the datatype base is decimal or one of its sub-values
	Scenario: manifest-validation#test165 decimal type with NaN
		Given I have a metadata file called "csvw/test165-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test165-metadata.json"
		And I have a file called "csvw/test165.csv" at the url "https://w3c.github.io/csvw/tests/test165.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test166
	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, is one of the special values NaN, INF, or -INF, if the datatype base is decimal or one of its sub-values
	Scenario: manifest-validation#test166 decimal type with INF
		Given I have a metadata file called "csvw/test166-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test166-metadata.json"
		And I have a file called "csvw/test166.csv" at the url "https://w3c.github.io/csvw/tests/test166.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test167
	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, is one of the special values NaN, INF, or -INF, if the datatype base is decimal or one of its sub-values
	Scenario: manifest-validation#test167 decimal type with -INF
		Given I have a metadata file called "csvw/test167-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test167-metadata.json"
		And I have a file called "csvw/test167.csv" at the url "https://w3c.github.io/csvw/tests/test167.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test168
	# When parsing the string value of a cell against this format specification, implementations MUST recognise and parse numbers
	Scenario: manifest-validation#test168 decimal with implicit groupChar
		Given I have a metadata file called "csvw/test168-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test168-metadata.json"
		And I have a file called "csvw/test168.csv" at the url "https://w3c.github.io/csvw/tests/test168.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test169
	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above
	Scenario: manifest-validation#test169 invalid decimal
		Given I have a metadata file called "csvw/test169-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test169-metadata.json"
		And I have a file called "csvw/test169.csv" at the url "https://w3c.github.io/csvw/tests/test169.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test170
	# Implementations MUST use the sign, exponent, percent, and per-mille signs when parsing the string value of a cell to provide the value of the cell
	Scenario: manifest-validation#test170 decimal with percent
		Given I have a metadata file called "csvw/test170-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test170-metadata.json"
		And I have a file called "csvw/test170.csv" at the url "https://w3c.github.io/csvw/tests/test170.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test171
	# Implementations MUST use the sign, exponent, percent, and per-mille signs when parsing the string value of a cell to provide the value of the cell
	Scenario: manifest-validation#test171 decimal with per-mille
		Given I have a metadata file called "csvw/test171-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test171-metadata.json"
		And I have a file called "csvw/test171.csv" at the url "https://w3c.github.io/csvw/tests/test171.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test172
	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above
	Scenario: manifest-validation#test172 invalid byte
		Given I have a metadata file called "csvw/test172-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test172-metadata.json"
		And I have a file called "csvw/test172.csv" at the url "https://w3c.github.io/csvw/tests/test172.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test173
	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above
	Scenario: manifest-validation#test173 invald unsignedLong
		Given I have a metadata file called "csvw/test173-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test173-metadata.json"
		And I have a file called "csvw/test173.csv" at the url "https://w3c.github.io/csvw/tests/test173.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test174
	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above
	Scenario: manifest-validation#test174 invalid unsignedShort
		Given I have a metadata file called "csvw/test174-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test174-metadata.json"
		And I have a file called "csvw/test174.csv" at the url "https://w3c.github.io/csvw/tests/test174.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test175
	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above
	Scenario: manifest-validation#test175 invalid unsignedByte
		Given I have a metadata file called "csvw/test175-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test175-metadata.json"
		And I have a file called "csvw/test175.csv" at the url "https://w3c.github.io/csvw/tests/test175.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test176
	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above
	Scenario: manifest-validation#test176 invalid positiveInteger
		Given I have a metadata file called "csvw/test176-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test176-metadata.json"
		And I have a file called "csvw/test176.csv" at the url "https://w3c.github.io/csvw/tests/test176.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test177
	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above
	Scenario: manifest-validation#test177 invalid negativeInteger
		Given I have a metadata file called "csvw/test177-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test177-metadata.json"
		And I have a file called "csvw/test177.csv" at the url "https://w3c.github.io/csvw/tests/test177.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test178
	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above
	Scenario: manifest-validation#test178 invalid nonPositiveInteger
		Given I have a metadata file called "csvw/test178-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test178-metadata.json"
		And I have a file called "csvw/test178.csv" at the url "https://w3c.github.io/csvw/tests/test178.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test179
	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above
	Scenario: manifest-validation#test179 invalid nonNegativeInteger
		Given I have a metadata file called "csvw/test179-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test179-metadata.json"
		And I have a file called "csvw/test179.csv" at the url "https://w3c.github.io/csvw/tests/test179.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test180
	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above
	Scenario: manifest-validation#test180 invalid double
		Given I have a metadata file called "csvw/test180-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test180-metadata.json"
		And I have a file called "csvw/test180.csv" at the url "https://w3c.github.io/csvw/tests/test180.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test181
	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above
	Scenario: manifest-validation#test181 invalid number
		Given I have a metadata file called "csvw/test181-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test181-metadata.json"
		And I have a file called "csvw/test181.csv" at the url "https://w3c.github.io/csvw/tests/test181.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test182
	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above
	Scenario: manifest-validation#test182 invalid float
		Given I have a metadata file called "csvw/test182-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test182-metadata.json"
		And I have a file called "csvw/test182.csv" at the url "https://w3c.github.io/csvw/tests/test182.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test183
	# If the datatype base for a cell is boolean, the datatype format annotation provides the true and false values expected, separated by `|`.
	Scenario: manifest-validation#test183 boolean format (valid combinations)
		Given I have a metadata file called "csvw/test183-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test183-metadata.json"
		And I have a file called "csvw/test183.csv" at the url "https://w3c.github.io/csvw/tests/test183.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test184
	# If the datatype base for a cell is boolean, the datatype format annotation provides the true and false values expected, separated by `|`.
	Scenario: manifest-validation#test184 boolean format (bad format string)
		Given I have a metadata file called "csvw/test184-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test184-metadata.json"
		And I have a file called "csvw/test184.csv" at the url "https://w3c.github.io/csvw/tests/test184.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test185
	# If the datatype base for a cell is boolean, the datatype format annotation provides the true and false values expected, separated by `|`.
	Scenario: manifest-validation#test185 boolean format (value not matching format)
		Given I have a metadata file called "csvw/test185-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test185-metadata.json"
		And I have a file called "csvw/test185.csv" at the url "https://w3c.github.io/csvw/tests/test185.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test186
	# Implementations MUST add a validation error to the errors annotation for the cell if the string being parsed
	Scenario: manifest-validation#test186 boolean format (not matching datatype)
		Given I have a metadata file called "csvw/test186-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test186-metadata.json"
		And I have a file called "csvw/test186.csv" at the url "https://w3c.github.io/csvw/tests/test186.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test187
	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification.
	Scenario: manifest-validation#test187 date format (valid native combinations)
		Given I have a metadata file called "csvw/test187-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test187-metadata.json"
		And I have a file called "csvw/test187.csv" at the url "https://w3c.github.io/csvw/tests/test187.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test188
	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification.
	Scenario: manifest-validation#test188 date format (valid date combinations with formats)
		Given I have a metadata file called "csvw/test188-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test188-metadata.json"
		And I have a file called "csvw/test188.csv" at the url "https://w3c.github.io/csvw/tests/test188.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test189
	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification.
	Scenario: manifest-validation#test189 date format (valid time combinations with formats)
		Given I have a metadata file called "csvw/test189-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test189-metadata.json"
		And I have a file called "csvw/test189.csv" at the url "https://w3c.github.io/csvw/tests/test189.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test190
	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification.
	Scenario: manifest-validation#test190 date format (valid dateTime combinations with formats)
		Given I have a metadata file called "csvw/test190-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test190-metadata.json"
		And I have a file called "csvw/test190.csv" at the url "https://w3c.github.io/csvw/tests/test190.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test191
	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification.
	Scenario: manifest-validation#test191 date format (bad format string)
		Given I have a metadata file called "csvw/test191-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test191-metadata.json"
		And I have a file called "csvw/test191.csv" at the url "https://w3c.github.io/csvw/tests/test191.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test192
	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification.
	Scenario: manifest-validation#test192 date format (value not matching format)
		Given I have a metadata file called "csvw/test192-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test192-metadata.json"
		And I have a file called "csvw/test192.csv" at the url "https://w3c.github.io/csvw/tests/test192.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test193
	# If the datatype base is a duration type, the datatype format annotation provides a regular expression for the string values
	Scenario: manifest-validation#test193 duration format (valid combinations)
		Given I have a metadata file called "csvw/test193-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test193-metadata.json"
		And I have a file called "csvw/test193.csv" at the url "https://w3c.github.io/csvw/tests/test193.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test194
	# If the datatype base is a duration type, the datatype format annotation provides a regular expression for the string values
	Scenario: manifest-validation#test194 duration format (value not matching format)
		Given I have a metadata file called "csvw/test194-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test194-metadata.json"
		And I have a file called "csvw/test194.csv" at the url "https://w3c.github.io/csvw/tests/test194.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test195
	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell.
	Scenario: manifest-validation#test195 values with matching length
		Given I have a metadata file called "csvw/test195-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test195-metadata.json"
		And I have a file called "csvw/test195.csv" at the url "https://w3c.github.io/csvw/tests/test195.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test196
	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell.
	Scenario: manifest-validation#test196 values with wrong length
		Given I have a metadata file called "csvw/test196-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test196-metadata.json"
		And I have a file called "csvw/test196.csv" at the url "https://w3c.github.io/csvw/tests/test196.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test197
	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell.
	Scenario: manifest-validation#test197 values with wrong maxLength
		Given I have a metadata file called "csvw/test197-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test197-metadata.json"
		And I have a file called "csvw/test197.csv" at the url "https://w3c.github.io/csvw/tests/test197.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test198
	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell.
	Scenario: manifest-validation#test198 values with wrong minLength
		Given I have a metadata file called "csvw/test198-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test198-metadata.json"
		And I have a file called "csvw/test198.csv" at the url "https://w3c.github.io/csvw/tests/test198.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test199
	# Applications MUST raise an error if both length and minLength are specified and length is less than minLength. 
	Scenario: manifest-validation#test199 length less than minLength
		Given I have a metadata file called "csvw/test199-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test199-metadata.json"
		And I have a file called "csvw/test199.csv" at the url "https://w3c.github.io/csvw/tests/test199.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test200
	# Applications MUST raise an error if both length and maxLength are specified and length is greater than maxLength. 
	Scenario: manifest-validation#test200 length > maxLength
		Given I have a metadata file called "csvw/test200-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test200-metadata.json"
		And I have a file called "csvw/test200.csv" at the url "https://w3c.github.io/csvw/tests/test200.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test201
	# Applications MUST raise an error if length, maxLength, or minLength are specified and the base datatype is not string or one of its subtypes, or a binary type.
	Scenario: manifest-validation#test201 length on date
		Given I have a metadata file called "csvw/test201-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test201-metadata.json"
		And I have a file called "csvw/test201.csv" at the url "https://w3c.github.io/csvw/tests/test201.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test202
	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell.
	Scenario: manifest-validation#test202 float matching constraints
		Given I have a metadata file called "csvw/test202-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test202-metadata.json"
		And I have a file called "csvw/test202.csv" at the url "https://w3c.github.io/csvw/tests/test202.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test203
	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell.
	Scenario: manifest-validation#test203 float value constraint not matching minimum
		Given I have a metadata file called "csvw/test203-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test203-metadata.json"
		And I have a file called "csvw/test203.csv" at the url "https://w3c.github.io/csvw/tests/test203.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test204
	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell.
	Scenario: manifest-validation#test204 float value constraint not matching maximum
		Given I have a metadata file called "csvw/test204-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test204-metadata.json"
		And I have a file called "csvw/test204.csv" at the url "https://w3c.github.io/csvw/tests/test204.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test205
	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell.
	Scenario: manifest-validation#test205 float value constraint not matching minInclusive
		Given I have a metadata file called "csvw/test205-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test205-metadata.json"
		And I have a file called "csvw/test205.csv" at the url "https://w3c.github.io/csvw/tests/test205.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test206
	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell.
	Scenario: manifest-validation#test206 float value constraint not matching minExclusive
		Given I have a metadata file called "csvw/test206-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test206-metadata.json"
		And I have a file called "csvw/test206.csv" at the url "https://w3c.github.io/csvw/tests/test206.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test207
	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell.
	Scenario: manifest-validation#test207 float value constraint not matching maxInclusive
		Given I have a metadata file called "csvw/test207-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test207-metadata.json"
		And I have a file called "csvw/test207.csv" at the url "https://w3c.github.io/csvw/tests/test207.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test208
	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell.
	Scenario: manifest-validation#test208 float value constraint not matching maxExclusive
		Given I have a metadata file called "csvw/test208-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test208-metadata.json"
		And I have a file called "csvw/test208.csv" at the url "https://w3c.github.io/csvw/tests/test208.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test209
	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell.
	Scenario: manifest-validation#test209 date matching constraints
		Given I have a metadata file called "csvw/test209-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test209-metadata.json"
		And I have a file called "csvw/test209.csv" at the url "https://w3c.github.io/csvw/tests/test209.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test210
	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell.
	Scenario: manifest-validation#test210 date value constraint not matching minimum
		Given I have a metadata file called "csvw/test210-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test210-metadata.json"
		And I have a file called "csvw/test210.csv" at the url "https://w3c.github.io/csvw/tests/test210.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test211
	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell.
	Scenario: manifest-validation#test211 date value constraint not matching maximum
		Given I have a metadata file called "csvw/test211-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test211-metadata.json"
		And I have a file called "csvw/test211.csv" at the url "https://w3c.github.io/csvw/tests/test211.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test212
	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell.
	Scenario: manifest-validation#test212 date value constraint not matching minInclusive
		Given I have a metadata file called "csvw/test212-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test212-metadata.json"
		And I have a file called "csvw/test212.csv" at the url "https://w3c.github.io/csvw/tests/test212.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test213
	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell.
	Scenario: manifest-validation#test213 date value constraint not matching minExclusive
		Given I have a metadata file called "csvw/test213-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test213-metadata.json"
		And I have a file called "csvw/test213.csv" at the url "https://w3c.github.io/csvw/tests/test213.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test214
	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell.
	Scenario: manifest-validation#test214 date value constraint not matching maxInclusive
		Given I have a metadata file called "csvw/test214-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test214-metadata.json"
		And I have a file called "csvw/test214.csv" at the url "https://w3c.github.io/csvw/tests/test214.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test215
	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell.
	Scenario: manifest-validation#test215 date value constraint not matching maxExclusive
		Given I have a metadata file called "csvw/test215-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test215-metadata.json"
		And I have a file called "csvw/test215.csv" at the url "https://w3c.github.io/csvw/tests/test215.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test216
	# Applications MUST raise an error if both minInclusive and minExclusive are specified, or if both maxInclusive and maxExclusive are specified. 
	Scenario: manifest-validation#test216 minInclusive and minExclusive
		Given I have a metadata file called "csvw/test216-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test216-metadata.json"
		And I have a file called "csvw/test216.csv" at the url "https://w3c.github.io/csvw/tests/test216.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test217
	# Applications MUST raise an error if both minInclusive and minExclusive are specified, or if both maxInclusive and maxExclusive are specified. 
	Scenario: manifest-validation#test217 maxInclusive and maxExclusive
		Given I have a metadata file called "csvw/test217-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test217-metadata.json"
		And I have a file called "csvw/test217.csv" at the url "https://w3c.github.io/csvw/tests/test217.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test218
	# Applications MUST raise an error if both minInclusive and maxInclusive are specified and maxInclusive is less than minInclusive, or if both minInclusive and maxExclusive are specified and maxExclusive is less than or equal to minInclusive.
	Scenario: manifest-validation#test218 maxInclusive less than minInclusive
		Given I have a metadata file called "csvw/test218-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test218-metadata.json"
		And I have a file called "csvw/test218.csv" at the url "https://w3c.github.io/csvw/tests/test218.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test219
	# Applications MUST raise an error if both minInclusive and maxInclusive are specified and maxInclusive is less than minInclusive, or if both minInclusive and maxExclusive are specified and maxExclusive is less than or equal to minInclusive.
	Scenario: manifest-validation#test219 maxExclusive = minInclusive
		Given I have a metadata file called "csvw/test219-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test219-metadata.json"
		And I have a file called "csvw/test219.csv" at the url "https://w3c.github.io/csvw/tests/test219.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test220
	# Applications MUST raise an error if both minExclusive and maxExclusive are specified and maxExclusive is less than minExclusive, or if both minExclusive and maxInclusive are specified and maxInclusive is less than or equal to minExclusive.
	Scenario: manifest-validation#test220 maxExclusive less than minExclusive
		Given I have a metadata file called "csvw/test220-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test220-metadata.json"
		And I have a file called "csvw/test220.csv" at the url "https://w3c.github.io/csvw/tests/test220.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test221
	# Applications MUST raise an error if both minExclusive and maxExclusive are specified and maxExclusive is less than minExclusive, or if both minExclusive and maxInclusive are specified and maxInclusive is less than or equal to minExclusive.
	Scenario: manifest-validation#test221 maxInclusive = minExclusive
		Given I have a metadata file called "csvw/test221-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test221-metadata.json"
		And I have a file called "csvw/test221.csv" at the url "https://w3c.github.io/csvw/tests/test221.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test222
	# Applications MUST raise an error if minimum, minInclusive, maximum, maxInclusive, minExclusive, or maxExclusive are specified and the base datatype is not a numeric, date/time, or duration type.
	Scenario: manifest-validation#test222 string datatype with minimum
		Given I have a metadata file called "csvw/test222-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test222-metadata.json"
		And I have a file called "csvw/test222.csv" at the url "https://w3c.github.io/csvw/tests/test222.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test223
	# Applications MUST raise an error if minimum, minInclusive, maximum, maxInclusive, minExclusive, or maxExclusive are specified and the base datatype is not a numeric, date/time, or duration type.
	Scenario: manifest-validation#test223 string datatype with maxium
		Given I have a metadata file called "csvw/test223-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test223-metadata.json"
		And I have a file called "csvw/test223.csv" at the url "https://w3c.github.io/csvw/tests/test223.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test224
	# Applications MUST raise an error if minimum, minInclusive, maximum, maxInclusive, minExclusive, or maxExclusive are specified and the base datatype is not a numeric, date/time, or duration type.
	Scenario: manifest-validation#test224 string datatype with minInclusive
		Given I have a metadata file called "csvw/test224-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test224-metadata.json"
		And I have a file called "csvw/test224.csv" at the url "https://w3c.github.io/csvw/tests/test224.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test225
	# Applications MUST raise an error if minimum, minInclusive, maximum, maxInclusive, minExclusive, or maxExclusive are specified and the base datatype is not a numeric, date/time, or duration type.
	Scenario: manifest-validation#test225 string datatype with maxInclusive
		Given I have a metadata file called "csvw/test225-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test225-metadata.json"
		And I have a file called "csvw/test225.csv" at the url "https://w3c.github.io/csvw/tests/test225.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test226
	# Applications MUST raise an error if minimum, minInclusive, maximum, maxInclusive, minExclusive, or maxExclusive are specified and the base datatype is not a numeric, date/time, or duration type.
	Scenario: manifest-validation#test226 string datatype with minExclusive
		Given I have a metadata file called "csvw/test226-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test226-metadata.json"
		And I have a file called "csvw/test226.csv" at the url "https://w3c.github.io/csvw/tests/test226.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test227
	# Applications MUST raise an error if minimum, minInclusive, maximum, maxInclusive, minExclusive, or maxExclusive are specified and the base datatype is not a numeric, date/time, or duration type.
	Scenario: manifest-validation#test227 string datatype with maxExclusive
		Given I have a metadata file called "csvw/test227-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test227-metadata.json"
		And I have a file called "csvw/test227.csv" at the url "https://w3c.github.io/csvw/tests/test227.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test228
	# If the value is a list, the constraint applies to each element of the list.
	Scenario: manifest-validation#test228 length with separator
		Given I have a metadata file called "csvw/test228-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test228-metadata.json"
		And I have a file called "csvw/test228.csv" at the url "https://w3c.github.io/csvw/tests/test228.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test229
	# If the value is a list, the constraint applies to each element of the list.
	Scenario: manifest-validation#test229 matching minLength with separator
		Given I have a metadata file called "csvw/test229-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test229-metadata.json"
		And I have a file called "csvw/test229.csv" at the url "https://w3c.github.io/csvw/tests/test229.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test230
	# If the value is a list, the constraint applies to each element of the list.
	Scenario: manifest-validation#test230 failing minLength with separator
		Given I have a metadata file called "csvw/test230-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test230-metadata.json"
		And I have a file called "csvw/test230.csv" at the url "https://w3c.github.io/csvw/tests/test230.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test231
	# As defined in [tabular-data-model], validators MUST check that each row has a unique combination of values of cells in the indicated columns.
	Scenario: manifest-validation#test231 single column primaryKey success
		Given I have a metadata file called "csvw/test231-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test231-metadata.json"
		And I have a file called "csvw/test231.csv" at the url "https://w3c.github.io/csvw/tests/test231.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test232
	# Validators MUST raise errors if there is more than one row with the same primary key
	Scenario: manifest-validation#test232 single column primaryKey violation
		Given I have a metadata file called "csvw/test232-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test232-metadata.json"
		And I have a file called "csvw/test232.csv" at the url "https://w3c.github.io/csvw/tests/test232.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test233
	# As defined in [tabular-data-model], validators MUST check that each row has a unique combination of values of cells in the indicated columns.
	Scenario: manifest-validation#test233 multiple column primaryKey success
		Given I have a metadata file called "csvw/test233-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test233-metadata.json"
		And I have a file called "csvw/test233.csv" at the url "https://w3c.github.io/csvw/tests/test233.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test234
	# Validators MUST raise errors if there is more than one row with the same primary key
	Scenario: manifest-validation#test234 multiple column primaryKey violation
		Given I have a metadata file called "csvw/test234-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test234-metadata.json"
		And I have a file called "csvw/test234.csv" at the url "https://w3c.github.io/csvw/tests/test234.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test235
	# if row titles is not null, insert any titles specified for the row. For each value, tv, of the row titles annotation
	Scenario: manifest-validation#test235 rowTitles on one column
		Given I have a metadata file called "csvw/test235-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test235-metadata.json"
		And I have a file called "csvw/countries.csv" at the url "https://w3c.github.io/csvw/tests/countries.csv"
		And I have a file called "csvw/country_slice.csv" at the url "https://w3c.github.io/csvw/tests/country_slice.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test236
	# if row titles is not null, insert any titles specified for the row. For each value, tv, of the row titles annotation
	Scenario: manifest-validation#test236 rowTitles on multiple columns
		Given I have a metadata file called "csvw/test236-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test236-metadata.json"
		And I have a file called "csvw/countries.csv" at the url "https://w3c.github.io/csvw/tests/countries.csv"
		And I have a file called "csvw/country_slice.csv" at the url "https://w3c.github.io/csvw/tests/country_slice.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test237
	# if row titles is not null, insert any titles specified for the row. For each value, tv, of the row titles annotation
	Scenario: manifest-validation#test237 rowTitles on one column (minimal)
		Given I have a metadata file called "csvw/test237-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test237-metadata.json"
		And I have a file called "csvw/countries.csv" at the url "https://w3c.github.io/csvw/tests/countries.csv"
		And I have a file called "csvw/country_slice.csv" at the url "https://w3c.github.io/csvw/tests/country_slice.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test238
	# it must be the name of one of the built-in datatypes defined in section 5.11.1 Built-in Datatypes
	Scenario: manifest-validation#test238 datatype value an absolute URL
		Given I have a metadata file called "csvw/test238-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test238-metadata.json"
		And I have a file called "csvw/test238.csv" at the url "https://w3c.github.io/csvw/tests/test238.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test242
	# If included, @id is a link property that identifies the datatype described by this datatype description.
	Scenario: manifest-validation#test242 datatype @id an absolute URL
		Given I have a metadata file called "csvw/test242-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test242-metadata.json"
		And I have a file called "csvw/test242.csv" at the url "https://w3c.github.io/csvw/tests/test242.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test243
	# It MUST NOT start with `_:`.
	Scenario: manifest-validation#test243 invalid datatype @id
		Given I have a metadata file called "csvw/test243-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test243-metadata.json"
		And I have a file called "csvw/test243.csv" at the url "https://w3c.github.io/csvw/tests/test243.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test244
	# It MUST NOT be the URL of a built-in datatype.
	Scenario: manifest-validation#test244 invalid datatype @id
		Given I have a metadata file called "csvw/test244-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test244-metadata.json"
		And I have a file called "csvw/test244.csv" at the url "https://w3c.github.io/csvw/tests/test244.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test245
	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification.
	Scenario: manifest-validation#test245 date format (valid time combinations with formats and milliseconds)
		Given I have a metadata file called "csvw/test245-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test245-metadata.json"
		And I have a file called "csvw/test245.csv" at the url "https://w3c.github.io/csvw/tests/test245.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test246
	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification.
	Scenario: manifest-validation#test246 date format (valid dateTime combinations with formats and milliseconds)
		Given I have a metadata file called "csvw/test246-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test246-metadata.json"
		And I have a file called "csvw/test246.csv" at the url "https://w3c.github.io/csvw/tests/test246.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test247
	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification.
	Scenario: manifest-validation#test247 date format (extra milliseconds)
		Given I have a metadata file called "csvw/test247-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test247-metadata.json"
		And I have a file called "csvw/test247.csv" at the url "https://w3c.github.io/csvw/tests/test247.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test248
	# No Unicode normalization (as specified in [UAX15]) is applied to these string values
	Scenario: manifest-validation#test248 Unicode in non-Normalized form
		Given I have a metadata file called "csvw/test248-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test248-metadata.json"
		And I have a file called "csvw/test248.csv" at the url "https://w3c.github.io/csvw/tests/test248.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test249
	# When comparing URLs, processors MUST use Syntax-Based Normalization as defined in [[RFC3968]]. Processors perform Scheme-Based Normalization for HTTP (80) and HTTPS (443)
	Scenario: manifest-validation#test249 http normalization
		Given I have a CSV file called "csvw/test249.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test249.csv"
		And I have a metadata file called "csvw/test249-user-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test249-user-metadata.json"
		And there is no file at the url "https://w3c.github.io/.well-known/csvm"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test249.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test250
	# As defined in [tabular-data-model], validators MUST check that, for each row, the combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns.
	Scenario: manifest-validation#test250 valid case
		Given I have a metadata file called "csvw/test250-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test250-metadata.json"
		And I have a file called "csvw/countries.csv" at the url "https://w3c.github.io/csvw/tests/countries.csv"
		And I have a file called "csvw/country_slice.csv" at the url "https://w3c.github.io/csvw/tests/country_slice.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test251
	# As defined in [tabular-data-model], validators MUST check that, for each row, the combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns.
	Scenario: manifest-validation#test251 missing source reference
		Given I have a metadata file called "csvw/test251-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test251-metadata.json"
		And I have a file called "csvw/countries.csv" at the url "https://w3c.github.io/csvw/tests/countries.csv"
		And I have a file called "csvw/country_slice.csv" at the url "https://w3c.github.io/csvw/tests/country_slice.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test252
	# As defined in [tabular-data-model], validators MUST check that, for each row, the combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns.
	Scenario: manifest-validation#test252 missing destination reference column
		Given I have a metadata file called "csvw/test252-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test252-metadata.json"
		And I have a file called "csvw/countries.csv" at the url "https://w3c.github.io/csvw/tests/countries.csv"
		And I have a file called "csvw/country_slice.csv" at the url "https://w3c.github.io/csvw/tests/country_slice.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test253
	# As defined in [tabular-data-model], validators MUST check that, for each row, the combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns.
	Scenario: manifest-validation#test253 missing destination table
		Given I have a metadata file called "csvw/test253-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test253-metadata.json"
		And I have a file called "csvw/countries.csv" at the url "https://w3c.github.io/csvw/tests/countries.csv"
		And I have a file called "csvw/country_slice.csv" at the url "https://w3c.github.io/csvw/tests/country_slice.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test254
	# The combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns.
	Scenario: manifest-validation#test254 foreign key single column same table
		Given I have a metadata file called "csvw/test254-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test254-metadata.json"
		And I have a file called "csvw/test254.csv" at the url "https://w3c.github.io/csvw/tests/test254.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test255
	# The combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns.
	Scenario: manifest-validation#test255 foreign key single column different table
		Given I have a metadata file called "csvw/test255-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test255-metadata.json"
		And I have a file called "csvw/countries.csv" at the url "https://w3c.github.io/csvw/tests/countries.csv"
		And I have a file called "csvw/test255.csv" at the url "https://w3c.github.io/csvw/tests/test255.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test256
	# The combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns.
	Scenario: manifest-validation#test256 foreign key multiple columns
		Given I have a metadata file called "csvw/test256-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test256-metadata.json"
		And I have a file called "csvw/countries.csv" at the url "https://w3c.github.io/csvw/tests/countries.csv"
		And I have a file called "csvw/test256.csv" at the url "https://w3c.github.io/csvw/tests/test256.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test257
	# Validators MUST raise errors for each row that does not have a referenced row for each of the foreign keys on the table in which the row appears
	Scenario: manifest-validation#test257 foreign key no referenced row
		Given I have a metadata file called "csvw/test257-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test257-metadata.json"
		And I have a file called "csvw/test257.csv" at the url "https://w3c.github.io/csvw/tests/test257.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test258
	# Validators MUST raise errors for each row that does not have a referenced row for each of the foreign keys on the table in which the row appears
	Scenario: manifest-validation#test258 foreign key multiple referenced rows
		Given I have a metadata file called "csvw/test258-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test258-metadata.json"
		And I have a file called "csvw/test258.csv" at the url "https://w3c.github.io/csvw/tests/test258.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test259
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration.
	Scenario: manifest-validation#test259 tree-ops example with csvm.json (w3.org/.well-known/csvm)
		Given I have a CSV file called "csvw/test259/tree-ops.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test259/tree-ops.csv"
		And I have a file called "w3.org/.well-known/csvm" at the url "https://www.w3.org/.well-known/csvm"
		And I have a file called "csvw/test259/csvm.json" at the url "https://w3c.github.io/csvw/tests/test259/csvm.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test259/tree-ops.csv.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test259/tree-ops.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test259/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test260
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration.
	Scenario: manifest-validation#test260 tree-ops example with {+url}.json (w3.org/.well-known/csvm)
		Given I have a CSV file called "csvw/test260/tree-ops.csv"
		And it is stored at the url "https://w3c.github.io/csvw/tests/test260/tree-ops.csv"
		And I have a file called "w3.org/.well-known/csvm" at the url "https://www.w3.org/.well-known/csvm"
		And I have a file called "csvw/test260/tree-ops.csv.json" at the url "https://w3c.github.io/csvw/tests/test260/tree-ops.csv.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test260/csvm.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test260/tree-ops.csv-metadata.json"
		And there is no file at the url "https://w3c.github.io/csvw/tests/test260/csv-metadata.json"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test261
	# Applications MUST raise an error if both minLength and maxLength are specified and minLength is greater than maxLength.
	Scenario: manifest-validation#test261 maxLength less than minLength
		Given I have a metadata file called "csvw/test261-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test261-metadata.json"
		And I have a file called "csvw/test261.csv" at the url "https://w3c.github.io/csvw/tests/test261.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test263
	# The value of any member of `@type` MUST be either a _term_ defined in [csvw-context], a _prefixed name_ where the prefix is a term defined in [csvw-context], or an absolute URL.
	Scenario: manifest-validation#test263 @type on a common property can be a built-in type
		Given I have a metadata file called "csvw/test263-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test263-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test264
	# The value of any member of `@type` MUST be either a _term_ defined in [csvw-context], a _prefixed name_ where the prefix is a term defined in [csvw-context], or an absolute URL.
	Scenario: manifest-validation#test264 @type on a common property can be a CURIE if the prefix is one of the built-in ones
		Given I have a metadata file called "csvw/test264-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test264-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test266
	# Processors MUST issue a warning if a property is set to an invalid value type
	Scenario: manifest-validation#test266 `null` contains an array of (valid) string & (invalid) numeric values
		Given I have a metadata file called "csvw/test266-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test266-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test267
	# It MUST NOT start with `_:` and it MUST NOT be the URL of a built-in datatype.
	Scenario: manifest-validation#test267 @id on datatype is invalid (eg starts with _:)
		Given I have a metadata file called "csvw/test267-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test267-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test268
	# An atomic property that contains a single string: the name of one of the built-in datatypes, as listed above (and which are defined as terms in the default context). Its default is string.
	Scenario: manifest-validation#test268 `base` missing on datatype (defaults to string)
		Given I have a metadata file called "csvw/test268-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test268-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test269
	# If the datatype base for a cell is `boolean`, the datatype format annotation provides the true value followed by the false value, separated by `|`. If the format does not follow this syntax, implementations MUST issue a warning and proceed as if no format had been provided.
	Scenario: manifest-validation#test269 `format` for a boolean datatype is a string but in the wrong form (eg YN)
		Given I have a metadata file called "csvw/test269-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test269-metadata.json"
		And I have a file called "csvw/test269.csv" at the url "https://w3c.github.io/csvw/tests/test269.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test270
	# All terms used within a metadata document MUST be defined in [csvw-context] defined for this specification
	Scenario: manifest-validation#test270 transformation includes an invalid property (eg foo)
		Given I have a metadata file called "csvw/test270-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test270-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test271
	# A foreign key definition is a JSON object that must contain only the following properties. . .
	Scenario: manifest-validation#test271 foreign key includes an invalid property (eg `dc:description`)
		Given I have a metadata file called "csvw/test271-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test271-metadata.json"
		And I have a file called "csvw/test271.csv" at the url "https://w3c.github.io/csvw/tests/test271.csv"
		And I have a file called "csvw/countries.csv" at the url "https://w3c.github.io/csvw/tests/countries.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test272
	# A foreign key definition is a JSON object that must contain only the following properties. . .
	Scenario: manifest-validation#test272 foreign key reference includes an invalid property (eg `dc:description`)
		Given I have a metadata file called "csvw/test272-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test272-metadata.json"
		And I have a file called "csvw/test272.csv" at the url "https://w3c.github.io/csvw/tests/test272.csv"
		And I have a file called "csvw/countries.csv" at the url "https://w3c.github.io/csvw/tests/countries.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test273
	# If present, its value MUST be a string that is interpreted as a URL which is resolved against the location of the metadata document to provide the **base URL** for other URLs in the metadata document.
	Scenario: manifest-validation#test273 `@base` set in `@context` overriding eg CSV location
		Given I have a metadata file called "csvw/test273-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test273-metadata.json"
		And I have a file called "csvw/test273/action.csv" at the url "https://w3c.github.io/csvw/tests/test273/action.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test274
	# The `@context` MUST have one of the following values: An array composed of a string followed by an object, where the string is `http://www.w3.org/ns/csvw` and the object represents a local context definition, which is restricted to contain either or both of the following members.
	Scenario: manifest-validation#test274 `@context` object includes properties other than `@base` and `@language`
		Given I have a metadata file called "csvw/test274-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test274-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test275
	# Table Group may only use defined properties.
	Scenario: manifest-validation#test275 property acceptable on column appears on table group
		Given I have a metadata file called "csvw/test275-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test275-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test276
	# Table may only use defined properties.
	Scenario: manifest-validation#test276 property acceptable on column appears on table
		Given I have a metadata file called "csvw/test276-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test276-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test277
	# Column may only use defined properties.
	Scenario: manifest-validation#test277 property acceptable on table appears on column
		Given I have a metadata file called "csvw/test277-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test277-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should be warnings
	
	# manifest-validation#test278
	# Two schemas are compatible if they have the same number of non-virtual column descriptions, and the non-virtual column descriptions at the same index within each are compatible with each other.
	Scenario: manifest-validation#test278 CSV has more headers than there are columns in the metadata
		Given I have a metadata file called "csvw/test278-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test278-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test279
	# Value MUST be a valid xsd:duration.
	Scenario: manifest-validation#test279 duration not matching xsd pattern
		Given I have a metadata file called "csvw/test279-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test279-metadata.json"
		And I have a file called "csvw/test279.csv" at the url "https://w3c.github.io/csvw/tests/test279.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test280
	# Value MUST be a valid xsd:dayTimeDuration.
	Scenario: manifest-validation#test280 dayTimeDuration not matching xsd pattern
		Given I have a metadata file called "csvw/test280-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test280-metadata.json"
		And I have a file called "csvw/test280.csv" at the url "https://w3c.github.io/csvw/tests/test280.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test281
	# Value MUST be a valid xsd:yearMonthDuration.
	Scenario: manifest-validation#test281 yearMonthDuration not matching xsd pattern
		Given I have a metadata file called "csvw/test281-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test281-metadata.json"
		And I have a file called "csvw/test281.csv" at the url "https://w3c.github.io/csvw/tests/test281.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test282
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	@ignore
	Scenario: manifest-validation#test282 valid number patterns
		Given I have a metadata file called "csvw/test282-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test282-metadata.json"
		And I have a file called "csvw/test282.csv" at the url "https://w3c.github.io/csvw/tests/test282.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test283
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;
	Scenario: manifest-validation#test283 valid number patterns (signs and percent/permille)
		Given I have a metadata file called "csvw/test283-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test283-metadata.json"
		And I have a file called "csvw/test283.csv" at the url "https://w3c.github.io/csvw/tests/test283.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test284
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test284 valid number patterns (grouping)
		Given I have a metadata file called "csvw/test284-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test284-metadata.json"
		And I have a file called "csvw/test284.csv" at the url "https://w3c.github.io/csvw/tests/test284.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test285
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	@ignore
	Scenario: manifest-validation#test285 valid number patterns (fractional grouping)
		Given I have a metadata file called "csvw/test285-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test285-metadata.json"
		And I have a file called "csvw/test285.csv" at the url "https://w3c.github.io/csvw/tests/test285.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test286
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test286 invalid ##0 1,234
		Given I have a metadata file called "csvw/test286-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test286-metadata.json"
		And I have a file called "csvw/test286.csv" at the url "https://w3c.github.io/csvw/tests/test286.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test287
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test287 invalid ##0 123.4
		Given I have a metadata file called "csvw/test287-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test287-metadata.json"
		And I have a file called "csvw/test287.csv" at the url "https://w3c.github.io/csvw/tests/test287.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test288
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test288 invalid #,#00 1
		Given I have a metadata file called "csvw/test288-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test288-metadata.json"
		And I have a file called "csvw/test288.csv" at the url "https://w3c.github.io/csvw/tests/test288.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test289
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test289 invalid #,#00 1234
		Given I have a metadata file called "csvw/test289-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test289-metadata.json"
		And I have a file called "csvw/test289.csv" at the url "https://w3c.github.io/csvw/tests/test289.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test290
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test290 invalid #,#00 12,34
		Given I have a metadata file called "csvw/test290-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test290-metadata.json"
		And I have a file called "csvw/test290.csv" at the url "https://w3c.github.io/csvw/tests/test290.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test291
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test291 invalid #,#00 12,34,567
		Given I have a metadata file called "csvw/test291-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test291-metadata.json"
		And I have a file called "csvw/test291.csv" at the url "https://w3c.github.io/csvw/tests/test291.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test292
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test292 invalid #,##,#00 1
		Given I have a metadata file called "csvw/test292-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test292-metadata.json"
		And I have a file called "csvw/test292.csv" at the url "https://w3c.github.io/csvw/tests/test292.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test293
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test293 invalid #,##,#00 1234
		Given I have a metadata file called "csvw/test293-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test293-metadata.json"
		And I have a file called "csvw/test293.csv" at the url "https://w3c.github.io/csvw/tests/test293.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test294
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test294 invalid #,##,#00 12,34
		Given I have a metadata file called "csvw/test294-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test294-metadata.json"
		And I have a file called "csvw/test294.csv" at the url "https://w3c.github.io/csvw/tests/test294.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test295
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test295 invalid #,##,#00 1,234,567
		Given I have a metadata file called "csvw/test295-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test295-metadata.json"
		And I have a file called "csvw/test295.csv" at the url "https://w3c.github.io/csvw/tests/test295.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test296
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test296 invalid #0.# 12.34
		Given I have a metadata file called "csvw/test296-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test296-metadata.json"
		And I have a file called "csvw/test296.csv" at the url "https://w3c.github.io/csvw/tests/test296.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test297
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test297 invalid #0.# 1,234.5
		Given I have a metadata file called "csvw/test297-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test297-metadata.json"
		And I have a file called "csvw/test297.csv" at the url "https://w3c.github.io/csvw/tests/test297.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test298
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test298 invalid #0.0 1
		Given I have a metadata file called "csvw/test298-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test298-metadata.json"
		And I have a file called "csvw/test298.csv" at the url "https://w3c.github.io/csvw/tests/test298.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test299
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test299 invalid #0.0 12.34
		Given I have a metadata file called "csvw/test299-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test299-metadata.json"
		And I have a file called "csvw/test299.csv" at the url "https://w3c.github.io/csvw/tests/test299.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test300
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test300 invalid #0.0# 1
		Given I have a metadata file called "csvw/test300-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test300-metadata.json"
		And I have a file called "csvw/test300.csv" at the url "https://w3c.github.io/csvw/tests/test300.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test301
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test301 invalid #0.0# 12.345
		Given I have a metadata file called "csvw/test301-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test301-metadata.json"
		And I have a file called "csvw/test301.csv" at the url "https://w3c.github.io/csvw/tests/test301.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test302
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test302 invalid #0.0#,# 1
		Given I have a metadata file called "csvw/test302-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test302-metadata.json"
		And I have a file called "csvw/test302.csv" at the url "https://w3c.github.io/csvw/tests/test302.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test303
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test303 invalid #0.0#,# 12.345
		Given I have a metadata file called "csvw/test303-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test303-metadata.json"
		And I have a file called "csvw/test303.csv" at the url "https://w3c.github.io/csvw/tests/test303.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test304
	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`.
	Scenario: manifest-validation#test304 invalid #0.0#,# 12.34,567
		Given I have a metadata file called "csvw/test304-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test304-metadata.json"
		And I have a file called "csvw/test304.csv" at the url "https://w3c.github.io/csvw/tests/test304.csv"
		When I carry out CSVW validation
		Then there should be errors
	
	# manifest-validation#test305
	# Values in separate columns using the same propertyUrl are kept in proper relative order.
	Scenario: manifest-validation#test305 multiple values with same subject and property (unordered)
		Given I have a metadata file called "csvw/test305-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test305-metadata.json"
		And I have a file called "csvw/test305.csv" at the url "https://w3c.github.io/csvw/tests/test305.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test306
	# Values in separate columns using the same propertyUrl are kept in proper relative order.
	Scenario: manifest-validation#test306 multiple values with same subject and property (ordered)
		Given I have a metadata file called "csvw/test306-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test306-metadata.json"
		And I have a file called "csvw/test306.csv" at the url "https://w3c.github.io/csvw/tests/test306.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test307
	# Values in separate columns using the same propertyUrl are kept in proper relative order.
	Scenario: manifest-validation#test307 multiple values with same subject and property (ordered and unordered)
		Given I have a metadata file called "csvw/test307-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test307-metadata.json"
		And I have a file called "csvw/test307.csv" at the url "https://w3c.github.io/csvw/tests/test307.csv"
		When I carry out CSVW validation
		Then there should not be errors
		And there should not be warnings
	
	# manifest-validation#test308
	# If the value of the datatype property is a string, it must be one of the built-in datatypes.
	Scenario: manifest-validation#test308 invalid datatype string
		Given I have a metadata file called "csvw/test308-metadata.json"
		And the metadata is stored at the url "https://w3c.github.io/csvw/tests/test308-metadata.json"
		And I have a file called "csvw/tree-ops.csv" at the url "https://w3c.github.io/csvw/tests/tree-ops.csv"
		When I carry out CSVW validation
		Then there should be errors
	
