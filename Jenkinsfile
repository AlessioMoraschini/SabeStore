pipeline {
    agent any
    environment {
        JWT_SECRET = credentials('jwt-secret')
        SPRING_MAIL_PASSWORD = credentials('mail-password')
        BRANCH = "${env.BRANCH_NAME ?: 'main'}"
        //SPRING_PROFILES_ACTIVE = "default"
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    sh 'ls -la'
                    git branch: "${BRANCH}", url: 'https://github.com/AlessioMoraschini/SabeStore.git'
                }
            }
        }
        stage('setProfile') {
            steps {
                script {
                   // checks if branch starts with "release/"
                   if (BRANCH.startsWith('release/')) {
                       // extract subpath of release
                       def parts = BRANCH.split('/')
                       if (parts.length > 2) {
                           // If under release we have another subpath then use it as profile
                           env.SPRING_PROFILES_ACTIVE = "${parts[1]}" // Example: "testenvlocal"
                           echo "Profile set to: ${env.SPRING_PROFILES_ACTIVE}"
                       } else {
                           echo "No profile subpath found. Will fall to default"
                       }
                   } else {
                       echo "Not a release branch. No profile set."
                   }
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    docker.image('maven:3.8.4-openjdk-17').inside("-v maven-repo:/root/.m2") {
                        sh 'mvn -version'
                        sh 'mvn clean package'
                        script {
                            def projectVersion = sh(script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
                            echo "Extracted project version: ${projectVersion}"
                            env.PROJECT_VERSION = projectVersion
                        }
                    }
                }
                echo "Project version: ${env.PROJECT_VERSION}"
            }
        }
        stage('Build Docker Image') {
            when {
                expression { env.BRANCH_NAME.startsWith('release/') }
            }
            steps {
                script {
                    def imageName = "sabestore:${env.PROJECT_VERSION}"
                    echo "Building Docker image with tag: ${imageName}"
                    sh "docker build --build-arg PROJECT_VERSION=${env.PROJECT_VERSION} -t ${imageName} ."
                }
            }
        }
        stage('Deploy New Container') {
            when {
                expression { env.BRANCH_NAME.startsWith('release/') }
            }
            steps {
                script {
                    echo "Deploying container with profile: ${SPRING_PROFILES_ACTIVE}"

                    // Fond ID of the container in execution on port 8082
                    def oldTempContainerId = sh(script: "docker ps --filter publish=8082 | grep '0.0.0.0:8082->8082/tcp' | awk '{print \$1}'", returnStdout: true).trim()
                    // If found, stop and remove it
                    if (oldTempContainerId) {
                        sh "docker stop ${oldTempContainerId}"
                        sh "docker rm ${oldTempContainerId}"
                    }

                    def imageName = "sabestore:${env.PROJECT_VERSION}"
                    echo "Deploying Docker image: ${imageName} with SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}"
                    def newContainerId = sh(script: "docker run -e \"JWT_SECRET=${JWT_SECRET}\" -e \"SPRING_PROFILES_ACTIVE=${env.SPRING_PROFILES_ACTIVE}\" -e \"SPRING_MAIL_PASSWORD=${SPRING_MAIL_PASSWORD}\" -d -p 8082:8082 --name sabestoreLatest --network mynetwork -e SERVER_PORT=8082 ${imageName}", returnStdout: true).trim()
                    echo "New container started with ID: ${newContainerId} on port 8082"
                    writeFile file: 'newContainerId.txt', text: newContainerId
                }
            }
        }
        stage('Health Check') {
            when {
                expression { env.BRANCH_NAME.startsWith('release/') }
            }
            steps {
                script {
                    def maxAttempts = 10
                    def waitTime = 5
                    def newContainerRunning = false
                    def attempts = 0

                    while (attempts < maxAttempts && !newContainerRunning) {
                        try {
                            sh 'curl http://sabestoreLatest:8082/actuator/health'
                            newContainerRunning = true
                        } catch (Exception e) {
                            echo "Health check failed. Error: ${e.message}"
                            echo "New container is not yet available. Waiting ${waitTime} seconds..."
                            sleep(waitTime)
                            attempts++
                        }
                    }

                    if (!newContainerRunning) {
                        error "New container failed to start within ${maxAttempts * waitTime} seconds."
                    }
                }
            }
        }
        stage('Stop Existing Container') {
            when {
                expression { env.BRANCH_NAME.startsWith('release/') }
            }
            steps {
                script {
                    // Trova l'ID del container in esecuzione sulla porta 8081
                    def containerId = sh(script: "docker ps --filter publish=8081 | grep '0.0.0.0:8081->8081/tcp' | awk '{print \$1}'", returnStdout: true).trim()

                    // Se esiste un container in esecuzione sulla porta 8081, fermalo
                    if (containerId) {
                        sh "docker stop ${containerId}"
                        sh "docker rm ${containerId}"
                    }
                }
            }
        }
        stage('Reassign Port') {
            when {
                expression { env.BRANCH_NAME.startsWith('release/') }
            }
            steps {
                script {
                    def imageName = "sabestore:${env.PROJECT_VERSION}"
                    def imageNameContainer = "sabestore_${env.PROJECT_VERSION}".toLowerCase()
                    echo "Syntax-corrected image container name is: ${imageNameContainer}"
                    def newContainerId = readFile('newContainerId.txt').trim()
                    echo "newContainerId is: ${newContainerId}"

                    // Step 1:Use docker cp to copy the jar from running container with deployed app to the temp container (copy to volume)
                    echo "Copying new application jar file (SabeStore-${env.PROJECT_VERSION}.jar) to local..."
                    sh "docker cp ${newContainerId}:/app/SabeStore-${env.PROJECT_VERSION}.jar ."
                    // Step 2: Start new temp container with mounted volume
                    echo "Copying new application jar file (SabeStore-${env.PROJECT_VERSION}.jar) in the volume..."
                    sh "ls -la ${env.WORKSPACE}/SabeStore-${env.PROJECT_VERSION}.jar"
                    echo "BRANCH_NAME: ${env.BRANCH_NAME}"
                    // Save full path in variable
                    def filePathFull = sh(script: "pwd", returnStdout: true).trim() + "/SabeStore-${env.PROJECT_VERSION}.jar"
                    echo "The full absolute path of the copied file is: ${filePathFull}"
                    // Save local relative path in variable
                    def localRelativePath = sh(script: "echo ${filePathFull} | sed 's|/var/jenkins_home|/local|'", returnStdout: true).trim()
                    echo "The local relative path of the copied file is: ${localRelativePath}"
                    sh "docker run --rm -v AmDesignApplicationVolume:/app -v DockerVolume:/local busybox sh -c 'ls -la /local && rm -f /app/SabeStore-${env.PROJECT_VERSION}.jar && cp ${localRelativePath} /app/'"

                    // Restart new container on original port 8081
                    sh "docker stop ${newContainerId}"
                    sh "docker rm ${newContainerId}"
                    sh "docker run -e \"JWT_SECRET=${JWT_SECRET}\" -e \"SPRING_PROFILES_ACTIVE=${env.SPRING_PROFILES_ACTIVE}\" -e \"SPRING_MAIL_PASSWORD=${SPRING_MAIL_PASSWORD}\" -d -p 8081:8081 -e SERVER_PORT=8081 -v AmDesignApplicationVolume:/app --network mynetwork --name ${imageNameContainer} ${imageName}"
                }
            }
        }
    }
}
