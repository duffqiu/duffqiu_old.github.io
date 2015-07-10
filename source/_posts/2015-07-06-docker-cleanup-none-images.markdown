---
layout: post
title: "Docker清除none images"
date: 2015-07-06 15:32:40 +0800
comments: true
categories: 
---

###原由
---
如果是本地去build image，则中间过程会产生不少<none>的image，特别是如果build的过程使用了ctrl+c取消进程的执行。如何有效的清除这些<none>
的image而节约空间呢？

###解决办法
---

Docker在查询image的命令中增加了`--filer/-f`参数，利用该选项查询哪些none的image然后再逐一删除

```
docker rmi $(docker images -f "dangling=true" -q)
```

