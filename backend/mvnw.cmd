@echo off
setlocal

REM Prefer MAVEN_HOME if set
if not "%MAVEN_HOME%"=="" (
  if exist "%MAVEN_HOME%\bin\mvn.cmd" (
    call "%MAVEN_HOME%\bin\mvn.cmd" %*
    exit /b %errorlevel%
  )
)

REM Fallback to known local Maven install
if exist "C:\Users\DTI\.maven\maven-3.9.12\bin\mvn.cmd" (
  call "C:\Users\DTI\.maven\maven-3.9.12\bin\mvn.cmd" %*
  exit /b %errorlevel%
)

REM Fallback to PATH
mvn.cmd %*
exit /b %errorlevel%
