pipeline {
    agent any
    
    environment {
        GITHUB_REPO_URL = 'https://github.com/Aymen568/CoT_health_monit.git'
        GITHUB_CREDENTIALS = credentials('githubtoken')
        WILDFLY_HOME = '/opt/wildfly'
        M3_HOME = '/opt/apache-maven-3.8.8'
        PROJECT_DIR = 'code/api/Health-monitoring'  // Update to your actual project directory
    }
    stages {
        stage('Initialization') {
            steps {
               
                echo "GITHUB_REPO_URL: ${GITHUB_REPO_URL}"
                echo "WILDFLY_HOME: ${WILDFLY_HOME}"
                echo "PROJECT_DIR: ${PROJECT_DIR}"
                echo "M3_HOME: ${M3_HOME}"
            }
        }
        stage('Build') {
            steps {
                dir(PROJECT_DIR) {
                    script {
                        // Assuming your project uses Maven for building
                        sh "$M3_HOME/bin/mvn clean install"
                    }
                }
            }
        }
        stage('Test') {
            steps {
                dir(PROJECT_DIR) {
                    script {
                        // Assuming your project uses Maven for running tests
                        sh "$M3_HOME/bin/mvn test"
                    }
                }
            }
        }
        steps {
            script {
              // requires SonarQube Scanner 2.8+
              scannerHome = tool 'sonarcube1'
            }
            withSonarQubeEnv('SonarQube Scanner') {
              sh "${scannerHome}/bin/sonar-scanner"
            }
        }

        stage('Deploy to WildFly') {
            steps {
                dir(PROJECT_DIR) {
                    script {
                        sh "$WILDFLY_HOME/bin/jboss-cli.sh --connect -u=\"admin\" -p=\"admin\"  --command=\"deploy --force target/Health-monitoring-1.0-SNAPSHOT.war\""
                      
                    }
                }
            }
        }
    }
}
