pipeline {
    agent any
    environment {
            JWT_SECRET = credentials('jwt-secret')
        }
    stages {
        stage('Checkout') {
            steps {
                sh 'ls -la'
                git branch: 'main', url: 'https://github.com/AlessioMoraschini/SabeStore.git'
            }
        }
        stage('Build') {
            steps {
                script {
                    docker.image('maven:3.8.4-openjdk-17').inside("-v maven-repo:/root/.m2") {
                        sh 'mvn -version'
                        sh 'mvn clean package'
                    }
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t sabestore:latest .'
                }
            }
        }
        stage('Deploy New Container') {
            steps {
                script {
                    // Avvia il nuovo container su una porta temporanea e ottieni l'ID del nuovo container
                    def newContainerId = sh(script: 'docker run -e JWT_SECRET=${JWT_SECRET} -d -p 8082:8082 --name sabestoreLatest --network mynetwork -e SERVER_PORT=8082 sabestore:latest', returnStdout: true).trim()
                    echo "New container started with ID: ${newContainerId} on port 8082"

                    // Salva l'ID del nuovo container in un file per riferimento
                    writeFile file: 'newContainerId.txt', text: newContainerId
                }
            }
        }
        stage('Health Check') {
            steps {
                script {
                    // Verifica che la nuova istanza sia operativa
                    def maxAttempts = 10
                    def waitTime = 5
                    def newContainerRunning = false
                    def attempts = 0

                    while (attempts < maxAttempts && !newContainerRunning) {
                        try {
                            sh 'curl http://sabestoreLatest:8082/actuator/health'
                            newContainerRunning = true
                        } catch (Exception e) {
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
            steps {
                script {
                    // Riavvia il nuovo container con la porta originale
                    def newContainerId = readFile('newContainerId.txt').trim()
                    sh "docker stop ${newContainerId}"
                    sh "docker rm ${newContainerId}"
                    sh "docker run -e JWT_SECRET=${JWT_SECRET} -d -p 8081:8081 -e SERVER_PORT=8081 -v AmDesignApplicationVolume:/app --network mynetwork --name sabestore sabestore:latest"
                }
            }
        }
    }
}