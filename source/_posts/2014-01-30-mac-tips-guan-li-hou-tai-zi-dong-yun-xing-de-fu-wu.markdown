---
layout: post
title: "mac tips: 管理后台自动运行的服务"
date: 2014-01-30 13:29:43 +0800
comments: true
categories: OSX
---

###OSX系统后台服务简介
1. 系统级别的服务配置在 /Library/LaunchDaemons/ 目录里面。这里有个注意点，必须确保文件的拥有者是root用户，如果不是，则需要用chown命令改过来
       
       > sudo chmod root [文件名]

2. 用户级别的服务配置，即在用户登录后才会运行的服务，配置文件在 ~/Library/LaunchAgents/ 目录里面。

3. 如果需要自己定义后台服务，可以copy一个已有的文件，然后做一定的更改就可以。

4. 编辑工具，xcode是其默认的编辑工具，简要处理的方式是在shell中执行
        
        > open [文件名]
        
   然后系统会自动用xcode打开。  
   
   <div style="color:#F00">  注意：</div> 
   
   如果需要用xocode编辑首先要安装xcode（好像是废话）。如果是修改系统的服务，就必须将当前用户加入到wheel组，并更改 /Library/LaunchDaemons/目录权限为771，还要将被更改的plist文件的权限改为664，这样才可以用xcode处理文件。是不是很烦？所以还是直接用vi的方式好。编辑完后，还需要将目录权限更改过来
   
   - 添加当前用户到wheel组。 
   
   <div style="color:#F00">  注意：</div>
  
   OSX没有像Linux那样操作用户组的命令，似乎OSX给用户日常使用是不用管理用户组的。但是对于开发人员，则必须清楚。具体方式是通过dscl(Directory Service command line utility)目录服务命令工具完成的。后面再写个介绍这个。[Mac tips: 给用户添加用户组]({% post_url 2014-01-30-mac-tips-gei-yong-hu-zeng-jia-yong-hu-zu %})
   
   - 更改目录权限  
        
           sudo chmod 771 /Library/LaunchDaemons/
   
   - 更改文件权限
     
           sudo chmod 664 [plist 文件名]
           
   - 编辑完了记得再改回来
     
###服务的启动和停止
     
   