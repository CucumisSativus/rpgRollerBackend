FROM mongo

COPY init.json /init.json
CMD mongoimport --host mongo --db test --collection akka_persistence_journal --type json /init.json && mongoimport --host mongo --db test --collection akka_persistence_metadata --type json /init.json