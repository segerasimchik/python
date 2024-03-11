properties([
    parameters([
        string(defaultValue: 'master', description: 'Version  branch/commit/tag', name: 'branch'),
        booleanParam(defaultValue: false, description: 'Execute job in dry-run mode, this mean that all deployments and notifications are skipped', name: 'dry_run'),
    ])
])

node {

    stage('Dry run') {
        if (dry_run.toBoolean()) {
            currentBuild.result = 'NOT_BUILT'
        } else {
            echo "Continue build.."
        }
        sleep(30)
    }

    stage('Second stage') {
        echo "Hello world!"
        def checkout = checkout([$class: "GitSCM",
            branches: [[name: branch]],
            extensions: [[$class: 'CleanBeforeCheckout']],
            userRemoteConfigs: [[url: " https://github.com/segerasimchik/python.git"]]
        ])
    }

    stage('Check for Changes') {
        def changeSets = currentBuild.changeSets
        echo "ChangeSets variable value: ${changeSets}"
        if (changeSets.isEmpty()) {
            echo "No changes detected in the repository"
        } else {
            echo "Changes detected in the repository"
            echo "Applying tag to build..."
            echo "Hello World!"
        }
        sleep(10)
    }
}
