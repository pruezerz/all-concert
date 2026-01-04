@echo off
echo Compiling...
if not exist classes mkdir classes
javac -d classes -cp "json-20231013.jar" *.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)
echo Starting application...
java -cp "classes;json-20231013.jar" Login
