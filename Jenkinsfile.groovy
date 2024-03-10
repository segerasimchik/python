node {
    stage('Hello') {
        ansiColor('xterm') {
            echo "\u001B[31mHello \u001B[0mworld!"
        }
        
    }

}
