pipeline {

    agent { label 'worker' }

    tools {
        jfrog 'jfrog-cli-latest'
    }

    environment {
        DOCKER_IMAGE = 'amazoncorretto:17.0.10'
        PROJECT_NAME = '8ball'
        VERSION = '0.0.1'
        JAR_PATH = "build/libs/${PROJECT_NAME}-${VERSION}.jar"
        ARTIFACTORY_REPO = "${PROJECT_NAME}/"
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
                    sh """
                        sudo docker run --rm --name builder -v "$PWD":/app -w /app amazoncorretto:17.0.10 ./gradlew clean build
                    """
                }
            }
        }
        stage('Push to the JFrog artifactory') {
            steps {
                jf "rt u ${JAR_PATH} ${ARTIFACTORY_REPO}"
            }
        }
    }
}