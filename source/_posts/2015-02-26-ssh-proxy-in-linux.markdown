---
layout: post
title: "ssh在linux下使用http/https代理"
date: 2015-02-26 18:00:30 +0800
comments: true
categories: [Proxy, Linux]
---

###原由
---
在公司有代理的环境下，如果在linux下使用github，则可以使用https的方式。
但是https的方式需要每次都输入用户名和密码（用户名可以写在url上避免输入，但是密码就必须要）
但是如果能使用ssh的rsa方式则可以避免这个麻烦，但是ssh默认是不支持使用http/https代理的。
那么有没有办法通过某种手段让ssh支持代理呢？答案是可定的，这个工具是[Corkscrew官网](http://www.agroman.net/corkscrew/)

###解决办法
---

通过Corkscrew来建立隧道的方式来为ssh提供代理，具体方法如下 

- 安装Corkscrew（主要下载源代码，然后配置、编译、安装，典型的C程序的方式）
- 配置ssh，在~/.ssh/目录下生成一个config=文将，然后增加一行：`ProxyCommand /usr/local/bin/corkscrew <proxy http url> <proxy port> %h %p`
- 根据github的说明配置ssh的public和private key,参考[git帮助](https://help.github.com/articles/generating-ssh-keys/#platform-linux)


