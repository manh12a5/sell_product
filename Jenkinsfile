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
//                 sh 'docker-compose build'
                sh 'docker-compose up -d'
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