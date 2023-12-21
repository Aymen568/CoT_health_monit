pipeline {
    agent any
    environment {
        MAVEN_HOME = tool 'Maven' // Use the Maven tool configured in Jenkins
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    checkout scm
                }
            }
        }

        stage('Verify and Test') {
            steps {
                script {
                    // Run your verification and testing commands here
                    // For example:
                    sh 'mvn clean verify'
                }
            }
        }

        stage('Merge to Staging') {
            when {
                // Run this stage only if the previous stage was successful
                expression { currentBuild.resultIsBetterOrEqualTo('SUCCESS') }
            }
            steps {
                script {
                    // Switch to the staging branch
                    sh 'git checkout staging'

                    // Merge changes from the dev branch
                    sh 'git merge origin/dev'

                    // Push changes to the remote repository
                    sh 'git push origin staging'
                }
            }
        }
    }
}
