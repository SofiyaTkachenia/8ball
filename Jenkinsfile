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
        COMMAND = "docker run --rm --name builder -v \"$PWD\":/app -v ${M2_LOCAL_PATH}:${M2_CONTAINER_PATH} -e CODEARTIFACT_AUTH_TOKEN=${CODEARTIFACT_AUTH_TOKEN} -w /app ${BUILDER_DOCKER_IMAGE}"
    }

    stages {

        stage('Get CodePipeline auth token') {
            when {
                branch 'main'
            }
            steps {
                script {
                    sh "${COMMAND} ${PUBLISH_TO_MAVEN_LOCAL}"
                }
            }
        }

        stage('Dockerized build') {
            when {
                branch 'main'
            }
            steps {
                script {
                    sh 'docker run --rm --name builder -v \"$PWD\":/app -v ${M2_LOCAL_PATH}:${M2_CONTAINER_PATH} -w /app ${BUILDER_DOCKER_IMAGE} -e CODEARTIFACT_AUTH_TOKEN=${CODEARTIFACT_AUTH_TOKEN} ./gradlew publish'
                }
            }
        }

//         stage('Run unit tests') {
//             when {
//                 branch 'main'
//             }
//             steps {
//                 script {
//                     sh "${COMMAND} ${TEST_COMMAND}"
//                 }
//             }
//         }
    }
}
