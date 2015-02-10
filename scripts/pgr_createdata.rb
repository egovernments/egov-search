require 'csv'
require 'json'

def map_to_json(data)
	{
		"searchable"=> {
			"complaint_number"=> data[:complaint_number],
			"type"=> data[:complaint_type],
			"title"=> data[:c_title],
			"details"=> data[:c_details]

		},
		"clauses"=> {
			"mode"=> data[:c_mode],
			"status"=> data[:c_status]
		},
		"common"=> {
			"citizen"=> {
				"mobile_number"=> data[:mobile_no].to_i,
				"first_name"=> data[:first_name],
				"middle_name"=> data[:middle_name],
				"last_name"=> data[:last_name],
				"email"=> data[:email],
				"address"=> data[:address],
				"pincode"=> data[:pin_code].to_i
			},
			"boundary"=> {
				"zone"=> data[:zone],
				"ward"=> data[:ward],
				"area"=> data[:area],
				"locality"=> data[:locality],
				"street"=> data[:street]
			},
			"department"=> data[:department],
			"created_date"=> data[:created_date],
			"expiry_date"=> data[:expiry_date],
			"completed_date"=> data[:completion_date],
			"created_by"=> {
				"name"=> data[:created_name],
				"position"=> data[:created_position],
				"designation"=> data[:created_designation],
				"department"=> data[:created_department]
			},
			"owner"=> {
				"name"=> data[:owner_name],
				"position"=> data[:owner_position],
				"designation"=> data[:owner_designation],
				"department"=> data[:owner_department]
			}
		}
	}
end


CSV.foreach("pgr.csv", {:headers => true, :header_converters => :symbol}) do |row|
	json = map_to_json row
	File.write("pgr#{row[:id]}.json", json.to_json)
end
