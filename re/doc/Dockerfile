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
# 创建目录
RUN mkdir /re \
    && mkdir /re/re_api \
    && mkdir /re/re_admin \
    && mkdir /re/re_front \
    && mkdir /re/software
# 安装必要工具
RUN yum install -y wget \
    && yum install -y tree \
    && yum install -y vim
# 拷贝redis、mysql、nginx到相关文件夹
COPY redis/. /re/software/redis
COPY nginx/nginx/. /re/software/nginx
COPY mysql8/mysql/. /re/software/mysql
COPY jre/. /re/software/jre
# 配置mysql相关
# 安装mysql依赖库
RUN yum search libaio \
    && yum -y install libaio \
    && yum -y install numactl
# 添加mysql用户和组，配置相关权限
RUN groupadd mysql \
    && useradd -r -g mysql -s /bin/false mysql \
    && chown -R mysql:mysql /re/software/mysql
# 初始化mysql
RUN /re/software/mysql/bin/mysqld \
    --defaults-file=/re/software/mysql/my.cnf \
    --initialize
# 配置环境变量
ENV TZ Asia/Shanghai
ENV JAVA_HOME /re/software/jre
ENV MYSQL_HOME /re/software/mysql
ENV PATH $PATH:$JAVA_HOME/bin:$MYSQL_HOME/bin
# 刷新环境变量
RUN source /etc/profile
# 容器后台运行命令
CMD ["/usr/sbin/init"]