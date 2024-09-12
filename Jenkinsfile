pipeline {

    agent { label 'worker' }

    environment {
        BUILDER_DOCKER_IMAGE = 'amazoncorretto:17.0.10'
        PROJECT_NAME = '8ball'
        JAR_PATH = "${M2_LOCAL_PATH}/org/example/8ball/"
        ARTIFACTORY_REPO = "${PROJECT_NAME}"
        M2_LOCAL_PATH = "/home/jenkins/.m2/repository"
        M2_CONTAINER_PATH = "/root/.m2/repository"
        PUBLISH_TO_MAVEN_LOCAL = "./gradlew publish"
        TEST_COMMAND = "./gradlew test"
    }

    stages {

        stage('Get CodePipeline auth token') {
            when {
                branch 'main'
            }
            steps {
                script {
                    sh "ls -al /home/jenkins/.m2/repository"
                    env.CODEARTIFACT_AUTH_TOKEN = sh(
                        script: "aws codeartifact get-authorization-token --domain test-jenkins --domain-owner 175222917203 --region eu-central-1 --query authorizationToken --output text",
                        returnStdout: false
                    )
                }
            }
        }

        stage('Dockerized build') {
            when {
                branch 'main'
            }
            steps {
                script {
                    sh "echo $CODEARTIFACT_AUTH_TOKEN"
                    sh "docker run --rm --name builder -v \"$PWD\":/app -v ${M2_LOCAL_PATH}:${M2_CONTAINER_PATH} -w /app -e CODEARTIFACT_AUTH_TOKEN=${CODEARTIFACT_AUTH_TOKEN} ${BUILDER_DOCKER_IMAGE}"
                }
            }
        }

        stage('Run unit tests') {
            when {
                branch 'main'
            }
            steps {
                script {
                    sh "${COMMAND} ${TEST_COMMAND}"
                }
            }
        }
    }
}
