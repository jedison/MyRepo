Create on a MySql instance (I have tried with 5.6.24 Community ed and 5.7.9 Community ed) a database called "test".
Run on it the script /dbscripts/myDB.ddl which will simply create a single simple table.

In \src\test\resources\tomee.xml fix the url, user and password in both myDBXA and myDBXAPooled

Run "mvn package tomee:run"
You should see:
-------------------------------------
...
INFO: Deployed Application(path=E:\Workspace\mysql-src\TestApp\target\apache-tomee\webapps\TestApp-0.0.1-SNAPSHOT)
############## APPLICATION STARTING 1445524296566
############## APPLICATION STARTED
ott 22, 2015 4:31:36 PM org.apache.catalina.startup.HostConfig deployWAR
INFO: Deployment of web application archive E:\SirtomauWorkspace\mysql-src\TestApp\target\apache-tomee\webapps\TestApp-0.0.1-SNAPSHOT.war has finished in 804 ms

ott 22, 2015 4:31:36 PM org.apache.coyote.AbstractProtocol start
INFO: Starting ProtocolHandler ["http-bio-8080"]
ott 22, 2015 4:31:36 PM org.apache.coyote.AbstractProtocol start
INFO: Starting ProtocolHandler ["ajp-bio-8009"]
ott 22, 2015 4:31:36 PM org.apache.catalina.startup.Catalina start
INFO: Server startup in 871 ms
Number of threads:1 # Conn. obtained:1 # Conn. failures:0 # Average connection time:162,00 # Quickest conn. time:162 # Slowest conn. time:162
Number of threads:1 # Conn. obtained:2 # Conn. failures:0 # Average connection time:167,50 # Quickest conn. time:162 # Slowest conn. time:173
Number of threads:1 # Conn. obtained:3 # Conn. failures:0 # Average connection time:164,00 # Quickest conn. time:157 # Slowest conn. time:173
Number of threads:1 # Conn. obtained:5 # Conn. failures:0 # Average connection time:160,00 # Quickest conn. time:152 # Slowest conn. time:173
Number of threads:1 # Conn. obtained:6 # Conn. failures:0 # Average connection time:159,00 # Quickest conn. time:152 # Slowest conn. time:173
Number of threads:1 # Conn. obtained:7 # Conn. failures:0 # Average connection time:158,29 # Quickest conn. time:152 # Slowest conn. time:173
...
-------------------------------------

Using a mysql tool monitor the number of connections opened between tomee and the mysql db (i.e. use MySql Workbench).
Note that the datasource is defined in:
\src\test\resources\tomee.xml

The web app will start running a single "Emulator" thread.
You can increase or reduce their number using:
http://localhost:8080/TestApp-0.0.1-SNAPSHOT/ManageThreadsServlet?numOfThreadsToAdd=5
http://localhost:8080/TestApp-0.0.1-SNAPSHOT/ManageThreadsServlet?numOfThreadsToRemove=5

After playing a bit with adding and removing threads you should see the number of connections growing above maxActive and above maxActive+maxIdle, whatever interpretation you give to the meaning of these parameters.
My expectations would be to never see the number of connections above MaxActive.

You should also notice that when you stop tomee all the connections are closed.

If in \src\test\resources\tomee.xm you uncomment the non XA driver and comment the XA one there won't be the same leaking.

Apologies if what I see as an issue comes from a mistake on my side or from having misunderstood the usage of some parameters.
