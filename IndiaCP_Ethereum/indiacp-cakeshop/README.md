
### Running via Spring Boot

* Download WAR file
* Run `java -jar cakeshop.war`
* Navigate to [http://localhost:8080/cakeshop/](http://localhost:8080/cakeshop/)

### Set up quorum
sh bootstrap.sh
sh init.sh
sh start.sh
```

At this point you should have Cakeshop running and connected to node 1 in the Quorum cluster (dd1 on RPC port 22000).

You can change the node you're connected to by running the last two commands again from another directory and modifying the properties file at `data/local/application.properties` to point to a different RPC port in the range `22000-22006` before starting Cakeshop.

