docker run \
-d -p 8001:8001 -p 8002:8002 -p 8003:3306 -p 8004:6379 \
--privileged=true \
--name re \
-e LC_ALL="zh_CN.UTF-8" \
-e LANG="zh_CN.UTF-8" \
ljtnono/re_base:latest /usr/sbin/init
