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
              withSonarQubeEnv(installationName: 'sonarcube1') {
                  sh './mvnw clean org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar'
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
