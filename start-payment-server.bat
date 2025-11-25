@echo off
echo Starting Payment Server...
cd payment
python -m http.server 8080
pause
