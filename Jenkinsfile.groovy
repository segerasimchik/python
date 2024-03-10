node {

    checkout scm
    stage('Hello') {
        echo "Hello world!"
    }

    stage('Check for Changes') {
        def changes = checkout([$class: 'GitSCM']).poll().changeset
        if (changes.isEmpty()) {
            echo "No changes detected in the repository"
        } else {
            echo "Changes detected in the repository"
        }
    }
}


