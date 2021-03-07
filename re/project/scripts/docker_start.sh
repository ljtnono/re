#!/bin/bash

docker run -d \
-p 80:80 \
-p 443:443 \
-p 8003:3306 \
-p 8004:6379 \
-p 8005:9200 \
--name re_<version> \
--privileged=true \
re:<version> /sbin/init