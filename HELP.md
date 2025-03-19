# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.3.0-SNAPSHOT/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.3.0-SNAPSHOT/maven-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.3.0-SNAPSHOT/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot/docs/3.3.0-SNAPSHOT/reference/htmlsingle/index.html#io.validation)
* [Spring for Apache ActiveMQ Artemis](https://docs.spring.io/spring-boot/docs/3.3.0-SNAPSHOT/reference/htmlsingle/index.html#messaging.jms.artemis)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Messaging with JMS](https://spring.io/guides/gs/messaging-jms/)

### Jira tasks for the project
* [Jira tasks](https://alessiomora91.atlassian.net/jira/software/projects/AM/boards/1)

### Swagger page
``` 
java -jar target/sabe-store-0.0.1-SNAPSHOT.jar -Dserver.port=8080 
```
http://localhost:8080/swagger-ui/index.html
* replace the port with the one used to launch the app.

Actuator url: http://localhost:8080/actuator

### Docker usage for SabeStore
1. #### Install docker  
    * [Docker installation](https://docs.docker.com/get-docker/)  
    * [Docker compose installation](https://docs.docker.com/compose/install/)
    * [Docker hub](https://hub.docker.com/)
2. #### Create if needed the Jenkins image  
    ``` docker pull jenkins/jenkins:lts ```
3. #### Create Volumes for maven and Jenkins to persist data
    ``` docker volume create maven-repo ```
    ``` docker volume create DockerVolume ```
4. #### Create Volumes for User and Default DB storage
    ``` docker volume create UserDatabase ```
    ``` docker volume create DefaultDatabase ```
5. #### Create custom network for the containers
    ``` docker network create mynetwork ```
6. #### Create Docker image for jenkins
   ```
    FROM jenkins/jenkins:lts 
    USER root
    RUN apt-get update && apt-get install -y docker.io
    ENV PATH=$PATH:/usr/bin/
    USER jenkins 
      ```
   Enter the folder in which the Dockerfile is located and run the following command:
   ``` docker build -t jenkins-docker . ```
7.  #### Run Jenkins Container
   ```
   docker run --privileged -d -u root -p 8080:8080 -p 50000:50000 --name jenkins --network mynetwork -v DockerVolume:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock jenkins-docker
   ``` 
   ``` 
   docker exec -it --user root jenkins /bin/bash
   ls -l /var/run/docker.sock
       Should show something like this:
           srw-rw---- 1 root docker 0 Apr  6 10:43 /var/run/docker.sock
   chmod 660 /var/run/docker.sock
   groupadd docker
   usermod -aG docker jenkins
   docker exec -it jenkins /bin/bash
   docker run hello-world
   ```
8. #### Configure Jenkins 
   Install plugins: 
   * Git Plugin
   * Pipeline Plugin
   * Docker Plugin and Docker Pipeline
   * Create new multibranch pipeline ("New Item" and select "Multibranch Pipeline").
   * Configure the pipeline with the script defined in Jenkinsfile
   * Configure the pipeline to use git https URL, and exclude unused branches (e.g. keep only "main release*")
   * define credentials for jwt-secret (used in pipeline, see Jenkinsfile)
8. #### Create and Run Database containers
   Get the Oracle XE image
   
   ```docker pull gvenzl/oracle-xe```
 
   Now create a file named createUser.sql under "C:\Scripts" with the following content:
   ```
   CREATE USER myuser IDENTIFIED BY mypassword;
   GRANT CREATE SESSION TO myuser;
   GRANT CONNECT TO myuser;
   GRANT SELECT ANY TABLE TO myuser;
   GRANT CREATE ANY TABLE TO myuser;
   GRANT CREATE VIEW TO myuser;
   GRANT CREATE SEQUENCE TO myuser;
   GRANT UPDATE ANY TABLE TO myuser;
   GRANT INSERT ANY TABLE TO myuser;
   ALTER USER myuser QUOTA 500M ON users;
   ```
 
   Now run them and run the script above

   1. User DB
      ```docker run -d --name oracle-user-db --network mynetwork --hostname userdb -p 1521:1521 -p 5500:5500 -e ORACLE_PASSWORD=YOUR_PASSWORD_HERE -v UserDatabase:/opt/oracle/oradata -v C:/Scripts:/scripts gvenzl/oracle-xe```
      And then after the DB is fully started (you'll see DATA BASE IS READY TO USE in the container logs)
      ```docker exec -it oracle-user-db sh -c "sqlplus sys/YOUR_PASSWORD_HERE@localhost:1521/XEPDB1 as sysdba @/scripts/createUser.sql"```
   2. Default DB
      ```docker run -d --name oracle-default-db --network mynetwork --hostname defaultdb -p 1522:1521 -p 5501:5500 -e ORACLE_PASSWORD=YOUR_OTHER_PASSWORD_HERE -v DefaultDatabase:/opt/oracle/oradata -v C:/Scripts:/scripts gvenzl/oracle-xe```
      And then after the DB is fully started (you'll see DATA BASE IS READY TO USE in the container logs)
      ```docker exec -it oracle-default-db sh -c "sqlplus sys/YOUR_OTHER_PASSWORD_HERE@localhost:1521/XEPDB1 as sysdba @/scripts/createUser.sql"```
      
   3. As defined above and in application-testenvlocal.yaml, for simplicity in the local docker test env we use myuser user and mypassword.
      You can also connect with the software you'd like in order to view data or insert/modify anything, like DBeaver.
      Jdbc connection string is: 
      User-DB ```jdbc:oracle:thin:@localhost:1521/XEPDB1```
      Default-DB ```jdbc:oracle:thin:@localhost:1522/XEPDB1```
9. #### Run the pipeline 
   it should build the software, the image, and deploy the latest replacing the existing one!  

10. #### Security and authentication/authorization
   * security can be enabled/disabled using jvm args  ```-Dsecurity.enabled=true```
   * other params you have to specify as environment args are  ```SERVER_PORT=8083;JWT_SECRET=XXXXXXXX```
   * if enabled, security allows to login using the mail/password (crypted with Bcrypt) set in user database.  
   Just call the /login endpoint with username(mail) and password(plain text) matching a valid entry in the DB to receive back a 200 response with Authorization header.
   Then you can use that header in the following requests to be recognized as logged in user. Then, depending on the associated role you can be authorized or not for the various endpoints.
   
11. #### Profiles and other arguments
   Supported Profiles are defined in AppProfiles.java.
   Other properties are:
   * ```-Dspring.profiles.active=testenvlocal``` specify active profile
   * ```-Dspring.mail.password=xaaxasxasaxsxa``` specify password to connect to the mail
   * ```-Dlogging.file.name=``` empty to disable file logging, or specify the full path and name

   

   
