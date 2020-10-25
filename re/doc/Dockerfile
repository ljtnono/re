FROM ubuntu:20.04
# 作者信息
MAINTAINER ling, jiatong<935188400@qq.com>
# 备份apt镜像源
RUN mv /etc/apt/sources.list /etc/apt/sources.list.back \
    && touch /etc/apt/sources.list \
    && echo "deb http://mirrors.aliyun.com/ubuntu/ focal main restricted universe multiverse" >> /etc/apt/sources.list \
    && echo "deb-src http://mirrors.aliyun.com/ubuntu/ focal main restricted universe multiverse" >> /etc/apt/sources.list \
    && echo "deb http://mirrors.aliyun.com/ubuntu/ focal-security main restricted universe multiverse" >> /etc/apt/sources.list \
    && echo "deb-src http://mirrors.aliyun.com/ubuntu/ focal-security main restricted universe multiverse" >> /etc/apt/sources.list \
    && echo "deb http://mirrors.aliyun.com/ubuntu/ focal-updates main restricted universe multiverse" >> /etc/apt/sources.list \
    && echo "deb-src http://mirrors.aliyun.com/ubuntu/ focal-updates main restricted universe multiverse" >> /etc/apt/sources.list \
    && echo "deb http://mirrors.aliyun.com/ubuntu/ focal-proposed main restricted universe multiverse" >> /etc/apt/sources.list \
    && echo "deb-src http://mirrors.aliyun.com/ubuntu/ focal-proposed main restricted universe multiverse" >> /etc/apt/sources.list \
    && echo "deb http://mirrors.aliyun.com/ubuntu/ focal-backports main restricted universe multiverse" >> /etc/apt/sources.list \
    && echo "deb-src http://mirrors.aliyun.com/ubuntu/ focal-backports main restricted universe multiverse" >> /etc/apt/sources.list \
    && apt update \
    && apt-get install -y init \
    && apt install -y tree \
    && apt install -y vim \
    && apt install -y wget \
    && apt install -y curl \
    && apt install -y libpcre3 libpcre3-dev openssl libssl-dev libperl-dev zlib1g zlib1g-dev \
    && apt-get install -y numactl \
    && apt install -y libncurses5

ENV TZ Asia/Shanghai
CMD ["/sbin/init"]