#!/bin/bash
container_name=re
mysql_base_dir=/re/software/mysql
redis_base_dir=/re/software/redis
# 启动mysql
docker exec -it $container_name /bin/bash "$mysql_base_dir/support-files/mysql.server start"
# 启动redis
docker exec -it $container_name -c "$redis_base_dir/bin/redis-server $redis_base_dir/conf/redis.conf"