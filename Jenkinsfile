pipeline {
    agent { docker { image 'maven:3.10.0-eclipse-temurin-21-alpine' } }
    stages {
        stage('build') {
            steps {
                sh 'mvn --version'
                sh 'echo "Hello is this shit working???"'
            }
        }
    }
}