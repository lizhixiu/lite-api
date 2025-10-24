@echo off

rem -------------------------------------------------------------------------
rem
rem 使用说明：
rem
rem 1: 该脚本用于别的项目时只需要修改 MAIN_CLASS 即可运行
rem
rem 2: JAVA_OPTS 可通过 -D 传入 undertow.port 与 undertow.host 这类参数覆盖
rem    配置文件中的相同值此外还有 undertow.resourcePath, undertow.ioThreads
rem    undertow.workerThreads 共五个参数可通过 -D 进行传入
rem
rem 3: JAVA_OPTS 可传入标准的 java 命令行参数,例如 -Xms256m -Xmx1024m 这类常用参数
rem
rem 4: 脚本支持指定端口启动，默认端口为8088
rem
rem 5: 脚本支持 status 命令检查应用状态
rem
rem -------------------------------------------------------------------------

setlocal & pushd

rem 启动入口类,该脚本文件用于别的项目时要改这里
set MAIN_CLASS=com.xclite.api.DemoApp

rem 默认端口
set PORT=8088

rem 检查参数
if "%1"=="start" goto normal
if "%1"=="stop" goto normal
if "%1"=="restart" goto normal
if "%1"=="status" goto normal

goto error

:error
echo Usage: lite.bat start [port] ^| stop ^| restart ^| status
goto :eof

:normal
rem 如果提供了端口参数，则使用提供的端口
if "%2" neq "" set PORT=%2

if "%1"=="start" goto start
if "%1"=="stop" goto stop
if "%1"=="restart" goto restart
if "%1"=="status" goto status
goto :eof

:start
rem 设置Java参数，包括端口
set "JAVA_OPTS=-Dundertow.port=%PORT% -Dundertow.host=0.0.0.0"

set APP_BASE_PATH=%~dp0
set CP=%APP_BASE_PATH%config;%APP_BASE_PATH%lib\*

rem 检查进程是否已存在
echo Checking if application is already running...
for /f "tokens=*" %%i in ('jps -l ^| find "%MAIN_CLASS%"') do (
    echo Application is already running with PID: %%i
    goto :eof
)

echo Starting lite undertow on port %PORT%...
echo Main class: %MAIN_CLASS%
java -Xverify:none %JAVA_OPTS% -cp %CP% %MAIN_CLASS%
goto :eof

:stop
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo Stopping lite undertow...
set PROCESS_FOUND=0
for /f "tokens=1" %%i in ('jps -l ^| find "%MAIN_CLASS%"') do (
    echo Terminating process %%i
    taskkill /F /PID %%i
    set PROCESS_FOUND=1
)

if "%PROCESS_FOUND%"=="0" (
    echo Application is not running
)

goto :eof

:restart
call :stop
timeout /t 2 /nobreak >nul
call :start
goto :eof

:status
set PROCESS_FOUND=0
for /f "tokens=1,*" %%i in ('jps -l ^| find "%MAIN_CLASS%"') do (
    echo Application is running with PID: %%i
    set PROCESS_FOUND=1
)

if "%PROCESS_FOUND%"=="0" (
    echo Application is not running
)

goto :eof

endlocal & popd
pause