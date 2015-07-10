---
layout: post
title: "Install ZSH and oh_my_zsh in CoreOS"
date: 2015-07-09 21:13:24 +0800
comments: true
categories: [CoreOS ZSH]
---

###Why
---

Since some guys from other country ask me how to install zsh and oh-my-zsh in CoreOS, I have to write it in English.

ZSH with [oh_my_zsh](https://github.com/robbyrussell/oh-my-zsh) is very convinient shell instead of bash or other shells. 

But if you are using CoreOS (most people use it because of using Docker, fleet and etcd), you will meet a challenge because CoreOS doesn't include zsh with it.

Since there is no installation tool like yum or apt-get in CoreOS, we have to do some workaround mannually to have zsh on it.


###How
---

- Get the zsh related files. (Of course you can build a zsh from source code, but the process is too heavy)

   - Create a local folder in CoreOS. For example: `/home/core/zsh`. I will refer it as `zsh_home`. And also create subfolders: `lib64`, `share`, `bin`
   - Run a docker container with centos7 image: `docker run -it --rm -v <zsh_home>:/root/zsh centos /bin/bash`
   - In the container, run `yum update` and `yum install zsh`
   - In the container, query what is installed for zsh: `rpm -aql zsh`
   - Copy the zsh files to the folder you mount for the container in the docker run command. `cp /bin/zsh root/zsh/bin`, `cp -r /usrlib64/zsh /root/zsh/lib64`, `cp -r /usr/share/zsh /root/zsh/share`
   - Copy the dynamic library file for zsh: `copy /usr/lib64/libtinfo.so.5 /root/zsh/lib64/`

- Install oh-my-zsh. Follow the instruction of installation of oh-my-zsh is OK.
- Update .bashrc file (under home path)

   - Break the link of .bashrc: `rm .bashrc` because it is a link default
   - Get the default version: `cp ../../usr/share/skel/.bashrc .bashrc`
   - Update .bashrc: add below lines in the bottom of the file `export PATH=$PATH:/home/core/zsh/bin/` and `export LD_LIBRARY_PATH=/home/core/zsh/lib64/`
   - add the `zsh` command in the last line of .bashrc file becuase we can't use `chsh -s <zsh_home>/zsh`
. The file `/etc/shells` is read only

- Update oh-my-zsh

   - add the below lines on the top of the file: .zshrc
   
```
module_path=(<zsh_home>/lib64/zsh/5.0.2/)

fpath=(<zsh_home>/share/zsh/5.0.2/functions/ <zsh_home>/share/zsh/site-functions/ $fpath)

export PATH=$PATH:<zsh_home>/bin
```

- ...
   - add `PATH=$PATH:/home/core/zsh/bin` on the top of file .oh-my-zsh/oh-my-zsh.sh
   - add below lines on the top of the file .oh-my-zsh/tools/check_for_upgrade.sh
   
```
module_path=(<zsh_home>/lib64/zsh/5.0.2/)

fpath=(<zsh_home>/share/zsh/5.0.2/functions/ <zsh_home>/share/zsh/site-functions/ $fpath)
```   

- relogin CoreOS shall be ok to use zsh