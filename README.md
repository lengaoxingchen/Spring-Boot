Spring-Boot

简介:
本代码是根据:<从零开始学SpringBoot>视频教程中提取

关于数据库驱动的pom.xml中的maven依赖和SpringBoot1.4.2可能会不兼容.建议使用SpringBoot1.5以上版本
    <dependency>
        <groupId>mysql</groupId>
         <artifactId>mysql-connector-java</artifactId>
    </dependency>
本人在学习使用Themeleaf模板，直接上手项目，然后想在themeleaf上使用shiro标签实现权限控制，然后踩到这个坑，上网一查这方面的知识少之又少。踩下去两天才找到解决方法。

Caused by: java.lang.ClassNotFoundException: org.thymeleaf.dialect.AbstractProcessorDialect

at java.net.URLClassLoader$1.run(URLClassLoader.java:366)
at java.net.URLClassLoader$1.run(URLClassLoader.java:355)
at java.security.AccessController.doPrivileged(Native Method)
at java.net.URLClassLoader.findClass(URLClassLoader.java:354)
at java.lang.ClassLoader.loadClass(ClassLoader.java:425)
at sun.misc.Launcher$AppClassLoader.loadClass(Launcher.java:308)
at java.lang.ClassLoader.loadClass(ClassLoader.java:358)
... 43 more



我的maven上springboot是1.5.10.RELEASE，

thymeleaf依赖和shiro整合依赖(整合依赖版本是我按网上教程找来的)

<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>
<dependency>  
		    <groupId>com.github.theborakompanioni</groupId>  
		    <artifactId>thymeleaf-extras-shiro</artifactId>  
		    <version>2.0.0</version>  
		</dependency> 

ClassNotFoundException:org.thymeleaf.dialect.AbstractProcessorDialect   找不到类AbstractProcessorDialect（自定义标签抽象类，需要自定义标签需要继承此类，重写他的方法)
@Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {}

而这里的问题出在：使用的SpringBoot1.5.2.RELEASE版本集成Thymeleaf时，它使用的版本是2.1.5.RELEASE，而在这个版本中没有AbstractProcessorDialect类。

解决方法一：可以把Thymeleaf版本更改为3.0.7.RELEASE

<thymeleaf.version>3.0.7.RELEASE</thymeleaf.version>再加上<thymeleaf-layout-dialect.version>2.2.2</thymeleaf-layout-dialect.version>

解决方法二：还可以把thymeleaf-extras-shiro的版本改为1.2.1(**本文采用此解决方案**)

