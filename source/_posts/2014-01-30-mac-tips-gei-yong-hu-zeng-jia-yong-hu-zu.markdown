---
layout: post
title: "Mac tips: 给用户增加用户组"
date: 2014-01-30 18:06:43 +0800
comments: true
categories: [MAC, OSX]
---

###查看用户所在用户组

       
    >groups
    
###查看当前有那些用户

    >uers
    
###给用户增加用户组
  通过用户管理创建的用户，如果赋予管理员权限，则用户在admin组，当时有时候需要将用户增加到wheel组，但是OSX没有像相关的命令，似乎apple就不想普通用户像linux那样操作OSX。不过对于技术人员，apple提供了个dscl(Directory Service command line utility)目录服务命令工具完成的，具体操作如下
  
1. 现进入dscl
       
       >dscl localhost
2. 进入组目录，使用ls 你就可以看到Mac所有的group（同理，在/Local/Default/Users可以到所有的用户）
       
       >cd /Local/Default/Groups
       >ls
3. 添加用户到用户组，\<groupname>为组名，\<username>为用户名
       
       >append <groupname> GroupMembership <username>
4. 将用户从用户组中移除
       
       >delete <groupname> GroupMembership <username>
5. 查看用户组信息，同理可以查看用户信息
       
       >read  <groupname>

     
