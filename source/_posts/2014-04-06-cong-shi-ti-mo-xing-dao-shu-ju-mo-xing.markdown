---
layout: post
title: "从实体模型到数据模型"
date: 2014-04-06 11:41:20 +0800
comments: true
categories: [Entity Model, Data Model]
---

###原由
---

在系统设计过程中，我们总会用到Entity Model和Data Model，但是两者之间的关系是如何演变的呢？

###解决办法
---

1. 首先建立一个最详尽的Entity Model，尽可能将所有具体的entity以及他们间的关系都列举出来
2. 将该详尽的Entity Model尽可能的浓缩抽象，变成7-10个框的图形
3. 根据浓缩后的Entity Model和业务操作的要求来抽取成Data Model

因为设计公司机密问题，无法在这里给出具体的示例。后面有好的例子再补充

同时对于Data Model，有几点个人设计的体验：

1. 不要过于局限于SQL的那几个范式。
2. 尽可能单表操作
3. 如果业务上不是要求严格的事物处理，尽可能不做事务操作，而是通过后续的数据处理清理垃圾数据。只是数据的使用过程中要注意保护（如先query后操作create/update）
4. 如果两个业务模块操作同一个表的不同数据，则可以考虑将表拆成两个对立的子表。
5. 当数据库性能达到一定瓶颈的时候，可以考虑使用NOSQL的方式做cache，如用[Redis](http://redis.io)



####小插曲 

使用plantuml画entity model或者component 图都是出现自动排列时图形是乱的，很不好看。不过通过一个简单打设置就可以比较工整了。在plantuml文件的前面增加以下代码

```
left to right direction
```


