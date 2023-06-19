node {
    stage("Clone the project") {
        git branch: 'test', url: 'https://github.com/HungPhan200591/bmi.git'
    }

    stage("Compilation") {
        sh "./mvnw clean install -DskipTests"
    }

    stage("Tests and Deployment") {
        stage("Runing unit tests") {
            sh "./mvnw test -Punit"
        }
        stage("Deployment") {
            sh 'nohup ./mvnw spring-boot:run -Dserver.port=8000 &'
        }
    }
}