#!/bin/bash

mvn -q exec:java -Dexec.mainClass="com.lepacheco.nettyexamples.kvstore.KVClient" -Dexec.args="$@"
