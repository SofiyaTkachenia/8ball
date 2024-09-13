pipeline {

    agent { label 'worker' }

    environment {
        BUILDER_DOCKER_IMAGE = 'amazoncorretto:17.0.10'
        M2_LOCAL_PATH = "/home/jenkins/.m2/repository"
        M2_CONTAINER_PATH = "/root/.m2/repository"
        GET_TOKEN_COMMAND = "aws codeartifact get-authorization-token --domain test-jenkins --domain-owner 175222917203 --region eu-central-1 --query authorizationToken --output text"
        PUBLISH_COMMAND = "./gradlew publish"
        TEST_COMMAND = "./gradlew test"
        TOKEN_FILE = "/tmp/codeartifact_token"
    }

    stages {

        stage('Get CodePipeline auth token') {
            steps {
                script {
                    sh(script: "${GET_TOKEN_COMMAND} > ${TOKEN_FILE}", returnStatus: true)
                }
            }
        }

        stage('Run unit tests') {
            when {
                branch 'main'
            }
            steps {
                script {
                    sh "${getRunDockerCommandFromFile(TOKEN_FILE)} ${TEST_COMMAND}"
                }
            }
        }

        stage('Dockerized build') {
            when {
                branch 'main'
            }
            steps {
                script {
                    sh "${getRunDockerCommandFromFile(TOKEN_FILE)} ${PUBLISH_COMMAND}"
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

def getRunDockerCommandFromFile(tokenFile) {
    def token = readFile(tokenFile).trim()
    def tokenEnv = token ? "-e CODEARTIFACT_AUTH_TOKEN=${token}" : ""
    return """
        docker run --rm --name builder \
        -v "$PWD":/app \
        -v ${M2_LOCAL_PATH}:${M2_CONTAINER_PATH} \
        ${tokenEnv} \
        -w /app ${BUILDER_DOCKER_IMAGE}
    """
}