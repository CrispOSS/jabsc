#!/bin/bash

./run-bnfc.sh
./run-cup.sh
./run-jflex.sh
mvn clean compile
