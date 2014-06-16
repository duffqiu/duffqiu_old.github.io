---
layout: post
title: "如何定义一个好的REST接口2"
date: 2014-03-13 22:06:13 +0800
comments: true
categories: [REST]
---

###原由
---
经过一段实践的实践，特别是需求总在变化的项目（项目开始阶段总是比较痛苦的）中，如何去管理REST接口的描述文件变得非常重要。即要美观，又要开发人员随时能看，能更新。之前写过第一篇的这个[“如何定义一个好的REST接口”]({% post_url 2014-03-03-ru-he-ding-yi-[?]-ge-hao-de-restjie-kou %})，但是如果写到word文档中，痛苦是难以仍受的。特别是需要比较大篇幅的改动。同时开发人员也很难知道具体那些被改了，就会陷入迷茫中。


###解决办法
---

####前提条件
1. 前提条件1，将设计文档放在离开发最近的地方。那么当然是代码工程的地方是最进的。
2. 前提条件2，有利于跟踪变化。对于开发人员而言，当然是像代码那样跟踪最顺手了。（目前我用的是Git）
3. 尽可能足够简单

####解决方式
1. 参照[JSON SCHEMA](http://json-schema.org)的方式
2. 简化JSON SCHEMA，但依然是用JSON的方式字描述接口
3. 安装Eclipse的JSON editor plugin，可以在outline上很清晰的看到JSON的结构。当然如果是windows下也可以用notepad++的jason plugin看。不过如何是代码就在eclispe上，最好还是用eclipse的plugin
4. 定义设计规则：

   - 接口文件的目录结构： `<ComponentName>\<ResourceName>\Method_<create/delete/query/update>.json`. ComponentName：组件或模块名称，ResourceName：资源名称，最后是方法的接口文件名。举个例子：有个组件Calendar，它下面有个资源叫Event，那么操作这个资源的更新的接口文件为Method_update.json，它放在了Calendar\Event\目录下。
   - 接口文件的层次结构。因为是基于HTTP的方式，则整个REST接口的层次结构为： 
   

![rest structure1](http://duffqiu.github.io/images/rest_structure1.png "REST API First Level Structure"")
   
   
   其中，Revision Information用来描述文档的要点变化情况  
   
```
{
   "Revision Information" : [
   {
      "Revision": "v0.1",
      "Section":"Whole document",
      "Change":"initialized"
   },
   {
      ...
   }
   ]
}
```
   
   description用来描述接口的说明
   
```
{
   "description":{
    "method_description":"Create xxxxx",
    "method_direction":"xxx->yyy"
   }
}
```
   
   request描述接口的http的query parameter和request body
   
   response描述接口的http的response body
   
   error_list描述接口的可能的错误信息
   
```
{
   "error_list":[
   {
      "errorCode":"91101",
      "errorMsg":"xxxx"
    },
    {
      "errorCode":"91102",
      "errorMsg":"yyyyy"
   }]
}
```
   
   - request的层次结构

![rest structure2](http://duffqiu.github.io/images/rest_structure2.png "REST API Request Structure")
   
```
{
   "request":{
  	"http_method" : "GET",
    "http_request_url":"https://www.xxxx.com/<warContext>/p/<component>/v1/<resouce>/{<resourceId>}",
    "path_parameter":{
      "<resourceId>":"resourceId"
    },
    "required_query_parameter":{
      "<parameter1>" : "xxx"
    },
    "optional_query_parameter":{
      "<parameter2>":"yyy"
    },
    "request_body":{
    ...
    }
   }
}
```
   http_method指明http调用的方法：GET/DELETE/PUT/POST  
   http_request_url指明http调用的URL的格式  
   path_parameter指明唯一索引到某个特定资源的标识  
   required_query_parameter必须传入的参数
   optional_query_parameter可选传入的参数
   request_body传入的JSON复杂对象

   - response的层次结构
   
   就是返回一个JSON的对象。这里有个BEST PRACTISE的建议，尽量返回能索引到特定资源的标识信息无论输入是否有该信息

```
{
   "response":{
    "<parameter1>":{
      "string":"<parameter1 description>",
      "required":"y"
    },
    "<parameter2>":{
      "integer":"<parameter2 description>",
      "required":"n"
    }        
   }
}
```
   
   对于request body和response body中JSON对象的定义，不使用JSON Schema那种繁复的写法，直接参照google的方式，将parameter的类型定义为key，描述为value，然后再用"required"标识是否是必须的

####完整的例子模版

```
{  
  "Revision Information" : [
    {
      "Revision": "v0.1",
      "Section":"Whole document",
      "Change":"initialized"
    },
    {
      "Revision": "v0.2",
      "Section":"xx",
      "Change":"yyy"
    }
  ],
  "description":{
    "method_description":"query xxxxx",
    "method_direction":"xx->yyy"
  },
  "request":{
  	"http_method" : "GET",
    "http_request_url":"https://www.xxxx.com/<warContext>/p/<component>/v1/<resouce>/{<resourceId>}",
    "path_parameter":{
      "<resourceId>":"resourceId"
    },
    "required_query_parameter":{
      "<parameter1>" : "xxx"
    },
    "optional_query_parameter":{
      "<parameter2>":"yyy"
    },
    "request_body":{
    }
  },
  "response":{
    "<parameter1>":{
      "string":"<parameter1 description>",
      "required":"y"
    },
    "<parameter2>":{
      "integer":"<parameter2 description>",
      "required":"n"
    }        
  },
  "error_list":[
    {
      "errorCode":"xx1",
      "errorMsg":"yyyy"
    },
    {
      "errorCode":"xx2",
      "errorMsg":"yyyy"
    }
  ]
}
```