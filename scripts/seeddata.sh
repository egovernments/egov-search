
#!/usr/bin/env bash

# set -x

HOST=localhost
PORT=9200
SERVER=http://${HOST}:${PORT}

printf "\nDeleting indices\n"
curl -X DELETE ${SERVER}/pgr
curl -X DELETE ${SERVER}/tradelicense

printf "\nCreating 'pgr' index with mappings\n"
curl -X POST ${SERVER}/pgr -d '
{
	"mappings" : {
		"_default_" : {
			"dynamic_templates": [
			    {
			      "clauses": {
			        "path_match": "clauses.*",
			        "mapping": {
			          "index": "not_analyzed"
			        }
			      }
			    }
			]
		}
	}
}'

printf "\nCreating 'works' index with mappings\n"
curl -X POST ${SERVER}/works -d '
{
	"mappings" : {
		"_default_" : {
			"dynamic_templates": [
			    {
			      "clauses": {
			        "path_match": "clauses.*",
			        "mapping": {
			          "index": "not_analyzed"
			        }
			      }
			    }
			]
		}
	}
}'

printf "\nIndexing PGR data\n"
for idx in {203461..203471}
do
	curl -XPOST ${SERVER}/pgr/complaint/${idx} --data-binary @pgr${idx}.json	
done

printf "\nIndexing Works Estimate data\n"
for idx in {1..6}
do
	curl -XPOST ${SERVER}/works/estimate/${idx} --data-binary @estimate${idx}.json	
done

printf "\nIndexing Works Package data\n"
for idx in {1..5}
do
	curl -XPOST ${SERVER}/works/workspackage/${idx} --data-binary @works${idx}.json	
done

