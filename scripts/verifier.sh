#! /bin/bash
export dir=$( dirname "${BASH_SOURCE[0]}" )
export CLASSPATH=$dir/verifier.jar:$dir/lib/stax-1.2.0-all-bin.zip

java -cp $CLASSPATH main.Main "$@"
