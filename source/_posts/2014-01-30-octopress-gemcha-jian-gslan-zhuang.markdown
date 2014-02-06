---
layout: post
title: "Octopress gem插件gsl安装"
date: 2014-01-30 20:00:10 +0800
comments: true
categories: [Octopress, GSL]
---

###原由
Octopress用rake generate的时候会提示使用GSL将会加速文件的生成，速度是原来的的10+倍，但是需要安装[Ruby GSL](http://rb-gsl.rubyforge.org/)插件。

###安装
当使用命令安装Ruby GSL的时候确发现了问题 
    
    gem install gsl
    
原因是缺少了[GSL的C的开发库](http://www.gnu.org/software/gsl/ "GSL Library官网")    

然后在GSL Library官网下载了最新的GSL1.16，然后编译安装，然后再重新安装Ruby GSL，但是还是出现错误，后来搜索[stackoverflow](http://stackoverflow.com)找到一个说明，说是GSL和Ruby GSL配合的版本只能用1.14的。所以再次下载GSL1.14编译、安装然后再安装Ruby GSL就可以了

###使用
    
    rake notify
    
目前看除了rake generate没有那个提示，好像速度没有看出来，或许是文章的数目不多体现不出来 