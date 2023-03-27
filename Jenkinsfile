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
//                 bat 'docker build -t 0398927895/sell_product-docker:latest .'
                bat 'docker-compose -f docker-compose.yml build'
                bat 'docker-compose -f docker-compose.yml up -d'
                bat 'docker rmi $(docker images -f "dangling=true" -q)'
            }
        }
    }

//     post {
//         always {
//             junit 'build/test-results/test/*.xml'
//         }
//     }
}