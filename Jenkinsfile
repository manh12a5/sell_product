pipeline {
    agent any
    tools {
        gradle 'Gradle 6.8'
        jdk 'JDK8'
    }

    stages {
        stage('Clean') {
            steps {
                bat 'gradle clean'
            }
        }
//         stage('Test') {
//             steps {
//                 bat 'gradle test'
//             }
//         }
        stage('Build') {
            steps {
                bat 'gradle build'
            }
        }
        stage('Docker Build') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'env', variable: 'mySecretEnvFile')]){
                        bat 'copy $mySecretEnvFile $WORKSPACE'
                    }
                }
                // Build docker image
//              bat 'docker build -t 0398927895/sell_product-docker:latest .'

                // Run docker-compose
                bat 'docker-compose -f docker-compose.yml build'
                bat 'docker-compose -f docker-compose.yml up -d'
            }
        }
    }

    post {
        always {
//             junit 'build/test-results/test/*.xml'

            // Remove old and unused Docker
            bat "docker image prune -f"
//             bat "docker volume prune -f"
//             bat "docker container prune -f"
//             bat "docker system prune -a -f"
//             bat "docker builder prune -a -f"
        }
    }
}