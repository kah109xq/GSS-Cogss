pipeline {
    agent none
    stages {
        stage('Test') {
            agent {
                dockerfile {
                    args '-u root:root'
                }
            }
            steps {
                sh "if [ -f src/main/bin/run-csvw-tests ]; then rm src/main/bin/run-csvw-tests; fi"
                sh "if [ -f test/resources/features/csvw_validation_tests.feature ]; then rm test/resources/features/csvw_validation_tests.feature; fi"
                sh "if [ -d test/resources/features/fixtures/csvw ]; then rm -Rf test/resources/features/fixtures/csvw; fi"
                sh "sbt compile"
                sh "sbt test"
            }
        }
    }
    post {
        always {
            script {
                node {
                    junit allowEmptyResults: true, testResults: 'target/test-reports/*.xml'
                }
            }
        }
    }
}
