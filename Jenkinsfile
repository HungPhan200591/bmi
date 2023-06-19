node {
    stage("Clone the project") {
        git branch: 'test', url: 'https://github.com/HungPhan200591/bmi.git'
    }

    stage ("Compilation"): {
        sh "./mvnw clean install -DskipTests"
    }

    stage("Deployment") {

        sh "pid=\$(lsof -i:8989 -t); kill -TERM \$pid " 
            + "|| kill -KILL \$pid"
        withEnv(['JENKINS_NODE_COOKIE=dontkill']) {
            sh 'nohup ./mvnw spring-boot:run -Dserver.port=8989 &'
        }   
    }
}