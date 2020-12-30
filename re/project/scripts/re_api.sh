#!/bin/bash
# re_api 启动、停止、重启、替换脚本
# 容器名
containerName=re
# 容器内re_api的路径
containerDirPath=/re/re_api
# 容器内jdk路径
containerJavaHome=/re/software/jdk
# jar包名
jarName=
# 检查脚本指令是否正确
if [ -z "$1" ]; then
    echo "usage: ./re_api.sh (start|stop|restart|replace) [jar]"
    echo "please input your instruction like (start|stop|restart|replace), exit"
    exit 1
else
    # 检查指令是否正确
    if [ "$1" != "start" ] && [ "$1" != "stop" ] && [ "$1" != "restart" ] && [ "$1" != "replace" ]; then
        echo "usage: ./re_api.sh (start|stop|restart|replace) [jar]"
        echo "instruction is not in (start|stop|restart|replace), exit"
        exit 2
    fi
    # 如果是替换命令
    if [ "$1" == "replace" ]; then
        # 检查jar包是否存在
        if [ -z "$2" ]; then
            echo "usage: ./re_api.sh (start|stop|restart|replace) [jar]"
            echo "please specify the executable jar path, exit"
            exit 3
        else
            # jar包不存在
            if [ ! -f "$2" ]; then
                echo "usage: ./re_api.sh (start|stop|restart|replace) [jar]"
                echo "the executable jar is not exist, exit"
                exit 4
            fi
            # jar包存在
            jarName=$(basename "$2")
        fi
    fi
fi

# 检查docker是否启动
dockerStatus="systemctl status docker | sed -n '3p' | awk '{print \$2}'"
if [[ $(eval "${dockerStatus}") != "active" ]]; then
    # 没有启动docker
    echo "docker service is not active, try to start docker"
    systemctl start docker
    if [[ $(eval "${dockerStatus}") != "active" ]]; then
        echo "start docker failed, exit"
        exit 5
    fi
fi

# 检查容器状态
checkContainerStatus="docker ps -aq --filter 'name=$containerName' --filter status=running"
if [[ -z "$(eval "$checkContainerStatus")" ]]; then
    echo "the container $containerName is not running, try to run the container"
    docker start $containerName
    if [ -z "$(eval "$checkContainerStatus")" ]; then
        echo "start $containerName failed, exit"
        exit 6
    fi
fi

# 启动函数
function start() {
    jarPid=$(docker exec -it $containerName /bin/bash -c 'ps aux | grep re-.*\.jar | grep -v grep | awk {"print \$2"}')
    if [ -z "$jarPid" ]; then
        # 检查是否是启动状态
        jarPath=/re/re_api/re-0.0.1.jar
        javaCmdPath=$containerJavaHome/bin/java
        logPath=$containerDirPath/re.log
        docker exec -i $containerName /bin/bash -c 'nohup '"$javaCmdPath"' -jar '"$jarPath"' > '"$logPath"' 2>&1 &'
    fi
}

# 停止
function stop() {
    # 检查jar包状态
    jarPid=$(docker exec -it $containerName /bin/bash -c "ps aux | grep 're-.*\.jar' | grep -v grep | awk '{print \$2}'")
    if [ -n "$jarPid" ]; then
        # 如果jar包在运行，先停止
        docker exec -it $containerName sh -c "kill -9 $jarPid"
    fi
}

# 重启
function restart() {
    jarPid=$(docker exec -it $containerName /bin/bash -c 'ps aux | grep re-.*\.jar | grep -v grep | awk "{print \$2}"')
    if [ -n "$jarPid" ]; then
        # 如果jar包在运行，先停止
        docker exec -i $containerName sh -c "kill -9 $jarPid"
    fi
    # 找到相关文件并启动
    jarPath=/re/re_api/re-0.0.1.jar
    javaCmdPath=$containerJavaHome/bin/java
    logPath=$containerDirPath/re.log
    docker exec -i $containerName /bin/bash -c 'nohup '"$javaCmdPath"' -jar '"$jarPath"' > '"$logPath"' 2>&1 &'
}

# 替换
function replace() {
    # 检查jar包状态
    jarPid=$(docker exec -it $containerName /bin/bash -c "ps aux | grep $jarName | grep -v grep | awk '{print \$2}'")
    if [ -n "$jarPid" ]; then
        # 如果jar包在运行，先停止
        docker exec -it $containerName sh -c "kill -9 $jarPid"
    fi
    # 将新的jar包拷贝到容器中去
    docker cp "$1" $containerName:$containerDirPath/"$jarName"
    # 启动新的jar包, 这里不能使用docker exec -it，否则在退出时 nohup 会被杀死，只能使用docker exec -i
    docker exec -i $containerName /bin/bash -c "nohup $containerJavaHome/bin/java -jar $containerDirPath/$jarName &> $containerDirPath/re.log 2>&1 &"
}

# 执行脚本
case $1 in
"start")
    start
    exit 0
    ;;
"restart")
    restart
    exit 0
    ;;
"stop")
    stop
    exit 0
    ;;
"replace")
    replace "$2"
    exit 0
    ;;
esac
