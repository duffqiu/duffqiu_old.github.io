---
layout: post
title: "Mac上用google-app-engine搭建proxy"
date: 2014-01-28 11:20:35 +0800
comments: true
categories: Proxy
---

####原由
在家访问Google时总时不时就无法访问，而技术类问题的搜索在百度的结果总是与期望相差太远，要不就是国外的网站无法访问，所以最好的就是使用国外的代理，作为技术人员最好的办法就是自己搭建一个。

####平台选择
目前有多中选择，但是看了下攻略，最简单的方式就是用google app engine。而且有开源的proxy服务器软件Goagent以及有Mac上好用的GoagentX客户端，那么就它了。

####安装Goagent
访问[Goagent](https://code.google.com/p/goagent/ "官网")  
但是在没有代理的时候，访问code.google.com总是很慢，这里附上一个[Goagent 3.1.5](/assets/goagent-goagent-v3.1.5-12-gd6029f0.zip)  
具体教程可以参考Goagent的官网
在Mac上的简单操作就是

1. 到[Google App Engine](https://appengine.google.com/)创建一个App
2. 解开Goagent下载下来的软件，到server/gae目录，编写gae.py文件配置密码（避免有人偷用你的流量）
3. 使用命令上传goagent并部署
   
        python upload.zip
   然后按照提示操作就可（就是输入两次google account和密码）
4. 注意：如果提示等待x秒后尝试看看部署是否成功，等待一会就好，如果不行取消当前操作(ctrol+c)重复之前的操作直到成功为止。估计又是Great Wall的问题

####安装GoagentX
访问[GoagentX](http://goagentx.com)，获得一个App，打开就可
然后就是简单配置GoagentX

1. 设置AppId
2. 设置服务密码，就是在gae.py上设置的密码
3. 然后启动就可
4. 启动成功后，Mac会被自动设置了代理
5. 接着就能够访问了(如果你能看到youtube／facebook就证明OK了)
6. 如需在局域网共享，则将启动Goagent的地方勾上局域网选项，然后在代理设置里面也勾上代理选项（记得重启代理）
7. 在其它机器上配置代理服务器就可以了

####免费增加流量
Google App Engine目前是提供28个小时的前端应用免费流量，如果需要增加则再多创建一个或多个这样的应用就可。  

Google App Engine允许一个账户最多创建10个应用