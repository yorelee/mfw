#!/bin/bash
mvn clean install -DskipTests=true && 
docker build --tag vup-registry.cloudzcp.io/modern/example/<%=name%>:0.0.1 . && 
docker push vup-registry.cloudzcp.io/modern/example/<%=name%>:0.0.1
