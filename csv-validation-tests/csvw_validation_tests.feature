# Auto-generated file based on standard validation CSVW tests from https://w3c.github.io/csvw/tests/manifest-validation.jsonld 

Feature: CSVW Validation Tests 

	# The simplest possible table without metadata 
	# https://w3c.github.io/csvw/tests/manifest-validation#test001 
	Scenario: manifest-validation#test001 Simple table 
	# A table with entity identifiers and references to other entities without metadata 
	# https://w3c.github.io/csvw/tests/manifest-validation#test005 
	Scenario: manifest-validation#test005 Identifier references 
	# Records contain two entities with relationships which are duplicated without metadata 
	# https://w3c.github.io/csvw/tests/manifest-validation#test006 
	Scenario: manifest-validation#test006 No identifiers 
	# Joined data with identified records without metadata 
	# https://w3c.github.io/csvw/tests/manifest-validation#test007 
	Scenario: manifest-validation#test007 Joined table with unique identifiers 
	# One field has comma-separated values without metadata 
	# https://w3c.github.io/csvw/tests/manifest-validation#test008 
	Scenario: manifest-validation#test008 Microsyntax - internal field separator 
	# Field with parseable human formatted time without metadata 
	# https://w3c.github.io/csvw/tests/manifest-validation#test009 
	Scenario: manifest-validation#test009 Microsyntax - formatted time 
	# Country-codes-and-names example 
	# https://w3c.github.io/csvw/tests/manifest-validation#test010 
	Scenario: manifest-validation#test010 Country-codes-and-names example 
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test011 
	Scenario: manifest-validation#test011 tree-ops example with metadata 
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test012 
	Scenario: manifest-validation#test012 tree-ops example with directory metadata 
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test013 
	Scenario: manifest-validation#test013 tree-ops example from user metadata 
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test014 
	Scenario: manifest-validation#test014 tree-ops example with linked metadata 
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test015 
	Scenario: manifest-validation#test015 tree-ops example with user and directory metadata 
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test016 
	Scenario: manifest-validation#test016 tree-ops example with linked and directory metadata 
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test017 
	Scenario: manifest-validation#test017 tree-ops example with file and directory metadata 
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test018 
	Scenario: manifest-validation#test018 tree-ops example with user, file and directory metadata 
	# If `true`, sets the `header row count` flag to 1, and if `false` to 0, unless `headerRowCount` is provided, in which case the value provided for the `header` property is ignored. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test023 
	Scenario: manifest-validation#test023 dialect: header=false 
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test027 
	Scenario: manifest-validation#test027 tree-ops minimal output 
	# If no metadata is supplied or found, processors MUST use embedded metadata. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test028 
	Scenario: manifest-validation#test028 countries.csv example 
	# If no metadata is supplied or found, processors MUST use embedded metadata. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test029 
	Scenario: manifest-validation#test029 countries.csv minimal 
	# countries.json from metadata 
	# https://w3c.github.io/csvw/tests/manifest-validation#test030 
	Scenario: manifest-validation#test030 countries.json example 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/countries.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/countries.json 

	# countries.json from metadata minimal output 
	# https://w3c.github.io/csvw/tests/manifest-validation#test031 
	Scenario: manifest-validation#test031 countries.json example minimal output 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/countries.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/countries.json 

	# events-listing example from metadata, virtual columns and multiple subjects per row 
	# https://w3c.github.io/csvw/tests/manifest-validation#test032 
	Scenario: manifest-validation#test032 events-listing.csv example 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test032/csv-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test032/csv-metadata.json 

	# events-listing example from metadata, virtual columns and multiple subjects per row; minimal output 
	# https://w3c.github.io/csvw/tests/manifest-validation#test033 
	Scenario: manifest-validation#test033 events-listing.csv minimal output 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test033/csv-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test033/csv-metadata.json 

	# Public Sector Roles example with referenced schemas. Validation fails because organization.csv intentionally contains an invalid reference. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test034 
	Scenario: manifest-validation#test034 roles example 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test034/csv-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test034/csv-metadata.json 

	# Public Sector Roles example with referenced schemas; minimal output. Validation fails because organization.csv intentionally contains an invalid reference. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test035 
	Scenario: manifest-validation#test035 roles minimal 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test035/csv-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test035/csv-metadata.json 

	# tree-ops extended example 
	# https://w3c.github.io/csvw/tests/manifest-validation#test036 
	Scenario: manifest-validation#test036 tree-ops-ext example 
	# tree-ops extended example; minimal output 
	# https://w3c.github.io/csvw/tests/manifest-validation#test037 
	Scenario: manifest-validation#test037 tree-ops-ext minimal 
	# Setting inherited properties at different levels inherit to cell 
	# https://w3c.github.io/csvw/tests/manifest-validation#test038 
	Scenario: manifest-validation#test038 inherited properties propagation 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test038-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test038-metadata.json 

	# Different combinations of valid inherited properties 
	# https://w3c.github.io/csvw/tests/manifest-validation#test039 
	Scenario: manifest-validation#test039 valid inherited properties 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test039-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test039-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test040 
	Scenario: manifest-validation#test040 invalid null 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test040-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test040-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test041 
	Scenario: manifest-validation#test041 invalid lang 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test041-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test041-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test042 
	Scenario: manifest-validation#test042 invalid textDirection 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test042-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test042-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test043 
	Scenario: manifest-validation#test043 invalid separator 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test043-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test043-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test044 
	Scenario: manifest-validation#test044 invalid ordered 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test044-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test044-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test045 
	Scenario: manifest-validation#test045 invalid default 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test045-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test045-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test046 
	Scenario: manifest-validation#test046 invalid dataype 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test046-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test046-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test047 
	Scenario: manifest-validation#test047 invalid aboutUrl 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test047-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test047-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test048 
	Scenario: manifest-validation#test048 invalid propertyUrl 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test048-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test048-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test049 
	Scenario: manifest-validation#test049 invalid valueUrl 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test049-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test049-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test059 
	Scenario: manifest-validation#test059 dialect: invalid commentPrefix 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test059-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test059-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test060 
	Scenario: manifest-validation#test060 dialect: invalid delimiter 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test060-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test060-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test061 
	Scenario: manifest-validation#test061 dialect: invalid doubleQuote 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test061-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test061-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test062 
	Scenario: manifest-validation#test062 dialect: invalid encoding 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test062-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test062-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test063 
	Scenario: manifest-validation#test063 dialect: invalid header 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test063-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test063-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test065 
	Scenario: manifest-validation#test065 dialect: invalid headerRowCount 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test065-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test065-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test066 
	Scenario: manifest-validation#test066 dialect: invalid lineTerminators 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test066-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test066-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test067 
	Scenario: manifest-validation#test067 dialect: invalid quoteChar 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test067-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test067-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test068 
	Scenario: manifest-validation#test068 dialect: invalid skipBlankRows 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test068-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test068-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test069 
	Scenario: manifest-validation#test069 dialect: invalid skipColumns 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test069-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test069-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test070 
	Scenario: manifest-validation#test070 dialect: invalid skipInitialSpace 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test070-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test070-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test071 
	Scenario: manifest-validation#test071 dialect: invalid skipRows 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test071-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test071-metadata.json 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test072 
	Scenario: manifest-validation#test072 dialect: invalid trim 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test072-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test072-metadata.json 

	# The value of `@language` MUST be a valid `BCP47` language code 
	# https://w3c.github.io/csvw/tests/manifest-validation#test073 
	Scenario: manifest-validation#test073 invalid @language 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test073-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test073-metadata.json 

	# Compliant application MUST raise an error if this array does not contain one or more `table descriptions`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test074 
	Scenario: manifest-validation#test074 empty tables 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test074-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test074-metadata.json 

	# An atomic property that MUST have a single string value that is one of "rtl", "ltr" or "auto". 
	# https://w3c.github.io/csvw/tests/manifest-validation#test075 
	Scenario: manifest-validation#test075 invalid tableGroup tableDirection 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test075-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test075-metadata.json 

	# An atomic property that MUST have a single string value that is one of "rtl", "ltr" or "auto". 
	# https://w3c.github.io/csvw/tests/manifest-validation#test076 
	Scenario: manifest-validation#test076 invalid table tableDirection 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test076-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test076-metadata.json 

	# It MUST NOT start with `_:`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test077 
	Scenario: manifest-validation#test077 invalid tableGroup @id 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test077-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test077-metadata.json 

	# It MUST NOT start with `_:`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test078 
	Scenario: manifest-validation#test078 invalid table @id 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test078-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test078-metadata.json 

	# It MUST NOT start with `_:`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test079 
	Scenario: manifest-validation#test079 invalid schema @id 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test079-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test079-metadata.json 

	# It MUST NOT start with `_:`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test080 
	Scenario: manifest-validation#test080 invalid column @id 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test080-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test080-metadata.json 

	# It MUST NOT start with `_:`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test081 
	Scenario: manifest-validation#test081 invalid dialect @id 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test081-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test081-metadata.json 

	# It MUST NOT start with `_:`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test082 
	Scenario: manifest-validation#test082 invalid template @id 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test082-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test082-metadata.json 

	# If included `@type` MUST be `TableGroup` 
	# https://w3c.github.io/csvw/tests/manifest-validation#test083 
	Scenario: manifest-validation#test083 invalid tableGroup @type 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test083-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test083-metadata.json 

	# If included `@type` MUST be `TableGroup` 
	# https://w3c.github.io/csvw/tests/manifest-validation#test084 
	Scenario: manifest-validation#test084 invalid table @type 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test084-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test084-metadata.json 

	# If included `@type` MUST be `TableGroup` 
	# https://w3c.github.io/csvw/tests/manifest-validation#test085 
	Scenario: manifest-validation#test085 invalid schema @type 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test085-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test085-metadata.json 

	# If included `@type` MUST be `TableGroup` 
	# https://w3c.github.io/csvw/tests/manifest-validation#test086 
	Scenario: manifest-validation#test086 invalid column @type 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test086-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test086-metadata.json 

	# If included `@type` MUST be `Dialect` 
	# https://w3c.github.io/csvw/tests/manifest-validation#test087 
	Scenario: manifest-validation#test087 invalid dialect @type 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test087-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test087-metadata.json 

	# If included `@type` MUST be `Template` 
	# https://w3c.github.io/csvw/tests/manifest-validation#test088 
	Scenario: manifest-validation#test088 invalid transformation @type 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test088-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test088-metadata.json 

	# The `tables` property is required in a `TableGroup` 
	# https://w3c.github.io/csvw/tests/manifest-validation#test089 
	Scenario: manifest-validation#test089 missing tables in TableGroup 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test089-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test089-metadata.json 

	# The `url` property is required in a `Table` 
	# https://w3c.github.io/csvw/tests/manifest-validation#test090 
	Scenario: manifest-validation#test090 missing url in Table 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test090-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test090-metadata.json 

	# All compliant applications MUST generate errors and stop processing if a metadata document does not use valid JSON syntax 
	# https://w3c.github.io/csvw/tests/manifest-validation#test092 
	Scenario: manifest-validation#test092 invalid JSON 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test092-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test092-metadata.json 

	# Compliant applications MUST ignore properties (aside from _common properties_) which are not defined in this specification and MUST generate a warning when they are encoutered 
	# https://w3c.github.io/csvw/tests/manifest-validation#test093 
	Scenario: manifest-validation#test093 undefined properties 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test093-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test093-metadata.json 

	# Any items within an array that are not valid objects of the type expected are ignored 
	# https://w3c.github.io/csvw/tests/manifest-validation#test094 
	Scenario: manifest-validation#test094 inconsistent array values: tables 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test094-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test094-metadata.json 

	# Any items within an array that are not valid objects of the type expected are ignored 
	# https://w3c.github.io/csvw/tests/manifest-validation#test095 
	Scenario: manifest-validation#test095 inconsistent array values: transformations 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test095-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test095-metadata.json 

	# Any items within an array that are not valid objects of the type expected are ignored 
	# https://w3c.github.io/csvw/tests/manifest-validation#test096 
	Scenario: manifest-validation#test096 inconsistent array values: columns 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test096-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test096-metadata.json 

	# Any items within an array that are not valid objects of the type expected are ignored 
	# https://w3c.github.io/csvw/tests/manifest-validation#test097 
	Scenario: manifest-validation#test097 inconsistent array values: foreignKeys 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test097-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test097-metadata.json 

	# If the supplied value of an array property is not an array (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been supplied with an empty array. Compliant application MUST raise an error if this array does not contain one or more table descriptions. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test098 
	Scenario: manifest-validation#test098 inconsistent array values: tables 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test098-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test098-metadata.json 

	# If the supplied value of an array property is not an array (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been supplied with an empty array 
	# https://w3c.github.io/csvw/tests/manifest-validation#test099 
	Scenario: manifest-validation#test099 inconsistent array values: transformations 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test099-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test099-metadata.json 

	# If the supplied value of an array property is not an array (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been supplied with an empty array 
	# https://w3c.github.io/csvw/tests/manifest-validation#test100 
	Scenario: manifest-validation#test100 inconsistent array values: columns 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test100-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test100-metadata.json 

	# If the supplied value of an array property is not an array (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been supplied with an empty array 
	# https://w3c.github.io/csvw/tests/manifest-validation#test101 
	Scenario: manifest-validation#test101 inconsistent array values: foreignKeys 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test101-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test101-metadata.json 

	# If the supplied value of an array property is not an array (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been supplied with an empty array 
	# https://w3c.github.io/csvw/tests/manifest-validation#test102 
	Scenario: manifest-validation#test102 inconsistent link values: @id 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test102-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test102-metadata.json 

	# If the supplied value of an array property is not an array (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been supplied with an empty array 
	# https://w3c.github.io/csvw/tests/manifest-validation#test103 
	Scenario: manifest-validation#test103 inconsistent link values: url 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test103-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test103-metadata.json 

	# The referenced description object MUST have a name property 
	# https://w3c.github.io/csvw/tests/manifest-validation#test104 
	Scenario: manifest-validation#test104 invalid columnReference 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test104-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test104-metadata.json 

	# The referenced description object MUST have a name property 
	# https://w3c.github.io/csvw/tests/manifest-validation#test105 
	Scenario: manifest-validation#test105 invalid primaryKey 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test105-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test105-metadata.json 

	# If the supplied value of an object property is not a string or object (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been specified as an object with no properties. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test106 
	Scenario: manifest-validation#test106 invalid dialect 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test106-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test106-metadata.json 

	# If the supplied value of an object property is not a string or object (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been specified as an object with no properties. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test107 
	Scenario: manifest-validation#test107 invalid tableSchema 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test107-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test107-metadata.json 

	# If the supplied value of an object property is not a string or object (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been specified as an object with no properties. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test108 
	Scenario: manifest-validation#test108 invalid reference 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test108-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test108-metadata.json 

	# Natural Language properties may be objects whose properties MUST be language codes as defined by [BCP47] and whose values are either strings or arrays, providing natural language strings in that language. Validation fails because without a title, the metadata is incompatible with the CSV, which isn't a problem when not validating. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test109 
	Scenario: manifest-validation#test109 titles with invalid language 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test109-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test109-metadata.json 

	# Natural Language properties may be objects whose properties MUST be language codes as defined by [BCP47] and whose values are either strings or arrays, providing natural language strings in that language 
	# https://w3c.github.io/csvw/tests/manifest-validation#test110 
	Scenario: manifest-validation#test110 titles with non-string values 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test110-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test110-metadata.json 

	# If the supplied value of a natural language property is not a string, array or object (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been specified as an empty array. Validation fails because without a title, the metadata is incompatible with the CSV, which isn't a problem when not validating. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test111 
	Scenario: manifest-validation#test111 titles with invalid value 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test111-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test111-metadata.json 

	# If the supplied value is an array, any items in that array that are not strings MUST be ignored 
	# https://w3c.github.io/csvw/tests/manifest-validation#test112 
	Scenario: manifest-validation#test112 titles with non-string array values 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test112-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test112-metadata.json 

	# Atomic properties: Processors MUST issue a warning if a property is set to an invalid value type 
	# https://w3c.github.io/csvw/tests/manifest-validation#test113 
	Scenario: manifest-validation#test113 invalid suppressOutput 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test113-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test113-metadata.json 

	# Atomic properties: Processors MUST issue a warning if a property is set to an invalid value type 
	# https://w3c.github.io/csvw/tests/manifest-validation#test114 
	Scenario: manifest-validation#test114 invalid name 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test114-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test114-metadata.json 

	# Atomic properties: Processors MUST issue a warning if a property is set to an invalid value type 
	# https://w3c.github.io/csvw/tests/manifest-validation#test115 
	Scenario: manifest-validation#test115 invalid virtual 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test115-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test115-metadata.json 

	# processors MUST attempt to locate a metadata documents through site-wide configuration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test116 
	Scenario: manifest-validation#test116 file-metadata with query component not found 
	# If the metadata file found at this location does not explicitly include a reference to the requested tabular data file then it MUST be ignored. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test117 
	Scenario: manifest-validation#test117 file-metadata not referencing file 
	# processors MUST attempt to locate a metadata documents through site-wide configuration. component. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test118 
	Scenario: manifest-validation#test118 directory-metadata with query component 
	# If the metadata file found at this location does not explicitly include a reference to the requested tabular data file then it MUST be ignored. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test119 
	Scenario: manifest-validation#test119 directory-metadata not referencing file 
	# If the metadata file found at this location does not explicitly include a reference to the requested tabular data file then it MUST be ignored. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test120 
	Scenario: manifest-validation#test120 link-metadata not referencing file 
	# User-specified metadata does not need to reference the starting CSV 
	# https://w3c.github.io/csvw/tests/manifest-validation#test121 
	Scenario: manifest-validation#test121 user-metadata not referencing file 
	# If the metadata file found at this location does not explicitly include a reference to the requested tabular data file then it MUST be ignored. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test122 
	Scenario: manifest-validation#test122 link-metadata not describing file uses file-metadata 
	# If the metadata file found at this location does not explicitly include a reference to the requested tabular data file then it MUST be ignored. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test123 
	Scenario: manifest-validation#test123 file-metadata not describing file uses directory-metadata 
	# If not validating, and one schema has a name property but not a titles property, and the other has a titles property but not a name property. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test124 
	Scenario: manifest-validation#test124 metadata with columns not matching csv titles 
	# If the column required annotation is true, add an error to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test125 
	Scenario: manifest-validation#test125 required column with empty cell 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test125-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test125-metadata.json 

	# if the string is the same as any one of the values of the column null annotation, then the resulting value is null. If the column separator annotation is null and the column required annotation is true, add an error to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test126 
	Scenario: manifest-validation#test126 required column with cell matching null 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test126-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test126-metadata.json 

	# if TM is not compatible with EM validators MUST raise an error, other processors MUST generate a warning and continue processing 
	# https://w3c.github.io/csvw/tests/manifest-validation#test127 
	Scenario: manifest-validation#test127 incompatible table 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test127-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test127-metadata.json 

	# The name properties of the column descriptions MUST be unique within a given table description. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test128 
	Scenario: manifest-validation#test128 duplicate column names 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test128-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test128-metadata.json 

	# This (name) MUST be a string and this property has no default value, which means it MUST be ignored if the supplied value is not a string. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test129 
	Scenario: manifest-validation#test129 columnn name as integer 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test129-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test129-metadata.json 

	# column names are restricted as defined in Variables in [URI-TEMPLATE]  
	# https://w3c.github.io/csvw/tests/manifest-validation#test130 
	Scenario: manifest-validation#test130 invalid column name 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test130-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test130-metadata.json 

	# column names are restricted ... names beginning with '_' are reserved by this specification and MUST NOT be used within metadata documents. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test131 
	Scenario: manifest-validation#test131 invalid column name 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test131-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test131-metadata.json 

	# If there is no name property defined on this column, the first titles value having the same language tag as default language, or und or if no default language is specified, becomes the name annotation for the described column. This annotation MUST be percent-encoded as necessary to conform to the syntactic requirements defined in [RFC3986] 
	# https://w3c.github.io/csvw/tests/manifest-validation#test132 
	Scenario: manifest-validation#test132 name annotation from title percent encoded 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test132-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test132-metadata.json 

	# If present, a virtual column MUST appear after all other non-virtual column definitions. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test133 
	Scenario: manifest-validation#test133 virtual before non-virtual 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test133-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test133-metadata.json 

	# A metadata document MUST NOT add a new context 
	# https://w3c.github.io/csvw/tests/manifest-validation#test134 
	Scenario: manifest-validation#test134 context in common property 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test134-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test134-metadata.json 

	# Values MUST NOT use list objects or set objects. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test135 
	Scenario: manifest-validation#test135 @list value 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test135-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test135-metadata.json 

	# Values MUST NOT use list objects or set objects. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test136 
	Scenario: manifest-validation#test136 @set value 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test136-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test136-metadata.json 

	# The value of any @id or @type contained within a metadata document MUST NOT be a blank node. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test137 
	Scenario: manifest-validation#test137 @type out of range (as datatype) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test137-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test137-metadata.json 

	# The value of any @id or @type contained within a metadata document MUST NOT be a blank node. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test138 
	Scenario: manifest-validation#test138 @type out of range (as node type) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test138-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test138-metadata.json 

	# The value of any member of @type MUST be either a term defined in [csvw-context], a prefixed name where the prefix is a term defined in [csvw-context], or an absolute URL. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test139 
	Scenario: manifest-validation#test139 @type out of range (as node type) - string 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test139-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test139-metadata.json 

	# The value of any member of @type MUST be either a term defined in [csvw-context], a prefixed name where the prefix is a term defined in [csvw-context], or an absolute URL. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test140 
	Scenario: manifest-validation#test140 @type out of range (as node type) - integer 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test140-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test140-metadata.json 

	# The value of any @id or @type contained within a metadata document MUST NOT be a blank node. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test141 
	Scenario: manifest-validation#test141 @id out of range (as node type) - bnode 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test141-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test141-metadata.json 

	# If a @value property is used on an object, that object MUST NOT have any other properties aside from either @type or @language, and MUST NOT have both @type and @language as properties. The value of the @value property MUST be a string, number, or boolean value. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test142 
	Scenario: manifest-validation#test142 @value with @language and @type 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test142-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test142-metadata.json 

	# If a @value property is used on an object, that object MUST NOT have any other properties aside from either @type or @language, and MUST NOT have both @type and @language as properties. The value of the @value property MUST be a string, number, or boolean value. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test143 
	Scenario: manifest-validation#test143 @value with extra properties 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test143-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test143-metadata.json 

	# A @language property MUST NOT be used on an object unless it also has a @value property. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test144 
	Scenario: manifest-validation#test144 @language outside of @value 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test144-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test144-metadata.json 

	# If a @language property is used, it MUST have a string value that adheres to the syntax defined in [BCP47], or be null. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test145 
	Scenario: manifest-validation#test145 @value with invalid @language 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test145-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test145-metadata.json 

	# Aside from @value, @type, @language, and @id, the properties used on an object MUST NOT start with @. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test146 
	Scenario: manifest-validation#test146 Invalid faux-keyword 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test146-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test146-metadata.json 

	# If there is a non-empty case-sensitive intersection between the titles values, where matches MUST have a matching language; `und` matches any language, and languages match if they are equal when truncated, as defined in [BCP47], to the length of the shortest language tag. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test147 
	Scenario: manifest-validation#test147 title incompatible with title on case 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test147-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test147-metadata.json 

	# If there is a non-empty case-sensitive intersection between the titles values, where matches MUST have a matching language; `und` matches any language, and languages match if they are equal when truncated, as defined in [BCP47], to the length of the shortest language tag. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test148 
	Scenario: manifest-validation#test148 title incompatible with title on language 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test148-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test148-metadata.json 

	# If there is a non-empty case-sensitive intersection between the titles values, where matches MUST have a matching language; `und` matches any language, and languages match if they are equal when truncated, as defined in [BCP47], to the length of the shortest language tag. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test149 
	Scenario: manifest-validation#test149 title compatible with title on less specific language 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test149-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test149-metadata.json 

	# If the value of this property is a string, it MUST be one of the built-in datatypes defined in section 5.11.1 Built-in Datatypes or an absolute URL 
	# https://w3c.github.io/csvw/tests/manifest-validation#test150 
	Scenario: manifest-validation#test150 non-builtin datatype (datatype value) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test150-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test150-metadata.json 

	# If the value of this property is a string, it MUST be one of the built-in datatypes 
	# https://w3c.github.io/csvw/tests/manifest-validation#test151 
	Scenario: manifest-validation#test151 non-builtin datatype (base value) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test151-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test151-metadata.json 

	# If the datatype base is not numeric, boolean, a date/time type, or a duration type, the datatype format annotation provides a regular expression for the string values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test152 
	Scenario: manifest-validation#test152 string format (valid combinations) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test152-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test152-metadata.json 

	# If the datatype base is not numeric, boolean, a date/time type, or a duration type, the datatype format annotation provides a regular expression for the string values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test153 
	Scenario: manifest-validation#test153 string format (bad format string) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test153-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test153-metadata.json 

	# If the datatype base is not numeric, boolean, a date/time type, or a duration type, the datatype format annotation provides a regular expression for the string values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test154 
	Scenario: manifest-validation#test154 string format (value not matching format) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test154-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test154-metadata.json 

	# If the datatype format annotation is a single string, this is interpreted in the same way as if it were an object with a pattern property whose value is that string 
	# https://w3c.github.io/csvw/tests/manifest-validation#test155 
	Scenario: manifest-validation#test155 number format (valid combinations) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test155-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test155-metadata.json 

	# If the datatype format annotation is a single string, this is interpreted in the same way as if it were an object with a pattern property whose value is that string 
	# https://w3c.github.io/csvw/tests/manifest-validation#test156 
	Scenario: manifest-validation#test156 number format (bad format string) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test156-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test156-metadata.json 

	# If the datatype format annotation is a single string, this is interpreted in the same way as if it were an object with a pattern property whose value is that string 
	# https://w3c.github.io/csvw/tests/manifest-validation#test157 
	Scenario: manifest-validation#test157 number format (value not matching format) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test157-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test157-metadata.json 

	# Numeric dataype with object format 
	# https://w3c.github.io/csvw/tests/manifest-validation#test158 
	Scenario: manifest-validation#test158 number format (valid combinations) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test158-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test158-metadata.json 

	# If the datatype format annotation is a single string, this is interpreted in the same way as if it were an object with a pattern property whose value is that string 
	# https://w3c.github.io/csvw/tests/manifest-validation#test159 
	Scenario: manifest-validation#test159 number format (bad pattern format string) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test159-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test159-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell if the string being parsed 
	# https://w3c.github.io/csvw/tests/manifest-validation#test160 
	Scenario: manifest-validation#test160 number format (not matching values with pattern) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test160-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test160-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell if the string being parsed 
	# https://w3c.github.io/csvw/tests/manifest-validation#test161 
	Scenario: manifest-validation#test161 number format (not matching values without pattern) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test161-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test161-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell if the string being parsed contains two consecutive groupChar strings 
	# https://w3c.github.io/csvw/tests/manifest-validation#test162 
	Scenario: manifest-validation#test162 numeric format (consecutive groupChar) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test162-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test162-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell if the string being parsed contains the decimalChar, if the datatype base is integer or one of its sub-values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test163 
	Scenario: manifest-validation#test163 integer datatype with decimalChar 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test163-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test163-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, if the datatype base is decimal or one of its sub-values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test164 
	Scenario: manifest-validation#test164 decimal datatype with exponent 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test164-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test164-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, is one of the special values NaN, INF, or -INF, if the datatype base is decimal or one of its sub-values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test165 
	Scenario: manifest-validation#test165 decimal type with NaN 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test165-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test165-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, is one of the special values NaN, INF, or -INF, if the datatype base is decimal or one of its sub-values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test166 
	Scenario: manifest-validation#test166 decimal type with INF 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test166-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test166-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, is one of the special values NaN, INF, or -INF, if the datatype base is decimal or one of its sub-values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test167 
	Scenario: manifest-validation#test167 decimal type with -INF 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test167-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test167-metadata.json 

	# When parsing the string value of a cell against this format specification, implementations MUST recognise and parse numbers 
	# https://w3c.github.io/csvw/tests/manifest-validation#test168 
	Scenario: manifest-validation#test168 decimal with implicit groupChar 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test168-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test168-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test169 
	Scenario: manifest-validation#test169 invalid decimal 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test169-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test169-metadata.json 

	# Implementations MUST use the sign, exponent, percent, and per-mille signs when parsing the string value of a cell to provide the value of the cell 
	# https://w3c.github.io/csvw/tests/manifest-validation#test170 
	Scenario: manifest-validation#test170 decimal with percent 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test170-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test170-metadata.json 

	# Implementations MUST use the sign, exponent, percent, and per-mille signs when parsing the string value of a cell to provide the value of the cell 
	# https://w3c.github.io/csvw/tests/manifest-validation#test171 
	Scenario: manifest-validation#test171 decimal with per-mille 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test171-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test171-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test172 
	Scenario: manifest-validation#test172 invalid byte 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test172-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test172-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test173 
	Scenario: manifest-validation#test173 invald unsignedLong 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test173-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test173-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test174 
	Scenario: manifest-validation#test174 invalid unsignedShort 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test174-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test174-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test175 
	Scenario: manifest-validation#test175 invalid unsignedByte 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test175-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test175-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test176 
	Scenario: manifest-validation#test176 invalid positiveInteger 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test176-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test176-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test177 
	Scenario: manifest-validation#test177 invalid negativeInteger 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test177-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test177-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test178 
	Scenario: manifest-validation#test178 invalid nonPositiveInteger 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test178-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test178-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test179 
	Scenario: manifest-validation#test179 invalid nonNegativeInteger 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test179-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test179-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test180 
	Scenario: manifest-validation#test180 invalid double 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test180-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test180-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test181 
	Scenario: manifest-validation#test181 invalid number 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test181-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test181-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test182 
	Scenario: manifest-validation#test182 invalid float 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test182-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test182-metadata.json 

	# If the datatype base for a cell is boolean, the datatype format annotation provides the true and false values expected, separated by `|`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test183 
	Scenario: manifest-validation#test183 boolean format (valid combinations) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test183-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test183-metadata.json 

	# If the datatype base for a cell is boolean, the datatype format annotation provides the true and false values expected, separated by `|`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test184 
	Scenario: manifest-validation#test184 boolean format (bad format string) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test184-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test184-metadata.json 

	# If the datatype base for a cell is boolean, the datatype format annotation provides the true and false values expected, separated by `|`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test185 
	Scenario: manifest-validation#test185 boolean format (value not matching format) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test185-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test185-metadata.json 

	# Implementations MUST add a validation error to the errors annotation for the cell if the string being parsed 
	# https://w3c.github.io/csvw/tests/manifest-validation#test186 
	Scenario: manifest-validation#test186 boolean format (not matching datatype) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test186-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test186-metadata.json 

	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test187 
	Scenario: manifest-validation#test187 date format (valid native combinations) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test187-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test187-metadata.json 

	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test188 
	Scenario: manifest-validation#test188 date format (valid date combinations with formats) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test188-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test188-metadata.json 

	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test189 
	Scenario: manifest-validation#test189 date format (valid time combinations with formats) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test189-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test189-metadata.json 

	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test190 
	Scenario: manifest-validation#test190 date format (valid dateTime combinations with formats) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test190-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test190-metadata.json 

	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test191 
	Scenario: manifest-validation#test191 date format (bad format string) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test191-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test191-metadata.json 

	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test192 
	Scenario: manifest-validation#test192 date format (value not matching format) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test192-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test192-metadata.json 

	# If the datatype base is a duration type, the datatype format annotation provides a regular expression for the string values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test193 
	Scenario: manifest-validation#test193 duration format (valid combinations) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test193-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test193-metadata.json 

	# If the datatype base is a duration type, the datatype format annotation provides a regular expression for the string values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test194 
	Scenario: manifest-validation#test194 duration format (value not matching format) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test194-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test194-metadata.json 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test195 
	Scenario: manifest-validation#test195 values with matching length 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test195-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test195-metadata.json 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test196 
	Scenario: manifest-validation#test196 values with wrong length 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test196-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test196-metadata.json 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test197 
	Scenario: manifest-validation#test197 values with wrong maxLength 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test197-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test197-metadata.json 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test198 
	Scenario: manifest-validation#test198 values with wrong minLength 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test198-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test198-metadata.json 

	# Applications MUST raise an error if both length and minLength are specified and length is less than minLength.  
	# https://w3c.github.io/csvw/tests/manifest-validation#test199 
	Scenario: manifest-validation#test199 length less than minLength 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test199-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test199-metadata.json 

	# Applications MUST raise an error if both length and maxLength are specified and length is greater than maxLength.  
	# https://w3c.github.io/csvw/tests/manifest-validation#test200 
	Scenario: manifest-validation#test200 length > maxLength 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test200-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test200-metadata.json 

	# Applications MUST raise an error if length, maxLength, or minLength are specified and the base datatype is not string or one of its subtypes, or a binary type. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test201 
	Scenario: manifest-validation#test201 length on date 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test201-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test201-metadata.json 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test202 
	Scenario: manifest-validation#test202 float matching constraints 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test202-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test202-metadata.json 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test203 
	Scenario: manifest-validation#test203 float value constraint not matching minimum 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test203-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test203-metadata.json 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test204 
	Scenario: manifest-validation#test204 float value constraint not matching maximum 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test204-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test204-metadata.json 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test205 
	Scenario: manifest-validation#test205 float value constraint not matching minInclusive 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test205-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test205-metadata.json 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test206 
	Scenario: manifest-validation#test206 float value constraint not matching minExclusive 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test206-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test206-metadata.json 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test207 
	Scenario: manifest-validation#test207 float value constraint not matching maxInclusive 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test207-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test207-metadata.json 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test208 
	Scenario: manifest-validation#test208 float value constraint not matching maxExclusive 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test208-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test208-metadata.json 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test209 
	Scenario: manifest-validation#test209 date matching constraints 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test209-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test209-metadata.json 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test210 
	Scenario: manifest-validation#test210 date value constraint not matching minimum 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test210-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test210-metadata.json 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test211 
	Scenario: manifest-validation#test211 date value constraint not matching maximum 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test211-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test211-metadata.json 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test212 
	Scenario: manifest-validation#test212 date value constraint not matching minInclusive 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test212-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test212-metadata.json 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test213 
	Scenario: manifest-validation#test213 date value constraint not matching minExclusive 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test213-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test213-metadata.json 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test214 
	Scenario: manifest-validation#test214 date value constraint not matching maxInclusive 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test214-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test214-metadata.json 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test215 
	Scenario: manifest-validation#test215 date value constraint not matching maxExclusive 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test215-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test215-metadata.json 

	# Applications MUST raise an error if both minInclusive and minExclusive are specified, or if both maxInclusive and maxExclusive are specified.  
	# https://w3c.github.io/csvw/tests/manifest-validation#test216 
	Scenario: manifest-validation#test216 minInclusive and minExclusive 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test216-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test216-metadata.json 

	# Applications MUST raise an error if both minInclusive and minExclusive are specified, or if both maxInclusive and maxExclusive are specified.  
	# https://w3c.github.io/csvw/tests/manifest-validation#test217 
	Scenario: manifest-validation#test217 maxInclusive and maxExclusive 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test217-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test217-metadata.json 

	# Applications MUST raise an error if both minInclusive and maxInclusive are specified and maxInclusive is less than minInclusive, or if both minInclusive and maxExclusive are specified and maxExclusive is less than or equal to minInclusive. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test218 
	Scenario: manifest-validation#test218 maxInclusive less than minInclusive 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test218-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test218-metadata.json 

	# Applications MUST raise an error if both minInclusive and maxInclusive are specified and maxInclusive is less than minInclusive, or if both minInclusive and maxExclusive are specified and maxExclusive is less than or equal to minInclusive. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test219 
	Scenario: manifest-validation#test219 maxExclusive = minInclusive 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test219-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test219-metadata.json 

	# Applications MUST raise an error if both minExclusive and maxExclusive are specified and maxExclusive is less than minExclusive, or if both minExclusive and maxInclusive are specified and maxInclusive is less than or equal to minExclusive. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test220 
	Scenario: manifest-validation#test220 maxExclusive less than minExclusive 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test220-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test220-metadata.json 

	# Applications MUST raise an error if both minExclusive and maxExclusive are specified and maxExclusive is less than minExclusive, or if both minExclusive and maxInclusive are specified and maxInclusive is less than or equal to minExclusive. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test221 
	Scenario: manifest-validation#test221 maxInclusive = minExclusive 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test221-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test221-metadata.json 

	# Applications MUST raise an error if minimum, minInclusive, maximum, maxInclusive, minExclusive, or maxExclusive are specified and the base datatype is not a numeric, date/time, or duration type. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test222 
	Scenario: manifest-validation#test222 string datatype with minimum 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test222-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test222-metadata.json 

	# Applications MUST raise an error if minimum, minInclusive, maximum, maxInclusive, minExclusive, or maxExclusive are specified and the base datatype is not a numeric, date/time, or duration type. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test223 
	Scenario: manifest-validation#test223 string datatype with maxium 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test223-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test223-metadata.json 

	# Applications MUST raise an error if minimum, minInclusive, maximum, maxInclusive, minExclusive, or maxExclusive are specified and the base datatype is not a numeric, date/time, or duration type. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test224 
	Scenario: manifest-validation#test224 string datatype with minInclusive 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test224-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test224-metadata.json 

	# Applications MUST raise an error if minimum, minInclusive, maximum, maxInclusive, minExclusive, or maxExclusive are specified and the base datatype is not a numeric, date/time, or duration type. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test225 
	Scenario: manifest-validation#test225 string datatype with maxInclusive 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test225-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test225-metadata.json 

	# Applications MUST raise an error if minimum, minInclusive, maximum, maxInclusive, minExclusive, or maxExclusive are specified and the base datatype is not a numeric, date/time, or duration type. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test226 
	Scenario: manifest-validation#test226 string datatype with minExclusive 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test226-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test226-metadata.json 

	# Applications MUST raise an error if minimum, minInclusive, maximum, maxInclusive, minExclusive, or maxExclusive are specified and the base datatype is not a numeric, date/time, or duration type. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test227 
	Scenario: manifest-validation#test227 string datatype with maxExclusive 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test227-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test227-metadata.json 

	# If the value is a list, the constraint applies to each element of the list. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test228 
	Scenario: manifest-validation#test228 length with separator 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test228-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test228-metadata.json 

	# If the value is a list, the constraint applies to each element of the list. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test229 
	Scenario: manifest-validation#test229 matching minLength with separator 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test229-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test229-metadata.json 

	# If the value is a list, the constraint applies to each element of the list. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test230 
	Scenario: manifest-validation#test230 failing minLength with separator 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test230-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test230-metadata.json 

	# As defined in [tabular-data-model], validators MUST check that each row has a unique combination of values of cells in the indicated columns. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test231 
	Scenario: manifest-validation#test231 single column primaryKey success 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test231-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test231-metadata.json 

	# Validators MUST raise errors if there is more than one row with the same primary key 
	# https://w3c.github.io/csvw/tests/manifest-validation#test232 
	Scenario: manifest-validation#test232 single column primaryKey violation 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test232-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test232-metadata.json 

	# As defined in [tabular-data-model], validators MUST check that each row has a unique combination of values of cells in the indicated columns. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test233 
	Scenario: manifest-validation#test233 multiple column primaryKey success 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test233-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test233-metadata.json 

	# Validators MUST raise errors if there is more than one row with the same primary key 
	# https://w3c.github.io/csvw/tests/manifest-validation#test234 
	Scenario: manifest-validation#test234 multiple column primaryKey violation 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test234-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test234-metadata.json 

	# if row titles is not null, insert any titles specified for the row. For each value, tv, of the row titles annotation 
	# https://w3c.github.io/csvw/tests/manifest-validation#test235 
	Scenario: manifest-validation#test235 rowTitles on one column 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test235-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test235-metadata.json 

	# if row titles is not null, insert any titles specified for the row. For each value, tv, of the row titles annotation 
	# https://w3c.github.io/csvw/tests/manifest-validation#test236 
	Scenario: manifest-validation#test236 rowTitles on multiple columns 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test236-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test236-metadata.json 

	# if row titles is not null, insert any titles specified for the row. For each value, tv, of the row titles annotation 
	# https://w3c.github.io/csvw/tests/manifest-validation#test237 
	Scenario: manifest-validation#test237 rowTitles on one column (minimal) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test237-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test237-metadata.json 

	# it must be the name of one of the built-in datatypes defined in section 5.11.1 Built-in Datatypes 
	# https://w3c.github.io/csvw/tests/manifest-validation#test238 
	Scenario: manifest-validation#test238 datatype value an absolute URL 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test238-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test238-metadata.json 

	# If included, @id is a link property that identifies the datatype described by this datatype description. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test242 
	Scenario: manifest-validation#test242 datatype @id an absolute URL 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test242-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test242-metadata.json 

	# It MUST NOT start with `_:`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test243 
	Scenario: manifest-validation#test243 invalid datatype @id 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test243-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test243-metadata.json 

	# It MUST NOT be the URL of a built-in datatype. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test244 
	Scenario: manifest-validation#test244 invalid datatype @id 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test244-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test244-metadata.json 

	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test245 
	Scenario: manifest-validation#test245 date format (valid time combinations with formats and milliseconds) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test245-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test245-metadata.json 

	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test246 
	Scenario: manifest-validation#test246 date format (valid dateTime combinations with formats and milliseconds) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test246-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test246-metadata.json 

	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test247 
	Scenario: manifest-validation#test247 date format (extra milliseconds) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test247-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test247-metadata.json 

	# No Unicode normalization (as specified in [UAX15]) is applied to these string values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test248 
	Scenario: manifest-validation#test248 Unicode in non-Normalized form 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test248-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test248-metadata.json 

	# When comparing URLs, processors MUST use Syntax-Based Normalization as defined in [[RFC3968]]. Processors perform Scheme-Based Normalization for HTTP (80) and HTTPS (443) 
	# https://w3c.github.io/csvw/tests/manifest-validation#test249 
	Scenario: manifest-validation#test249 http normalization 
	# As defined in [tabular-data-model], validators MUST check that, for each row, the combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test250 
	Scenario: manifest-validation#test250 valid case 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test250-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test250-metadata.json 

	# As defined in [tabular-data-model], validators MUST check that, for each row, the combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test251 
	Scenario: manifest-validation#test251 missing source reference 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test251-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test251-metadata.json 

	# As defined in [tabular-data-model], validators MUST check that, for each row, the combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test252 
	Scenario: manifest-validation#test252 missing destination reference column 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test252-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test252-metadata.json 

	# As defined in [tabular-data-model], validators MUST check that, for each row, the combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test253 
	Scenario: manifest-validation#test253 missing destination table 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test253-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test253-metadata.json 

	# The combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test254 
	Scenario: manifest-validation#test254 foreign key single column same table 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test254-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test254-metadata.json 

	# The combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test255 
	Scenario: manifest-validation#test255 foreign key single column different table 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test255-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test255-metadata.json 

	# The combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test256 
	Scenario: manifest-validation#test256 foreign key multiple columns 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test256-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test256-metadata.json 

	# Validators MUST raise errors for each row that does not have a referenced row for each of the foreign keys on the table in which the row appears 
	# https://w3c.github.io/csvw/tests/manifest-validation#test257 
	Scenario: manifest-validation#test257 foreign key no referenced row 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test257-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test257-metadata.json 

	# Validators MUST raise errors for each row that does not have a referenced row for each of the foreign keys on the table in which the row appears 
	# https://w3c.github.io/csvw/tests/manifest-validation#test258 
	Scenario: manifest-validation#test258 foreign key multiple referenced rows 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test258-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test258-metadata.json 

	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test259 
	Scenario: manifest-validation#test259 tree-ops example with csvm.json (w3.org/.well-known/csvm) 
	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test260 
	Scenario: manifest-validation#test260 tree-ops example with {+url}.json (w3.org/.well-known/csvm) 
	# Applications MUST raise an error if both minLength and maxLength are specified and minLength is greater than maxLength. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test261 
	Scenario: manifest-validation#test261 maxLength less than minLength 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test261-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test261-metadata.json 

	# The value of any member of `@type` MUST be either a _term_ defined in [csvw-context], a _prefixed name_ where the prefix is a term defined in [csvw-context], or an absolute URL. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test263 
	Scenario: manifest-validation#test263 @type on a common property can be a built-in type 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test263-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test263-metadata.json 

	# The value of any member of `@type` MUST be either a _term_ defined in [csvw-context], a _prefixed name_ where the prefix is a term defined in [csvw-context], or an absolute URL. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test264 
	Scenario: manifest-validation#test264 @type on a common property can be a CURIE if the prefix is one of the built-in ones 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test264-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test264-metadata.json 

	# Processors MUST issue a warning if a property is set to an invalid value type 
	# https://w3c.github.io/csvw/tests/manifest-validation#test266 
	Scenario: manifest-validation#test266 `null` contains an array of (valid) string & (invalid) numeric values 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test266-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test266-metadata.json 

	# It MUST NOT start with `_:` and it MUST NOT be the URL of a built-in datatype. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test267 
	Scenario: manifest-validation#test267 @id on datatype is invalid (eg starts with _:) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test267-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test267-metadata.json 

	# An atomic property that contains a single string: the name of one of the built-in datatypes, as listed above (and which are defined as terms in the default context). Its default is string. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test268 
	Scenario: manifest-validation#test268 `base` missing on datatype (defaults to string) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test268-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test268-metadata.json 

	# If the datatype base for a cell is `boolean`, the datatype format annotation provides the true value followed by the false value, separated by `|`. If the format does not follow this syntax, implementations MUST issue a warning and proceed as if no format had been provided. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test269 
	Scenario: manifest-validation#test269 `format` for a boolean datatype is a string but in the wrong form (eg YN) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test269-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test269-metadata.json 

	# All terms used within a metadata document MUST be defined in [csvw-context] defined for this specification 
	# https://w3c.github.io/csvw/tests/manifest-validation#test270 
	Scenario: manifest-validation#test270 transformation includes an invalid property (eg foo) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test270-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test270-metadata.json 

	# A foreign key definition is a JSON object that must contain only the following properties. . . 
	# https://w3c.github.io/csvw/tests/manifest-validation#test271 
	Scenario: manifest-validation#test271 foreign key includes an invalid property (eg `dc:description`) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test271-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test271-metadata.json 

	# A foreign key definition is a JSON object that must contain only the following properties. . . 
	# https://w3c.github.io/csvw/tests/manifest-validation#test272 
	Scenario: manifest-validation#test272 foreign key reference includes an invalid property (eg `dc:description`) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test272-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test272-metadata.json 

	# If present, its value MUST be a string that is interpreted as a URL which is resolved against the location of the metadata document to provide the **base URL** for other URLs in the metadata document. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test273 
	Scenario: manifest-validation#test273 `@base` set in `@context` overriding eg CSV location 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test273-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test273-metadata.json 

	# The `@context` MUST have one of the following values: An array composed of a string followed by an object, where the string is `http://www.w3.org/ns/csvw` and the object represents a local context definition, which is restricted to contain either or both of the following members. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test274 
	Scenario: manifest-validation#test274 `@context` object includes properties other than `@base` and `@language` 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test274-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test274-metadata.json 

	# Table Group may only use defined properties. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test275 
	Scenario: manifest-validation#test275 property acceptable on column appears on table group 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test275-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test275-metadata.json 

	# Table may only use defined properties. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test276 
	Scenario: manifest-validation#test276 property acceptable on column appears on table 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test276-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test276-metadata.json 

	# Column may only use defined properties. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test277 
	Scenario: manifest-validation#test277 property acceptable on table appears on column 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test277-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test277-metadata.json 

	# Two schemas are compatible if they have the same number of non-virtual column descriptions, and the non-virtual column descriptions at the same index within each are compatible with each other. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test278 
	Scenario: manifest-validation#test278 CSV has more headers than there are columns in the metadata 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test278-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test278-metadata.json 

	# Value MUST be a valid xsd:duration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test279 
	Scenario: manifest-validation#test279 duration not matching xsd pattern 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test279-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test279-metadata.json 

	# Value MUST be a valid xsd:dayTimeDuration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test280 
	Scenario: manifest-validation#test280 dayTimeDuration not matching xsd pattern 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test280-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test280-metadata.json 

	# Value MUST be a valid xsd:yearMonthDuration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test281 
	Scenario: manifest-validation#test281 yearMonthDuration not matching xsd pattern 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test281-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test281-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test282 
	Scenario: manifest-validation#test282 valid number patterns 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test282-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test282-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test283 
	Scenario: manifest-validation#test283 valid number patterns (signs and percent/permille) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test283-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test283-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test284 
	Scenario: manifest-validation#test284 valid number patterns (grouping) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test284-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test284-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test285 
	Scenario: manifest-validation#test285 valid number patterns (fractional grouping) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test285-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test285-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test286 
	Scenario: manifest-validation#test286 invalid ##0 1,234 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test286-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test286-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test287 
	Scenario: manifest-validation#test287 invalid ##0 123.4 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test287-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test287-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test288 
	Scenario: manifest-validation#test288 invalid #,#00 1 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test288-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test288-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test289 
	Scenario: manifest-validation#test289 invalid #,#00 1234 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test289-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test289-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test290 
	Scenario: manifest-validation#test290 invalid #,#00 12,34 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test290-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test290-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test291 
	Scenario: manifest-validation#test291 invalid #,#00 12,34,567 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test291-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test291-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test292 
	Scenario: manifest-validation#test292 invalid #,##,#00 1 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test292-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test292-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test293 
	Scenario: manifest-validation#test293 invalid #,##,#00 1234 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test293-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test293-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test294 
	Scenario: manifest-validation#test294 invalid #,##,#00 12,34 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test294-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test294-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test295 
	Scenario: manifest-validation#test295 invalid #,##,#00 1,234,567 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test295-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test295-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test296 
	Scenario: manifest-validation#test296 invalid #0.# 12.34 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test296-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test296-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test297 
	Scenario: manifest-validation#test297 invalid #0.# 1,234.5 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test297-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test297-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test298 
	Scenario: manifest-validation#test298 invalid #0.0 1 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test298-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test298-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test299 
	Scenario: manifest-validation#test299 invalid #0.0 12.34 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test299-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test299-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test300 
	Scenario: manifest-validation#test300 invalid #0.0# 1 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test300-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test300-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test301 
	Scenario: manifest-validation#test301 invalid #0.0# 12.345 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test301-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test301-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test302 
	Scenario: manifest-validation#test302 invalid #0.0#,# 1 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test302-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test302-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test303 
	Scenario: manifest-validation#test303 invalid #0.0#,# 12.345 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test303-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test303-metadata.json 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test304 
	Scenario: manifest-validation#test304 invalid #0.0#,# 12.34,567 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test304-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test304-metadata.json 

	# Values in separate columns using the same propertyUrl are kept in proper relative order. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test305 
	Scenario: manifest-validation#test305 multiple values with same subject and property (unordered) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test305-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test305-metadata.json 

	# Values in separate columns using the same propertyUrl are kept in proper relative order. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test306 
	Scenario: manifest-validation#test306 multiple values with same subject and property (ordered) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test306-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test306-metadata.json 

	# Values in separate columns using the same propertyUrl are kept in proper relative order. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test307 
	Scenario: manifest-validation#test307 multiple values with same subject and property (ordered and unordered) 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test307-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test307-metadata.json 

	# If the value of the datatype property is a string, it must be one of the built-in datatypes. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test308 
	Scenario: manifest-validation#test308 invalid datatype string 
		Given I have a metadata file called https://w3c.github.io/csvw/tests/test308-metadata.json 
		And the metadata is stored at the url https://w3c.github.io/csvw/tests/test308-metadata.json 

