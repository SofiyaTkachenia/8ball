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
        M2_LOCAL_PATH = "/home/jenkins/.m2"
        M2_CONTAINER_PATH = "/root/.m2/repository"
        PUBLISH_TO_MAVEN_LOCAL = "./gradlew publishToMavenLocal"
        TEST_COMMAND = "./gradlew test"
        COMMAND = 'docker run --rm --name builder -v \"$PWD\":/app -v ${M2_LOCAL_PATH}:${M2_CONTAINER_PATH} -w /app ${BUILDER_DOCKER_IMAGE}'
    }

    stages {
        stage('Dockerized build') {
            when {
                buildingTag()
            }
            steps {
                script {
                    sh "${COMMAND} ${PUBLISH_TO_MAVEN_LOCAL}"
                    sh "ls -al ${M2_LOCAL_PATH}"
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
