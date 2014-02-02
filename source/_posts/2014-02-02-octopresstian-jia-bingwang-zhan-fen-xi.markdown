---
layout: post
title: "Octopress添加bing网站分析"
date: 2014-02-02 11:30:01 +0800
comments: true
categories: [Octopress, Bing]
---

###原由
Google提供了相应的Analytics网站分析工具，同样Bing也提供了类似的工具：[Bing网站管理员](http://www.bing.com/toolbox/webmaster/ "官网")，同样可以给你的Octopress很快速的添加该分析工具

###配置

1. 在Bing网站管理员注册，并填写必要的信息
2. 添加一个新网站，填写相应的信息
3. 根据认证所有权网页配置Octopress，具体如下
   
   - 下载你的BingSiteAuth.xml到octopres的source目录下
   - 配置_config_yml文件，设置bing analytics开关，在文件最后加入，如果以后不用bing analytics，则可以将开关设置为false就可以，不用到处改文件
       
         # Bing Analytics  
         bing_analytics: true
        
   - 修改octopres/source/_include目录下的head.html文件，在\<head\>下面增加以下内容，记得替换\<...\>bing给你的blog的标识
       
         {% raw %}
         
         {% if site.bing_analytics %}
         <meta name="msvalidate.01" content="<...>" />
         {% endif %}
         
         {% endraw %}
         
   - 然后重新生成并部署就可以了      
       
         >rake generate
         >rake deploy