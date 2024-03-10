def changeSets = currentBuild.changeSets

node {

    checkout scm
    stage('Hello') {
        echo "Hello world!"
    }

    stage('Check for Changes') {
        if (changeSets.isEmpty()) {
            echo "No changes detected in the repository"
        } else {
            echo "Changes detected in the repository"
            echo "applying tag to build..."
            echo "Hello World!"
        }
    }
}

