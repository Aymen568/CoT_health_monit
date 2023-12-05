pipeline {
    agent any
    
    environment {'https://github.com/Aymen568/CoT_health_monit/.git'
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
        
        stage('Scan') {
          steps {
              dir(PROJECT_DIR) {
                withSonarQubeEnv(installationName: 'sq1') { 
                  sh "$M3_HOME/bin/mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar"
                }
            }
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
                dir(PROJECT_DIR) {
                    script {
                        sh "$WILDFLY_HOME/bin/jboss-cli.sh --connect -u=\"admin\" -p=\"admin\"  --command=\"deploy --force target/smartlighting-1.0-SNAPSHOT.war\""
                      
                    }
                }
            }
        }
    }
}
