1. Unzip india-commercial-paper.zip

2. **Installing Nodes**:
Via command line terminals - go to india-commercial-paper directory and run:
./gradlew deployNodes
This will create 4 node directories under india-commercial-paper/build/indiacpdemo
controller
issuer
investor1
investor2

3. **Starting Nodes:**
You could run all these nodes using the runnodes script in the same directory.
However I am running them independently by opening 4 separate terminal windows and navigating to each node
say for e.g. for launching the controller node:
cd india-commercial-paper/build/indiacpdemo/controller
java -jar corda.jar
(This also provides more control for starting the node in debug mode, using:
java -Dcapsule.jvm.args="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005" -jar corda.jar
Change the debug port for each node that you want to debug
)

4. Repeat the same for all 4 nodes
Each Node has an embedded Jetty Server and MQ Server
Check the node.conf in each of the above node directories for the port nos.
Issuer: WebServer: 10005
Investor1: WebServer: 10007
Investor1: WebServer: 10009

5. You can now interact with the Issuer and Investor Nodes using REST Apis.
To check all the REST Apis that have been registered with the Node on Startup, navigate to:
http://localhost:10005/api/application.wadl
The REST APIs are the same for all the nodes. We could change this later based on our requirements.
The Key REST APIs are:
http://localhost:<port>/api/indiacp/issueCP   (Genesis of CP on the ledger with the ownership assigned to the Issuer)
http://localhost:<port>/api/indiacp/issueCash (To issue dummy cash on the ledger to buy CP)
http://localhost:<port>/api/indiacp/fetchAllCP   (To view all CP in the particular node's Vault)
http://localhost:<port>/api/indiacp/fetchCP/{ref} (To view a specific CP in the particular node's Vault)
http://localhost:<port>/api/indiacp/enterDeal/{investor} (To transfer CP from Issuer to Investor with atomic swap of Cash from Investor to Issuer. Note that you would have to first Issue Cash on the Investor Node)

6. First verify that the Issuer's Vault is empty by running:
curl http://localhost:10005/api/indiacp/fetchAllCP

7. Issue a CP on Issuer's Node:
curl -H "Content-Type: application/json" -X POST -d '{"cpRefId":"123","issuer":"Bank of London","faceValue":1000,"maturityDays":10}' http://localhost:10005/api/indiacp/issueCP

8. Verify the CP Issuance on Issuer Node:
curl http://localhost:10005/api/indiacp/fetchAllCP

NOTE: There is a bug somewhere that is causing the CP to appear twice in the Vault. Ignore it for the time being. It won't impact any test flows.


9. Issue another CP on Issuer's Node:
curl -H "Content-Type: application/json" -X POST -d '{"cpRefId":"234","issuer":"Bank of Ireland","faceValue":1000,"maturityDays":10}' http://localhost:10005/api/indiacp/issueCP

10. Verify the second CP Issuance on Issuer Node:
curl http://localhost:10005/api/indiacp/fetchAllCP
curl http://localhost:10007/api/indiacp/fetchAllCP

11. Issue cash on Investor1 Node:
curl -H "Content-Type: application/json" -X POST -d '{"amount":1000}' http://localhost:10007/api/indiacp/issueCash

12. Generate ISIN (Step to demonstrate how the trade can be versioned and new attributes appended)
curl -H "Content-Type: application/json" -X POST -d '1234566' http://localhost:10005/api/indiacp/generateISIN/123

13. Verify that the version of the trade has been updated to 1 and that ISIN is now stamped on the trade on Issuer Node:
curl http://localhost:10005/api/indiacp/fetchAllCP

14. Enter Deal to Transfer first CP ("123", "Bank of London") from Issuer to Investor1:
curl -H "Content-Type: application/json" -X POST -d '{"cpRefId":"123","acceptablePrice":1000}' http://localhost:10005/api/indiacp/enterDeal/Investor1

15. Verify that the CP ownership is transferred to Investor1 by fetching CP in Investor1's Vault
Issuer's Vault: curl http://localhost:10005/api/indiacp/fetchAllCP
Investor1's Vault: curl http://localhost:10007/api/indiacp/fetchAllCP
Notice that the "Bank of London" CP is now owned by Investor1

16. Issue cash on Investor2 Node:
curl -H "Content-Type: application/json" -X POST -d '{"amount":1000}' http://localhost:10009/api/indiacp/issueCash

17. Enter Deal to Transfer second CP ("234", "Bank of Ireland")  from Issuer to Investor2:
curl -H "Content-Type: application/json" -X POST -d '{"cpRefId":"234","acceptablePrice":1000}' http://localhost:10005/api/indiacp/enterDeal/Investor2

18. Verify that the CP ownership is transferred to Investor2 by fetching CP in Investor2's Vault
Issuer's Vault: curl http://localhost:10005/api/indiacp/fetchAllCP
Investor1's Vault: curl http://localhost:10009/api/indiacp/fetchAllCP
Notice that the "Bank of Ireland" CP is now owned by Investor2
