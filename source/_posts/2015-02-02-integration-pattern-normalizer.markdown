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

对于Normalizer内部，通常从逻辑上可以这样分

{% plantuml %}

title "Normalizer Pattern Internal"

state Normalizer {

EmailTransport -down-> Router
RestTransport -down-> Router
TCPTransport -down-> Router

Router -down-> TextTranslator
Router -down-> JsonTranslator
Router -down-> BinaryTranslator
}

{% endplantuml %}


### 特点说明

- 对于集成简单的场景比较适用。多数用在连接多个类似的client端到一个系统
- 如果对于Endpoint本身没有太复杂的逻辑则可以忽略Router并且合并translator的功能
- 唯一的不足是如果集成的系统越来越多，则需要的Endpoint将成几何基数的增长
- 可以通过Canonical Data Model(规范化数据模型)的方式来解决Normalizer模式的不足


