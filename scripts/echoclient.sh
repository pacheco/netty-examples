#!/bin/bash

mvn -q exec:java -Dexec.mainClass="com.lepacheco.nettyexamples.echo.EchoClient" -Dexec.args="$@"
