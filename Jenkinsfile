pipeline {
    agent { label 'worker' }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/SofiyaTkachenia/8ball'
            }
        }
        stage('Docker build') {
            steps {
                script {
                    sh 'sudo docker run --rm --name builder -v "$PWD":/app -v "$HOME/.m2/repository":/root/.m2/repository -w /app amazoncorretto:17.0.10 ./gradlew clean build'
                    sh 'cd build/libs && ls -al'
                }
            }
        }
    }
}