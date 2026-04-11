Set WshShell = CreateObject("WScript.Shell")
WshShell.Run "cmd /c mvnw.cmd javafx:run -Djavafx.args=officials", 0, False