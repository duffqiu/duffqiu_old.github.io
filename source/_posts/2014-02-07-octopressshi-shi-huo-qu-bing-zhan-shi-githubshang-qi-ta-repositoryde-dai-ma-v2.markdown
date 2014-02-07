---
layout: post
title: "Octopress实时获取并展示github上其它repository的代码v2"
date: 2014-02-07 17:02:16 +0800
comments: true
categories: Octopress
---

###原由
之前做了个Octopress实时获取并展示github上其它repository的代码的rake任务，参见[1]({% post_url 2014-02-05-octopressshi-shi-huo-qu-bing-zhan-shi-githubshang-qi-ta-repositoryde-dai-ma %})，但是每次都要打开github来获取文件的连接，还是比较烦。如果需要更改github branch的名字就需要重新更改整个链接。

###解决方案
github上的代码存放实际上是有规则的。格式如下“https://raw.github.com/\<your github account>/\<repository name>/\<branch name>/\<file folder>/\<java package name>/\<file name>”

根据这个规则，可以将https://raw.github.com/，\<your github account>固定在Rakefile的变量中  
将\<repository name>，\<branch name>，\<file folder>，\<java package name>，\<file name>配置到文件中  

这样的话后面如果需要更改分支名就不用先去查找github上的raw文件url了  
注意，如果用了[git flow](http://nvie.com/posts/a-successful-git-branching-model/)，则分支名可能是feature/\<branch name>


<div style="text-decoration:underline; font-size: 24px; color: red" onclick="showdiv('gitflow')"> Git flow Picture </div>  


<div style="display:none" class="prev" id="gitflow"  onclick="hidediv('gitflow')">
  <img src="http://nvie.com/img/2009/12/Screen-shot-2009-12-24-at-11.32.03.png" title="点击关闭">
</div>





###代码实现

首先增加变量配置到Rakefile中

    # github code fetch config
    github_code_url = "https://raw.github.com/"
    github_account  = "duffqiu"

为了保留原由的实现，则在Rakefile上增加一个新的任务，起名为github_fetch，对应使用的配置文件为githubfetch.cf，示例如下：  

	DesignPatternDemo develop src/main/java org.duffqiu.patterndemo       LazySingleton.java
	
	DesignPatternDemo develop src/test/java org.duffqiu.patterndemotest   LazySingletonTest.java
	
	DesignPatternDemo develop src/main/java org.duffqiu.patterndemo       SerialibleSingleton.java
	
	DesignPatternDemo develop src/test/java org.duffqiu.patterndemotest   SerialibleSingletonTest.java
	
	DesignPatternDemo develop src/main/java org.duffqiu.patterndemo       EnumSingleton.java
	
	DesignPatternDemo develop src/test/java org.duffqiu.patterndemotest   EnumSingletonTest.java

Rake中的任务代码为：

	desc "fetch source code file from github"
	task :github_fetch do
	
	  begin
	    rm_rf "source/downloads"
	    mkdir_p "source/downloads/code"
	    if File.exist?("github_fetch.cf")
	      IO.foreach("github_fetch.cf") do |line|
	        if line.strip.empty? == false
	            cd "source/downloads/code" do
	            attr = line.split(' ')
	            repo = attr[0].strip
	            if repo.empty?
	              abort("repo name is empty")
	            end
	            branch = attr[1].strip
	            if branch.empty?
	              abort("branch name is empty")
	            end
	            src_fold = attr[2].strip
	            if src_fold.empty?
	              abort("src fold is empty")
	            end
	            package = attr[3].strip
	            if package.empty?
	              abort("package name is empty")
	            end
	            file_name = attr[4].strip
	            if file_name.empty?
	              abort("file name is empty")
	            end
	            package_dir = package.gsub(/\./, '/')
	            target_dir = repo + '/' + package_dir
	            mkdir_p target_dir
	            cd target_dir do
	              system ("wget  " + "#{github_code_url}/#{github_account}/#{repo}/#{branch}/#{src_fold}/#{package_dir}/#{file_name}")
	            end
	          end
	        end
	      end
	    else
	      puts 'github_fetch.cf not found'
	    end
	  end
	end    

####Vi小提示

1. Copy单前行到文件结尾：“yG”
2. Delete单前行到文件结尾：“dG”
3. 回复上一次的操纵：“u”