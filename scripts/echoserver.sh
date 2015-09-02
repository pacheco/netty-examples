#!/bin/bash

mvn -q exec:java -Dexec.mainClass="com.lepacheco.nettyexamples.echo.EchoServer" -Dexec.args="$@"
