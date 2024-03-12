properties([
    parameters([
        string(defaultValue: 'master', description: 'Version  branch/commit/tag', name: 'branch'),
        booleanParam(defaultValue: false, description: 'Execute job in dry-run mode, this mean that all deployments and notifications are skipped', name: 'dry_run'),
    ])
])

def testForSleepFnc(int time) {
    echo "Start"
    sleep(time)
}

try {
    if (branch == "") {
        throw new Exception("Branch parameter is mandatory!")
    }
} catch (Exception e) {
    echo "Exception occured: " + e.toString()
    error("Pipeline execution halted due to exception: ${e}")
}

node {

    stage('Dry run') {
        if (dry_run.toBoolean()) {
            currentBuild.result = 'NOT_BUILT'
        } else {
            echo "Continue build.."
        }
    }

    stage('Scm') {
        echo "Hello world!"
        testForSleepFnc(5)
        def checkout = checkout([$class: "GitSCM",
            branches: [[name: branch]],
            extensions: [[$class: 'CleanBeforeCheckout']],
            userRemoteConfigs: [[url: " https://github.com/segerasimchik/python.git"]]
        ])
    }

    stage('Remote job info') {

        build job: 'get-info-job',  parameters: [
            string(name: 'branch', value: branch),
        ]
    }

    // stage('Check for Changes') {
    //     def changeSets = currentBuild.changeSets
    //     echo "ChangeSets variable value: ${changeSets}"
    //     if (changeSets.isEmpty()) {
    //         echo "No changes detected in the repository. Skip tag creation."
    //     } else {
    //         echo "Changes detected in the repository"
    //         echo "Applying tag to repo..."
    //         echo "Hello World!"
    //     }
    // }
}
