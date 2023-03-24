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
                bat 'docker run -p 8081:8080 0398927895/sell_product-docker'
            }
        }
    }

    post {
//         always {
//             junit 'build/test-results/test/*.xml'
//         }
        always {
            sh "docker-compose down || true"
        }
    }
}