 	node {
	  stage("Clone the project") {
	    git branch: 'test', url: 'https://github.com/HungPhan200591/bmi.git'
	  }
	
	  stage("Deployment") {

	    stage("Deployment") {
	      sh 'nohup ./mvnw spring-boot:run -Dserver.port=8001 &'
	    }
	  }
	}