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

	# countries.json from metadata minimal output 
	# https://w3c.github.io/csvw/tests/manifest-validation#test031 
	Scenario: manifest-validation#test031 countries.json example minimal output 

	# events-listing example from metadata, virtual columns and multiple subjects per row 
	# https://w3c.github.io/csvw/tests/manifest-validation#test032 
	Scenario: manifest-validation#test032 events-listing.csv example 

	# events-listing example from metadata, virtual columns and multiple subjects per row; minimal output 
	# https://w3c.github.io/csvw/tests/manifest-validation#test033 
	Scenario: manifest-validation#test033 events-listing.csv minimal output 

	# Public Sector Roles example with referenced schemas. Validation fails because organization.csv intentionally contains an invalid reference. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test034 
	Scenario: manifest-validation#test034 roles example 

	# Public Sector Roles example with referenced schemas; minimal output. Validation fails because organization.csv intentionally contains an invalid reference. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test035 
	Scenario: manifest-validation#test035 roles minimal 

	# tree-ops extended example 
	# https://w3c.github.io/csvw/tests/manifest-validation#test036 
	Scenario: manifest-validation#test036 tree-ops-ext example 

	# tree-ops extended example; minimal output 
	# https://w3c.github.io/csvw/tests/manifest-validation#test037 
	Scenario: manifest-validation#test037 tree-ops-ext minimal 

	# Setting inherited properties at different levels inherit to cell 
	# https://w3c.github.io/csvw/tests/manifest-validation#test038 
	Scenario: manifest-validation#test038 inherited properties propagation 

	# Different combinations of valid inherited properties 
	# https://w3c.github.io/csvw/tests/manifest-validation#test039 
	Scenario: manifest-validation#test039 valid inherited properties 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test040 
	Scenario: manifest-validation#test040 invalid null 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test041 
	Scenario: manifest-validation#test041 invalid lang 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test042 
	Scenario: manifest-validation#test042 invalid textDirection 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test043 
	Scenario: manifest-validation#test043 invalid separator 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test044 
	Scenario: manifest-validation#test044 invalid ordered 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test045 
	Scenario: manifest-validation#test045 invalid default 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test046 
	Scenario: manifest-validation#test046 invalid dataype 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test047 
	Scenario: manifest-validation#test047 invalid aboutUrl 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test048 
	Scenario: manifest-validation#test048 invalid propertyUrl 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test049 
	Scenario: manifest-validation#test049 invalid valueUrl 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test059 
	Scenario: manifest-validation#test059 dialect: invalid commentPrefix 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test060 
	Scenario: manifest-validation#test060 dialect: invalid delimiter 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test061 
	Scenario: manifest-validation#test061 dialect: invalid doubleQuote 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test062 
	Scenario: manifest-validation#test062 dialect: invalid encoding 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test063 
	Scenario: manifest-validation#test063 dialect: invalid header 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test065 
	Scenario: manifest-validation#test065 dialect: invalid headerRowCount 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test066 
	Scenario: manifest-validation#test066 dialect: invalid lineTerminators 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test067 
	Scenario: manifest-validation#test067 dialect: invalid quoteChar 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test068 
	Scenario: manifest-validation#test068 dialect: invalid skipBlankRows 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test069 
	Scenario: manifest-validation#test069 dialect: invalid skipColumns 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test070 
	Scenario: manifest-validation#test070 dialect: invalid skipInitialSpace 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test071 
	Scenario: manifest-validation#test071 dialect: invalid skipRows 

	# If a property has a value that is not permitted by this specification, then if a default value is provided for that property, compliant applications MUST use that default value and MUST generate a warning. If no default value is provided for that property, compliant applications MUST generate a warning and behave as if the property had not been specified. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test072 
	Scenario: manifest-validation#test072 dialect: invalid trim 

	# The value of `@language` MUST be a valid `BCP47` language code 
	# https://w3c.github.io/csvw/tests/manifest-validation#test073 
	Scenario: manifest-validation#test073 invalid @language 

	# Compliant application MUST raise an error if this array does not contain one or more `table descriptions`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test074 
	Scenario: manifest-validation#test074 empty tables 

	# An atomic property that MUST have a single string value that is one of "rtl", "ltr" or "auto". 
	# https://w3c.github.io/csvw/tests/manifest-validation#test075 
	Scenario: manifest-validation#test075 invalid tableGroup tableDirection 

	# An atomic property that MUST have a single string value that is one of "rtl", "ltr" or "auto". 
	# https://w3c.github.io/csvw/tests/manifest-validation#test076 
	Scenario: manifest-validation#test076 invalid table tableDirection 

	# It MUST NOT start with `_:`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test077 
	Scenario: manifest-validation#test077 invalid tableGroup @id 

	# It MUST NOT start with `_:`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test078 
	Scenario: manifest-validation#test078 invalid table @id 

	# It MUST NOT start with `_:`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test079 
	Scenario: manifest-validation#test079 invalid schema @id 

	# It MUST NOT start with `_:`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test080 
	Scenario: manifest-validation#test080 invalid column @id 

	# It MUST NOT start with `_:`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test081 
	Scenario: manifest-validation#test081 invalid dialect @id 

	# It MUST NOT start with `_:`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test082 
	Scenario: manifest-validation#test082 invalid template @id 

	# If included `@type` MUST be `TableGroup` 
	# https://w3c.github.io/csvw/tests/manifest-validation#test083 
	Scenario: manifest-validation#test083 invalid tableGroup @type 

	# If included `@type` MUST be `TableGroup` 
	# https://w3c.github.io/csvw/tests/manifest-validation#test084 
	Scenario: manifest-validation#test084 invalid table @type 

	# If included `@type` MUST be `TableGroup` 
	# https://w3c.github.io/csvw/tests/manifest-validation#test085 
	Scenario: manifest-validation#test085 invalid schema @type 

	# If included `@type` MUST be `TableGroup` 
	# https://w3c.github.io/csvw/tests/manifest-validation#test086 
	Scenario: manifest-validation#test086 invalid column @type 

	# If included `@type` MUST be `Dialect` 
	# https://w3c.github.io/csvw/tests/manifest-validation#test087 
	Scenario: manifest-validation#test087 invalid dialect @type 

	# If included `@type` MUST be `Template` 
	# https://w3c.github.io/csvw/tests/manifest-validation#test088 
	Scenario: manifest-validation#test088 invalid transformation @type 

	# The `tables` property is required in a `TableGroup` 
	# https://w3c.github.io/csvw/tests/manifest-validation#test089 
	Scenario: manifest-validation#test089 missing tables in TableGroup 

	# The `url` property is required in a `Table` 
	# https://w3c.github.io/csvw/tests/manifest-validation#test090 
	Scenario: manifest-validation#test090 missing url in Table 

	# All compliant applications MUST generate errors and stop processing if a metadata document does not use valid JSON syntax 
	# https://w3c.github.io/csvw/tests/manifest-validation#test092 
	Scenario: manifest-validation#test092 invalid JSON 

	# Compliant applications MUST ignore properties (aside from _common properties_) which are not defined in this specification and MUST generate a warning when they are encoutered 
	# https://w3c.github.io/csvw/tests/manifest-validation#test093 
	Scenario: manifest-validation#test093 undefined properties 

	# Any items within an array that are not valid objects of the type expected are ignored 
	# https://w3c.github.io/csvw/tests/manifest-validation#test094 
	Scenario: manifest-validation#test094 inconsistent array values: tables 

	# Any items within an array that are not valid objects of the type expected are ignored 
	# https://w3c.github.io/csvw/tests/manifest-validation#test095 
	Scenario: manifest-validation#test095 inconsistent array values: transformations 

	# Any items within an array that are not valid objects of the type expected are ignored 
	# https://w3c.github.io/csvw/tests/manifest-validation#test096 
	Scenario: manifest-validation#test096 inconsistent array values: columns 

	# Any items within an array that are not valid objects of the type expected are ignored 
	# https://w3c.github.io/csvw/tests/manifest-validation#test097 
	Scenario: manifest-validation#test097 inconsistent array values: foreignKeys 

	# If the supplied value of an array property is not an array (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been supplied with an empty array. Compliant application MUST raise an error if this array does not contain one or more table descriptions. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test098 
	Scenario: manifest-validation#test098 inconsistent array values: tables 

	# If the supplied value of an array property is not an array (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been supplied with an empty array 
	# https://w3c.github.io/csvw/tests/manifest-validation#test099 
	Scenario: manifest-validation#test099 inconsistent array values: transformations 

	# If the supplied value of an array property is not an array (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been supplied with an empty array 
	# https://w3c.github.io/csvw/tests/manifest-validation#test100 
	Scenario: manifest-validation#test100 inconsistent array values: columns 

	# If the supplied value of an array property is not an array (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been supplied with an empty array 
	# https://w3c.github.io/csvw/tests/manifest-validation#test101 
	Scenario: manifest-validation#test101 inconsistent array values: foreignKeys 

	# If the supplied value of an array property is not an array (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been supplied with an empty array 
	# https://w3c.github.io/csvw/tests/manifest-validation#test102 
	Scenario: manifest-validation#test102 inconsistent link values: @id 

	# If the supplied value of an array property is not an array (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been supplied with an empty array 
	# https://w3c.github.io/csvw/tests/manifest-validation#test103 
	Scenario: manifest-validation#test103 inconsistent link values: url 

	# The referenced description object MUST have a name property 
	# https://w3c.github.io/csvw/tests/manifest-validation#test104 
	Scenario: manifest-validation#test104 invalid columnReference 

	# The referenced description object MUST have a name property 
	# https://w3c.github.io/csvw/tests/manifest-validation#test105 
	Scenario: manifest-validation#test105 invalid primaryKey 

	# If the supplied value of an object property is not a string or object (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been specified as an object with no properties. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test106 
	Scenario: manifest-validation#test106 invalid dialect 

	# If the supplied value of an object property is not a string or object (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been specified as an object with no properties. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test107 
	Scenario: manifest-validation#test107 invalid tableSchema 

	# If the supplied value of an object property is not a string or object (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been specified as an object with no properties. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test108 
	Scenario: manifest-validation#test108 invalid reference 

	# Natural Language properties may be objects whose properties MUST be language codes as defined by [BCP47] and whose values are either strings or arrays, providing natural language strings in that language. Validation fails because without a title, the metadata is incompatible with the CSV, which isn't a problem when not validating. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test109 
	Scenario: manifest-validation#test109 titles with invalid language 

	# Natural Language properties may be objects whose properties MUST be language codes as defined by [BCP47] and whose values are either strings or arrays, providing natural language strings in that language 
	# https://w3c.github.io/csvw/tests/manifest-validation#test110 
	Scenario: manifest-validation#test110 titles with non-string values 

	# If the supplied value of a natural language property is not a string, array or object (eg if it is an integer), compliant applications MUST issue a warning and proceed as if the property had been specified as an empty array. Validation fails because without a title, the metadata is incompatible with the CSV, which isn't a problem when not validating. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test111 
	Scenario: manifest-validation#test111 titles with invalid value 

	# If the supplied value is an array, any items in that array that are not strings MUST be ignored 
	# https://w3c.github.io/csvw/tests/manifest-validation#test112 
	Scenario: manifest-validation#test112 titles with non-string array values 

	# Atomic properties: Processors MUST issue a warning if a property is set to an invalid value type 
	# https://w3c.github.io/csvw/tests/manifest-validation#test113 
	Scenario: manifest-validation#test113 invalid suppressOutput 

	# Atomic properties: Processors MUST issue a warning if a property is set to an invalid value type 
	# https://w3c.github.io/csvw/tests/manifest-validation#test114 
	Scenario: manifest-validation#test114 invalid name 

	# Atomic properties: Processors MUST issue a warning if a property is set to an invalid value type 
	# https://w3c.github.io/csvw/tests/manifest-validation#test115 
	Scenario: manifest-validation#test115 invalid virtual 

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

	# if the string is the same as any one of the values of the column null annotation, then the resulting value is null. If the column separator annotation is null and the column required annotation is true, add an error to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test126 
	Scenario: manifest-validation#test126 required column with cell matching null 

	# if TM is not compatible with EM validators MUST raise an error, other processors MUST generate a warning and continue processing 
	# https://w3c.github.io/csvw/tests/manifest-validation#test127 
	Scenario: manifest-validation#test127 incompatible table 

	# The name properties of the column descriptions MUST be unique within a given table description. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test128 
	Scenario: manifest-validation#test128 duplicate column names 

	# This (name) MUST be a string and this property has no default value, which means it MUST be ignored if the supplied value is not a string. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test129 
	Scenario: manifest-validation#test129 columnn name as integer 

	# column names are restricted as defined in Variables in [URI-TEMPLATE]  
	# https://w3c.github.io/csvw/tests/manifest-validation#test130 
	Scenario: manifest-validation#test130 invalid column name 

	# column names are restricted ... names beginning with '_' are reserved by this specification and MUST NOT be used within metadata documents. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test131 
	Scenario: manifest-validation#test131 invalid column name 

	# If there is no name property defined on this column, the first titles value having the same language tag as default language, or und or if no default language is specified, becomes the name annotation for the described column. This annotation MUST be percent-encoded as necessary to conform to the syntactic requirements defined in [RFC3986] 
	# https://w3c.github.io/csvw/tests/manifest-validation#test132 
	Scenario: manifest-validation#test132 name annotation from title percent encoded 

	# If present, a virtual column MUST appear after all other non-virtual column definitions. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test133 
	Scenario: manifest-validation#test133 virtual before non-virtual 

	# A metadata document MUST NOT add a new context 
	# https://w3c.github.io/csvw/tests/manifest-validation#test134 
	Scenario: manifest-validation#test134 context in common property 

	# Values MUST NOT use list objects or set objects. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test135 
	Scenario: manifest-validation#test135 @list value 

	# Values MUST NOT use list objects or set objects. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test136 
	Scenario: manifest-validation#test136 @set value 

	# The value of any @id or @type contained within a metadata document MUST NOT be a blank node. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test137 
	Scenario: manifest-validation#test137 @type out of range (as datatype) 

	# The value of any @id or @type contained within a metadata document MUST NOT be a blank node. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test138 
	Scenario: manifest-validation#test138 @type out of range (as node type) 

	# The value of any member of @type MUST be either a term defined in [csvw-context], a prefixed name where the prefix is a term defined in [csvw-context], or an absolute URL. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test139 
	Scenario: manifest-validation#test139 @type out of range (as node type) - string 

	# The value of any member of @type MUST be either a term defined in [csvw-context], a prefixed name where the prefix is a term defined in [csvw-context], or an absolute URL. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test140 
	Scenario: manifest-validation#test140 @type out of range (as node type) - integer 

	# The value of any @id or @type contained within a metadata document MUST NOT be a blank node. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test141 
	Scenario: manifest-validation#test141 @id out of range (as node type) - bnode 

	# If a @value property is used on an object, that object MUST NOT have any other properties aside from either @type or @language, and MUST NOT have both @type and @language as properties. The value of the @value property MUST be a string, number, or boolean value. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test142 
	Scenario: manifest-validation#test142 @value with @language and @type 

	# If a @value property is used on an object, that object MUST NOT have any other properties aside from either @type or @language, and MUST NOT have both @type and @language as properties. The value of the @value property MUST be a string, number, or boolean value. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test143 
	Scenario: manifest-validation#test143 @value with extra properties 

	# A @language property MUST NOT be used on an object unless it also has a @value property. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test144 
	Scenario: manifest-validation#test144 @language outside of @value 

	# If a @language property is used, it MUST have a string value that adheres to the syntax defined in [BCP47], or be null. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test145 
	Scenario: manifest-validation#test145 @value with invalid @language 

	# Aside from @value, @type, @language, and @id, the properties used on an object MUST NOT start with @. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test146 
	Scenario: manifest-validation#test146 Invalid faux-keyword 

	# If there is a non-empty case-sensitive intersection between the titles values, where matches MUST have a matching language; `und` matches any language, and languages match if they are equal when truncated, as defined in [BCP47], to the length of the shortest language tag. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test147 
	Scenario: manifest-validation#test147 title incompatible with title on case 

	# If there is a non-empty case-sensitive intersection between the titles values, where matches MUST have a matching language; `und` matches any language, and languages match if they are equal when truncated, as defined in [BCP47], to the length of the shortest language tag. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test148 
	Scenario: manifest-validation#test148 title incompatible with title on language 

	# If there is a non-empty case-sensitive intersection between the titles values, where matches MUST have a matching language; `und` matches any language, and languages match if they are equal when truncated, as defined in [BCP47], to the length of the shortest language tag. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test149 
	Scenario: manifest-validation#test149 title compatible with title on less specific language 

	# If the value of this property is a string, it MUST be one of the built-in datatypes defined in section 5.11.1 Built-in Datatypes or an absolute URL 
	# https://w3c.github.io/csvw/tests/manifest-validation#test150 
	Scenario: manifest-validation#test150 non-builtin datatype (datatype value) 

	# If the value of this property is a string, it MUST be one of the built-in datatypes 
	# https://w3c.github.io/csvw/tests/manifest-validation#test151 
	Scenario: manifest-validation#test151 non-builtin datatype (base value) 

	# If the datatype base is not numeric, boolean, a date/time type, or a duration type, the datatype format annotation provides a regular expression for the string values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test152 
	Scenario: manifest-validation#test152 string format (valid combinations) 

	# If the datatype base is not numeric, boolean, a date/time type, or a duration type, the datatype format annotation provides a regular expression for the string values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test153 
	Scenario: manifest-validation#test153 string format (bad format string) 

	# If the datatype base is not numeric, boolean, a date/time type, or a duration type, the datatype format annotation provides a regular expression for the string values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test154 
	Scenario: manifest-validation#test154 string format (value not matching format) 

	# If the datatype format annotation is a single string, this is interpreted in the same way as if it were an object with a pattern property whose value is that string 
	# https://w3c.github.io/csvw/tests/manifest-validation#test155 
	Scenario: manifest-validation#test155 number format (valid combinations) 

	# If the datatype format annotation is a single string, this is interpreted in the same way as if it were an object with a pattern property whose value is that string 
	# https://w3c.github.io/csvw/tests/manifest-validation#test156 
	Scenario: manifest-validation#test156 number format (bad format string) 

	# If the datatype format annotation is a single string, this is interpreted in the same way as if it were an object with a pattern property whose value is that string 
	# https://w3c.github.io/csvw/tests/manifest-validation#test157 
	Scenario: manifest-validation#test157 number format (value not matching format) 

	# Numeric dataype with object format 
	# https://w3c.github.io/csvw/tests/manifest-validation#test158 
	Scenario: manifest-validation#test158 number format (valid combinations) 

	# If the datatype format annotation is a single string, this is interpreted in the same way as if it were an object with a pattern property whose value is that string 
	# https://w3c.github.io/csvw/tests/manifest-validation#test159 
	Scenario: manifest-validation#test159 number format (bad pattern format string) 

	# Implementations MUST add a validation error to the errors annotation for the cell if the string being parsed 
	# https://w3c.github.io/csvw/tests/manifest-validation#test160 
	Scenario: manifest-validation#test160 number format (not matching values with pattern) 

	# Implementations MUST add a validation error to the errors annotation for the cell if the string being parsed 
	# https://w3c.github.io/csvw/tests/manifest-validation#test161 
	Scenario: manifest-validation#test161 number format (not matching values without pattern) 

	# Implementations MUST add a validation error to the errors annotation for the cell if the string being parsed contains two consecutive groupChar strings 
	# https://w3c.github.io/csvw/tests/manifest-validation#test162 
	Scenario: manifest-validation#test162 numeric format (consecutive groupChar) 

	# Implementations MUST add a validation error to the errors annotation for the cell if the string being parsed contains the decimalChar, if the datatype base is integer or one of its sub-values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test163 
	Scenario: manifest-validation#test163 integer datatype with decimalChar 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, if the datatype base is decimal or one of its sub-values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test164 
	Scenario: manifest-validation#test164 decimal datatype with exponent 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, is one of the special values NaN, INF, or -INF, if the datatype base is decimal or one of its sub-values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test165 
	Scenario: manifest-validation#test165 decimal type with NaN 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, is one of the special values NaN, INF, or -INF, if the datatype base is decimal or one of its sub-values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test166 
	Scenario: manifest-validation#test166 decimal type with INF 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, is one of the special values NaN, INF, or -INF, if the datatype base is decimal or one of its sub-values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test167 
	Scenario: manifest-validation#test167 decimal type with -INF 

	# When parsing the string value of a cell against this format specification, implementations MUST recognise and parse numbers 
	# https://w3c.github.io/csvw/tests/manifest-validation#test168 
	Scenario: manifest-validation#test168 decimal with implicit groupChar 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test169 
	Scenario: manifest-validation#test169 invalid decimal 

	# Implementations MUST use the sign, exponent, percent, and per-mille signs when parsing the string value of a cell to provide the value of the cell 
	# https://w3c.github.io/csvw/tests/manifest-validation#test170 
	Scenario: manifest-validation#test170 decimal with percent 

	# Implementations MUST use the sign, exponent, percent, and per-mille signs when parsing the string value of a cell to provide the value of the cell 
	# https://w3c.github.io/csvw/tests/manifest-validation#test171 
	Scenario: manifest-validation#test171 decimal with per-mille 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test172 
	Scenario: manifest-validation#test172 invalid byte 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test173 
	Scenario: manifest-validation#test173 invald unsignedLong 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test174 
	Scenario: manifest-validation#test174 invalid unsignedShort 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test175 
	Scenario: manifest-validation#test175 invalid unsignedByte 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test176 
	Scenario: manifest-validation#test176 invalid positiveInteger 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test177 
	Scenario: manifest-validation#test177 invalid negativeInteger 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test178 
	Scenario: manifest-validation#test178 invalid nonPositiveInteger 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test179 
	Scenario: manifest-validation#test179 invalid nonNegativeInteger 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test180 
	Scenario: manifest-validation#test180 invalid double 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test181 
	Scenario: manifest-validation#test181 invalid number 

	# Implementations MUST add a validation error to the errors annotation for the cell contains an exponent, does not meet the numeric format defined above 
	# https://w3c.github.io/csvw/tests/manifest-validation#test182 
	Scenario: manifest-validation#test182 invalid float 

	# If the datatype base for a cell is boolean, the datatype format annotation provides the true and false values expected, separated by `|`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test183 
	Scenario: manifest-validation#test183 boolean format (valid combinations) 

	# If the datatype base for a cell is boolean, the datatype format annotation provides the true and false values expected, separated by `|`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test184 
	Scenario: manifest-validation#test184 boolean format (bad format string) 

	# If the datatype base for a cell is boolean, the datatype format annotation provides the true and false values expected, separated by `|`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test185 
	Scenario: manifest-validation#test185 boolean format (value not matching format) 

	# Implementations MUST add a validation error to the errors annotation for the cell if the string being parsed 
	# https://w3c.github.io/csvw/tests/manifest-validation#test186 
	Scenario: manifest-validation#test186 boolean format (not matching datatype) 

	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test187 
	Scenario: manifest-validation#test187 date format (valid native combinations) 

	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test188 
	Scenario: manifest-validation#test188 date format (valid date combinations with formats) 

	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test189 
	Scenario: manifest-validation#test189 date format (valid time combinations with formats) 

	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test190 
	Scenario: manifest-validation#test190 date format (valid dateTime combinations with formats) 

	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test191 
	Scenario: manifest-validation#test191 date format (bad format string) 

	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test192 
	Scenario: manifest-validation#test192 date format (value not matching format) 

	# If the datatype base is a duration type, the datatype format annotation provides a regular expression for the string values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test193 
	Scenario: manifest-validation#test193 duration format (valid combinations) 

	# If the datatype base is a duration type, the datatype format annotation provides a regular expression for the string values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test194 
	Scenario: manifest-validation#test194 duration format (value not matching format) 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test195 
	Scenario: manifest-validation#test195 values with matching length 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test196 
	Scenario: manifest-validation#test196 values with wrong length 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test197 
	Scenario: manifest-validation#test197 values with wrong maxLength 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test198 
	Scenario: manifest-validation#test198 values with wrong minLength 

	# Applications MUST raise an error if both length and minLength are specified and length is less than minLength.  
	# https://w3c.github.io/csvw/tests/manifest-validation#test199 
	Scenario: manifest-validation#test199 length less than minLength 

	# Applications MUST raise an error if both length and maxLength are specified and length is greater than maxLength.  
	# https://w3c.github.io/csvw/tests/manifest-validation#test200 
	Scenario: manifest-validation#test200 length > maxLength 

	# Applications MUST raise an error if length, maxLength, or minLength are specified and the base datatype is not string or one of its subtypes, or a binary type. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test201 
	Scenario: manifest-validation#test201 length on date 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test202 
	Scenario: manifest-validation#test202 float matching constraints 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test203 
	Scenario: manifest-validation#test203 float value constraint not matching minimum 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test204 
	Scenario: manifest-validation#test204 float value constraint not matching maximum 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test205 
	Scenario: manifest-validation#test205 float value constraint not matching minInclusive 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test206 
	Scenario: manifest-validation#test206 float value constraint not matching minExclusive 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test207 
	Scenario: manifest-validation#test207 float value constraint not matching maxInclusive 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test208 
	Scenario: manifest-validation#test208 float value constraint not matching maxExclusive 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test209 
	Scenario: manifest-validation#test209 date matching constraints 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test210 
	Scenario: manifest-validation#test210 date value constraint not matching minimum 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test211 
	Scenario: manifest-validation#test211 date value constraint not matching maximum 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test212 
	Scenario: manifest-validation#test212 date value constraint not matching minInclusive 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test213 
	Scenario: manifest-validation#test213 date value constraint not matching minExclusive 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test214 
	Scenario: manifest-validation#test214 date value constraint not matching maxInclusive 

	# validate the value based on the length constraints described in section 4.6.1 Length Constraints, the value constraints described in section 4.6.2 Value Constraints and the datatype format annotation if one is specified, as described below. If there are any errors, add them to the list of errors for the cell. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test215 
	Scenario: manifest-validation#test215 date value constraint not matching maxExclusive 

	# Applications MUST raise an error if both minInclusive and minExclusive are specified, or if both maxInclusive and maxExclusive are specified.  
	# https://w3c.github.io/csvw/tests/manifest-validation#test216 
	Scenario: manifest-validation#test216 minInclusive and minExclusive 

	# Applications MUST raise an error if both minInclusive and minExclusive are specified, or if both maxInclusive and maxExclusive are specified.  
	# https://w3c.github.io/csvw/tests/manifest-validation#test217 
	Scenario: manifest-validation#test217 maxInclusive and maxExclusive 

	# Applications MUST raise an error if both minInclusive and maxInclusive are specified and maxInclusive is less than minInclusive, or if both minInclusive and maxExclusive are specified and maxExclusive is less than or equal to minInclusive. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test218 
	Scenario: manifest-validation#test218 maxInclusive less than minInclusive 

	# Applications MUST raise an error if both minInclusive and maxInclusive are specified and maxInclusive is less than minInclusive, or if both minInclusive and maxExclusive are specified and maxExclusive is less than or equal to minInclusive. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test219 
	Scenario: manifest-validation#test219 maxExclusive = minInclusive 

	# Applications MUST raise an error if both minExclusive and maxExclusive are specified and maxExclusive is less than minExclusive, or if both minExclusive and maxInclusive are specified and maxInclusive is less than or equal to minExclusive. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test220 
	Scenario: manifest-validation#test220 maxExclusive less than minExclusive 

	# Applications MUST raise an error if both minExclusive and maxExclusive are specified and maxExclusive is less than minExclusive, or if both minExclusive and maxInclusive are specified and maxInclusive is less than or equal to minExclusive. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test221 
	Scenario: manifest-validation#test221 maxInclusive = minExclusive 

	# Applications MUST raise an error if minimum, minInclusive, maximum, maxInclusive, minExclusive, or maxExclusive are specified and the base datatype is not a numeric, date/time, or duration type. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test222 
	Scenario: manifest-validation#test222 string datatype with minimum 

	# Applications MUST raise an error if minimum, minInclusive, maximum, maxInclusive, minExclusive, or maxExclusive are specified and the base datatype is not a numeric, date/time, or duration type. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test223 
	Scenario: manifest-validation#test223 string datatype with maxium 

	# Applications MUST raise an error if minimum, minInclusive, maximum, maxInclusive, minExclusive, or maxExclusive are specified and the base datatype is not a numeric, date/time, or duration type. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test224 
	Scenario: manifest-validation#test224 string datatype with minInclusive 

	# Applications MUST raise an error if minimum, minInclusive, maximum, maxInclusive, minExclusive, or maxExclusive are specified and the base datatype is not a numeric, date/time, or duration type. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test225 
	Scenario: manifest-validation#test225 string datatype with maxInclusive 

	# Applications MUST raise an error if minimum, minInclusive, maximum, maxInclusive, minExclusive, or maxExclusive are specified and the base datatype is not a numeric, date/time, or duration type. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test226 
	Scenario: manifest-validation#test226 string datatype with minExclusive 

	# Applications MUST raise an error if minimum, minInclusive, maximum, maxInclusive, minExclusive, or maxExclusive are specified and the base datatype is not a numeric, date/time, or duration type. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test227 
	Scenario: manifest-validation#test227 string datatype with maxExclusive 

	# If the value is a list, the constraint applies to each element of the list. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test228 
	Scenario: manifest-validation#test228 length with separator 

	# If the value is a list, the constraint applies to each element of the list. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test229 
	Scenario: manifest-validation#test229 matching minLength with separator 

	# If the value is a list, the constraint applies to each element of the list. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test230 
	Scenario: manifest-validation#test230 failing minLength with separator 

	# As defined in [tabular-data-model], validators MUST check that each row has a unique combination of values of cells in the indicated columns. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test231 
	Scenario: manifest-validation#test231 single column primaryKey success 

	# Validators MUST raise errors if there is more than one row with the same primary key 
	# https://w3c.github.io/csvw/tests/manifest-validation#test232 
	Scenario: manifest-validation#test232 single column primaryKey violation 

	# As defined in [tabular-data-model], validators MUST check that each row has a unique combination of values of cells in the indicated columns. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test233 
	Scenario: manifest-validation#test233 multiple column primaryKey success 

	# Validators MUST raise errors if there is more than one row with the same primary key 
	# https://w3c.github.io/csvw/tests/manifest-validation#test234 
	Scenario: manifest-validation#test234 multiple column primaryKey violation 

	# if row titles is not null, insert any titles specified for the row. For each value, tv, of the row titles annotation 
	# https://w3c.github.io/csvw/tests/manifest-validation#test235 
	Scenario: manifest-validation#test235 rowTitles on one column 

	# if row titles is not null, insert any titles specified for the row. For each value, tv, of the row titles annotation 
	# https://w3c.github.io/csvw/tests/manifest-validation#test236 
	Scenario: manifest-validation#test236 rowTitles on multiple columns 

	# if row titles is not null, insert any titles specified for the row. For each value, tv, of the row titles annotation 
	# https://w3c.github.io/csvw/tests/manifest-validation#test237 
	Scenario: manifest-validation#test237 rowTitles on one column (minimal) 

	# it must be the name of one of the built-in datatypes defined in section 5.11.1 Built-in Datatypes 
	# https://w3c.github.io/csvw/tests/manifest-validation#test238 
	Scenario: manifest-validation#test238 datatype value an absolute URL 

	# If included, @id is a link property that identifies the datatype described by this datatype description. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test242 
	Scenario: manifest-validation#test242 datatype @id an absolute URL 

	# It MUST NOT start with `_:`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test243 
	Scenario: manifest-validation#test243 invalid datatype @id 

	# It MUST NOT be the URL of a built-in datatype. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test244 
	Scenario: manifest-validation#test244 invalid datatype @id 

	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test245 
	Scenario: manifest-validation#test245 date format (valid time combinations with formats and milliseconds) 

	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test246 
	Scenario: manifest-validation#test246 date format (valid dateTime combinations with formats and milliseconds) 

	# The supported date and time formats listed here are expressed in terms of the date field symbols defined in [UAX35] and MUST be interpreted by implementations as defined in that specification. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test247 
	Scenario: manifest-validation#test247 date format (extra milliseconds) 

	# No Unicode normalization (as specified in [UAX15]) is applied to these string values 
	# https://w3c.github.io/csvw/tests/manifest-validation#test248 
	Scenario: manifest-validation#test248 Unicode in non-Normalized form 

	# When comparing URLs, processors MUST use Syntax-Based Normalization as defined in [[RFC3968]]. Processors perform Scheme-Based Normalization for HTTP (80) and HTTPS (443) 
	# https://w3c.github.io/csvw/tests/manifest-validation#test249 
	Scenario: manifest-validation#test249 http normalization 

	# As defined in [tabular-data-model], validators MUST check that, for each row, the combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test250 
	Scenario: manifest-validation#test250 valid case 

	# As defined in [tabular-data-model], validators MUST check that, for each row, the combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test251 
	Scenario: manifest-validation#test251 missing source reference 

	# As defined in [tabular-data-model], validators MUST check that, for each row, the combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test252 
	Scenario: manifest-validation#test252 missing destination reference column 

	# As defined in [tabular-data-model], validators MUST check that, for each row, the combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test253 
	Scenario: manifest-validation#test253 missing destination table 

	# The combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test254 
	Scenario: manifest-validation#test254 foreign key single column same table 

	# The combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test255 
	Scenario: manifest-validation#test255 foreign key single column different table 

	# The combination of cells in the referencing columns references a unique row within the referenced table through a combination of cells in the referenced columns. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test256 
	Scenario: manifest-validation#test256 foreign key multiple columns 

	# Validators MUST raise errors for each row that does not have a referenced row for each of the foreign keys on the table in which the row appears 
	# https://w3c.github.io/csvw/tests/manifest-validation#test257 
	Scenario: manifest-validation#test257 foreign key no referenced row 

	# Validators MUST raise errors for each row that does not have a referenced row for each of the foreign keys on the table in which the row appears 
	# https://w3c.github.io/csvw/tests/manifest-validation#test258 
	Scenario: manifest-validation#test258 foreign key multiple referenced rows 

	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test259 
	Scenario: manifest-validation#test259 tree-ops example with csvm.json (w3.org/.well-known/csvm) 

	# Processors MUST use the first metadata found for processing a tabular data file by using overriding metadata, if provided. Otherwise processors MUST attempt to locate the first metadata document from the Link header or the metadata located through site-wide configuration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test260 
	Scenario: manifest-validation#test260 tree-ops example with {+url}.json (w3.org/.well-known/csvm) 

	# Applications MUST raise an error if both minLength and maxLength are specified and minLength is greater than maxLength. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test261 
	Scenario: manifest-validation#test261 maxLength less than minLength 

	# The value of any member of `@type` MUST be either a _term_ defined in [csvw-context], a _prefixed name_ where the prefix is a term defined in [csvw-context], or an absolute URL. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test263 
	Scenario: manifest-validation#test263 @type on a common property can be a built-in type 

	# The value of any member of `@type` MUST be either a _term_ defined in [csvw-context], a _prefixed name_ where the prefix is a term defined in [csvw-context], or an absolute URL. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test264 
	Scenario: manifest-validation#test264 @type on a common property can be a CURIE if the prefix is one of the built-in ones 

	# Processors MUST issue a warning if a property is set to an invalid value type 
	# https://w3c.github.io/csvw/tests/manifest-validation#test266 
	Scenario: manifest-validation#test266 `null` contains an array of (valid) string & (invalid) numeric values 

	# It MUST NOT start with `_:` and it MUST NOT be the URL of a built-in datatype. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test267 
	Scenario: manifest-validation#test267 @id on datatype is invalid (eg starts with _:) 

	# An atomic property that contains a single string: the name of one of the built-in datatypes, as listed above (and which are defined as terms in the default context). Its default is string. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test268 
	Scenario: manifest-validation#test268 `base` missing on datatype (defaults to string) 

	# If the datatype base for a cell is `boolean`, the datatype format annotation provides the true value followed by the false value, separated by `|`. If the format does not follow this syntax, implementations MUST issue a warning and proceed as if no format had been provided. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test269 
	Scenario: manifest-validation#test269 `format` for a boolean datatype is a string but in the wrong form (eg YN) 

	# All terms used within a metadata document MUST be defined in [csvw-context] defined for this specification 
	# https://w3c.github.io/csvw/tests/manifest-validation#test270 
	Scenario: manifest-validation#test270 transformation includes an invalid property (eg foo) 

	# A foreign key definition is a JSON object that must contain only the following properties. . . 
	# https://w3c.github.io/csvw/tests/manifest-validation#test271 
	Scenario: manifest-validation#test271 foreign key includes an invalid property (eg `dc:description`) 

	# A foreign key definition is a JSON object that must contain only the following properties. . . 
	# https://w3c.github.io/csvw/tests/manifest-validation#test272 
	Scenario: manifest-validation#test272 foreign key reference includes an invalid property (eg `dc:description`) 

	# If present, its value MUST be a string that is interpreted as a URL which is resolved against the location of the metadata document to provide the **base URL** for other URLs in the metadata document. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test273 
	Scenario: manifest-validation#test273 `@base` set in `@context` overriding eg CSV location 

	# The `@context` MUST have one of the following values: An array composed of a string followed by an object, where the string is `http://www.w3.org/ns/csvw` and the object represents a local context definition, which is restricted to contain either or both of the following members. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test274 
	Scenario: manifest-validation#test274 `@context` object includes properties other than `@base` and `@language` 

	# Table Group may only use defined properties. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test275 
	Scenario: manifest-validation#test275 property acceptable on column appears on table group 

	# Table may only use defined properties. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test276 
	Scenario: manifest-validation#test276 property acceptable on column appears on table 

	# Column may only use defined properties. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test277 
	Scenario: manifest-validation#test277 property acceptable on table appears on column 

	# Two schemas are compatible if they have the same number of non-virtual column descriptions, and the non-virtual column descriptions at the same index within each are compatible with each other. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test278 
	Scenario: manifest-validation#test278 CSV has more headers than there are columns in the metadata 

	# Value MUST be a valid xsd:duration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test279 
	Scenario: manifest-validation#test279 duration not matching xsd pattern 

	# Value MUST be a valid xsd:dayTimeDuration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test280 
	Scenario: manifest-validation#test280 dayTimeDuration not matching xsd pattern 

	# Value MUST be a valid xsd:yearMonthDuration. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test281 
	Scenario: manifest-validation#test281 yearMonthDuration not matching xsd pattern 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test282 
	Scenario: manifest-validation#test282 valid number patterns 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test283 
	Scenario: manifest-validation#test283 valid number patterns (signs and percent/permille) 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test284 
	Scenario: manifest-validation#test284 valid number patterns (grouping) 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test285 
	Scenario: manifest-validation#test285 valid number patterns (fractional grouping) 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test286 
	Scenario: manifest-validation#test286 invalid ##0 1,234 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test287 
	Scenario: manifest-validation#test287 invalid ##0 123.4 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test288 
	Scenario: manifest-validation#test288 invalid #,#00 1 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test289 
	Scenario: manifest-validation#test289 invalid #,#00 1234 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test290 
	Scenario: manifest-validation#test290 invalid #,#00 12,34 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test291 
	Scenario: manifest-validation#test291 invalid #,#00 12,34,567 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test292 
	Scenario: manifest-validation#test292 invalid #,##,#00 1 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test293 
	Scenario: manifest-validation#test293 invalid #,##,#00 1234 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test294 
	Scenario: manifest-validation#test294 invalid #,##,#00 12,34 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test295 
	Scenario: manifest-validation#test295 invalid #,##,#00 1,234,567 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test296 
	Scenario: manifest-validation#test296 invalid #0.# 12.34 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test297 
	Scenario: manifest-validation#test297 invalid #0.# 1,234.5 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test298 
	Scenario: manifest-validation#test298 invalid #0.0 1 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test299 
	Scenario: manifest-validation#test299 invalid #0.0 12.34 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test300 
	Scenario: manifest-validation#test300 invalid #0.0# 1 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test301 
	Scenario: manifest-validation#test301 invalid #0.0# 12.345 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test302 
	Scenario: manifest-validation#test302 invalid #0.0#,# 1 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test303 
	Scenario: manifest-validation#test303 invalid #0.0#,# 12.345 

	# A number format pattern as defined in [UAX35]. Implementations MUST recognise number format patterns containing the symbols `0`, `#`, the specified decimalChar (or `.` if unspecified), the specified groupChar (or `,` if unspecified), `E`, `+`, `%` and `&permil;`. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test304 
	Scenario: manifest-validation#test304 invalid #0.0#,# 12.34,567 

	# Values in separate columns using the same propertyUrl are kept in proper relative order. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test305 
	Scenario: manifest-validation#test305 multiple values with same subject and property (unordered) 

	# Values in separate columns using the same propertyUrl are kept in proper relative order. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test306 
	Scenario: manifest-validation#test306 multiple values with same subject and property (ordered) 

	# Values in separate columns using the same propertyUrl are kept in proper relative order. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test307 
	Scenario: manifest-validation#test307 multiple values with same subject and property (ordered and unordered) 

	# If the value of the datatype property is a string, it must be one of the built-in datatypes. 
	# https://w3c.github.io/csvw/tests/manifest-validation#test308 
	Scenario: manifest-validation#test308 invalid datatype string 

