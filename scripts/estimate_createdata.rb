require 'csv'
require 'json'

def map_to_json(data)
	{
		"searchable"=> {
			"estimate_number" => data[:estimate_no],
			"user_department" => data[:user_dept],
			"prepared_by"=> {
				"name"=> data[:prepared_user_name],
				"position"=> data[:prepared_position],
				"designation"=> data[:prepared_designation],
				"department"=> data[:prepared_department]
			},
			"description" => data[:description],
			"fund" => data[:fund],
			"function" => data[:function],
			"budgethead" => data[:budgethead],
			"estimate_amount" => data[:estimate_amount],			
			"asset_category" => data[:asset_category],
			"asset_name" => data[:asset_name]
		},
		"clauses"=> {
			"type" => data[:type_of_work],
			"sub_type" => data[:subtype_of_work],
			"nature" => data[:nature_of_work],
			"status" => data[:status],
			"project_code" => data[:project_code],
			"asset_code" => data[:asset_code],
		},
		"common"=> {			
			"boundary"=> {
				"zone"=> data[:zone],
				"ward"=> data[:ward],
				"area"=> data[:area],
				"locality"=> data[:locality],
				"street"=> data[:street]
			},
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


CSV.foreach("estimate.csv", {:headers => true, :header_converters => :symbol}) do |row|
	json = map_to_json row
	File.write("estimate#{row[:id]}.json", json.to_json)
end
