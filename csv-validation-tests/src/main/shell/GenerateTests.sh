#!/usr/bin/env ruby
require 'json'
require 'open-uri'
require 'uri'



BASE_URI = "https://w3c.github.io/csvw/tests/"
BASE_PATH = File.join(File.dirname(__FILE__), "..", "fixtures", "csvw")
FEATURE_BASE_PATH = File.join(File.dirname(__FILE__), "..")
VALIDATION_FEATURE_FILE_PATH = File.join(FEATURE_BASE_PATH, "csvw_validation_tests.feature")
SCRIPT_FILE_PATH = File.join(File.dirname(__FILE__), "..", "..", "bin", "run-csvw-tests")

Dir.mkdir(BASE_PATH) unless Dir.exist?(BASE_PATH)

def cache_file(filename)
	file = File.join(BASE_PATH, filename)
	uri = URI.join(BASE_URI, filename)
	unless File.exist?(file)
		if filename.include? "/"
			levels = filename.split("/")[0..-2]
			for i in 0..levels.length
				dir = File.join(BASE_PATH, levels[0..i].join("/"))
				Dir.mkdir(dir) unless Dir.exist?(dir)
			end
		end
		STDERR.puts("storing #{file} locally")
		File.open(file, 'wb') do |f|
			f.puts open(uri, 'rb').read
		end
	end
	return uri, file
end
