---
layout: post
title: "sublime入门以及搭建scala开发环境"
date: 2015-02-25 14:49:46 +0800
comments: true
categories: [Sublime, Scala]
---

###原由
---
Sublime号称是神级编辑器，同时支持多种插件的扩展。并且支持多个平台。
这里主要讲以下如何安装和配置，并说明一下如何使用scala插件来搭建开发环境，
避免使用其他的IDE这么重的工具，节约内存和系统消耗


###sublime使用入门
---

- 安装sublime
可以直接从[sublime官网](http://www.sublimetext.com/)下载对应的版本安装
这里针对不同的系统的配置的文件的路径如下：

   - Mac OSX: `~/Library/Library/Application Support/Sublime Text 3`
   - Linux CentOS7: `~/.config/sublime-text-3`

Mac OSX有对应的dmg安装包，一部一部安装就可以
CentOS7没有安装包，需要下载压缩包，4然后解压到`/opt/sublime-text`目录(注意要把名字中的3去掉)，然后将目录中的sublime_text.desktop复制到`/usr/share/applications`，这样在CentOS的启动菜单上就对应的图表了

- 安装包管理插件Package Control

    - 最先要安装的是插件[管理工具Package Control](https://packagecontrol.io/installation): 下载插件包，然后放到配置路径的Installed Packages目录下就可以了。
    - 每次通过`shift+Ctrl+P`来打开命令板选择“Package Control: Install Package”来安装插件

- 安装scala插件SublimeREPL

    - `shift+Ctrl+P`来打开命令板选择“Package Control: Install Package”，然后选择SublimeREPL
    - 安装成功后就可以在命令板中选择对应的scala/sbt来打开scala REPL和sbt REPL了
    - 配置： 有可能你安装sbt/scala的目录不同，造成无法打开scala REPL和sbt REPL，这个时候需要手工配置一下。在配置的文件的路径下找到`Packages/SublimeREPL/config/Scala`目录下的Main.sublime-menu文件，编辑该文件，为对应的scala和sbt指定好对应的目录就可以了
    - 使用：对于SublimeREPL:scala只是打开了scala REPL，用处不大，可以使用下面的scala worksheet插件代替。但是对于SublimeREPL:sbt则比较有用。方式是通过sublime先打开sbt的工程的目录，然后在命令板执行SublimeREPL:SBT for opened folder。不过还有更方便的插件SbblimeSBT

- 安装scala worksheet插件

    - `shift+Ctrl+P`来打开命令板选择“Package Control: Install Package”，然后选择Scala Worksheet
    - 安装成功后就可以在命令板中选择对应的show scala worksheet了。
    - 使用方式是编写对应的scala文件并保存后，再执行show scala worksheet，这样scala REPL就自动会执行你编辑的文件了。
    - 有可能在打开show scala worksheet出现无法找到scala的无法，简单的解决方式是在1`/usr/bin`下给scala建立一个soft link `sudo ln <scala install path> /usr/bin/scala`
    - 如果需要给Scala worksheet增加额外的jar包，则需要配置Sublime中的setting。`preferences->Settings - User`，然后增加一个JSON key: (注意不要用相对路径，要用绝对路径)

```
	"scala_worksheet_classpath":
	[
		"<jar path>/<jar name>"
	]
```

- 安装SublimeSBT插件

    - `shift+Ctrl+P`来打开命令板选择“Package Control: Install Package”，然后选择SublimeSBT
    - 使用：shift+Ctrl+P`来打开命令板选择SBT: xxx来运行对应的sbt命令

- 给对应的scala sbt项目配置SBT插件：sbt-sublime 

    - 在project/plugin.sbt中增加`addSbtPlugin("com.orrsella" % "sbt-sublime" % "1.0.9")`
    - 在sbt console中调用`gen-sublime`来获取依赖包的源文件以及生成sublime的工程文件（每次SBT Clean都会自动删除这些文件。）。这样也节约了需要在每一个依赖包的定义后面增加withSource的选项
    - 使用步骤1： 在sublime中， project->open project来打开生成sublime的工程文件，这个时候将会将SBT的工程目录加载进来，同时多了一个External Libraries的目录（其存放了依赖包的源代码）
    - 使用步骤2： 使用SublimeSBT插件调用SBT: Start continus compiling，这样每次更改源文件都会被自动编译。如果发现错误，则Sublime会自动提示在那一行代码上
    - 使用步骤3： 因为引入了依赖包的源代码，则可以通过在sublime中的Goto菜单来调转到指定的类/方法的源文件处(仅Sublime Text 3支持)
    - 使用步骤4： 使用Snippet：case class...等来协助快速编写代码（但是不知道是上面哪个插件体提供的了）

- 使用Git插件，在“Package Control: Install Package”中选择Git就可以了，然后在命令板选择对应的Git:xxx命令就可如Git status。

Sublime搭建Scala开发环境小结：
    - 总体而言还是不错，运行速度又快，基本的功能都有
    - 但是唯一的遗憾是没有办法在编写代码的时候自动提示可能的方法/类等

####小提示
对于sublime text 3的最新版本在linus下无法使用ibus的输入法，所以编写这个blog的时候我又回到了gedit
从这个对比来看，linus和mac osx从个人用户的使用来看还是差很远的

