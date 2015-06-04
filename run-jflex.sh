#!/bin/bash

mvn exec:java -Dexec.mainClass="jflex.Main" -Dexec.args="--jlex src/main/java/bnfc/abs/Yylex"
