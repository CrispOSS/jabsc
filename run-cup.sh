#!/bin/bash

mvn exec:java -Dexec.mainClass="java_cup.Main" -Dexec.args="-expect 1000 -package bnfc.abs src/main/java/bnfc/abs/abs.cup"
mv *.java src/main/java/bnfc/abs
