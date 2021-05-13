# BCtrade

A blockchain-based(hyperledger) web application built by vue+springboot

# Quick Start

## prerequisite

nodejs

webpack-dev-server

node_modules

maven



## web application

locate to the directory, download the dependencies and use npm run command 

```shell
$ cd front
$ sudo npm install #be patient, It's going to take a while
$ sudo npm run dev
```



## backend

```shell
$ cd backend
$ mvn package -DskipTests
$ java -jar /target/demo-0.0.1-SNAPSHOT.jar
```



### api

```yaml
#注册服务，注册的用户信息放在body, 示例如下
register:
	http://localhost:8999/admin/register
	body:
	{
	    "id":"114514",
	    "username":"luojun",
	    "password":"1919810",
	    "available":"true",
	    "role":"user",
	    "regtime":" "
	}

#登录服务
login:
	http://localhost:8999/admin/login?username=bt&password=bt
	username和password是请求的参数
	
#退出登录	
logout:
	http://localhost:8999/admin/logout	

#权限测试:权限为user的用户请求该api会接收hello报文
hello:
	http://localhost:8999/admin/hello
	
#权限测试:权限为admin的用户请求该api会接收hello报文
ahello:
	http://localhost:8999/admin/ahello
```

