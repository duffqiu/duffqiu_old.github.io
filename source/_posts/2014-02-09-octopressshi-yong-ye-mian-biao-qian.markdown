---
layout: post
title: "Octopress使用页面标签"
date: 2014-02-09 21:57:59 +0800
comments: true
categories: Octopress
---

###原由
---
当一篇blog太长的时候，总是希望有个按钮能回到某个章节上，或者是文件的开头

###解决办法
---
因为Markdown语法没有提供这个支持，只用用Div的方式来支持了，具体方式如下

- 定义标签

   {% codeblock lang:sh %} 
   <a name="标签名"></a>  
   {% endcodeblock %}

- 定义跳转链接

   {% codeblock lang:sh %}  
   <div style="text-decoration:underline; font-size: 16px; color: red" onclick="self.location.href='#标签名'">跳转</div>
   {% endcodeblock %}
