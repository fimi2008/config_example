# config客户端
主要展示如何在业务项目中去获取server端的配置信息

## 1、添加依赖
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
引入spring-boot-starter-web包方便web测试

## 2、配置文件
需要配置两个配置文件，application.properties和bootstrap.properties

    application.properties如下：
        spring.application.name=spring-cloud-config-client
        server.port=8002

    bootstrap.properties如下：
        spring.cloud.config.name=data
        spring.cloud.config.profile=dev
        spring.cloud.config.uri=http://localhost:8001/
        spring.cloud.config.label=master
        spring.application.name：对应{application}部分
        spring.cloud.config.profile：对应{profile}部分
        spring.cloud.config.label：对应git的分支。如果配置中心使用的是本地存储，则该参数无用
        spring.cloud.config.uri：配置中心的具体地址
        spring.cloud.config.discovery.service-id：指定配置中心的service-id，便于扩展为高可用配置集群。

特别注意：
    上面这些与spring-cloud相关的属性必须配置在bootstrap.yml，config部分内容才能被正确加载。
因为config的相关配置会先于application.yml，而bootstrap.yml的加载也是先于application.yml。

## 3、启动类
启动类添加@EnableConfigServer，激活对配置中心的支持

    @SpringBootApplication
    public class ConfigClientApplication {
    
        public static void main(String[] args) {
            SpringApplication.run(ConfigClientApplication.class, args);
        }
    }
启动类只需要@SpringBootApplication注解就可以

## 4、web测试
使用@Value注解来获取server端参数的值

    @RestController
    class HelloController {
        @Value("${lionxxw.name}")
        private String hello;
    
        @RequestMapping("/hello")
        public String from() {
            return this.hello;
        }
    }
启动项目后访问：http://localhost:8002/hello，
返回：lionxxw_dev 说明已经正确的从server端获取到了参数。
到此一个完整的服务端提供配置服务，客户端获取配置参数的例子就完成了。

我们在进行一些小实验，
手动修改data-dev.yml中配置信息为：lionxxw.name=hello im dev lionxxw
提交到github,再次在浏览器访问http://localhost:8002/hello，
返回：lionxxw_dev，说明获取的信息还是旧的参数，这是为什么呢？
因为springboot项目只有在启动的时候才会获取配置文件的值，修改github信息后，
client端并没有在次去获取，所以导致这个问题。

## 如何去解决这个问题呢？留到下一章我们在介绍。
