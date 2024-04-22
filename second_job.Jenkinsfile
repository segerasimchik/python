properties([
    parameters([
        string(defaultValue: 'master', description: 'Version  branch/commit/tag', name: 'branch'),
    ])
])

def testForSleepFnc(int time) {
    echo "Start"
    sleep(time)
}

node {
    stage('Job info') {
        echo "${branch}"
        echo "Success!"
    }
}
