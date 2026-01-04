@echo off
if not exist classes mkdir classes
javac -d classes -cp "json-20231013.jar" *.java
