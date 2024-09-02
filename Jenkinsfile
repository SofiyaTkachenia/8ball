pipeline {

    agent { label 'worker' }

    tools {
        jfrog 'jfrog-cli-latest'
    }

    environment {
        BUILDER_DOCKER_IMAGE = 'amazoncorretto:17.0.10'
        PROJECT_NAME = '8ball'
        JAR_PATH = "build/libs/${PROJECT_NAME}.jar"
        ARTIFACTORY_REPO = "${PROJECT_NAME}/"
    }

    stages {
        stage('Print branch or tag name') {
            steps {
                script {
                    echo "Current branch or tag: ${env.BRANCH_NAME}"
                }
            }
        }
        stage('Docker build') {
            steps {
                script {
                    sh 'sudo docker run --rm --name builder -v "$PWD":/app -v "/home/ubuntu/jenkins/.m2/Users/sofiatkachenia/.m2/repository":/root/.m2/repository -w /app ${BUILDER_DOCKER_IMAGE} ./gradlew ${env.BRANCH_NAME.startsWith('refs/tags/')} clean build'
                }
            }
        }

        stage('Push to the JFrog artifactory') {
            when {
                expression { env.BRANCH_NAME.startsWith('refs/tags/') }
            }
            steps {
                script {
                    sh "jf rt upload ${JAR_PATH} ${ARTIFACTORY_REPO}/${env.BRANCH_NAME.replace('refs/tags/', '')}/"
                }
            }
        }
    }
}
