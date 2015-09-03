#!/bin/bash

mvn -q exec:java -Dexec.mainClass="com.lepacheco.nettyexamples.frame.FrameEchoClient" -Dexec.args="$@"
