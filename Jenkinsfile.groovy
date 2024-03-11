properties([
    parameters([
        string(defaultValue: 'master', description: 'Version  branch/commit/tag', name: 'branch'),
        booleanParam(defaultValue: true, description: 'Execute job in dry-run mode, this mean that all deployments and notifications are skipped', name: 'dry_run'),
    ])
])

node {

    stage('Dry run') {
        if (dry_run.toBoolean()) {
            currentBuild.result == "SUCCESS"
        } else {
            echo "Continue build.."
        }
    }

    stage('Hello') {
        echo "Hello world!"
        def checkout = checkout([$class: "GitSCM",
            branches: [[name: branch]],
            extensions: [[$class: 'CleanBeforeCheckout']],
            userRemoteConfigs: [[credentialsId: "jenkins_github_ssh", url: "git@github.com:Playtika/bingoblitz-versions.git"]]
        ])
    }

    stage('Check for Changes') {
        def changeSets = currentBuild.changeSets
        echo "${changeSets}"
        if (changeSets.isEmpty()) {
            echo "No changes detected in the repository"
        } else {
            echo "Changes detected in the repository"
            echo "Applying tag to build..."
            echo "Hello World!"
        }
    }
}
