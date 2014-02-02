---
layout: post
title: "加速Octopress的装载"
date: 2014-02-01 14:23:15 +0800
comments: true
categories: [Octopress, Jekyll]
---

###原由 
加速网页的存取有多种方式，其中之一就是减少一个页面的http的请求数量，因为浏览器同时发送http请求的数量是有限的，而对于很多页面而言，通常定义了很多css和js文件的链接，这样就会造成浏览器需要多次请求后才能完整的展示页面，所以最好的方式将多个css和js文件合并，如果需要还可以将合并后的文件压缩再传输，从而达到加速的目的。

###Gem插件安装
Octopress的[jekyll-asset-pipeline](https://github.com/matthodan/jekyll-asset-pipeline)就是一个这样机制的开源插件  
简单的可以通过以下命令安装

    >sudo gem install jekyll-asset-pipeline
    
**注意：** 有可能安装完成后，需要更新bundle，这行命令

    >sudo bunlde update  
    
压缩需要用到插件YUI Compressor，可以简单通过以下命令安装

    >sudo gem install yui-compressor
    
另外一个选择是使用Google的[Closure](https://developers.google.com/closure/?hl=zh-cn)，可以通过简单通过以下命令安装

    >sudo gem install Closure          
    
###配置Octopress
1. 配置octopress目录下的Gemfile文件，在:development中，增加以下两行
       
       gem "jekyll-asset-pipeline"
       gem "yui-compressor
       
2. 在 Octopress的plugins目录中，创建一个名为“jekyll_asset_pipeline.rb”的文件，内容如下：可以用注释那行代替上面那行就是使用Google的closure来压缩js文件
       
        require 'jekyll_asset_pipeline'

		module JekyllAssetPipeline
		
		  class CssCompressor < JekyllAssetPipeline::Compressor
		    require 'yui/compressor'
		
		    def self.filetype
		      '.css'
		    end
		
		    def compress
		      return YUI::CssCompressor.new.compress(@content)
		    end
		  end
		
		  class JavaScriptCompressor < JekyllAssetPipeline::Compressor
		    require 'yui/compressor'
		    #require 'closure-compiler'
		
		    def self.filetype
		      '.js'
		    end
		
		    def compress
		      return YUI::JavaScriptCompressor.new(munge: true).compress(@content)
		      #return Closure::Compiler.new.compile(@content)
		    end
		  end
		
		end

3. 修改Octopress的配置文件_config.yml，在文件最后增加以下配置
       
       asset_pipeline:
		  bundle: true            # Default = true
		  compress: true          # Default = true
		  #output_path: assets     # Default = assets
		  #display_path: nil       # Default = nil
		  gzip: false             # Default = false
		  
4. 修改相应的html文件，激活js/css文件的合并压缩。不同的Octopress的template，对应的文件或有不同，我这里用的是Octopress默认的官方template，因该适合大多数人
       
   - 修改source/_include目录下的head.html文件，注释掉带有js/css的几行
       
         {% raw %}
         
          <!-- <link href="{{ root_url }}/stylesheets/screen.css" media="screen, projection" rel="stylesheet" type="text/css"> -->
		 
		  <!-- <script src="{{ root_url }}/javascripts/modernizr-2.0.js"></script> -->
		  <!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script> -->
		  <!-- <script>!window.jQuery && document.write(unescape('%3Cscript src="./javascripts/libs/jquery.min.js"%3E%3C/script%3E'))</script> -->
		  <!-- <script src="{{ root_url }}/javascripts/octopress.js" type="text/javascript"></script> -->
		  
	     {% endraw %}
		  
   - 增加以下内容
       
         {% raw %}
       
         {% css_asset_tag global %}
		  - /stylesheets/screen.css
		 {% endcss_asset_tag %}
		
		 {% javascript_asset_tag global %}
		 - /javascripts/octopress.js
		 - /javascripts/modernizr-2.0.js
		 - /javascripts/libs/jquery.min.js
		 - /javascripts/libs/swfobject-dynamic.js
		 - /javascripts/libs/jXHR.js
		 {% endjavascript_asset_tag %}
       
         {% endraw %}
         
    - 同理修改source/_includes/custom目录下的head.html文件，注释掉里面的内容，增加上面的内容就可   

5. 到底有多css和js文件需要加入到需要增加的内容中呢？在octopress/source目录中搜索一下
       
       find . -name '*.css'
       find . -name '*.js'
   
   然后将必要的文件加入到上面的文件列表中

###Vi小提示

1. 跳到文件头用 ":0"
2. 跳到行头在非编辑状态下直接收入"0"
3. 跳到行尾在非编辑状态下直接收入"$"      


  
  