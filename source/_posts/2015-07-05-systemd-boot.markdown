---
layout: post
title: "Systemd的启动顺序和自动启动配置"
date: 2015-07-05 17:56:27 +0800
comments: true
categories: [Systemd]
---

###Systemd的启动顺序
---

- /etc/systemd/system/
- /run/systemd/system/
- /usr/lib/systemd/system/


在CoreOS中，自带的服务都是放在了/usr/lib/systemd/system/目录下，但是该目录是只读的，如需更改，可以将对应的service unit文件copy到/etc/systemd/system目录下，然后更改后重新启动

每次更改service unit文件需要执行 `sudo systemctl daemon-reload`



###Systemd的服务需要在系统boot时启动
---

需要在Service Unit文件中加入：

```
[Installl]
WantedBy=multi-user.target
```

然后执行`sudo systemctl daemon-reload`和`sudo systemctl enable <xxxx.service>`

