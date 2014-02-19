---
layout: post
title: "Appscale安装"
date: 2014-02-11 21:49:00 +0800
comments: true
categories: Appscale
---

###原由
---
之前看了下Appscale的介绍，觉得还是不错的，这两天自己装一个玩玩。



###安装过程 
---

####准备工作
1. 下载并安装[virtual box](https://www.virtualbox.org)
2. 下载并安装[vagrant](https://github.com/mitchellh/vagrant)，不过好像中国下载有问题，这里有个baidu pan的copy，版本[vagrant 1.4.3](http://pan.baidu.com/s/1dDtaIul)

3. 下载并安装[appscale-tools](https://github.com/AppScale/appscale-tools)，帮助安装配置appscale的，可以参照[官方的安装说明](https://github.com/AppScale/appscale-tools/wiki/Installing-the-AppScale-Tools-on-Mac-OS-X)使用`brew`安装，
4. 下载[Appscale for mac的virtual box的image](http://download.appscale.com/apps/AppScale%201.13.0%20VirtualBox%20Image)，因为很大，直接下载会有问题，这里分享一个已经下好的在百度云盘的copy，版本为[AppScale 1.13.0](http://pan.baidu.com/s/1i3p4NsT) 


####安装VM过程 
1. 创建一个appscale的目录
2. 获取一个[Vagrant的配置文件](https://s3.amazonaws.com/appscale_CDN/files/Vagrantfile)，也可以通过命令`vagrant init`获得  
3. 打开Vagrantfile，将`config.vm.box_url`配置为你下载好的Appscale for mac的virtual box的image  
4. 将`config.vm.network`配置成你的网络地址，但是不要用host机器的IP
5. 启动vm
       
       vagrant up
       vagrant ssh     # now you're in the VM as the "vagrant" user
       sudo -s passwd  # change the root password
       
####部署AppScale

1. 初始化一个配置文件AppScalefile，需要执行`appscale init cluster`
2. 修改AppScalefile中的`ips_layout`，将IP都设置为你刚才配置的VM IP，然后启动Appscale，命令：`appscale up`      
   
       ips_layout:
         master : 192.168.33.10
         appengine: 192.168.33.10
         database: 192.168.33.10
         zookeeper: 192.168.33.10
         
####部署应用

用命令：`appscale deploy <app path>`         

####关闭
1. 关闭appscale，用命令`appscale down`
2. 关闭VM，用命令`vagrant halt`

####Appscale tools安装过程(官方介绍复制)

安装命令：

    brew install wget
	brew install ssh-copy-id
	brew install swig
	wget https://github.com/AppScale/appscale-tools/archive/1.13.0.tar.gz -O appscale-tools-1.13.0.tar.gz
	tar xvf appscale-tools-1.13.0.tar.gz
	
	sudo ./appscale-tools-1.13.0/osx/appscale_install.sh

将`/usr/local/share/python:/usr/local/appscale-tools/bin`放入到path中就可以了

小提示：
1. 官网说用euca-version检查安装是否成功，但是根本就没有这个命令。这个好像是给EC2和S3用的python工具，可以用`port search 'euca'`查到
2. Vagrant有对应的oh-my-zsh的plugins，配置进.zshrc文件中就可以了