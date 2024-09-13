pipeline {

    agent { label 'worker' }

    environment {
        BUILDER_DOCKER_IMAGE = 'amazoncorretto:17.0.10'
        M2_LOCAL_PATH = "/home/jenkins/.m2/repository"
        M2_CONTAINER_PATH = "/root/.m2/repository"
        GET_TOKEN_COMMAND = "aws codeartifact get-authorization-token --domain test-jenkins --domain-owner 175222917203 --region eu-central-1 --query authorizationToken --output text"
        PUBLISH_COMMAND = "./gradlew publish"
        TEST_COMMAND = "./gradlew test"
    }

    stages {

        stage('Get CodePipeline auth token') {
            steps {
                script {
                    env.CODEARTIFACT_AUTH_TOKEN = sh(
                        script: "${GET_TOKEN_COMMAND}",
                        returnStdout: true
                    ).trim()
                }
            }
        }

        stage('Run unit tests') {
            when {
                branch 'main'
            }
            steps {
                script {
                    sh "${getRunDockerCommand(env.CODEARTIFACT_AUTH_TOKEN)} ${TEST_COMMAND}"
                }
            }
        }

        stage('Dockerized build') {
//             when {
//                 buildingTag()
//             }
            steps {
                script {
                    sh "${getRunDockerCommand(null)} ${PUBLISH_COMMAND}"
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}

def getRunDockerCommand(token) {
    def tokenEnv = token ? "-e CODEARTIFACT_AUTH_TOKEN=${token}" : ""
    return """
        docker run -q --rm --name builder \
        -v "$PWD":/app \
        -v ${M2_LOCAL_PATH}:${M2_CONTAINER_PATH} \
        ${tokenEnv} \
        -w /app ${BUILDER_DOCKER_IMAGE}
    """
}