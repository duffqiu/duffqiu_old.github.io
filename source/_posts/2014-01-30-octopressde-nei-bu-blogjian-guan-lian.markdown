---
layout: post
title: "Octopress的内部blog间关联"
date: 2014-01-30 18:28:34 +0800
comments: true
categories: Octopress
---

### 首先鸣谢参考资料的作者

参考资料来自与 [1](http://blog.eavatar.com/post/2013/06/use-post-url-internal-link-octopress/)

### 完善Octopress的配置

1. 获取内部关联的[post url plugin](https://raw.github.com/michael-groble/jekyll/fix_post_url/lib/jekyll/tags/post_url.rb)，然后将其放入到plugin目录中    

2. 确保已经安装了kramdown，如果没有，用以下命令安装
       
       >sudo gem install kramdown
3. 编辑配置文件_configy.yml，注释掉markdown: rdiscount，然后增加
       
       #markdown: rdiscount
       markdown: kramdown
       
### 在blog中使用关联，语法如下
    
    {% raw %}
    
    [<链接名字>]({% post_url <blog的文件名字，不要带目录和文件后缀> %}) 
    
    {% endraw %}
    
然后重新生成和部署就可以了
    
    >rake generate
    >rake deploy
    
    
### 一个小插曲 
今天在生成地8个blog后，rake generate就出错了，报在matrix.rb文件中反馈出"Not Regular Matrix"错误，但是就是死活找不到问题所在，只要将刚生成的文件删除就好了，而文件还没写东西。后来经历反复的从头再来，终于发现了问题所在，结论是之前参照网上配置关联blog的配置，在_configy.yml中增加了以下配置：

    lsi: true    
    
只要将这个选项关闭或删除就可，好像octopress已经没有用了


### Octopress经验总结

尽管使用上就是用markdown的方式写文章，但是使用octopress这个平台，就像使用开发工具一样，总会出现这样那样的错误，而且还很难定位，最好的办法就是像编程一样，今早“编译”－》“试运行”－》“改错”－》“再运行”，不然当你写了很多后一次行生成出现错误就无从下手去解决问题了，只能一个一个做减法然后再试。

最好的方式是

  1. 写一部分，就立马rake generate  
  2. rake new_post["<文章名>"]，不写任何东西，立马rake generate
  3. 只要用到{% raw %} {%  %}{% endraw %}这些内嵌语法，立马rake generate
  4. 更改一项_config.yml配置，立马rake generate