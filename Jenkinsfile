pipeline {
    agent any
        tools {
            gradle 'Gradle 6.9.1'
            jdk 'JDK8'
        }

    stages {
        stage('Build') {
            steps {
                bat 'gradle clean'
            }
        }
        stage('Test') {
            steps {
                bat 'gradle test'
            }
        }
        stage('Install') {
            steps {
                bat 'gradle build'
            }
        }
    }

    post {
        always {
            junit 'build/test-results/test/*.xml'
        }
    }
}