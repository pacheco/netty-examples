#!/bin/bash

mvn -q exec:java -Dexec.mainClass="com.lepacheco.nettyexamples.bgtask.BGTaskClient" -Dexec.args="$@"
