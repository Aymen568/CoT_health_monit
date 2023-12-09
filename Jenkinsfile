pipeline {
    agent any
    environment {
        WILDFLY_HOME = '/opt/wildfly'
        M3_HOME = '/opt/maven'
    }
    
    stages {
        stage('Initialization') {
            steps {
                
                echo "WILDFLY_HOME: ${WILDFLY_HOME}"
                echo "M3_HOME: ${M3_HOME}"
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
