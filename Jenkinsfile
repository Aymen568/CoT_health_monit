pipeline {
    agent any
    environment {'https://github.com/Aymen568/CoT_health_monit.git'
        GITHUB_CREDENTIALS = credentials('githubtoken')
        WILDFLY_HOME = '/opt/wildfly'
        M3_HOME = '/opt/maven'
        PROJECT_DIR = 'Cot_project'  // Update to your actual project directory
    }
    
    stages {
        stage('Initialization') {
            steps {
                echo " env ${ENV}"
                echo "GITHUB_REPO_URL: ${GITHUB_REPO_URL}"
                echo "WILDFLY_HOME: ${WILDFLY_HOME}"
                echo "PROJECT_DIR: ${PROJECT_DIR}"
                echo "M3_HOME: ${M3_HOME}"
                echo "PATH: ${PATH}"
            }
        }
        
        stage('Build') {
            steps {
                echo " build"
                
            }
        }

        stage('Test') {
            steps {
                 echo " test"
            }
        }
        
        stage('Scan') {
          steps {
                echo " Scan"
          }
        }
        
        stage("Quality Gate") {
          steps {
            timeout(time: 2, unit: 'MINUTES') {
              waitForQualityGate abortPipeline: true
            }
          }
        }
        
        stage('Deploy to WildFly') {
            steps {
                echo "Deploy"
            }
        }
    }
}
