---
layout: post
title: "如何定义一个好的REST接口"
date: 2014-03-03 21:25:32 +0800
comments: true
categories: REST
---

###原由
---
现在[基于JSON的REST接口](http://www.json.org)使用的是越来越多了，但是在自己去定义JSON的REST API的时候，我们确难免遇到不少疑惑，以下我们来谈谈这些疑惑 


###RCP-BASED or REST-BASED
---

在接口定义上，我们以往一般都是去定义请求什么，然后响应是什么，这就是典型的RPC(Remote Procedure Call)接口的定义方式。一般可以使用IDL（Interface Description Language，IDL）的方式来描述，如典型的WSDL等。RPC我的理解是，更多的是描述响应端提供什么样的行为给请求端调用。响应端会提供一个服务接入端点(End Point)，依据请求来返回响应。

对于REST-BASED的接口定义，通常针对的是响应端提供什么样的资源给客户端使用。这里有一个很重要的特点是，所有的行为都是围绕“资源”来进行的。主要有：

- GET  获取资源(可以是一个或多个)
- PUT  更新资源，PUT方法有个很重要的特性就是多次调用，结果是一直的。
- POST 创建资源，这个方法不是幂等
- DELETE 删除资源 

对于每个操作的返回都是基于同一个资源描述，只是不同的方法，资料描述里的某些内容被省略了。


其实对于使用JSON在RPC-Based上还是REST-Based都可以。接口定义成RPC-Based上还是REST-Based没有绝对的好坏，而是根据实际的使用场景。简单来说就是如果可以能被描述成“资源”(如：用户账户、购物车等等)，则用REST-Based更合适，如果是需要执行某些动作(如：发送邮件等)则RPC-Based更合适。

###REST-BASED BEST PRACTICE

如果使用REST-Based，建议可以参考[Google Canlendar的API定义](https://developers.google.com/google-apps/calendar/v3/reference/)。主要的要点如下：

1. 确定什么是资源(如calendar, event...)以及资源的表述

例如：

```
{
  "id": string,
  "summary": string,
  "description": string,
  "location": string,
  "timeZone": string,
  "summaryOverride": string,
  "colorId": string,
  "backgroundColor": string,
  "foregroundColor": string,
  "hidden": boolean,
  "selected": boolean,
  "accessRole": string,
  "defaultReminders": [
    {
      "method": string,
      "minutes": integer
    }
  ],
  "primary": boolean
}
```

2. 列举每个资源能提供的操作简要

   Method	| HTTP request	| Description
   --------|-----------------|-----------
   delete	|DELETE  /calendars/calendarId/acl/ruleId |	 Deletes an access control rule.
get	| GET  /calendars/calendarId/acl/ruleId	| Returns an access control rule.
insert	| POST  /calendars/calendarId/acl	 | Creates an access control rule.

3. 详细描述每个资源操作
   
   - http request形式，如
   
   ```
   POST https://www.googleapis.com/calendar/v3/calendars/{calendarId}/clear
   ```
   
   -  http request parameter
      
      这里需要注意的是什么放在Path上，什么放在query parameter上。一般而言，一个资源的子资源放在path上，定位资源的条件放在query paramenter上
   
      Path parameter  	| Value	 | Description         
--------------------|--------|---------------------
calendarId         | string  | Calendar identifier   
   ...             |         |                   
   
   



   
      |Query parameter   | Value	 | Description   | Required? |
|---------------------|--------|----------------|-----|
|   ...             |         |                   |     |
   
   

   
   - Authorization  
      定义是否需要授权
      
   - Request Body (通常只用在Post操作上)，定义方式如下：
   
     |Property name	| Value	| Description|	Notes|
|---------------------|--------|----------------|-----|
|email|string|user's email address||
|...||||
  
  
   - http response representations(针对RPC的接口，如果是REST原则是公用一个resource的描述，只是不同的操作某些参数可以被忽略)，如
   
     ``` 
{  
  "id": string,  
  "status": string,  
  "htmlLink": string,   
  "created": datetime   
}  
     ```
   
   - http reponse parameters(对于资源描述中使用到的参数的解释)
   
     |Property name	| Value	| Description|	Notes|
|---------------------|--------|----------------|-----|
|home address|string|user's home address||
|...||||  

###JSON Style Guideline

如何编写JSON，可以参见[Google JSON STYLE](https://google-styleguide.googlecode.com/svn/trunk/jsoncstyleguide.xml)

主要是参考保留的定义属性名

``` java
object {
  string apiVersion?;
  string context?;
  string id?;
  string method?;
  object {
    string id?
  }* params?;
  object {
    string kind?;
    string fields?;
    string etag?;
    string id?;
    string lang?;
    string updated?; # date formatted RFC 3339
    boolean deleted?;
    integer currentItemCount?;
    integer itemsPerPage?;
    integer startIndex?;
    integer totalItems?;
    integer pageIndex?;
    integer totalPages?;
    string pageLinkTemplate /^https?:/ ?;
    object {}* next?;
    string nextLink?;
    object {}* previous?;
    string previousLink?;
    object {}* self?;
    string selfLink?;
    object {}* edit?;
    string editLink?;
    array [
      object {}*;
    ] items?;
  }* data?;
  object {
    integer code?;
    string message?;
    array [
      object {
        string domain?;
        string reason?;
        string message?;
        string location?;
        string locationType?;
        string extendedHelp?;
        string sendReport?;
      }*;
    ] errors?;
  }* error?;
}*;
```

###接口定义模版参考

[REST JSON API模版]()