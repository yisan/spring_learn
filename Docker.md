# 初识Docker

## 一、概念

Docker 是一个开源的应用容器引擎，基于 Go 语言实现，dotCloud 公司出品（后改名为Docker Inc）

Docker 可以让开发者打包他们的应用以及依赖包到一个轻量级、可移植的容器中，然后发布到任何流行的 Linux 机器上。

容器是完全使用沙箱机制，相互隔离，容器性能开销极低。



## 二、安装

Docker可以运行在MAC、Windows、CentOS、UBUNTU等操作系统上，作者基于Mac osx Monterey 12.1 安装Docker。

官网：https://www.docker.com



## 三、架构

![img](https://tva1.sinaimg.cn/large/008i3skNly1gyetnmw8ndj30ed0awt9u.jpg)



| Docker 镜像(Images)    | Docker 镜像（Image）是用于创建 Docker 容器的模板，就相当于是一个 root 文件系统。比如官方镜像 ubuntu:16.04 就包含了完整的一套 Ubuntu16.04 最小系统的 root 文件系统 |
| ---------------------- | ------------------------------------------------------------ |
| Docker 容器(Container) | 容器是独立运行的一个或一组应用，是镜像运行时的实体。镜像（Image）和容器（Container）的关系，就像是面向对象程序设计中的类和对象一样，镜像是静态的定义，容器是镜像运行时的实体。容器可以被创建、启动、停止、删除、暂停等。 |
| Docker 客户端(Client)  | Docker 客户端通过命令行或者其他工具使用 Docker SDK (https://docs.docker.com/develop/sdk/) 与 Docker 的守护进程通信。 |
| Docker 主机(Host)      | 一个物理或者虚拟的机器用于执行 Docker 守护进程和容器。       |
| Docker Registry        | Docker 仓库用来保存镜像，可以理解为代码控制中的代码仓库。Docker Hub([https://hub.docker.com](https://hub.docker.com/)) 提供了庞大的镜像集合供使用。一个 Docker Registry 中可以包含多个仓库（Repository）；每个仓库可以包含多个标签（Tag）；每个标签对应一个镜像。通常，一个仓库会包含同一个软件不同版本的镜像，而标签就常用于对应该软件的各个版本。我们可以通过 **<仓库名>:<标签>** 的格式来指定具体是这个软件哪个版本的镜像。如果不给出标签，将以 **latest** 作为默认标签。 |
| Docker Machine         | Docker Machine是一个简化Docker安装的命令行工具，通过一个简单的命令行即可在相应的平台上安装Docker，比如VirtualBox、 Digital Ocean、Microsoft Azure。 |

## 四、配置镜像加速器

国内从 DockerHub 拉取镜像有时会遇到困难，此时可以配置镜像加速器。Docker 官方和国内很多云服务商都提供了国内加速器服务，例如：

- 科大镜像：**https://docker.mirrors.ustc.edu.cn/**
- 网易：**https://hub-mirror.c.163.com/**
- 阿里云：**https://<你的ID>.mirror.aliyuncs.com**
- 七牛云加速器：**https://reg-mirror.qiniu.com**

笔者使用的是阿里云的镜像，具体配置方法如下

注册阿里云账号，注册后访问 https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors 

选择你对应的系统环境，根据操作指南进行操作即可

![image-20220113114943569](https://tva1.sinaimg.cn/large/008i3skNly1gyetnpooh5j31na0ki761.jpg)



由于我本机环境为 Mac osx 12.1 所以安装的是 Docker for Mac ,按照提示

> 针对安装了Docker for Mac的用户，您可以参考以下配置步骤：在任务栏点击 Docker Desktop 应用图标 -> Perferences，在左侧导航菜单选择 Docker Engine，在右侧输入栏编辑 json 文件。将 **你的加速器地址** 加到"registry-mirrors"的数组里，点击 Apply & Restart按钮，等待Docker重启并应用配置的镜像加速器。

配置重启Docker后，终端输入

```shell
docker info 
```

如果看到一下内容

![image-20220113120323007](/Users/ing/Library/Application Support/typora-user-images/image-20220113120323007.png)

说明配置成功。



## 五、Docker命令

由于笔者的电脑是Mac的，所以部分指令可能有些许差别，请自行百度你当前系统环境下的指令。

### （一）服务相关命令

#### 1. mac下启动Docker服务

当你安装完Docker client之后，其实Docker 服务已经是启动状态，此时我们可命令查看docker server 

```shell
launchctl list | grep docker  

19947	0	application.com.docker.docker.112883005.112883348
```

后面的 `application.com.docker.docker.112883005.112883348` 就是我本机当前Docker 的服务名。

> TIP
>
> 
>
> 虽然网上有说通过 launchctl start application.com.docker.docker.112883005.112883348 可以重新启动 ，但经过测试发现，无效。
>
> 所以还是直接点开Docker client应用 或者 `open /Applications/Docker.app` 来启动吧。

#### 2. 关闭服务

```shell
launchctl stop application.com.docker.docker.112883005.112883348
```



### （二）镜像相关命令

#### 1.获取镜像

首先我们可以从 Docker Hub 中查找镜像，**https://hub.docker.com/** ，还有就是可以直接使用 docker search 命令来搜索镜像，假设我们想获取一个 centos 的镜像

```shell
docker search 

docker search centos                                                                              
NAME                              DESCRIPTION                                     STARS     OFFICIAL   AUTOMATED
centos                            The official build of CentOS.                   6973      [OK]
ansible/centos7-ansible           Ansible on Centos7                              135                  [OK]
consol/centos-xfce-vnc            Centos container with "headless" VNC session…   135                  [OK]
...
...
```



如果我们想预先下载这个镜像，我们可以使用 docker pull 命令来下载它。比如我们来安装一个 centos7 的镜像

```shell
docker pull centos:7                                                                             
```

#### 2.运行容器

我们以刚才下载的 centos 为例，可以通过以下命令来运行容器

```shell
docker run -i -t centos:7 /bin/bash 

[root@e0b78eb7dac7 /]#
```

> TIP
>
> 1. 如果不指定一个镜像的版本标签，docker 默认使用 centos:lastest 镜像，lastest 指的就是最新版本。
> 2. 当运行容器时，使用的镜像如果在本地中不存在，docker 就会自动从 docker 镜像仓库中下载。





#### 3. 查看本地镜像列表

我们可以使用 **docker images** 来列出本地主机上的镜像。

```shell
docker images        

REPOSITORY   TAG       IMAGE ID       CREATED        SIZE
centos       7         eeb6ee3f44bd   4 months ago   204MB
```

可以看到我们刚才获取的centos 的镜像了。

> 各个选项栏的说明:
>
> * **REPOSITORY：**表示镜像的仓库源
>
> - **TAG：**镜像的版本
> - **IMAGE ID：**镜像ID
> - **CREATED：**镜像创建时间
> - **SIZE：**镜像大小
>
> TIP
>
> REPOSITORY:TAG 可以定义不同版本的镜像，比如前面的 centos:7







删除镜像

```shell
docker rmi imageId
```





### （三）容器相关命令



## 六、数据卷

认识数据卷之前我们先来思考几个问题

•Docker 容器删除后，在容器中产生的数据也会随之销毁，怎么持久化？

•Docker 容器和外部机器可以直接交换文件吗？

•容器之间想要进行数据交互怎么办？

### （一）概念



### （二）配置数据卷

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gyetnjnytqj30rk0gc3zl.jpg" alt="image-20220116002744405" style="zoom:50%;" />

创建启动容器时，使用-v参数，设置数据卷

```shell
docker run -it --name=c1 -v /Users/ing/test:/root/test_container centos:7 /bin/bash
```

* --name 表示容器的名称
* -v 表示挂载数据卷
* ： 表示将数据卷挂载到哪
* /Users/ing/test 表示的是宿主机（我本地电脑）下的一个目录或文件
* /root/test_container 表示的是容器中的一个目录或文件

执行后，宿主机的目录文件就和容器内的目录文件建立了映射。宿主机的目录文件就被作为数据卷。

> TIP
>
> 1. 目录必须是绝对路径
> 2. 目录不存在，会自动创建
> 3. 可以挂载多个数据卷

