---
layout: post
title: "sublime入门"
date: 2015-02-25 14:49:46 +0800
comments: true
categories: 
---

###原由
---
Sublime号称是神级编辑器，同时支持多种插件的扩展。并且支持多个平台。
这里主要讲以下如何安装和配置，并说明一下如何使用scala插件


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

- 安装SublimeSBT插件

    - `shift+Ctrl+P`来打开命令板选择“Package Control: Install Package”，然后选择SublimeSBT
    - 使用：shift+Ctrl+P`来打开命令板选择SBT: xxx来运行对应的sbt命令


####小提示
对于sublime text 3的最新版本在linus下无法使用ibus的输入法，所以编写这个blog的时候我又回到了gedit
从这个对比来看，linus和mac osx从个人用户的使用来看还是差很远的

