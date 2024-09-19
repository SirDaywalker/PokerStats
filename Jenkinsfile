pipeline {
    agent any

    stages {
        stage('Clone Repository') {
            steps {
                git url: 'https://github.com/SirDaywalker/PokerStats', branch: 'main'
            }
        }
        stage('Build with Maven') {
            steps {
                script {
                    def mvnHome = tool name: 'Maven 3.9.9', type: 'maven'
                    withEnv(["MVN_HOME=$mvnHome"]) {
                        sh '"$MVN_HOME/bin/mvn" clean install'
                    }
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    def dockerImage = docker.build("sirdaywalker/stammtischhub:${env.BUILD_ID}", "-f Dockerfile .")
                }
            }
        }
        stage('Push to Docker Hub') {
            environment {
                DOCKER_CREDENTIALS_ID = 'd690bda5-2b77-4bd0-a5c6-75c3e56ed28b' // ID der gespeicherten Docker Hub Anmeldedaten
            }
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                        docker.image("sirdaywalker/stammtischhub:${env.BUILD_ID}").push('latest')
                    }
                }
            }
        }
        stage('Run Docker Compose') {
            steps {
                script {
                    sh 'docker compose up -d'
                }
            }
        }
    }
}