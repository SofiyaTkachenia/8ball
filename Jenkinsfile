pipeline {
    agent { label 'worker' }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/SofiyaTkachenia/8ball'
            }
        }
        stage('Docker version') {
            steps {
                script {
                    sh 'docker --version'
                }
            }
        }
    }
}