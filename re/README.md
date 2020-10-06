## mysql8关闭group by全匹配
```mysql
set @@GLOBAL.sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
SELECT @@GLOBAL.sql_mode;
SELECT @@SESSION.sql_mode;
```

# 开启GC日志和设置最大内存为8G
```shell script
nohup /usr/local/jre1.8.0_261/bin/java -jar -XX:+PrintGCDetails -Xloggc:gc.log -Xms8192m -Xmx8192m oss-web.jar > /dev/null 2>&1 &
```