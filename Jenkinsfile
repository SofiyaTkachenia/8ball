pipeline {

    agent { label 'worker' }

    tools {
        jfrog 'jfrog-cli-latest'
    }

    environment {
        BUILDER_DOCKER_IMAGE = 'amazoncorretto:17.0.10'
        PROJECT_NAME = '8ball'
        VERSION = '0.0.1'
        JAR_PATH = "build/libs/${PROJECT_NAME}-${VERSION}.jar"
        ARTIFACTORY_REPO = "${PROJECT_NAME}/"
    }

    stages {
        stage('Check folder') {
            steps {
                script {
                    sh "ls -al ${HOME}/jenkins/.m2/"
                }
            }
        }
        stage('Docker build') {
            steps {
                script {
                    sh 'sudo docker run --rm --name builder -v "$PWD":/app -v "$HOME/jenkins/.m2/":/root/.m2/repository -w /app ${BUILDER_DOCKER_IMAGE} ./gradlew clean build'
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