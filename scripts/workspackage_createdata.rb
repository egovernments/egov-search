require 'csv'
require 'json'

def map_to_json(data)
	{
		"searchable"=> {
			"package_name" =>data[:package_name]
		},
		"clauses"=> {
			"estimate_number" => data[:estimate_number],
			"package_number" => data[:package_number],
			"tender_file_number" => data[:tender_file_number],
			"package_amount"=> data[:package_amount].to_i,
			"status" => data[:status],
			"manual_work_package_number" => data[:manual_work_package_number]
		},
		"common"=> {
			"owner"=> {
				"name"=> data[:owner_name],
				"position"=> data[:owner_position],
				"designation"=> data[:owner_designation],
				"department"=> data[:owner_department]
			}
		}
	}
end


CSV.foreach("workspackage.csv", {:headers => true, :header_converters => :symbol}) do |row|
	# puts row[:id]
	json = map_to_json row
	File.write("workspackage#{row[:id]}.json", json.to_json)
end
