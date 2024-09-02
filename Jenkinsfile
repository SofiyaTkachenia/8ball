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
        stage('Docker build for tag') {
            when {
                expression { env.BRANCH_NAME.startsWith('refs/tags/') }
            }
            steps {
                script {
                    def gradleCommand = env.BRANCH_NAME.startsWith('refs/tags/') ? 'clean build' : 'test'
                    sh 'echo ${gradleCommand}'
                    sh 'sudo docker run --rm --name builder -v "$PWD":/app -v "/home/ubuntu/jenkins/.m2/Users/sofiatkachenia/.m2/repository":/root/.m2/repository -w /app ${BUILDER_DOCKER_IMAGE} ./gradlew test'
                }
            }
        }

        stage('Docker build for branch') {
            when {
                expression { !env.BRANCH_NAME.startsWith('refs/tags/') }
            }
            steps {
                script {
                    def gradleCommand = env.BRANCH_NAME.startsWith('refs/tags/') ? 'clean build' : 'test'
                    sh 'echo ${gradleCommand}'
                    sh 'sudo docker run --rm --name builder -v "$PWD":/app -v "/home/ubuntu/jenkins/.m2/Users/sofiatkachenia/.m2/repository":/root/.m2/repository -w /app ${BUILDER_DOCKER_IMAGE} ./gradlew clean build'
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
