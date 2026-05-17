@echo off
setlocal
set BASE_DIR=%~dp0
set WRAPPER_DIR=%BASE_DIR%\.mvn\wrapper
set JAR=%WRAPPER_DIR%\maven-wrapper.jar
set PROPS=%WRAPPER_DIR%\maven-wrapper.properties

if not exist "%PROPS%" (
  echo Missing %PROPS%
  exit /b 1
)

if not exist "%JAR%" (
  for /f "tokens=2 delims==" %%A in ('findstr /b wrapperUrl= "%PROPS%"') do set WRAPPER_URL=%%A
  if "%WRAPPER_URL%"=="" (
    echo Missing wrapperUrl in %PROPS%
    exit /b 1
  )
  echo Downloading Maven wrapper jar...
  powershell -Command "Invoke-WebRequest -UseBasicParsing -Uri '%WRAPPER_URL%' -OutFile '%JAR%'" || exit /b 1
)

if defined JAVA_HOME (
  set JAVA_EXEC=%JAVA_HOME%\bin\java.exe
) else (
  set JAVA_EXEC=java
)

"%JAVA_EXEC%" -jar "%JAR%" -Dmaven.multiModuleProjectDirectory="%BASE_DIR%" %*

