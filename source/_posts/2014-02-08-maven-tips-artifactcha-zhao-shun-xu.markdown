---
layout: post
title: "Maven tips: artifact查找顺序"
date: 2014-02-08 14:36:17 +0800
comments: true
categories: Maven
---

###原由 

因为要用到一个maven plugin的snapshot版本，所以需要增加一个专门的snapshot repository，但是增加了后总是不起左右


###解决办法

尝试了多次以后，发现是maven的settings.xml的配置上问题，因为我增加了一个mirror从而造成了新增的repository不起作用。  
解决的方式去掉mirror的配置，增加repository到profile中，并激活
提示：maven下载artifactor的顺序为pom.xml上的repository配置，然后是settings.xml的mirror配置，然后才是profiles中的repository配置

<a name="rep-seq"></a>

<div style="text-decoration:underline; font-size: 16px; color: red" onclick="showdiv('pic')"> Maven下载artifactor的顺序</div>

<div style="display:none" class="prev" id="pic"  onclick="hidediv('pic');self.location.href='#rep-seq'">

<img src="http://maven.apache.org/repository/maven-repositories.png" title="点击关闭">

</div>


###比较好用的repository

<a name="rep"></a>

<div style="text-decoration:underline; font-size: 16px; color: red" onclick="showdiv('rep-code')" title="点击打开">repository配置</div>

<div style="display:none" class="prev" id="rep-code" onclick="hidediv('rep-code');self.location.href='#rep'" title="点击关闭">

{% codeblock lang:xml %}

			<repository>
			   <id>Codehaus Snapshots</id>
			   <url>http://nexus.codehaus.org/snapshots/</url>
			   <snapshots>
			       <enabled>true</enabled>
			   </snapshots>
			   <releases>
			       <enabled>false</enabled>
			   </releases>
			</repository>
			
			<repository>
			    <id>MavenCentral</id>
			    <name>Maven repository</name>
			    <url>http://repo1.maven.org/maven2</url>
			    <releases>
			        <enabled>true</enabled>
			    </releases>
			    <snapshots>
			        <enabled>false</enabled>
			    </snapshots>
			</repository>
			
			<repository>
			    <id>objectweb</id>
			    <name>Objectweb repository</name>
			    <url>http://maven.objectweb.org/maven2</url>
			    <releases>
			        <enabled>true</enabled>
			    </releases>
			    <snapshots>
			        <enabled>false</enabled>
			    </snapshots>
			</repository>
			
			<repository>
			    <id>jboss</id>
			    <name>JBoss Maven2 repository</name>
			    <url>http://repository.jboss.com/maven2/</url>
			    <snapshots>
			        <enabled>false</enabled>
			    </snapshots>
			    <releases>
			        <enabled>true</enabled>
			    </releases>
			</repository>
			
			<repository>
			    <id>apache.snapshots</id>
			    <name>Apache Snapshot Repository</name>
			    <url>
			        http://people.apache.org/repo/m2-snapshot-repository
			    </url>
			    <releases>
			        <enabled>false</enabled>
			    </releases>
			    <snapshots>
			        <enabled>true</enabled>
			    </snapshots>
			</repository>
			
			<repository>
			    <id>ops4j.repository</id>
			    <name>OPS4J Repository</name>
			    <url>http://repository.ops4j.org/maven2</url>
			    <releases>
			        <enabled>true</enabled>
			    </releases>
			    <snapshots>
			        <enabled>false</enabled>
			    </snapshots>
			</repository>

{% endcodeblock %}

</div>