---
layout: post
title: "在其它机器上继续编写blog或者是新建编辑环境处理已有的Octopress系统"
date: 2015-01-13 12:14:22 +0800
comments: true
categories: Octopress
---

###原由
---
当你有多台机器或者是需要重新从Github上获取最后的blog，然后继续编辑，则需要重新手动配置Octopress
但是Octopress没有相关的教程说明如何做。

###解决办法
---

参考网上的多个资料以及个人摸索后，具体实践如下：

- 重新获取源文件

``` sh
git clone <your octopress url in Github>
```
- 切换到"source" branch

``` sh
git checkout source
```
- 重建_deploy目录

``` sh
mkdir _deploy
cd _deploy
git init
git remote add origin <your octopress url in Github>
git pull origin master
cd ..
```
- 在新的机器上设置octopress需要的环境配置rbenv

- 然后就可以在根目录下继续用rake命令了。

- 注意：不用去重新运行`rake install`，不然会将你客户化的东西冲掉(最好先从Github上备份一个最新的才开始在新的机器安装环境)

###plugin问题

如果遇到pygments_code的问题，则需要去看看你的系统安装的python的版本问题。如果python指向了python3.x则需要改成python2.x

####Git小提示
这次重新搭建中途换了Theme，谁知道新的theme会覆盖和删除某些改动，所以对于theme的选择最好在建立Octopress之初
换了theme之后我又做了几次提交，为了还原到原来没有换theme的时候，需要将多次的commit去掉。
在SCM同学的支持下，做了还原，主要操作为

先checkout到最后需要的某个commit上
然后强制提交这个commit

``` sh
git reset --hard <commit id's firt 5 characters>
git push -f
```


