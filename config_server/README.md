# config服务端

## 1、添加依赖
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
    </dependencies>
    
## 2、配置文件
    server:
      port: 8040
    spring:
      application:
        name: spring-cloud-config-server
      cloud:
        config:
          server:
            git:
               uri: https://github.com/fimi2008/config_example/     # 配置git仓库的地址
               # git仓库地址下的相对地址，可以配置多个，用,分割。
               search-paths: config/configs/dev,config/configs/test,config/configs/product
               username:                                           # git仓库的账号
               password:                                           # git仓库的密码
    
    
    Spring Cloud Config也提供本地存储配置的方式。
    我们只需要设置属性spring.profiles.active=native，
    也可以通过spring.cloud.config.server.native.searchLocations=file:E:/properties/
    属性来指定配置文件的位置。
    虽然Spring Cloud Config提供了这样的功能，但是为了支持更好的管理内容和版本控制的功能，还是推荐使用git的方式。

## 3、启动类
    启动类添加@EnableConfigServer，激活对配置中心的支持
    
    @EnableConfigServer
    @SpringBootApplication
    public class ConfigServerApplication {
    
        public static void main(String[] args) {
            SpringApplication.run(ConfigServerApplication.class, args);
        }
    }
    
## 4、测试
    首先我们先要测试server端是否可以读取到github上面的配置信息，
    直接访问：http://localhost:8040/data/dev

    返回信息如下：
    {
        name: "dubbo",
        profiles: [
            "test"
        ],
        label: null,
        version: null,
        state: null,
        propertySources: [
        {
            name: "https://github.com/fimi2008/config_example/config/configs/dev/data-dev.yml",
            source: {
                lionxxw.name: "lionxxw_dev",
                lionxxw.url: "www.lionxxw.top"
            }
        }
        ]
    }
    
    上述的返回的信息包含了配置文件的位置、版本、配置文件的名称以及配置文件中的具体内容，说明server端已经成功获取了git仓库的配置信息。
    
    如果直接查看配置文件中的配置信息可访问：http://localhost:8040/data-dev.yml，
    返回：
       lionxxw:
         name: lionxxw_dev
         url: www.lionxxw.top
    
    修改配置文件dubbo-dev.yml中配置信息为：
    lionxxw:
         name: lionxxw_dev!!!
    再次在浏览器访问http://localhost:8040/dubbo-dev.yml，
    返回：
     {
            name: "dubbo",
            profiles: [
                "test"
            ],
            label: null,
            version: null,
            state: null,
            propertySources: [
            {
                name: "https://github.com/fimi2008/config_example/config/configs/dev/data-dev.yml",
                source: {
                   lionxxw.name: "lionxxw_dev!!!",
                   lionxxw.url: "www.lionxxw.top"
                }
            }
            ]
        }
    。说明server端会自动读取最新提交的内容
    
    仓库中的配置文件会被转换成web接口，访问可以参照以下的规则：
    /{application}/{profile}[/{label}]
    /{application}-{profile}.yml
    /{label}/{application}-{profile}.yml
    /{application}-{profile}.properties
    /{label}/{application}-{profile}.properties
    以data-dev.yml为例子，它的application是data，profile是dev。client会根据填写的参数来选择读取对应的配置。