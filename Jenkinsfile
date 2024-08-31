pipeline {
    agent worker

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'git@github.com:SofiyaTkachenia/8ball.git'
            }
        }
        stage('Docker PS') {
            steps {
                script {
                    sh 'docker ps'
                }
            }
        }
    }
}