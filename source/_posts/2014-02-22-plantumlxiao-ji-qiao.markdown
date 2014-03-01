---
layout: post
title: "PlantUML小技巧"
date: 2014-02-22 15:10:21 +0800
comments: true
categories: PlantUML
---

###PlantUML画模块图/组件图的问题

在使用PlantUML的时候，如果模块中嵌套模块然后将关联关系定义在子模块中，则很容易造成模块的混沦。

###解决办法
我个人一个比较好的practice是这样一个模式，PlantUML代码如下  
同时最好的方式是在纸上先用笔画好初稿

```
@startuml
title "组件图Best Practice"

'全局的component
component [global 1]
component [global 2]
interface "inferface 1" as if1
interface "inferface 2" as if2
...

'模块定义
package "Parent1" {
	package "Children1-1" {
		'只定义component，不定义关心
		component [1-1-1]
		component [1-1-2]
		...
	}
	package "Children1-2" {
		component [1-2-1]
		component [1-2-2]
		...
	}
	
}

package "Parent2" {
	package "Children2-1" {
		'只定义component，不定义关心
		component [2-1-1]
		component [2-1-2]
		...
	}
	package "Children2-2" {
		component [2-2-1]
		component [2-2-2]
		...
	}
	
}

'完成component定义后再定义关系不要再component中定义关系，容易造成有些component还未必定义就被使用

'定义全局和子模块的关系
[global 1]..>[global 2]
[global 1] - if1
[global 2] - if2
[global 1] ..> [1-1-1]

'定义Parent1和children间的关系已经与其它Parent或children的关系
[1-1-1]..>[1-1-2]
[1-1-1]..>[1-2-1]
[1-2-1]..>[1-2-2]
[1-1-1]..>[2-1-1]

'定义Parent2和children间的关系已经与其它Parent或children的关系
[2-1-1]..>[2-1-2]
[2-1-1]..>[2-2-1]
[2-1-1]..>[2-2-2]

@enduml
```

<div style="text-decoration:underline; font-size: 16px; color: red" onclick="showdiv('componentuml')"> 最后的UML图如下： </div>
	
<div style="display:none" class="prev" id="componentuml"  onclick="hidediv('componentuml')" title="点击关闭">



{% plantuml %}
title "组件图Best Practice"

'全局的component
component [global 1]
component [global 2]
interface "inferface 1" as if1
interface "inferface 2" as if2

'模块定义
package "Parent1" {
	package "Children1-1" {
		'只定义component，不定义关心
		component [1-1-1]
		component [1-1-2]

	}
	package "Children1-2" {
		component [1-2-1]
		component [1-2-2]

	}
	
}

package "Parent2" {
	package "Children2-1" {
		'只定义component，不定义关心
		component [2-1-1]
		component [2-1-2]

	}
	package "Children2-2" {
		component [2-2-1]
		component [2-2-2]

	}
	
}

'完成component定义后再定义关系不要再component中定义关系，容易造成有些component还未必定义就被使用

'定义全局和子模块的关系
[global 1]..>[global 2]
[global 1] - if1
[global 2] - if2
[global 1] ..> [1-1-1]

'定义Parent1和children间的关系已经与其它Parent或children的关系
[1-1-1]..>[1-1-2]
[1-1-1]..>[1-2-1]
[1-2-1]..>[1-2-2]
[1-1-1]..>[2-1-1]

'定义Parent2和children间的关系已经与其它Parent或children的关系
[2-1-1]..>[2-1-2]
[2-1-1]..>[2-2-1]
[2-1-1]..>[2-2-2]

{% endplantuml %}

</div>