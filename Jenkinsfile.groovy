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

stage('Get info from remote job') {
    def remoteRepoInfo = build job: 'get-info-job',  parameters: [
                string(name: 'branch', value: branch),
            ]
    
    def remoteChangeSets = remoteRepoInfo.rawBuild.changeSets
    echo "${remoteChangeSets}"
    if (remoteChangeSets.isEmpty()) {
        echo "No changes detected in the repository. Skip tag creation."
    } else {
        echo "Changes detected in the repository"
        echo "Applying tag to repo..."
    }
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
        //testForSleepFnc(5)
        def checkout = checkout([$class: "GitSCM",
            branches: [[name: branch]],
            extensions: [[$class: 'CleanBeforeCheckout']],
            userRemoteConfigs: [[url: " https://github.com/segerasimchik/python.git"]]
        ])
    }

    // stage('Get info from remote job') {
    //     def remoteRepoInfo = build job: 'get-info-job',  parameters: [
    //                 string(name: 'branch', value: branch),
    //             ]

    //     def remoteChangeSets = remoteRepoInfo.rawBuild.changeSets
    //     echo "${remoteChangeSets}"

    //     if (remoteChangeSets.isEmpty()) {
    //         echo "No changes detected in the repository. Skip tag creation."
    //     } else {
    //         echo "Changes detected in the repository"
    //         echo "Applying tag to repo..."
    //     }
    // }

    stage('Remote job info') {
        echo "Success!"
    }
}
