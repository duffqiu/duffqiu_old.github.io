---
layout: post
title: "Octopress中使用plantuml"
date: 2014-02-12 22:36:09 +0800
comments: true
categories: [Octopress, PlantUML]
---

###原由
---
一直想便利的在Octopress使用UML，之前没有太好的办法，只能在astah画好后存为图片文件，然后再用链接的方式使用



###解决办法
---
现在经Leo同学推荐，使用文本的UML语言的方式可以写出UML图，他就是[Plant UML](http://plantuml.sourceforge.net)，同时在github上有个octopress的插件可以直接支持在Markdown中使用Plant UML，它是[jekyll-plantuml](https://github.com/yjpark/jekyll-plantuml)

####安装办法
1. 在octopress上建一个`_lib`的目录
2. 下载[plantuml.jar](http://plantuml.sourceforge.net/download.html)到上面建的`_lib`的目录
3. 下载[plantuml.rb](https://github.com/yjpark/jekyll-plantuml/raw/master/plantuml.rb)插件到`plugin`目录
4. 修改`_config.yml`文件，增加platuml的配置如下

       # PlantUML
       plantuml_jar: _lib/plantuml.jar
       plantuml_background_color: "#f8f8f8"

5. 然后用

``` sh
{% plantuml %}
...
{% endplantuml %}
```

括上platuml的代码就可以了，下面是一个简单的示例

源码如下：

``` sh  
{% plantuml %}  
Alice -> Bob: Authentication Request  
Bob --> Alice: Authentication Response
Alice -> Bob: Another authentication Request  
Alice <-- Bob: another authentication Response
{% endplantuml %}
```

图片展示如下： 

{% plantuml %}  
Alice -> Bob: Authentication Request  
Bob --> Alice: Authentication Response

Alice -> Bob: Another authentication Request  
Alice <-- Bob: another authentication Response
{% endplantuml %}

类的表示方式如下：

{% plantuml %} 
scale 900 width
Class01 <|-- Class02
Class03 *-- Class04
Class05 o-- Class06
Class07 .. Class08
Class09 -- Class10
Class11 <|.. Class12
Class13 --> Class14
Class15 ..> Class16
Class17 ..|> Class18
Class19 <--* Class20
{% endplantuml %}

###小插曲
默认情况下，plantuml只能支持sequence图的生成，如果要生成其它图，则需要用到Graphiz。  
在Mac上用`port`命令安装Graphiz，安装完成后`dot`命令是安装到了`/opt/local/bin/dot`目录下，而plantuml默认是要用`/usr/bin/dot`，这样plantuml.rb的plugin还是无法生成其它类型的图，但是platuml.jar是支持指定dot的路径的，所以手工改造一下plantuml.rb，具体方式如下：

1. 在`_config.yml`文件中增加一个plantuml的配置项并设定dot的路径：`lantuml_dotpath: /opt/local/bin/dot`

2. 在plantuml.rb文件中的`filename = Digest::MD5.hexdigest(code) + ".png"`语句上面增加dot的配置的读取

``` ruby
      dotpath = site.config['plantuml_dotpath']
      puts "using dot at: " + dotpath + "\n"
      if File.exist?(dotpath)
        puts "PlantUML set dot path:" + dotpath + "\n"
        dotcmd = " -graphvizdot " + dotpath
      else
        dotcmd = ""
      end
```

然后修改`cmd = "java -jar " + plantuml_jar + " -pipe > " + filepath`在最后增加` + dotcmd`，完整如下 

``` ruby
cmd = "java -jar " + plantuml_jar + " -pipe > " + filepath + dotcmd
```

因为之前发生过错误，所以需要手工清除`octopress/public/images/plantuml/`这个目录

这个我的第一个github pull request，纪念一下