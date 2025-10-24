#!/bin/bash
# ----------------------------------------------------------------------
# name:         lite.sh
# version:      1.1
#
# 使用说明：
# 1: 该脚本使用前需要首先修改 MAIN_CLASS 值，使其指向实际的启动类
#
# 2：使用命令行 ./lite.sh start | stop | restart 可启动/关闭/重启项目
#
# 3: JAVA_OPTS 可通过 -D 传入 undertow.port 与 undertow.host 这类参数覆盖
#    配置文件中的相同值此外还有 undertow.resourcePath、undertow.ioThreads、
#    undertow.workerThreads 共五个参数可通过 -D 进行传入，该功能尽可能减少了
#    修改 undertow 配置文件的必要性
#
# 4: JAVA_OPTS 可传入标准的 java 命令行参数，例如 -Xms256m -Xmx1024m 这类常用参数
#
# 5: 脚本会自动检查进程是否存在，避免重复启动
#
# 6: 错误日志将输出到 logs/error.log 文件中
#
# ----------------------------------------------------------------------

# 启动入口类，该脚本文件用于别的项目时要改这里
MAIN_CLASS=com.xclite.api.MockApp

if [[ "$MAIN_CLASS" == "com.yourpackage.YourMainClass" ]]; then
    echo "请先修改 MAIN_CLASS 的值为你自己项目启动Class，然后再执行此脚本。"
	exit 1
fi

COMMAND="$1"

# 默认端口
PORT="8088"

# 如果提供了端口参数，则使用提供的端口
if [[ -n "$2" ]]; then
    PORT="$2"
fi

if [[ "$COMMAND" != "start" ]] && [[ "$COMMAND" != "stop" ]] && [[ "$COMMAND" != "restart" ]] && [[ "$COMMAND" != "status" ]]; then
	echo "Usage: $0 start [port] | stop | restart | status"
	exit 1
fi

# Java 命令行参数，根据需要开启下面的配置，改成自己需要的，注意等号前后不能有空格
# 设置端口和其他参数
JAVA_OPTS="-Dundertow.port=${PORT} -Dundertow.host=0.0.0.0"

# 生成 class path 值
APP_BASE_PATH=$(cd `dirname $0`; pwd)
CP=${APP_BASE_PATH}/config:${APP_BASE_PATH}/lib/*
LOG_DIR=${APP_BASE_PATH}/logs
ERROR_LOG=${LOG_DIR}/error.log

# 创建日志目录
mkdir -p ${LOG_DIR}

# 检查进程是否已存在
function check_process() {
    if pgrep -f "${MAIN_CLASS}" >/dev/null 2>&1; then
        return 0
    else
        return 1
    fi
}

# 获取进程PID
function get_pid() {
    echo `pgrep -f "${MAIN_CLASS}"`
}

function start()
{
    # 检查进程是否已存在
    if check_process; then
        PID=$(get_pid)
        echo "进程已存在，PID: ${PID}"
        return 1
    fi
    
    echo "正在启动应用，端口: ${PORT}..."
    
    # 运行为后台进程，并将错误输出到日志文件
    nohup java -Xverify:none ${JAVA_OPTS} -cp ${CP} ${MAIN_CLASS} >${LOG_DIR}/output.log 2>${ERROR_LOG} &
    
    # 等待一段时间以确保进程启动
    sleep 2
    
    # 再次检查进程是否成功启动
    if check_process; then
        PID=$(get_pid)
        echo "应用启动成功，PID: ${PID}，端口: ${PORT}"
        echo "标准输出日志: ${LOG_DIR}/output.log"
        echo "错误日志: ${ERROR_LOG}"
    else
        echo "应用启动失败，请查看错误日志: ${ERROR_LOG}"
        return 1
    fi
}

function stop()
{
    echo "正在停止应用..."
    
    # 检查进程是否存在
    if ! check_process; then
        echo "应用未运行"
        return 1
    fi
    
    # 获取当前PID
    PID=$(get_pid)
    echo "正在终止进程，PID: ${PID}"
    
    # 正常停止进程
    kill ${PID} 2>/dev/null
    
    # 等待进程结束
    COUNT=0
    while check_process && [[ ${COUNT} -lt 30 ]]; do
        sleep 1
        ((COUNT++))
    done
    
    # 如果进程仍未结束，强制杀死
    if check_process; then
        echo "正常停止超时，正在强制终止进程..."
        kill -9 ${PID} 2>/dev/null
        sleep 2
        
        if check_process; then
            echo "无法终止进程"
            return 1
        fi
    fi
    
    echo "应用已停止"
}

function status()
{
    if check_process; then
        PID=$(get_pid)
        echo "应用正在运行，PID: ${PID}"
    else
        echo "应用未运行"
    fi
}

# 根据命令执行相应操作
case "${COMMAND}" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        stop
        start
        ;;
    status)
        status
        ;;
    *)
        echo "Usage: $0 start [port] | stop | restart | status"
        exit 1
        ;;
esac