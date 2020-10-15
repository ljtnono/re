FROM centos:7
# 作者信息
MAINTAINER ling, jiatong<935188400@qq.com>
# 备份yum镜像源
RUN mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.bak \
    && mv /etc/yum.repos.d/CentOS-CR.repo /etc/yum.repos.d/CentOS-CR.repo.bak \
    && mv /etc/yum.repos.d/CentOS-Debuginfo.repo /etc/yum.repos.d/CentOS-Debuginfo.repo.bak \
    && mv /etc/yum.repos.d/CentOS-fasttrack.repo /etc/yum.repos.d/CentOS-fasttrack.repo.bak \
    && mv /etc/yum.repos.d/CentOS-Sources.repo /etc/yum.repos.d/CentOS-Sources.repo.bak \
    && mv /etc/yum.repos.d/CentOS-Vault.repo /etc/yum.repos.d/CentOS-Vault.repo.bak
# 更换yum源
RUN curl -o /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo \
    && yum clean all \
    && yum makecache
# 更改时区
RUN echo export TZ=Asia/Shanghai >> /etc/profile \
    && source /etc/profile
# 创建目录
RUN mkdir /re \
    && mkdir /re/re_api \
    && mkdir /re/re_admin \
    && mkdir /re/re_front \
    && mkdir /re/software \
    && mkdir /re/software/redis \
    && mkdir /re/software/mysql8 \
    && mkdir /re/software/jre
# 编译安装redis和mysql


CMD ["/usr/sbin/init"]
