---
layout: post
title: "技术杂谈1"
date: 2014-02-09 18:15:58 +0800
comments: true
categories: Miscellany
---

### 原由
---

 
这个类别主要是想记录以下跑题中看到的一些“新”技术，或者是一些有意思的技术内容，但是不会详细展开，如有时间和必要会有专门的blog来描述某个内容或话题

### 今天的内容有
---

1. 云上的Jenkins，由[Cloudbees公司](http://www.cloudbees.com)提供，提供免费的有限的使用，但是需要用到的地方，其实都有免费的软件／服务，如Jenkins的程序，Github管理代码等，唯一的好处是不用自己来搭建、维护服务器。我找到它的原因是看到它的Jenkins能自动部署到Goolge App Engine上，而 Jenkins还没有提供类似的Plugin，或许直接用shell命令就能做到，后面有机会用到再展开这个。

2. 开源的类似Google App Engine的Paas的平台[AppScale](https://github.com/AppScale/appscale)，[youtube的AppScale介绍](http://www.youtube.com/user/AppScaleSystems)（不过需要代理才可以看，我前面由介绍如何搭建代理服务）。建议开发Google App Engine应用的开发者都自己搭建一套，[好处](http://www.appscale.com/features)是：

   - 便于直接本地测试、部署和运行
   - 需要部署私有云（特别是大型企业）
   - 需要使用其它Google App Engine不支持的APIs
   - Google App Engine全APIs兼容
   - 可用于生产环境的平台（Google App Engine不适合生产环境？）
   - 不再依赖于Google，可以部署在多个云平台如Amazon和虚拟机等
   
	 <div style="text-decoration:underline; font-size: 16px; color: red" onclick="showdiv('app')"> AppScale架构图 </div>
	
	<div style="display:none" class="prev" id="app"  onclick="hidediv('app')">
	
	<img src="http://www.appscale.com/assets/img/appscale_stack.png" title="点击关闭">
	
	</div>
   
3. [docker](http://www.docker.io)，轻量级的应用容器，不过还没成熟和商业化，值得关注。我还没有升入去看，只能提一下而已






