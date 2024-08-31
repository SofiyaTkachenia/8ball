pipeline {

    agent { label 'worker' }

    tools {
        jfrog 'jfrog-cli-latest'
    }

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
                }
            }
        }
        stage('Push to the JFrog artifactory') {
            steps {
                jf '-v'
                jf 'c show'
                jf 'rt ping'
                jf 'rt u build/libs/8ball-0.0.1.jar 8ball/'
                jf 'rt bp'
            }
        }
    }
}