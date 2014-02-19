---
layout: post
title: "Openstack上安装AppScale"
date: 2014-02-19 21:44:13 +0800
comments: true
categories: Appscale
---

###原由
---
之前用Mac安装了virtual box的Appscale，因为是Appscale的一个安装好的Image，配置一下启动就能用。但是如果真要安装到虚拟主机上，就不是这个方式了。所以找了个Openstack的Unbuntu的虚拟主机尝试安装，结果血泪不少，需要配置proxy是最啃爹的部分。估计官方没有试过在用proxy的情况下安装。


###解决办法
---
因为官网的文档在openstack上安装只是简单的介绍，所以中间的问题都需要自己解决了。

- 安装unbuntu VM
  在Openstack中生成一个instance，我这里选用了unbuntu server 12.04，估计redhat也可以，不过我没有试，配置至少要3-4G RAM，20G的硬盘，2个virtual CPU，然后启动该VM，并看看该VM最后的IP地址
  
- 设置SSH配置
   如果用openstack安装VM后，需要用private key才可以登录(`ssh root@service_IP`)，则必须先修改SSH的配置，然密码可登录，具体方式是更改`/etc/ssh/sshd_conf`文件，将`PasswordAuthentication`参数设置为`true`，然后更改root密码用命令`passwd`，最后重启SSH，用命令`/etc/init.d/ssh restart`


- 设置各种应用的代理
   - 首先设置apt-get的代理，新增一个`/etc/apt/apt.conf`配置文件，内容为
   
```
   Acquire::http::proxy "http://<username>:<password>@<proxy>:<port>/";
   Acquire::ftp::proxy "ftp://<username>:<password>@<proxy>:<port>/";
   Acquire::https::proxy "https://<username>:<password>@<proxy>:<port>/";
```
   将proxy改为对应的代理地址，port为代理端口，如果需要认证，则设置用户名和密码，不然就去掉用户名和密码
   
   － 设置ruby的gem的代理，在root的home目录下，创建.gemrc文件
   
```
http_proxy: http://<username>:<password>@<proxy>:<port>
https_proxy: http://<username>:<password>@<proxy>:<port>
ftp_proxy: http://<username>:<password>@<proxy>:<port>
```   
   - 设置wget以及其它代理，在对应的shell文件中(如bash的.bashrc)，增加代理变量设置，然后激活`source .bashrc`

```
export http_proxy=http://<username>:<password>@<proxy>:<port>
export https_proxy=http://<username>:<password>@<proxy>:<port>
export ftp_proxy=http://<username>:<password>@<proxy>:<port>
export HTTP_PROXY=http://<username>:<password>@<proxy>:<port>
export HTTPS_PROXY=http://<username>:<password>@<proxy>:<port>
export FTP_PROXY=http://<username>:<password>@<proxy>:<port>
```   
   
   - 设置java代理，因为安装的中间会用到java的下载等，在对应的shell文件中(如bash的.bashrc)，增加代理变量设置
   
```
export _JAVA_OPTIONS='-Dhttp.proxyHost=<proxy> -Dhttp.proxyPort=<port>'
```    
   - 增加git代理（针对git协议），因为要用到apt-get，所以apt-get的代理设置要先安装好
      先用apt-get安装socat
      `sudo apt-get install socat`，
      然后配置git代理文件，先`sudo touch /usr/bin/gitproxy`，然后编辑该文件的内容为
      
``` sh
PROXY=<proxy>
PROXYPORT=<port>
PROXYAUTH=<username:password>
exec socat STDIO PROXY:$PROXY:$1:$2,proxyport=$PROXYPORT,proxyauth=$PROXYAUTH
```        
    
   然后更改gitproxy的权限为可执行`sudo  chmod +x /usr/bin/gitproxy`，最后是更改git的配置，用命令`git config --global core.gitproxy gitproxy`
   这里有点啃爹，因为这个时候你还没有安装git，根本就没有git命令，所以只能先用 apt-get安装git，尽管Appscale的脚本有自动安装git。或者等到安装过程中git无发clone appscale的时候再配这一步
   

- 更新Python的pip工具
   这个是最坑爹的，因为pip依赖与Python，而如果VM没有带Python，只能等到安装到pip这一步出错了再来配置它，因为Appscale依赖的python的版本的问题，自行先安装Python似乎不是太好。这里需要更新pip是因为Appscale安装的是1.0版本，这个版本太低，无法用代理的方式（似乎是没有读取系统代理变量），所以需要更新到新的版本。执行一下命令：
   
```
sudo apt-get purge python-pip
curl https://raw.github.com/pypa/pip/master/contrib/get-pip.py | sudo python
```   

- 设置path
   
   增加`/usr/local/bin` (因为pip安装在这个目录中)，并在home目录激活shell配置`source .bashrc`

- 安装AppScale
  1. 可以用密码登录SSH了和apt-get以及wget的代理已经配置好
  2. 执行命令：`export HOME=/root` 和`wget -O - http://bootstrap.appscale.com | sh`
  3. 如果错误提示是git，则安装git代理，重新这行2的命令
  4. 如果错误提示是pip，则无需重新执行2的命令，因为appscale目录已经存在，只需要到`appscale/debian`目录下执行`appscale_build.sh`就可
  5. 如果错误提示是java，则安装JAVA代理，然后执行4
  
- 安装AppScale-Tool
  到`appscale-tools/debian`目录下执行`appscale_build.sh`，然后确保一下内容已经配置到shell文件(.bashrc)中
  
```
export TOOLS_PATH=/usr/local/appscale-tools
export PATH=${PATH}:${TOOLS_PATH}/bin
```    

- 配置Appscale

执行`appscale init cluster`，然后更改AppScalefile，将`ips_layout`里的IP地址都改为VM的地址

- 运行AppScale

执行`appscale up`
中间会要求输入admin的管理员的email和密码

- 查看安装后状态

如果正常启动，则可以通过`http://<vm ip>:1080/status`查看当前状态。如果只是这页全现实勾也是不行的，需要用启动时配置的email登录，然后点击Monit Dashboard(这个默认用户名和密码是admin/monit)，所有的process都应该时Running的状态

如果正常启动，则可以通过`http://<vm ip>:1080/status`查看当前状态。点击TaskQueue Monitor(这个默认用户名和密码是appscale/appscale)，则应该现实有两个工作的Workers，一个是apichecker一个是appscaledashboard

到此，真个Appscale的手工安装就结束了，然后就是可以部署应用。Googel App Engine的开发如何做，我还没有去看，后面有机会再写这个部分

**个人感叹：Open source真个给我们带来了很多的便利和选择，但是这的有很多坑是这些创始者没有遇到过。真的是需要有个个方面的能力才能去在生产环境中到它。**