# README

## 开发环境
+ JDK 1.8
+ MySQL 8.0+
+ Mybatis Plus
+ Hutool 工具包
## 开发
如果需要本地开发运行，可以修改`resource\application.yml`中`spring.application.name.profiles.active`的配置为`dev`.
`application-dev`为开发环境配置文件，可以根据需要进行修改。

## 测试
可以项目运行后可以浏览器打开`ip:8080/doc.html`进行在线接口调试

## Docker 部署
+ 修改`resource\application.yml`中`spring.application.name.profiles.active`的配置为`docker`.
+ 运行Maven的打包插件打包项目，将打包完成的jar包放置到docker目录中，上传到服务器（自己选好目录），运行`./start.sh`(如果原来有mysql环境可能会报错，最好将原来的mysql删除)
