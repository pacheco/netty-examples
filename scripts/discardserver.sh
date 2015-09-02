#!/bin/bash

mvn -q exec:java -Dexec.mainClass="com.lepacheco.nettyexamples.discard.DiscardServer" -Dexec.args="$@"
