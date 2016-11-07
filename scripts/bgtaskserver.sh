#!/bin/bash

mvn -q exec:java -Dexec.mainClass="com.lepacheco.nettyexamples.bgtask.BGTaskServer" -Dexec.args="$@"
