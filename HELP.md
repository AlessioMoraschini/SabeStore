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
4. #### Create custom network for the containers
    ``` docker network create mynetwork ```
5. #### Create Dockerfile if missing (already defined in the project)
   ```
    FROM jenkins/jenkins:lts 
    USER root
    RUN apt-get update && apt-get install -y docker.io
    ENV PATH=$PATH:/usr/bin/
    USER jenkins 
      ```
   Enter the folder in which the Dockerfile is located and run the following command:
   ``` docker build -t jenkins-docker . ```
6.  #### Run Jenkins Container
   ```
   docker run --privileged -d -u root -p 8080:8080 -p 50000:50000 --name jenkins --network mynetwork -v DockerVolume:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock jenkins-docker
   ``` 
   ``` 
   docker exec -it --user root jenkins /bin/bash
   ls -l /var/run/docker.sock
       Dovrebbe mostrare qualcosa del genere:
           srw-rw---- 1 root docker 0 Apr  6 10:43 /var/run/docker.sock
   chmod 660 /var/run/docker.sock
   groupadd docker
   usermod -aG docker jenkins
   docker exec -it jenkins /bin/bash
   docker run hello-world
   ```
7. #### Configure Jenkins 
   Install plugins: 
   * Git Plugin
   * Pipeline Plugin
   * Docker Plugin and Docker Pipeline
   * Create new pipeline ("New Item" and select "Pipeline").
   * Configure the pipeline with the script defined in Jenkinsfile
8. #### Run the pipeline and it should build the software, the image, and deploy the latest replacing the existing one!  


9. #### Security and authentication/authorization
   ... TODO explain how to use /login and bearer jwt token to access the endpoints
10. 

   

   
