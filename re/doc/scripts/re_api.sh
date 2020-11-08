#!/bin/bash
# 容器名
containerName=re
# 容器内re_api的路径
containerRePaiPath=/re/re_api
# jar包名
jarName=$(basename "$1")
# 容器内jdk路径
containerJavaHome=/re/software/jdk
# 检查jar包是否存在
if [ -z "$1" ]; then
  echo "usage: ./re_api.sh jar (start|stop|restart|replace)";
  echo "please specify the executable jar path, exit";
  exit 1;
else
  # jar包不存在
  if [ ! -f "$1" ]; then
    echo "usage: ./re_api.sh jar (start|stop|restart|replace)";
    echo "the executable jar is not exist, exit";
    exit 2;
  fi
fi

# 检查脚本指令是否正确
if [ -z "$2" ]; then
  echo "usage: ./re_api.sh jar (start|stop|restart|replace)";
  echo "please input your instruction like (start|stop|restart|replace), exit";
  exit 3;
else
  # 检查指令是否正确
  if [ "$2" != "start" ] && [ "$2" != "stop" ] && [ "$2" != "restart" ] && [ "$2" != "replace" ]; then
    echo "usage: ./re_api.sh jar (start|stop|restart|replace)";
    echo "instruction is not in (start|stop|restart|replace), exit";
    exit 4;
  fi
fi

# 检查docker是否启动
dockerStatus=$(systemctl status docker | sed -n '3p' | awk '{print $2}')
if [ "$dockerStatus" != "active" ]; then
  # 没有启动docker
  echo "docker service is not active, try to start docker";
  systemctl start docker
  if [ "$dockerStatus" != "active" ]; then
    echo "start docker failed, exit";
    exit 5;
  fi
fi

# 检查容器状态
checkContainerStatus=$(docker ps -aq --filter "name=$containerName" --filter status=running)
if [ -z "$checkContainerStatus" ]; then
  echo "the container $containerName is not running, try to run the container";
  docker start $containerName
  if [ -z "$checkContainerStatus" ]; then
    echo "start $containerName failed, exit";
    exit 6;
  fi
fi

# 启动函数
function start() {
  true
}

# 停止
function stop {
  true
}

# 重启
function restart {
  true
}

# 替换
function replace {
  # 检查jar包状态
  jarPid=$(docker exec -it $containerName /bin/bash -c "ps aux | grep $jarName" | grep -v grep | awk '{print $2}');
  if [ -n "$jarPid" ]; then
    # 如果jar包在运行，先停止
    docker exec -it $containerName /bin/bash -c "kill -9 $jarPid";
  fi
  # 将新的jar包拷贝到容器中去
  docker cp "$1" $containerName:$containerRePaiPath/"$jarName"
  # 启动新的jar包, 这里不能使用docker exec -it，否则在退出时 nohup 会被杀死
  docker exec -i $containerName /bin/bash -c "nohup $containerJavaHome/bin/java -jar $containerRePaiPath/$jarName > $containerRePaiPath/re.log 2>&1 &";
}

# 执行脚本
case $2 in
"start")
  start;
  exit 0;
  ;;
"restart")
  restart;
  exit 0;
  ;;
"stop")
  stop;
  exit 0;
  ;;
"replace")
  replace "$1" "$2";
  exit 0;
  ;;
esac