---
layout: post
title: "Octopress动态展示图片"
date: 2014-02-07 20:39:21 +0800
comments: true
categories: Octopress
---

###原由
在Octopress中如果展示图片太大，这回占用屏幕很大的篇幅，最好的办法是先隐藏起来，需要的时候点击展开 

###解决办法
利用div的display的设置的方式实现

具体的实现方式是  


- 增加两个javascript的方法，一个展示图片，一个隐藏图片，简单的方式可以直接将他们放入到source/javascripts/octopress.js文件的最开头  
  
{% codeblock lang:js %}   

function showdiv(elemid){

var fd = document.getElementById(elemid);
fd.style.left = event.clientX;
fd.style.top = event.clientY;
fd.style.display = "inline";

}

function hidediv(elemid){

document.getElementById(elemid).style.display = "none";

}	
	  
{% endcodeblock %}
      
  

- 在blog中增加使用图片的div的代码
       
{% codeblock lang:sh %}
	
<div style="text-decoration:underline; font-size: 24px; color: red" onclick="showdiv('pic')"> Picture Show Text </div>  

<div style="display:none" class="prev" id="pic"  onclick="hidediv('pic')">

<img src="xxx.png" title="点击关闭">

</div>
	
{% endcodeblock %}
        

效果是点击图片文字展开图片，点击图片后隐藏。     


###小插曲
整个2014春节假期就要过去了，回顾一下，整整谢了24篇blog，有不少是跑题写出来。很多题目只是开了个头，也期待自己后面继续努力！   

在octopress的source/_post目录里用命令，实际就是将ls输出用wc工具再统计
    
    >ls |wc -l