
#!/usr/bin/env bash

# set -x

HOST=localhost
PORT=9200
SERVER=http://${HOST}:${PORT}

printf "\nDeleting indices\n"
curl -X DELETE ${SERVER}/pgr
curl -X DELETE ${SERVER}/works

printf "\nCreating 'pgr' index with mappings\n"
curl -X POST ${SERVER}/pgr --data-binary @mapping.json

printf "\nCreating 'works' index with mappings\n"
curl -X POST ${SERVER}/works --data-binary @mapping.json

printf "\n\nIndexing PGR data\n\n"
for idx in {203461..203471}
do
	curl -XPOST ${SERVER}/pgr/complaint/${idx} --data-binary @pgr${idx}.json	
done

printf "\n\nIndexing Works Estimate data\n\n"
for idx in {1..6}
do
	curl -XPOST ${SERVER}/works/estimate/${idx} --data-binary @estimate${idx}.json	
done

printf "\n\nIndexing Works Package data\n\n"
for idx in {1..5}
do
	curl -XPOST ${SERVER}/works/workspackage/${idx} --data-binary @works${idx}.json	
done

