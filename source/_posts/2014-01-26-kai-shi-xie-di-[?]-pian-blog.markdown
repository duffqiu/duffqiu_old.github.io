---
layout: post
title: "开始写第一篇blog"
date: 2014-01-26 23:36:24 +0800
comments: true
categories: Octopress
---

####要简单，你必须有利器
要简单用好Octepress，首先要看看markdown语法（链接我前面有提到），基本很简单，半个小时就能看懂，剩下实操就可。

然后就是需要有个工具，下载了个[Mou][]，完全免费，到目前为止感觉很好用

[Mou]: http://moustand.com

####开始写第一篇blog
首先通过命令生成第一篇文章

     rake new_post\["<文章标题>"\] #<文章标题>替换成你的文章标题，可以是中文，最后生成的文件名自动转为拼音，这里用\是使用zsh shell自动生成的
     
然后通过mou打开生成好的文件用markdown语法进行编辑 
当你第一次打开文件的时候，你会发现这个文件有文件头     

{% img left /images/Snip20140126_1.png 400 1000 "Head of post" "headfile" %}          
  
  
注意几个内容：   

* 如果不需要comments，可以设置为false。后面有介绍如何配置comments 
* 为分类写分类名，后面会将如何配置分类的plugin。这个例子我用了Octopress做分类名 

然后就是按照markdown的方式写内容了。 
写完了以后，则需要运行以下命令进行部署 

    rake generate
    rake deploy
    
以后就是重复上面的步骤就可以。

####配置Octopress，更本地化

##### 用微博配置About me

通常在主页的侧边栏说明一下自己是谁，最简单的方式或许是直接连接自己的微博。具体步骤如下

1. 激活About me。
   
   Octopress已经准备了一个aboutme的侧边栏文件，在对应的Octopresss目录的/source/_includes/custom/asides子目录下的about.html文件
   编辑octopress的配置文件_config.yml，修改default_asides项，增加内容:
        
       custom/asides/about.html
2. 加入微博内容

    微博的插入代码可以到[微博的开放平台](http://app.weibo.com/tool/weiboshow?sudaref=open.weibo.com "微博秀")获取（不要copy搜索出来，很可能旧了）

    然后将代码放入到about.html文件
    如我的about.html文件内容

``` text    
        <section>
         <h1>About Me</h1>
         <iframe width="100%" height="550" class="share_self"  frameborder="0" scrolling="no" src="http://widget.weibo.com/weiboshow/index.php?language=&width=0&height=550&fansRow=2&ptype=1&speed=300&skin=1&isTitle=0&noborder=0&isWeibo=1&isFans=0&uid=1872168377&verifier=1cd3a528&dpc=1"></iframe>
        </section>
```  

3.  重修部署

        rake generate
        rake deploy

这个时候主页和每个页面都会出现你的微博了。如果只想出现在主页上，后面介绍如何配置post页的配置，配置完后就只有主页出现了。

##### 配置comment
octopress已经集成了[disqus](http://www.disqus.com disqus官网)系统，据说不是太好用，没有去试，只是感觉纯老外的东西，被中国用的很少，所以试着配置国内的[多说](http://dev.duoshuo.com 多说官网)

1. 用微博／百度账户开通多说，并配置一个应用指向你的blog，获得这个应用的short name
  
2. 在octopress的配置文件中增加以下内容 
  
        #duoshuo comments
        duoshuo_comments: true
        duoshuo_short_name: <你的多说的应用名>  
    	   
3. 修改source/_includes/article.html文件，在disqus配置后面 
      
   {% raw %}
   
       {% if site.disqus_short_name and page.comments != false and post.comments != false and site.disqus_show_comment_count == true %}
        | <a href="{% if index %}{{ root_url }}{{ post.url }}{% endif %}#disqus_thread"
        data-disqus-identifier="{% if post.meta.disqus_id %}{{ post.meta.disqus_id }}{% else %}{{ site.url }}{{ post.url }}
        {% endif %}">Comments</a> 
        {% endif %}
   
   {% endraw %}
                  
   增加多说的配置 
     
   {% raw %}

        {% if site.duoshuo_short_name and page.comments != false and post.comments != false and site.duoshuo_comments == true %}
        | <a href="{% if index %}{{ root_url }}{{ post.url }}{% endif %}#comments">Comments</a>
        {% endif %}

   {% endraw %}

         
4. 在source/_includes/post/目录里面增加一个duoshuo.html模块文件，然后从多说的应用对应的工具里面获取通用代码(不要用网上搜索到的代码)放入这个文件中 

5. 在source/_layouts/post.html 中的 disqus代码下
  
   {% raw %}  

        {% if site.disqus_short_name and page.comments == true %}
          <section>
            <h1>Comments</h1>
            <div id="disqus_thread" aria-live="polite">{% include post/disqus_thread.html %}</div>
          </section>
        {% endif %}    
            
   {% endraw %}
 
   增加多说模块
  
   {% raw %} 
 
        {% if site.duoshuo_short_name and site.duoshuo_comments == true and page.comments == true %}
          <section>
            <h1>Comments</h1>
            <div id="comments" aria-live="polite">{% include post/duoshuo.html %}</div>
          </section>
        {% endif %}        
   {% endraw %} 
   
6. 取消disqus
   
   编辑_config.yml文件，将配置项disqus_show_comment_count设置为false就可以
   
7. 然后就是重新部署
     
        rake generate
        rake deploy
        
**到目前为止，一个octopress在github上的配置就基本完成了**
Octopress还有不少功能，后面用到再补充了

#### 监控你的blog
最简单的方法是使用[google analytics](https://www.google.com/analytics/)，octopress已经集成了它，需要做的事情是

1. 先在google analytics上开设一个web的app，获得tracking_id
2. 然后修改_config.yml，找到google_analytics_tracking_id，填上你的tracking_id。
3. 重新部署就可以了

注意： _config.yml文件的配置要每个选项":"后面需要有一个空格
