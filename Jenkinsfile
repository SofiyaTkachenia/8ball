pipeline {
    agent { label 'worker' }

    environment {
        BUILDER_DOCKER_IMAGE = 'amazoncorretto:17.0.10'
        M2_LOCAL_PATH = "/home/jenkins/.m2/repository"
        M2_CONTAINER_PATH = "/root/.m2/repository"
        GET_TOKEN_COMMAND = "aws codeartifact get-authorization-token --domain test-jenkins --domain-owner 175222917203 --region eu-central-1 --query authorizationToken --output text"
        CODEARTIFACT_AUTH_TOKEN = ''
        PUBLISH_COMMAND = "./gradlew publish"
        TEST_COMMAND = "./gradlew test"
    }

    stages {

        stage('Get CodePipeline auth token') {
            steps {
                script {
                    env.CODEARTIFACT_AUTH_TOKEN = sh(
                        script: GET_TOKEN_COMMAND,
                        returnStdout: true
                    ).trim()
                }
            }
        }

        stage('Dockerized build') {
            when { branch 'main' }
            steps {
                runInDocker("${PUBLISH_COMMAND}", env.CODEARTIFACT_AUTH_TOKEN)
            }
        }

        stage('Run unit tests') {
            when { branch 'main' }
            steps {
                runInDocker("${TEST_COMMAND}", env.CODEARTIFACT_AUTH_TOKEN)
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}

def runInDocker(command, token) {
    sh """
        docker run --rm --name builder \
        -v /home/jenkins/workspace/lib-pipeline_main:/app \
        -v /home/jenkins/.m2/repository:/root/.m2/repository \
        -e CODEARTIFACT_AUTH_TOKEN=${CODEARTIFACT_AUTH_TOKEN} \
        -w /app amazoncorretto:17.0.10 ./gradlew publish
    """
}