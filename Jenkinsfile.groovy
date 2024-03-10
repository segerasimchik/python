node {

    checkout scm
    stage('Hello') {
        echo "Hello world!"
    }

    stage('Check for Changes') {
        def changeSets = currentBuild.changeSets
        if (changeSets.isEmpty()) {
            echo "No changes detected in the repository"
        } else {
            echo "Changes detected in the repository"
            echo "Applying tag to build..."
            echo "Hello World!"
        }
    }
}

