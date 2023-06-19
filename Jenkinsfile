node {
    def mvnHome = tool 'maven-3.8.7'

    stage('Clone Repo') { // for display purposes
      // Get some code from a GitHub repository
      git branch: 'test', url: 'https://github.com/HungPhan200591/bmi.git'
      // Get the Maven tool.
      // ** NOTE: This 'maven-3.6.1' Maven tool must be configured
      // **       in the global configuration.           
      mvnHome = tool 'maven-3.8.7'
    }

    stage('Build Project') {
      // build project via maven
      sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
    }

    stage('Build Docker Image') {
      // build docker image
      sh "whoami"
      sh "ls -all /var/run/docker.sock"
      sh "mv ./target/bmi.jar ./data" 
      
      dockerImage = docker.build("bmi")
    }
}