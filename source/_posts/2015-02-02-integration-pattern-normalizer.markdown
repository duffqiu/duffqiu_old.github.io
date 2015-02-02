---
layout: post
title: "集成模式：标准化模式"
date: 2015-02-02 13:14:35 +0800
comments: true
categories: Integration
---

###原由
---
在系统集成中，可以通过不同的接口（EndPoint）适配多个外围系统，然后转换成标准的消息与内部系统结成。这种模式叫Normalizer。即标准化模式


###解决办法
---

UML图例：

{% plantuml %}

title "Integration Pattern: Normalizer"

[*] -down-> Mailbox
[*] -down-> WebApp
[*] -down-> DesktopApp


Mailbox -down-> EmailEndPoint: Email
WebApp -down-> WebEndPoint: Rest API
DesktopApp -down-> DeskEndPoint: TCP

Mailbox : <<External System>>
WebApp : <<External System>>
DesktopApp : <<External System>>

EmailEndPoint -down-> InternalSystem : CommonMessage
WebEndPoint -down-> InternalSystem : CommonMessage
DeskEndPoint -down-> InternalSystem : CommonMessage

{% endplantuml %}


