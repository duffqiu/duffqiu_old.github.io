---
layout: post
title: "Octopress实时获取并展示github上其它repository的代码"
date: 2014-02-05 01:45:57 +0800
comments: true
categories: Octopress
---

###原由
在写blog的时候，想要直接关联github上的其它repository的代码，这样以后代码更新就不需要更新blog了。但是看octopress的文档，似乎[gist](https://gist.github.com)可以达到该目的，但是它不是一个代码工程，无法像正常的代码工程那样去管理，编译，运行等。  
所以就想看看能否给Octopress加个plugin直接展示该代码

###办法
原本想改一下plugins/include_code.rb，但是感觉还是太复杂，所以想个简单的方式
还是利用include_code这个插件，这个插件是可以将在source/downloads/code/目录下的文件展示出来的，这个目录原octopress代码没有生成，需要手工生成。  
思路就是想法将github上的代码在rake generate之前先用wget获取下来放到上面这个指定的目录中  
因为include_code.rb的复杂度，我选择了更改Rakefile文件，新增一个任务的方式来解决
具体步骤如下：

1. 在octopress目录下新增一个需要下载的文件的配置githubcode.cf，格式为<github repository name>空格<java package name>空格<java code file url>。java的包名可以保留'.'的格式，这个插件会自动转成目录

       DesignPatternDemo  org.duffqiu.patterndemo  https://raw.github.com/duffqiu/DesignPatternDemo/feature/LazySingletonDemo/src/org/duffqiu/patterndemo/LazySingleton.java

2. 确保已经安装好wget

       sudo port install wget

3. 更改Rakefile文件，在最后增加一个任务

		desc "get code file from github"
		task :github_code do
		
		  begin
		    require 'net/http'
		    require 'uri'
		    rm_rf "source/downloads"
		    mkdir_p "source/downloads/code"
		    if File.exist?("githubcode.cf")
		      IO.foreach("githubcode.cf") do |line|
		        if line.strip.empty? == false
		            cd "source/downloads/code" do
		            attr = line.split(' ')
		            repo = attr[0].strip
		            if repo.empty?
		              abort("repo name is empty")
		            end
		            package = attr[1].strip
		            file_url = attr[2].strip
		            if file_url.empty?
		              abort("file url is empty")
		            end
		            package_dir = package.gsub(/\./, '/')
		            target_dir = repo + '/' + package_dir
		            mkdir_p target_dir
		            cd target_dir do
		              system ("wget  " + file_url)
		            end
		          end
		        end
		      end
		    else
		      puts 'githubcode.cf not found'
		    end
		  end
		end        		
		
4. 在需要引用代码的地方使用以下方式
       
       {% raw %}
       
       {% include_code lang:java <repository name>/<package name>/<java file name> %}
       
       {% endraw %}
       
    注意：<repository name>前面没有'/'，<pakcage name>要将java的包名的'.'改为'/'

5. 每次有新增的github代码文件引入，则需要在rake generate前先调用
       
       rake github_code

###小插曲
####wget https访问问题

使用wget访问github的时候，因为github使用https，则会出现说github的“证书不可信”和“证书颁发者未知”的问题。
解决方式为

1. 使用--no-check-certificate参数访问github，但是还是用warning，不够完美
2. 决解证书问题  
   
   - 安装curl-ca-bundle
   
          sudo port install curl-ca-bundle
          
   - 配置wget
          
          echo CA_CERTIFICATE=/opt/local/share/curl/curl-ca-bundle.crt >> ~/.wgetrc

然后再运行wget就不会有问题了
