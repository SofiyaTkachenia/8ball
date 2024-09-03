pipeline {

    agent { label 'worker' }

    tools {
        jfrog 'jfrog-cli-latest'
    }

    environment {
        BUILDER_DOCKER_IMAGE = 'amazoncorretto:17.0.10'
        PROJECT_NAME = '8ball'
        JAR_PATH = "build/libs/${PROJECT_NAME}.jar"
        ARTIFACTORY_REPO = "${PROJECT_NAME}"
        M2_LOCAL_PATH = "/home/ubuntu/jenkins/.m2/Users/sofiatkachenia/.m2/repository"
        M2_CONTAINER_PATH = "/root/.m2/repository"
        BUILD_COMMAND = "./gradlew clean build"
        TEST_COMMAND = "./gradlew test"
        COMMAND = "sudo docker run --rm --name builder -v \"$PWD\":/app -v ${M2_LOCAL_PATH}:${M2_CONTAINER_PATH} -w /app ${BUILDER_DOCKER_IMAGE}"
    }

    stages {
        stage('Dockerized build') {
            when {
                buildingTag()
            }
            steps {
                script {
                    sh "${COMMAND} ${BUILD_COMMAND}"
                }
            }
        }

        stage('Run unit tests') {
            when {
                branch 'master'
            }
            steps {
                script {
                    sh "${COMMAND} ${TEST_COMMAND}"
                }
            }
        }

        stage('Push to the JFrog artifactory') {
            when {
                buildingTag()
            }
            steps {
                script {
                    jf "rt u ${JAR_PATH} ${ARTIFACTORY_REPO}/${env.BRANCH_NAME.replace('refs/tags/', '')}/"
                }
            }
        }
    }
}
