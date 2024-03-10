node {

    checkout scm
    stage('Hello') {
        echo "Hello world!"
    }

    stage('Check for Changes') {
        def changes = scm.changeset
        if (changes.isEmpty()) {
            echo "No changes detected in the repository"
        } else {
            echo "Changes detected in the repository"
            echo "Hello World!"
        }
    }
}


