@echo off
setlocal

set MAVEN_VERSION=3.9.9
set WRAPPER_DIR=%USERPROFILE%\.m2\wrapper\dists\apache-maven-%MAVEN_VERSION%
set MAVEN_HOME=%WRAPPER_DIR%\apache-maven-%MAVEN_VERSION%

if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
    echo Downloading Apache Maven %MAVEN_VERSION%...
    powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $ProgressPreference='SilentlyContinue'; $version='%MAVEN_VERSION%'; $url='https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/' + $version + '/apache-maven-' + $version + '-bin.zip'; $zip=Join-Path $env:TEMP ('apache-maven-' + $version + '-bin.zip'); $dest='%WRAPPER_DIR%'; Invoke-WebRequest -UseBasicParsing -Uri $url -OutFile $zip; New-Item -ItemType Directory -Force -Path $dest | Out-Null; Expand-Archive -Path $zip -DestinationPath $dest -Force"
)

call "%MAVEN_HOME%\bin\mvn.cmd" %*
