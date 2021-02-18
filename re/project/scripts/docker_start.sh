#!/bin/bash

docker run -d \
-p 8001:80 \
-p 8002:443 \
-p 8003:3306 \
-p 8004:6379 \
-p 8005:9200 \
--name re \
--privileged=true re_base:20210210