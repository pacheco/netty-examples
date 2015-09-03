#!/bin/bash

mvn -q exec:java -Dexec.mainClass="com.lepacheco.nettyexamples.kvstore.KVServer" -Dexec.args="$@"
