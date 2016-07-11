#!/bin/bash

bnfc -m  --java -o src/main/java --jflex -p bnfc src/main/resources/abs.cf


# Remove unused
rm src/main/java/bnfc/abs/ComposVisitor.java
rm src/main/java/bnfc/abs/PrettyPrinter.java
rm src/main/java/bnfc/abs/Test.java

# By default, use Long instead of Integer
sed -i -e 's/new Integer(yytext())/new Long(yytext())/g' src/main/java/bnfc/abs/Yylex
sed -i -e 's/terminal Integer/terminal Long/g' src/main/java/bnfc/abs/abs.cup
sed -i -e 's/Integer /Long /g' src/main/java/bnfc/abs/Absyn/LInt.java
