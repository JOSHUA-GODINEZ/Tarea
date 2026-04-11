@echo off
cd /d "%~dp0"

start "" /min cmd /c ".\mvnw.cmd javafx:run -Djavafx.args=admistrador"
