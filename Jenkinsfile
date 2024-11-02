pipeline {
    agent {
        docker {
            image 'maven'
        }
    }
    stages {
        stage('Git') {
            steps {
                echo env.label

                echo 'chekout GIT'
                checkout scmGit(branches: [[name: env.branch]], extensions: [[$class: 'WipeWorkspace']], userRemoteConfigs: [[url: 'https://github.com/EreminD/http_db_module.git']])
            }
        }
        stage('Validate'){
            steps {
                sh 'mvn clean compile'
            }
        }
        stage('Test'){
            steps {
                sh 'mvn test'
            }
        }
        stage('Report'){
            steps {
                junit keepProperties: true, keepTestNames: true, stdioRetention: '', testResults: 'target/surefire-reports/TEST*.xml'
            }
        }
    }
}
