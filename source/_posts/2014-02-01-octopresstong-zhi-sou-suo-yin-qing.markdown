---
layout: post
title: "Octopress通知搜索引擎"
date: 2014-02-01 13:21:02 +0800
comments: true
categories: Octopress
---

###目的
每次更新blog后，总希望搜索引擎可以尽快收录到，最好是多个搜索引擎都能收入到  
怎么做到呢？

###配置搜索引擎rake任务
首先感谢原作者的介绍[1](http://blog.eavatar.com/post/2013/06/octopress-ping-search-engines/)


1. 编辑octopress目录的Rakefile文件，在最后增加搜索引擎任务
       
       ＃Google search engine
       desc 'Notify Google of the new sitemap'
		task :sitemapgoogle do
		 begin
		   require 'net/http'
		   require 'uri'
		   puts '* Pinging Google about our sitemap'
		   Net::HTTP.get('www.google.com', '/webmasters/tools/ping?sitemap=' + URI.escape('http://duffqiu.github.io/sitemap.xml'))
		 rescue LoadError
		   puts '! Could not ping Google about our sitemap, because Net::HTTP or URI could not be found.'
		 end
		end

       #Baidu search engine       
       desc 'Ping Baidu'
		task :pingbaidu do
		  begin
		    require 'xmlrpc/client'
		    puts '* Pinging Baidu search engine'
		    XMLRPC::Client.new('ping.baidu.com', '/ping/RPC2').call('weblogUpdates.extendedPing', 'duffqiu.github.io' , 'http://duffqiu.github.io', 'http://duffqiu.github.io', 'http://duffqiu.github.io/rss.xml')
		  rescue LoadError
		    puts '! Could not ping Baidu, because XMLRPC::Client could not be found.'
		  end
		end
		
	   #Bing search engine
	   desc 'Notify Bing of the new sitemap'
		task :sitemapbing do
		  begin
		    require 'net/http'
		    require 'uri'
		    puts '* Pinging Bing about our sitemap'
		    Net::HTTP.get('www.bing.com', '/webmaster/ping.aspx?siteMap=' + URI.escape('http://duffqiu.github.io/sitemap.xml'))
		  rescue LoadError
		    puts '! Could not ping Bing about our sitemap, because Net::HTTP or URI could not be found.'
		  end
		end	
		
   将"duffqiu.github.io"换成你的blog域名字即可
   
2. 增加一个批量任务同时执行三个通知
       
       desc "Notify various services about new content"
	   task :notify => [:sitemapgoogle, :sitemapbing, :pingbaidu] do
       end

3. 每次部署后执行命令
       
       rake notify
       
###vi小技巧
今天有用到一个vi的命令，跳转到文件最后用":$"        

###小插曲
今天发现用Mac OSX10.9可以直接投射屏幕到小米盒子(v1.3.20)了
