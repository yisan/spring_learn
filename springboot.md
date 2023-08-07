# 一、基础部分



## （一）基础配置

### 1.配置文件格式

三种格式： `application.properties` , `application.yaml` , `application.yml` 



#### （1）application.properties

> SpringBoot中导入对应starter后，提供对应配置属性 , 书写SpringBoot配置采用关键字+提示形式书写

比如：

修改服务器端口

```properties
server.port=8082
```

设置日志级别

```properties
logging.level.root=debug
```

其他内容属性查询 [官方文档](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#application-properties)



#### （2） application.yaml





使用

```yaml
server:
  port: 8080

logging:
  level: debug

```



> TIP
>
> 以上三种格式，推荐使用 `application.yml`

#### （3）Yaml 介绍

##### ① 概念

YAML（YAML Ain't Markup Language）不是标记语言，而是一种数据序列化格式。



语法要求

> * 大小写敏感
> * 属性层级关系使用多行描述，每行结尾使用冒号结束
> * 使用缩进表示层级关系，同层级左侧对齐，只允许使用空格(不允许使用Tab键) 
> * 属性值前面添加空格(属性名与属性值之间使用冒号+空格作为分隔)
> * `#`表示注释

核心规则:

> 数据前面要加空格与冒号隔开

##### ② 常见的数据格式书写 

了解即可

```yaml
# 基本类型数据
boolean: TRUE  						#TRUE,true,True,FALSE,false，False均可
float: 3.14    						#6.8523015e+5  #支持科学计数法
int: 123       						#0b1010_0111_0100_1010_1110    #支持二进制、八进制、十六进制
null: ~        						#使用~表示null
string: HelloWorld      			#字符串可以直接书写
string2: "Hello World"  			#可以使用双引号包裹特殊字符
date: 2018-02-17        			#日期必须使用yyyy-MM-dd格式
datetime: 2018-02-17T15:02:31+08:00  #时间和日期之间使用T连接，最后使用+代表时区


#数组
address: 								
  - beijing
  - shanghai

address1: [beijing,shanghai]
		
# 对象
user: 
	name: ing
	age: 20
	
# 对象数组格式一												
users1: 							
  - name: Tom
    age: 4
  - name: Jerry
		age: 5
		
# 对象数组格式二
users2:							
  -
    name: Tom
    age: 4
  -
    name: Jerry
    age: 5

#对象数组格式三
users3: [ { name:Tom , age:4 } , { name:Jerry , age:5 } ] 	

# 引用格式
str1: "Hello"
str2: "World"
str3: ${str1} ${str2}

```



### 2.配置文件加载优先级

配置文件间的加载优先级

* properties(最高) 

* yml
* yaml(最低)

> TIP
>
> 不同配置文件中相同配置按照加载优先级相互覆盖，不同配置文件中不同配置全部保留

#### （1）内部配置加载顺序

Springboot程序启动时，会从以下位置加载配置文件: 

>1. file:./config/:当前项目下的/config目录下
>2. file:./ :当前项目的根目录
>3. classpath:/config/:classpath的/config目录 
>4. classpath:/ :classpath的根目录 也就是resources 下



加载顺序为上文中的排列顺序：优先级从高到低，高优先级的相同配置会覆盖优先级低的。



如下项目结构（只保留了测试需要的展示）



├── `application.properties`                `③` 其配置内容为：`server.port=8083`
├── config
│   └── `application.properties`			 `④`其配置内容为：`server.port=8084`
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── bingo
│   │   │           └── learn
│   │   │               └── SpringbootConfigApplication.java
│   │   └── resources
│   │       ├── `application.properties`     `①` 其配置内容为：`server.port=8081`
│   │       ├── config
│   │       │   └── `application.properties` `②` 其配置内容为：`server.port=8082`



依次按照后面的标号顺序添加其内容，测试下后添加的配置是否会覆盖前面的即可。

最终结果符合 `④` 会覆盖 `③` 会覆盖 `②` 会覆盖 `①` 就对了。



> TIP
>
> 如果 此时 ① 当中添加了别的配置内容：`server.servlet.context-path=/hello` ，而其他的几个都没有



我们添加一个 `HelloController`

```java
@RestController
public class HelloController {
    @RequestMapping("/say")
    public String say(){
        return "Hello SpringBoot";
    }
}
```



运行会发现

![image-20211210162020273](https://tva1.sinaimg.cn/large/008i3skNly1gx8t97rqkmj30vf01dt8z.jpg)



也就是说，端口是优先级加载的 `④` 里面的配置，而 context-path 加载的 `①` 当中的配置

访问测试：http://localhost:8084/hello/say



<img src="https://tva1.sinaimg.cn/large/008i3skNly1gx8tekrrgcj318u05o0tc.jpg" alt="image-20211210162511711" style="zoom:50%;" />





#### （2）外部配置加载顺序

首先打jar包



![image-20211210165824377](https://tva1.sinaimg.cn/large/008i3skNly1gx8ucwrapbj30qy04n3z8.jpg)





然后进到jar所在目录下，执行命令行命令

```shell
java -jar springboot_config-0.0.1-SNAPSHOT.jar
```



端口号为： `8082`

> 因为项目根目录下的 `application.properteis` 和 `config/application.properties` 不会被打进jar包内，所以优先加载的是classpath的/config目录中的配置文件



当然我们也可以在运行时添加一些参数：

```shell
java -jar springboot_config-0.0.1-SNAPSHOT.jar --server.port=8085
```

端口号为： `8085`



如上，如果参数多了，这种方式就不方便了，那么就可以利用加载外部配置文件来解决。

我们在当前目录下创建一个配置文件：`application.properties` 

```properties
server.port=8083
```



然后执行命令，并通过 `--spring.config.location` 来指定加载该配置文件：

```shell
java -jar springboot_config-0.0.1-SNAPSHOT.jar --spring.config.location=application.properties
```



端口号为： `8083`

如果不指定，也会默认加载当前目录下的配置文件 `application.properties`。 

```shell
java -jar springboot_config-0.0.1-SNAPSHOT.jar
```



端口号为： `8083`

如果此时在该目录下创建一个 `config` 目录下的配置文件 `application.properties` 会**优先加载此配置文件**。

```properties
server.port=8084
```



此时执行：

```shell
java -jar springboot_config-0.0.1-SNAPSHOT.jar
```

端口号为： `8084`



> TIP
>
> 1. 外部配置的优先级高与内部配置
>
> 2. 为什么有了内部配置，还需要外部配置？
>
>    外部与内部配置互补，比如内部配置错误了，可以通过外部配置来指定部分修改或补救，灵活性更高。





### 3.配置文件的读取

#### （1）yaml配置读取

比如有如下 application.yml 配置文件

```yaml
server:
  port: 8080
#自定义数据

#基本数据类型
name: abc

#对象
person:
  name: 张三 
  age: 20
  address:
    - beijing
    - shanghai

#对象行内写法
person2: {name: zhangsan,age: 20}

#数组
address: 							
  - beijing
  - shanghai

#数组行内写法
address1: {beijing,shanghai}

#纯量
msg1: 'hello \n world' #不会识别转义字符，会原样输出
msg2: "hello \n world" # 会识别转义字符

```

如何读取？

##### ① 方式一：@Value 读取

使用@Value读取单个数据，属性名引用方式:**${一级属性名.二级属性名...}**



```java
@RestController
public class HelloController {
    @Value("${name}")
    private String name;
    @Value("${person.name}")
    private String personName;
    @Value("${person.age}")
    private int personAge;
    @Value("${address[0]}")
    private String address1;
    @Value("${msg1}")
    private String msg1;
    @Value("${msg2}")
    private String msg2;
    
    @RequestMapping("/hello")
    public String hello() {
        System.out.println(name);
        System.out.println(personName);
        System.out.println(personAge);
        System.out.println(address1);
        System.out.println(msg1);
        System.out.println(msg2);
     
        return "Hello SpringBoot!!!";
    }
}
```

> TIP
>
> 注意写法: @Value("${name}") 千万不要写成了 @Value("name") !!!



##### ② 方式二：Environment 读取

Environment会将数据全部封装。使用 @Autowired 注入到容器中以供使用。

```java
@RestController
public class HelloController {
    @Autowired
    private Environment env;//在spring容器启动时就会初始化该对象实例，所以直接注入即可
    @RequestMapping("/hello")
    public String hello() {
        System.out.println(env.getProperty("person.name"));
        System.out.println(env.getProperty("person.age"));
        System.out.println(env.getProperty("address[0]"));
        return "Hello SpringBoot!!!";
    }
}
```



##### ③ 方式三：@ConfigurationProperties 

思路：

创建类用于封装部分配置信息，告知spring 加载这部分数据，由spring帮我们加载数据到对象中，使用时，直接从spring 容器中直接获取。



比如我们测试获取Person对象的值

步骤：

首先，创建一个`Person `实体类，其属性要和你配置的字段名称一致。

其次，创建的类要想让spring将读取的数据给到对象中，那必须得让其受spring管控，所以得加上 @Component 注解，成为bean。

然后，通过 @ConfigurationProperties 注解且指定要读取的数据--

```java
@Component 
@ConfigurationProperties("person")
public class Person {
    private String name;
    private int age;
    private String[] address;
		...
      getter,setter,toString省略
    ...
}
```





然后测试

```java
@RestController
public class HelloController {
    @Autowired
    private Person person;
    @RequestMapping("/hello")
    public String hello() {
        System.out.println(person.getName());
        System.out.println(person.getAge());
      	String[] address = person.getAddress();
        for (String s : address) {
            System.out.println(s);
        }
        return "Hello SpringBoot!!!";
    }
}
```

运行，访问测试：http://localhost:8080/hello

控制台输出：

```
abc
0
中国
上海
```

可以看出，这不是我们预想的结果。。。

原因在于，在配置文件当中，有两个 `name` 和两个 `address` 的存在

```yaml
name: abc

#对象
person:
  name: 张三 #${name}
  age: 20
  #数组
  address:
    - beijing
    - shanghai
    
#数组
address:
  - 中国
  - 上海
```

其最先匹配上的是第一层级的name和address,而此时 person 是无法注入的，age 也就为空了，打印的是默认值 0 



解决办法： `@ConfigurationProperties` 需要指定前缀 ，只要满足前缀，其层级内的字段与属性一一匹配，挨个注入

```java
@Component // 声明为一个bean，普通pojo实例化到spring容器中
@ConfigurationProperties(prefix = "person") // 指定前缀，匹配person下的属性
public class Person {
    private String name;
    private int age;
    private String[] address;
		...
      getter,setter,toString省略
    ...
}
```

此时再次测试，结果

```
张三
20
beijing
shanghai
```

就是正确的了。

另外一个小点，在我们给 Person 加上 `@ConfigurationProperties` 之后，上方出现了一个横条，逼死强迫症的玩意。。。

![image-20211209165706738](/Users/ing/Library/Application Support/typora-user-images/image-20211209165706738.png)



意思是缺少一个注解处理器，要去掉这个只需要在pom.xml加一个依赖：

```xml
 <dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-configuration-processor</artifactId>
</dependency>
```

`rebuild project`下，你会发现在yml文件中，idea 的自动提示功能就包含你自定义的内容了。



## （二）Profile配置

我们在开发Spring Boot应用时，通常同一套程序会被安装到不同环境，比如:开发、测试、生产等。其中数据库地址、服务 器端口等等配置都不同，如果每次打包时，都要修改配置文件，那么非常麻烦。profile功能就是来进行动态配置切换的。

1) profile配置方式
   * 多profile文件方式
   * yml多文档方式
2) profile激活方式
   * 配置文件 
   * 虚拟机参数 
   * 命令行参数



### 1. 配置方式

首先创建一个项目 `springboot_profile`

主要目录结构

├── java
│   └── com
│       └── bingo
│           └── learn
│               └── springboot_profile
│                   └── SpringbootProfileApplication.java
└── resources
    ├── application-dev.properties
    ├── application-pro.properties
    ├── application-test.properties
    ├── application.properties
    ├── static
    └── templates



#### （1）多profile文件配置

在`resources` 下另外创建了三个配置文件，分别为

 `application-dev.properties` 

```properties
server.port=8081
```

 `application-pro.properties` 

```properties
server.port=8082
```

 `application-test.properties` 

```properties
server.port=8083
```

> 命名规范应该是 `application-` 是固定写法（我试着改为其他写法，导致后续无法读取），后面的名称随便定义，不过一般都是如上，代表着 开发环境，生产环境，测试环境。

如果这时候启动服务，会发现使用的是默认端口：8080，而且有一句 No active profile set, 没有激活profile的配置



![](https://tva1.sinaimg.cn/large/008i3skNly1gx8ludwq2uj30ye01vdgc.jpg)



怎么激活呢？很简单，在 `application.properties` 中 

```properties
spring.profiles.active=dev
```

> 后面的值，是你要切换的配置文件中对应的`-`后面的名字

这时候，再次运行，会发现，`dev` 配置被激活，端口号为：8081

 ![image-20211210121023422](https://tva1.sinaimg.cn/large/008i3skNly1gx8m16y3uej30yg021q3h.jpg)



同理，将 `.properties` 后缀改为 `.yml` 或者 `.yaml` 效果是一样的，这里就不再赘述，请自行试验。



下面来讲一下，使用一个 yaml 文件如何实现上述配置。



#### （2）yaml的多文件配置

创建一个 `application.yml` 文件，内容如下

```yaml
---
server:
  port: 8081
spring:
  profiles:
    dev
---
server:
  port: 8082

spring:
  profiles:
    pro
---
server:
  port: 8083
spring:
  profiles:
    test

---
spring:
  profiles:
    active: pro
```

> 1. `---` 是文档分隔符，分隔几种配置
> 2. `Deprecated configuration property '' `
>
> 原因是在SpringBoot2 当中`spring.profiles` 已过时，知晓即可，后续学到SpringBoot2的部分再补充。

测试前，记得将其他的配置文件内容都注释掉，或者直接都删掉，测试结果



![image-20211210125645255](https://tva1.sinaimg.cn/large/008i3skNly1gx8nde701mj30y603rmyj.jpg)



说明配置生效。



### 2. 配置的激活方式

#### （1）虚拟机参数

分为两种

##### ① VM Options 配置

IDEA中，菜单【Run】--【Edit Configurations...】-- 【Configuration】--【Environment】--[VM options]选项

配置为：`-Dspring.profiles.active=test` 比如这里是激活的 `test` 环境。



<img src="/Users/ing/Library/Application Support/typora-user-images/image-20211210130335114.png" alt="image-20211210130335114"  />

<img src="/Users/ing/Library/Application Support/typora-user-images/image-20211210130431161.png" alt="image-20211210130431161"  />



配置后，点击ok ,然后重新运行，结果



![image-20211210130655138](https://tva1.sinaimg.cn/large/008i3skNly1gx8nnzdvl0j312203owg0.jpg)



会覆盖掉之前我们在配置文件中的配置，新的配置生效。



##### ② Program arguments 配置

和 ① 的打开方式一致，IDEA中，菜单【Run】--【Edit Configurations...】-- 【Configuration】--【Environment】--[Program arguments]选项

配置为：`--spring.profiles.active=pro`



![image-20211210131222256](https://tva1.sinaimg.cn/large/008i3skNly1gx8ntnzcdij30sk09bgmq.jpg)



运行结果



![image-20211210131325207](https://tva1.sinaimg.cn/large/008i3skNly1gx8nuq8f5wj30yn03o3zw.jpg)



> TIP
>
> 
>
> 如果以上两种同时配置，经过测试，② 会起作用，覆盖掉 ① 

#### （2）命令行参数

首先，我们通过maven对项目进行打包



![image-20211210132307878](https://tva1.sinaimg.cn/large/008i3skNly1gx8o4ugh8aj30ui04s0ss.jpg)



打包成功



![image-20211210132638925](https://tva1.sinaimg.cn/large/008i3skNly1gx8o8i4w7zj30vu04c753.jpg)



找到对应的 jar 包在终端执行命令行即可

```shell
java -jar /Users/ing/github/springLearn/springboot_profile/target/springboot_profile-0.0.1-SNAPSHOT.jar --spring.profiles.active=pro
```

后面指定的参数是 `pro` ，运行出来的结果就是 `pro` 环境的配置



![image-20211210132909949](https://tva1.sinaimg.cn/large/008i3skNly1gx8ob4rs80j31u8040wgk.jpg)



以上就是几种常见环境配置的方式。





## （三）整合框架

### 1.整合Junit



新建项目 springboot_test 

项目目录结构

├── main
│   ├── java
│   │   └── com
│   │       └── bingo
│   │           └── learn
│   │               ├── SpringbootTestApplication.java
│   │               └── UserService.java
│   └── resources
│       ├── application.properties
│       ├── static
│       └── templates
└── test
    └── java
        └── com
            └── bingo
                ├── learn
                │   └── UserServiceTest1.java
                └── test
                    └── UserServiceTest2.java







pom.xml

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
  </dependency>

  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
  </dependency>
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.3.2</version>
  </dependency>
  <dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
  </dependency>

</dependencies>

```



`UserService.java`

```java
@Service
public class UserService {
    public void save(){
        System.out.println("save...");
    }
}
```

`UserService2Test`

```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootTestApplication.class)
public class UserService2Test {
    @Autowired
    private UserService userService;
    @Test
    public void testSave(){
        userService.save();
    }
}
```

> TIP
>
> 1. Springboot2 已不需要写 `@RunWith(SpringRunner.class)`
> 2. 如果测试类所在包名和引导类 `SpringbootTestApplication` 所在包名一致，可以省略 引导类字节码的指定



也就是 `UserService1Test` 的写法

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserService1Test {
    @Autowired
    private UserService userService;
    @Test
    public void testSave(){
        userService.save();
    }
}
```

测试效果是一样的,控制台打印结果

```
save...
```

### 2.整合MyBatis

![image-20211214112332965](https://tva1.sinaimg.cn/large/008i3skNly1gxd75qvi7cj30kh0hlwfk.jpg)



最终项目结构

├── main
│   ├── java
│   │   └── com
│   │       └── bingo
│   │           └── learn
│   │               ├── SpringbootMybatisApplication.java
│   │               ├── domain
│   │               │   └── User.java
│   │               └── mapper
│   │                   ├── UserMapper.java
│   │                   └── UserXmlMapper.java
│   └── resources
│       ├── application.properties
│       ├── application.yml

│       ├── log4j.properties

│       └── mapper
│           └── UserMapper.xml
└── test
    └── java
        └── com
            └── bingo
                └── learn
                    └── SpringbootMybatisApplicationTests.java





Log4j.properties

```properties
### direct log messages to stdout ###
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.out
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{ABSOLUTE} %5p %c{1}:%L - %m%n

### direct messages to file mylog.log ###
log4j.appender.file=org.apache.log4j.FileAppender
log4j.appender.file.File=/Users/ing/Downloads/mybatis_log.log
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=%d{ABSOLUTE} %5p %c{1}:%L - %m%n

### set log levels - for more verbose logging change 'info' to 'debug' ###

log4j.rootLogger=debug, stdout

```



pom.xml

```xml
<dependencies>
  <dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.2.0</version>
    <exclusions>
      <!--排除Springboot默认日志logback 与log4j的冲突-->
      <exclusion>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-logging</artifactId>
      </exclusion>
    </exclusions>
  </dependency>

  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
  </dependency>
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
  </dependency>
</dependencies>
```

这里引入了lombok 就是简化实体类写法的。



建表

```sql
#创建数据库 mp
CREATE DATABASE IF NOT EXISTS mp DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

CREATE TABLE `mp_user`(
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
`user_name` varchar(20) NOT NULL COMMENT '用户名',
`password` varchar(20) NOT NULL COMMENT '密码',
`name` varchar(30) DEFAULT NULL COMMENT '姓名',
`age` int(11) DEFAULT NULL COMMENT '年龄',
`email` varchar(50) DEFAULT NULL COMMENT '邮箱',
PRIMARY KEY(`id`)

)ENGINE=INNODB DEFAULT CHARSET=utf8;

#插入数据
INSERT INTO `mp_user` (`id`, `user_name`, `password`, `name`, `age`, `email`) VALUES ('1', '法外狂徒', '123456', '张三', '18', 'zhangsan@163.com');
INSERT INTO `mp_user` (`id`, `user_name`, `password`, `name`, `age`, `email`) VALUES ('2', 'lisi', '123456', '李四', '20', 'lisi@163.com');
INSERT INTO `mp_user` (`id`, `user_name`, `password`, `name`, `age`, `email`) VALUES ('3', 'wangwu', '123456', '王五', '28', 'wangwu@163.com');
INSERT INTO `mp_user` (`id`, `user_name`, `password`, `name`, `age`, `email`) VALUES ('4', 'zhaoliu', '123456', '赵六', '21', 'zhaoliu@163.com');
INSERT INTO `mp_user` (`id`, `user_name`, `password`, `name`, `age`, `email`) VALUES ('5', 'sunqi', '123456', '孙七', '24', 'sunqi@163.com');


+----+-----------+----------+--------+------+------------------+
| id | user_name | password | name   | age  | email            |
+----+-----------+----------+--------+------+------------------+
|  1 | zhangsan  | 123456   | 张三   |   18 | zhangsan@163.com |
|  2 | lisi      | 123456   | 李四   |   20 | lisi@163.com     |
|  3 | wangwu    | 123456   | 王五   |   28 | wangwu@163.com   |
|  4 | zhaoliu   | 123456   | 赵六   |   21 | zhaoliu@163.com  |
|  5 | sunqi     | 123456   | 孙七   |   24 | sunqi@163.com    |
+----+-----------+----------+--------+------+------------------+

```

`User`

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String userName;
    private String name;
    private Integer age;
    private String email;
}
```

#### （1）注解的方式整合

`UserMapper`

```java
@Mapper
public interface UserMapper {
    @Select("select * from mp_user")
    List<User> findAll();
}
```

> TIP
>
> @Mapper: mybatis3.4.0开始加入了@Mapper注解，目的就是为了不再写mapper映射文件
>
> 添加了@Mapper注解之后这个接口在编译时会生成相应的实现类
>
> 需要注意的是：
>
> 这个接口中不可以定义同名的方法，因为会生成相同的id,也就是说这个接口是不支持重载的
>
> 
>
> @mapper或者@repository注解的区别
>
> 1. 使用@mapper后，不需要在spring配置中设置扫描地址,会自动，spring将动态的生成Bean后注入到ServiceImpl中。
> 2. @repository则需要在Spring中配置扫描包地址，然后生成dao层的bean，之后被注入到ServiceImpl中
>
> @Mapper和 @Mapperscan的区别
>
> 如果想要每个接口都要变成实现类，那么需要在每个接口类上加上@Mapper注解，比较麻烦，解决这个问题用@MapperScan
>
> @MapperScan 指定要变成实现类的接口所在的包，然后包下面的所有接口在编译之后都会生成相应的实现类。



此处不使用`application.properties` 而选用更简洁的 `application.yml`文件。新建application.yml配置文件

在 `application.yml` 中配置mysql的基本连接参数

```yaml
#datasource
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/mp?serverTimezone=UTC
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
```

> TIP
>
> 1. SpringBoot版本低于2.4.3(不含)，Mysql驱动版本大于8.0时，需要在url连接串中配置时区：serverTimezone=UTC
> 2. 驱动类过时，提醒更换为 `com.mysql.cj.jdbc.Driver`
> 3. 小坑： `username` 千万写对了，我写成了 `name` 报错：`Caused by: java.sql.SQLException: Access denied for user 'xxxx'@'localhost' (using password: YES)`



测试类 

```java
@RunWith(SpringRunner.class) // springboot2里可以省略不写
@SpringBootTest // 这里其包名和引导类`SpringbootMybatisApplication` 包名一致(com.bingo.learn),可以省略classes的指定，
class SpringbootMybatisApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserXmlMapper userXmlMapper;
    @Test
    void testFindAll() {
        List<User> userList = userMapper.findAll();
        for (User user : userList) {
            System.out.println(user);
        }
    }
}
```

执行测试方法结果

```
13:21:47,894 DEBUG findAll:137 - ==>  Preparing: select * from mp_user
13:21:47,920 DEBUG findAll:137 - ==> Parameters: 
13:21:47,945 DEBUG findAll:137 - <==      Total: 5
13:21:47,949 DEBUG SqlSessionUtils:49 - Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@5ec46cdd]
User(id=1, userName=null, name=张三, age=25, email=zhangsan@163.com)
User(id=2, userName=null, name=李四, age=20, email=lisi@163.com)
User(id=3, userName=null, name=王五, age=28, email=wangwu@163.com)
User(id=4, userName=null, name=赵六, age=21, email=zhaoliu@163.com)
User(id=5, userName=null, name=孙七, age=24, email=sunqi@163.com)
```

测试ok

#### （2）xml配置的方式

`UserXmlMapper`

```java
@Mapper
public interface UserXmlMapper {
    List<User> findAll();
}
```

配置 `UserMapper.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.bingo.learn.mapper.UserXmlMapper">
    <select id="findAll" resultType="user">
        select * from mp_user
    </select>
</mapper
```

在 `application.yml` 增加mybatis的相关配置

```yaml
#datasource
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/mp?serverTimezone=UTC
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver

#MyBatis
mybatis:
  mapper-locations: classpath:mapper/*Mapper.xml # mapper映射文件路径
  type-aliases-package: com.bingo.learn.domain # 指定起别名的实体类所在的包路径
#  config-location:  # 用于配置mybatis核心配置文件，这里未用到
```

测试类

```java
@RunWith(SpringRunner.class)
@SpringBootTest
class SpringbootMybatisApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserXmlMapper userXmlMapper;
    @Test
    void testFindAll() {
        List<User> userList1 = userXmlMapper.findAll();
        for (User user : userList1) {
            System.out.println(user);
        }
    }
}
```

执行测试结果和注解方式一样的

```
13:34:42,365 DEBUG findAll:137 - ==>  Preparing: select * from mp_user
13:34:42,392 DEBUG findAll:137 - ==> Parameters: 
13:34:42,417 DEBUG findAll:137 - <==      Total: 5
13:34:42,421 DEBUG SqlSessionUtils:49 - Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2f98635e]
User(id=1, userName=null, name=张三, age=25, email=zhangsan@163.com)
User(id=2, userName=null, name=李四, age=20, email=lisi@163.com)
User(id=3, userName=null, name=王五, age=28, email=wangwu@163.com)
User(id=4, userName=null, name=赵六, age=21, email=zhaoliu@163.com)
User(id=5, userName=null, name=孙七, age=24, email=sunqi@163.com)
```



### 3.整合Mybatis Plus

创建项目

.
├── main
│   ├── java
│   │   └── com
│   │       └── bingo
│   │           └── learn
│   │               └── SSMPApplication.java
│   └── resources
│       └── application.yml
└── test
    └── java
        └── com
            └── bingo
                └── learn
                    └── SSMPApplicationTests.java

#### （1）手动引入坐标

手动添加依赖坐标，可从 [mvnrepository](https://mvnrepository.com/artifact/com.baomidou/mybatis-plus-boot-starter) 获取

```xml
<dependency>
  <groupId>com.baomidou</groupId>
  <artifactId>mybatis-plus-boot-starter</artifactId>
  <version>3.4.3</version>
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
  </dependency>
</dependency>
```

> TIP
>
> 1. 由于SpringBoot中未收录MyBatis-Plus的坐标版本，需要指定对应的Version
> 1. 为了简化pojo的代码，引入了lombok

#### （2）定义实体类 User 

```java
@Data
public class User {
    private Long id;
    private String userName;
    private String name;
    private Integer age;
    private String email;
  	private String password;
}
```

#### （3）定义数据层接口与映射配置,集成BaseMapper

`UserMapper`

```java
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
```



#### （4）application.yml

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/mp?serverTimezong=UTC
    password: Bingoing
    username: root


mybatis-plus:
  global-config:
    db-config:
      table-prefix: mp_
```

> 由于数据表名字叫 mp_user ，这里 mybatis plus 根据实体类名映射的数据表名为 user ，如果不处理，会报错：`Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Table 'mp.user' doesn't exist`
>
> 解决办法：
>
> 1. 方法一：Application.yml 里设置 数据表名前缀 `table-prefix: mp_`
>
> 2. 方法二：实体类上映射表名：
>
>    ```java
>    @Data
>    @TableName("mp_user")
>    public class User {
>        private Long id;
>        private String userName;
>        private String name;
>        private Integer age;
>        private String email;
>        private String password;
>    }
>    ```
>
> 建议采用 方法一 ！

完成以上步骤，也就完成了基本的整合，测试结果

```tex
User(id=2, userName=lisi, name=李四, age=20, email=lisi@163.com)
```



### 4.ssmp整合项目案例

#### （1）案例实现方案分析

* 实体类开发————使用Lombok快速制作实体类
* Dao开发————整合MyBatisPlus，制作数据层测试类

- Service开发————基于MyBatisPlus进行增量开发，制作业务层测试类
- Controller开发————基于Restful开发，使用PostMan测试接口功能
- Controller开发————前后端开发协议制作
- 页面开发————基于VUE+ElementUI制作，前后端联调，页面数据处理，页面消息处理列表、新增、修改、删除、分页、查询 
- 项目异常处理
- 按条件查询————页面功能调整、Controller修正功能、Service修正功能

#### （2）创建项目

Spring Initializr 方式创建项目，勾选 `Spring Web` 、`MySQL Driver`、`Lombok` 依赖坐标。

创建后，pom.xml 里手动添加 mybatis plus 和 druid 的依赖

```xml
<dependency>
  <groupId>com.baomidou</groupId>
  <artifactId>mybatis-plus-boot-starter</artifactId>
  <version>3.4.3</version>
</dependency>
<dependency>
  <groupId>com.alibaba</groupId>
  <artifactId>druid-spring-boot-starter</artifactId>
  <version>1.2.8</version>
</dependency>
```

> TIP
>
> ！！不要引用错了包，我是 druid-spring-boot-starter 引成了 druid ,后续报错。。。错误找了半天！！

然后修改配置文件为.yml 格式，另外为了便于测试，在配置文件中将端口改为80

```yaml
server:
  port: 80
```

至此，项目模块创建成功。



#### （3）数据层开发

##### ① 实体类 

```java
@Data
public class User {
    private Long id;
    private String userName;
    private String name;
    private Integer age;
    private String email;
    private String password;
}
```

##### ② Dao层

首先补齐必要配置

> * Druid 数据源配置
>
> * Mybatis Plus 数据表前缀配置
>
> * 为了方便调试，开启 MP 的日志:`log-impl: org.apache.ibatis.logging.stdout.StdOutImpl` 

```yaml
server:
  port: 80
  
spring:
  datasource:
    druid:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/mp?serverTimezone=UTC
      username: root
      password: Bingoing
      
mybatis-plus:
  global-config:
    db-config:
      table-prefix: mp_
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    
```



编写数据层代码

`UserMapper`

```java
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
```

测试类 `SSMPApplicationTests`

```java
package com.bingo.learn.dao;

@SpringBootTest
class UserDaoTestCase {
    @Autowired
    private UserMapper userMapper;
    @Test
    void testSave() {
        User user = new User();
        user.setUserName("阿香");
        user.setName("孙尚香");
        user.setEmail("axiang@163.com");
        user.setAge(28);
        user.setPassword("123321");
        userMapper.insert(user);
    }
    @Test
    void testGetById(){
        User user = userMapper.selectById(3L);
        System.out.println(user);
    }
  ...
  ...
}
```

测试插入结果

![image-20211217143634334](https://tva1.sinaimg.cn/large/008i3skNly1gxgtlfz2n7j30v201wt8z.jpg)



数据是插入成功了，但是有个问题，就是id 并没有自增也就是变成 6 ，而是一个超大的数字，原因是 MP 有它自己的默认id 生成策略，用的是雪花算法（不做详述，主要我也不懂），这里我们需要配置 `id-type` 修改为 自增模式 `auto` 。

```yaml
mybatis-plus:
  global-config:
    db-config:
      table-prefix: mp_
      id-type: auto
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

> TIP
>
> 小提示：再次测试时，如果不想在刚才的id上累加，那么删除刚才添加的记录,然后设置表的自增初始值
>
> ```sql
> alter table mp_user AUTO_INCREMENT=6;
> ```
>
> 

测试插入结果

```tex
==>  Preparing: INSERT INTO mp_user ( user_name, name, age, email, password ) VALUES ( ?, ?, ?, ?, ? )
==> Parameters: 阿香(String), 孙尚香(String), 28(Integer), axiang@163.com(String), 123321(String)
<==    Updates: 1
```

表数据

```
mysql> select * from mp_user;
+----+--------------+----------+-----------+------+------------------+
| id | user_name    | password | name      | age  | email            |
+----+--------------+----------+-----------+------+------------------+
|  1 | 法外狂徒     | 889999   | 张三      |   25 | zhangsan@163.com |
|  2 | lisi         | 123456   | 李四      |   20 | lisi@163.com     |
|  3 | wangwu       | 123456   | 王五      |   28 | wangwu@163.com   |
|  4 | zhaoliu      | 123456   | 赵六      |   21 | zhaoliu@163.com  |
|  5 | sunqi        | 123456   | 孙七      |   24 | sunqi@163.com    |
|  6 | 阿香         | 123321   | 孙尚香    |   28 | axiang@163.com   |
+----+--------------+----------+-----------+------+------------------+
```



测试查询结果

```tex

==>  Preparing: SELECT id,user_name,name,age,email,password FROM mp_user WHERE id=?
==> Parameters: 3(Long)
<==    Columns: id, user_name, name, age, email, password
<==        Row: 3, wangwu, 王五, 28, wangwu@163.com, 123456
<==      Total: 1
Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@6d08b4e6]
User(id=3, userName=wangwu, name=王五, age=28, email=wangwu@163.com, password=123456)
2021-12-17 12:32:08.381  INFO 6518 --- [ionShutdownHook] com.alibaba.druid.pool.DruidDataSource   : {dataSource-1} closing ...
2021-12-17 12:32:08.400  INFO 6518 --- [ionShutdownHook] com.alibaba.druid.pool.DruidDataSource   : {dataSource-1} closed
```



##### ③ 分页数据查询

分页操作是在MyBatisPlus的常规操作基础上增强得到，内部是动态的拼写SQL语句，因此需要增强对应的功能， 使用MyBatisPlus拦截器实现

使用时需要自行将 分页拦截器 添加到 MP 的拦截器中，才会生效



定义拦截器

```java
@Configuration
public class MPConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
      	// 定义MP拦截器
        MybatisPlusInterceptor mpInterceptor = new MybatisPlusInterceptor();
      	// 添加具体的拦截器
        mpInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mpInterceptor;
    }
}
```



测试分页查询

```java
@Test
void testGetPage() {
  IPage<User> page = new Page<>(1,3);
  userMapper.selectPage(page, null);
  System.out.println("pages: "+page.getPages()); // 最大页码值
  System.out.println("current: "+page.getCurrent());  // 当前页码值
  System.out.println("records: "+page.getRecords()); // 当前页数据记录
  System.out.println("size: "+page.getSize());   // 每页最大数据条数
  System.out.println("total: "+page.getTotal()); // 数据总条数
}
```



执行结果

```tex
==>  Preparing: SELECT COUNT(*) FROM mp_user
==> Parameters: 
<==    Columns: COUNT(*)
<==        Row: 6
<==      Total: 1
==>  Preparing: SELECT id,user_name,name,age,email,password FROM mp_user LIMIT ?
==> Parameters: 3(Long)
<==    Columns: id, user_name, name, age, email, password
<==        Row: 1, 法外狂徒, 张三, 25, zhangsan@163.com, 889999
<==        Row: 2, lisi, 李四, 20, lisi@163.com, 123456
<==        Row: 3, wangwu, 王五, 28, wangwu@163.com, 123456
<==      Total: 3
Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@493b01ef]
pages: 2
current: 1
records: [User(id=1, userName=法外狂徒, name=张三, age=25, email=zhangsan@163.com, password=889999), User(id=2, userName=lisi, name=李四, age=20, email=lisi@163.com, password=123456), User(id=3, userName=wangwu, name=王五, age=28, email=wangwu@163.com, password=123456)]
size: 3
total: 6
```

补充：动态的查询条件

```java
@Test
public void testGetByCondition(){
  // String name = "";
  // String name = null;
  String name = "王五";
  IPage<User> page = new Page<>(1, 9);
  LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
  lqw.like(Strings.isNotEmpty(name),User::getName,name);
  userMapper.selectPage(page, lqw);
  System.out.println(page.getRecords());
}
```



#### （4）业务层开发

`IUserService`

```java
package com.bingo.learn.service;

public interface IUserService {
    Boolean save(User user);
    Boolean update(User user);
    Boolean delete(Integer id);
    User getById(Integer id);
    List<User> getAll();
    IPage<User> getPage(int currentPage,int pageSize);
}

```

`UserServiceImpl`

```java
package com.bingo.learn.service.impl;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public Boolean save(User user) {
       return userMapper.insert(user)>0;
    }

    @Override
    public Boolean update(User user) {
        return userMapper.updateById(user)>0;
    }

    @Override
    public Boolean delete(Integer id) {
        return userMapper.deleteById(id)>0;
    }

    @Override
    public User getById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> getAll() {
        return userMapper.selectList(null);
    }

    @Override
    public IPage<User> getPage(int currentPage, int pageSize) {
        IPage<User> page= new Page<>(currentPage,pageSize);
        userMapper.selectPage(page,null);
        return page;
    }
}

```

测试

```java
package com.bingo.learn.service;

@SpringBootTest
public class UserServiceTestCase {
    @Autowired
    private IUserService userService;

    @Test
    public void testGetById() {
        User user = userService.getById(2);
        System.out.println(user);
    }
    
    @Test
    public void testGetPage() {
        IPage<User> page = userService.getPage(2, 3);
        System.out.println(page.getRecords());
    }
  
  	...
    ...
    
}
```

执行结果

```tex
==>  Preparing: SELECT id,user_name,name,age,email,password FROM mp_user WHERE id=?
==> Parameters: 2(Integer)
<==    Columns: id, user_name, name, age, email, password
<==        Row: 2, lisi, 李四, 20, lisi@163.com, 123456
<==      Total: 1
```



#### （5）业务层快速开发

快速开发方案
 * 使用MyBatisPlus提供有业务层通用接口(ISerivce<T>)与业务层通用实现类(ServiceImpl<M,T>) 

* 当MP基于上面两个接口定义的通用功能不能满足的你需求时，就需要你去做功能重载或功能追加

> TIP
>
> 注意重载时不要覆盖原始操作，避免原始提供的功能丢失



开始改造

`IUserService`

```java
package com.bingo.learn.service;

public interface IUserService extends IService<User> {
    User getOneUser(Integer id);
}
```

> 自己定义了一个接口getOneUser，是MP里没有提供的,所以你还需要手动实现。



`UserServiceImpl`

```java
package com.bingo.learn.service.impl;
@Service
public class UserServiceImpl
  extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User getOneUser(Integer id) {
        return userMapper.selectById(id);
    }
}
```

然后就可以测试了

```java
@SpringBootTest
public class UserServiceTestCase {
    @Autowired
    private IUserService userService;

    @Test
    public void testGetById() {
        User user = userService.getById(2);
        System.out.println(user);
    }
    @Test
    public void testGetPage() {
        IPage<User> page = new Page<>(2,3);
        userService.page(page);
        System.out.println(page.getRecords());
    }
    @Test
  	public void testGetOneUser(){
    	User user = userService.getOneUser(2);
    	System.out.println(user);
  	}
}
```

测试效果和之前的写法是一致的。





#### （6）表现层开发

* 基于Restful进行表现层接口开发 
* 使用Postman测试表现层接口功能



##### ① Controller层

* 基于Restful制作表现层接口

  	 * 新增:POST

      	 *  删除:DELETE 

  * 修改:PUT

  * 查询:GET

* 接收参数

  	* 实体数据:@RequestBody
  	
  	* 路径变量:@PathVariable

`UserController`

```java
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping
    public List<User> getAll() {
        return userService.list();
    }
    @PostMapping
    public Boolean save(@RequestBody User user){
        return userService.save(user);
    }
    @PutMapping
    public Boolean update(@RequestBody User user){
        UpdateWrapper<User> updateWrapper =new UpdateWrapper<>();
        updateWrapper.setEntity(user);
        return userService.update(user,updateWrapper);
    }
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id){
        return userService.removeById(id);
    }
    @GetMapping("/{id}")
    public User getById(@PathVariable Integer id){
        return userService.getById(id);
    }
    @GetMapping("/{currentPage}/{pageSize}")
    public IPage<User> getPage(@PathVariable int currentPage,@PathVariable int pageSize){
        IPage<User> page =  new Page<>(currentPage,pageSize);
        return userService.page(page,null );
    }
}
```

> @RequestBody 
>
> @PathVariable





配合 PostMan 来进行接口测试





保存

![image-20211217175809700](/Users/ing/Library/Application Support/typora-user-images/image-20211217175809700.png)



查询全部

![image-20211217175845918](https://tva1.sinaimg.cn/large/008i3skNly1gxgzfu2vjtj31ki0au0to.jpg)





更新

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gxgzgbsbokj31ki0em764.jpg" alt="image-20211217175909968" style="zoom:50%;" />





分页查询

![image-20211217180012649](https://tva1.sinaimg.cn/large/008i3skNly1gxgzhculz4j31km0asq3w.jpg)



如果你测试了一遍的话，会发现，返回的数据格式五花八门，如果真的是生产环境这么搞，估计前端妹子能梨花带雨的来找你。

##### ② 表现层消息一致性处理

一般返回的数据格式会包括4个部分

* 请求处理是否成功
* 服务处理结果编码
* 编码对应的文本信息
* 返回值

```json
{
  "result": true,
   "code": 1,
   "msg": "SUCCESS",
   "data": {}
}
```



```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R {
    private Boolean result;
    private T data;
    private String msg;
    public R(Boolean success) {
        this.success = success;
    }

    public R(Boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public R(Boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }
}
```

> TIP
>
> * success:标识操作是否成功
> * data:操作返回数据
> * msg:操作返回信息

```java
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping
    public R getAll() {
        return new R(true, userService.list());
    }

    @PostMapping
    public R save(@RequestBody User user) {
        return new R(userService.save(user),"保存成功");
    }

    @PutMapping
    public R update(@RequestBody User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setEntity(user);
        return new R(userService.update(user, updateWrapper),"更新成功");
    }

    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        return new R(userService.removeById(id),"删除成功");
    }

    @GetMapping("/{id}")
    public R getById(@PathVariable Integer id) {
        return new R(true, userService.getById(id),"查询成功");
    }

    @GetMapping("/{currentPage}/{pageSize}")
    public R getPage(@PathVariable int currentPage, @PathVariable int pageSize) {
        IPage<User> page = new Page<>(currentPage, pageSize);
        return new R(true, userService.page(page, null),"查询成功");
    }
}

```



#### （7）异常处理

##### ① 全局异常处理

```java
@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public R doException(Exception ex){
        ex.printStackTrace();
        return new R(false,"服务器异常，请稍后重试");
    }
}
```

> TIP
>
> * @RestControllerAdvice：对 Controller 进行增强的，可以全局捕获Spring MVC抛的异常。
> * @ExceptionHandler：指定的要捕获的异常类型。 
> * 如果Dao 和 Service 层没有做try catch ，异常会自动抛到Controller表现层























# 二、高级部分

## （一）自动配置

### 1.**Condition**

@Condition 是在Spring 4.0 增加的条件判断功能，通过这个可以功能可以实现选择性的创建 Bean 操作。



假设我们有一个需求

> 在 Spring 的 IOC 容器中有一个 User 的 Bean，现要求:
>
> 1. 项目中导入 Druid 坐标后，加载该Bean，没导入，则不加载。
> 2. 将类的判断定义为动态的。判断哪个字节码文件存在可以动态指定。

通过 Sprig Initializr 创建springboot项目

项目主要结构，这里只展示用到的部分 java 目录

└── com
    └── bingo
        └── learn
            ├── SpringbootConditionApplication.java
            ├── config
            │   └── UserConfig.java
            └── domain
                └── User.java



pom.xml

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.2.8</version>
  </dependency>
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
  </dependency>

</dependencies>
```



`User`

```java
@Data
@NoArgsConstructor
public class User {
    private String name;
    private Integer age;
}
```

定义一个配置类 `UserConfig`

```java
@Configuration
public class UserConfig {
    @Bean
    public User user(){
        return new User();
    }
}
```

> @configuration 注解 详解 -- 待补充



#### （1）实现条件判断



如何实现条件判断呢，很简单，只需要在加@Bean注解的方法上,就就是产生Bean对象的方法上加一个注解 `@Conditional` 指定是否生成Bean所需要的条件。

`@Conditional` 源码

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Conditional {
    Class<? extends Condition>[] value();
}
-------------------------------------
  
@FunctionalInterface
public interface Condition {
    boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata);
}
```

可以看到其参数是 `Condition` 接口或者其实现类的Class集合,也就是这里能接收多个Class。

核心方法就是 `matches` 其返回值是boolean , 如果返回true ，则创建该bean ,反之，不创建。



首先，我们先定义一个类实现 Condition 接口

```java
public class ClassCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return false;
    }
}
```

然后在 UserConfig 中使用

```java
@Configuration
public class UserConfig {
    @Bean
    @Conditional(ClassCondition.class)
    public User user(){
        return new User("法外狂徒",20);
    }
}
```

因为我们在 `ClassCondition` 中没有做任何处理，返回值为false ，所以此时， User 并不会被创建。

测试方法

```java
@SpringBootApplication
public class SpringbootConditionApplication {

    public static void main(String[] args) {
        // 启动SpringBoot 的应用，返回Spring 的IOC容器上下文
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootConditionApplication.class, args);
        Object user = context.getBean("user");
        System.out.println("user: "+user);
    }
}
```

运行报错 `Exception in thread "main" org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'user' available`

很明显，User的bean并没有被创建。

而如果是 `matches` 返回true

```java
public class ClassCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return true;
    }
}
```

测试返回了生成的Bean

```
user: User(name=法外狂徒, age=20)
```

理解了其用法，接下来我们来实现需求：根据是否引入了 `druid` 来决定是否生成该Bean ,也就是我们需要重写 `matches` 

那么根据什么来判断是否加入了 `druid`的坐标了呢？我的办法就是通过反射能否拿到 `com.alibaba.druid.pool.DruidDataSource`  的字节码，拿得到就说明已经引入了该依赖坐标，最终 `ClassCondition` 的实现如下

```java
public class ClassCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        try {
            Class<?> cls = Class.forName("com.alibaba.druid.pool.DruidDataSource");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
```

前面已引入了 druid 的坐标了，这里直接运行测试，正确结果应该是能打印出 user 对象

```
user: User(name=法外狂徒, age=20)

Process finished with exit code 0
```

接着，来测试下，不引入 druid 的坐标依赖的情况，pom.xml 中将其依赖注释掉，记得maven需要重新同步下，然后再运行测试，结果报错

`Exception in thread "main" org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'user' available`

说明我们的条件判断是有效的，因为了没了 druid 的依赖，所以并不会创建 User 的 Bean 。



上面的方式是将判断条件给写死了，如何将类的判断改为动态的呢，接下来我们改造。

#### （2）实现判断条件的动态指定

首先我们自定义一个注解，实现与 Spring 的 `@Conditional` 相同的功能，所以仿照一下写法，先加入元注解，然后再加上 `@Conditional` 这样就基本用了和 `@Conditional`的功能。

只不过这里区别在于属性值，我们想实现的是 类似 `@ConditionOnClass({"com.alibaba.druid.pool.DruidDataSource","xxx.xxx.xxx"})` 的，传入的是类名的字符串数组，而不是传入的Class数组 。所以最终效果如下

```java
// 元注解
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
// 拥有了Conditional注解的功能
@Conditional(ClassCondition.class)
public @interface ConditionOnClass {
  String[] value();
}
```

接下来就是要改造 ClassCondition 的判断条件了,依旧是要在 matches 上做文章。

```java
public class ClassCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        
        return false;
    }
}
```

这里有必要认识下 matches 方法上的两个参数了

> 1. `context`：条件上下文，`ConditionContext`接口类型的，可以用来获取容器中上下文信息。比如环境，ioc容器，classLoader等。
>
>    ```java
>    public interface ConditionContext {
>        BeanDefinitionRegistry getRegistry();
>        @Nullable
>        ConfigurableListableBeanFactory getBeanFactory();
>        Environment getEnvironment();
>        ResourceLoader getResourceLoader();
>        @Nullable
>        ClassLoader getClassLoader();
>    }
>    ```
>
>    
>
> 2. `metadata`：用来获取注解定义的属性值。当然这里我们主要获取被 `@Conditional` 注解的

比如我们想获取自定义的 `@ConditionOnClass` 注解的信息

```java
public class ClassCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> map = metadata.getAnnotationAttributes(ConditionOnClass.class.getName());
        System.out.println(map);
        return true;
    }
}
```

使用我们自定义的注解,为了看效果，又添加了一个 Jedis 里的类名。

```java
@Configuration
public class UserConfig {
    @Bean
    // @Conditional(ClassCondition.class)
    @ConditionOnClass({"com.alibaba.druid.pool.DruidDataSource","redis.clients.jedis.Jedis"})
    public User user(){
        return new User("法外狂徒",20);
    }
}
```

执行结果

```
map: {value=[com.alibaba.druid.pool.DruidDataSource, redis.clients.jedis.Jedis]}
2021-12-15 13:13:45.045  INFO 7401 --- [           main] c.b.l.SpringbootConditionApplication     : Started SpringbootConditionApplication in 0.564 seconds (JVM running for 1.141)
user: User(name=法外狂徒, age=20)
```

可以看到，map 打印结果: key 是我们注解中定义属性名 value ，而key对应的值就是属性值所组成的数组，那么获取 value 就变得清晰了

```java
String[] value = (String[]) map.get("value");
```

然后加上判断条件，也就是如果这些类都能被加载到也就说明，对应的坐标也都被引入了。

pom.xml 引入两个类对应的坐标

```xml
<dependencies>
  ...其他依赖省略...
  <dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.2.8</version>
  </dependency>
  <dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
  </dependency>
<dependencies>

```

`ClassCondition`

```java
public class ClassCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // try {
        //     Class<?> cls = Class.forName("com.alibaba.druid.pool.DruidDataSource");
        // } catch (ClassNotFoundException e) {
        //     e.printStackTrace();
        //     return false;
        // }
        
        Map<String, Object> map = metadata.getAnnotationAttributes(ConditionOnClass.class.getName());
      	// 获取注解属性值
        String[] value = (String[]) map.get("value");

        try {
            for (String s : value) {
                Class<?> cls = Class.forName(s);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
```

运行程序，执行结果

```
user: User(name=法外狂徒, age=20)
```

接着，在pom.xml 注释掉两个或者一个依赖，再次测试，结果报错

`Exception in thread "main" org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'user' available`

符合条件判断。至此，判断条件的动态指定的简单实现就完成了。你学会了吗？



#### （3）小结

##### ① 自定义条件:

> 1. 定义条件类:自定义类实现Condition接口，重写 matches 方法，在 matches 方法中进行逻辑判断，返回boolean值 。 matches 方法两个参数:
>
>    • context:上下文对象，可以获取属性值，获取类加载器，获取BeanFactory等。
>
>    • metadata:元数据对象，用于获取注解属性。
>
> 2. 判断条件: 在初始化Bean时，使用 @Conditional(条件类.**class**)注解

##### ② Spring提供的常见条件注解

这些都是Spring为我们准备好的，直接拿来用即可。

> @ConditionOnClass 某个class位于类路径上，才会实例化这个Bean。也就是我们前面自定义实现的注解。
>
> @ConditionOnBean 仅在当前上下文中存在某个bean时，才会实例化这个Bean。
> @ConditionOnWebApplication 当前项目是Web项目的条件
> @ConditionOnExpression 基于SpEL表达式结果为true的时候，才会实例化这个Bean
> @ConditionOnJndi 在JNDI存在时查找指定的位置
> @ConditionMissingBean 仅在当前IOC容器中不存在某个bean时，才会实例化这个Bean
> @ConditionMissingClass 当SpringIoc容器内不存在指定的Class时才会实例化该Bean
>
> @ConditionOnNotWebApplication 不是web项目时才实例化该Bean
> @ConditionOnProperty 指定的属性是否有指定的值,自动注入属性文件
> @ConditionOnResource 类路径是否有指定的值
> @ConditionOnSingleCandidate 当指定Bean在IOC容器内只有一个，或者虽然有多个但时指定首选的Bean
>
> @AutoConfigureAfter，在某个bean完成自动配置后实例化这个bean。
>
> @AutoConfigureBefore，在某个bean完成自动配置前实例化这个bean。



### 2.@Enable* 

SpringBoot中提供了很多Enable开头的注解，这些注解都是用于动态启用某些功能的。而其底层原理是使用@Import注解导入一些配置类，实现Bean的动态加载。



思考：SpringBoot 工程是否可以直接获取jar包中定义的Bean?



验证

创建两个项目，一个是springboot_enable（简称 enable）  , 一个springboot_enable_other (简称 other)

other项目提供Bean ，然后 enable 中尝试加载，看是否可以。



<img src="https://tva1.sinaimg.cn/large/008i3skNly1gxeib8fddxj30kh0hlabd.jpg" alt="image-20211215143211190" style="zoom: 50%;" />





<img src="https://tva1.sinaimg.cn/large/008i3skNly1gxeiba8t5uj30kh0hl3zn.jpg" alt="image-20211215143445426" style="zoom:50%;" />



然后直接一路next 即可。



其中 other 项目结构

└── com
    └── bingo
        └── other
            ├── SpringbootEnableOtherApplication.java
            ├── config
            │   └── UserConfig.java
            └── domain
                └── User.java



`User`

```java
public class User {
}
```

`UserConfig`

```java
@Configuration
public class UserConfig {
    @Bean
    public User user() {
        return new User();
    }
}
```

这样，other 的任务就完成了，往spring ioc容器中注入了一个Bean , 接着我们就验证能否在 enable 引入 other 项目后可以加载该Bean



enable 的 pom.xml

```xml
 <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
   			<!--引入other 项目作为依赖-->
        <dependency>
            <groupId>com.bingo.other</groupId>
            <artifactId>springboot_enable_other</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>

```

接着尝试在enable中的 `SpringbootEnableApplication` 中获取 User 的Bean对象

```java
@SpringBootApplication
public class SpringbootEnableApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootEnableApplication.class, args);
        Object user = context.getBean("user");
        System.out.println(user);
    }
}
```

运行结果

`Exception in thread "main" org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'user' available`

所以上面问题的答案是：不行。

原因在于 @SpringBootApplication 中的 @ComponentScan 的扫描范围



![image-20211215150448268](https://tva1.sinaimg.cn/large/008i3skNly1gxej694egyj30q905i755.jpg)

其扫描范围： 当前引导类（`SpringbootEnableApplication`）所在包以及子包下才可以。

明显 `UserConfig` 所在的包为 com.bingo.other ， 而 SpringbootEnableApplication 所在的包为 com.bingo.enable，既不相同也不包含，所以是扫描不到的。

也就无法加载SpringIOC容器中。

> @ComponentScan 源码部分
>
> ```java
> public @interface ComponentScan {
> 
> 	/**
> 	 * Alias for {@link #basePackages}.
> 	 * <p>Allows for more concise annotation declarations if no other attributes
> 	 * are needed &mdash; for example, {@code @ComponentScan("org.my.pkg")}
> 	 * instead of {@code @ComponentScan(basePackages = "org.my.pkg")}.
> 	 */
> 	@AliasFor("basePackages")
> 	String[] value() default {}; 
>   
>   
> 	/**
> 	 * Controls the class files eligible for component detection.
> 	 * <p>Consider use of {@link #includeFilters} and {@link #excludeFilters}
> 	 * for a more flexible approach.
> 	 */
> 	String resourcePattern() default ClassPathScanningCandidateComponentProvider.DEFAULT_RESOURCE_PATTERN;
> 
> ```
>
> 有两个关键的属性 value() 指定的是扫描的包路径 这里就是默认 引导类所在的包 `com.bingo.enable`
>
> 而 resourcePattern() 就是指定的扫描匹配规则 默认值是 DEFAULT_RESOURCE_PATTERN
>
> ```java
> static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
> ```
>
> 而 `**/*.class` 的意思就是匹配的配置类包下的所有class文件。



那么你会想，如果我刚才创建的 other 项目 UserConfig 所在包名也是 com.bingo.enable 是否就可以加载到了呢，答案是的，不信的 自行验证。



有没有什么办法能解决这个问题呢？



#### （1）@ComponentScan 指定包名

```java
@SpringBootApplication
@ComponentScan("com.bingo.other")
public class SpringbootEnableApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootEnableApplication.class, args);
        Object user = context.getBean("user");
        System.out.println(user);
    }
}
```

> TIP
>
> 弊端明显，每一个都需要单独指定，太麻烦



#### （2）@Import 注解

使用@Import导入的类会被Spring加载到IOC容器中，并且被创建。

```java
@SpringBootApplication
@Import(UserConfig.class)
public class SpringbootEnableApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootEnableApplication.class, args);
        Object user = context.getBean("user");
        System.out.println(user);
    }
}
```

#### （3）封装@Import 

这里所谓的封装@Import，其实意思是 让第三方框架提供者自己通过 @Import 提前将配置类导入，从而让SpringBoot识别，并直接使用。

首先，在other中定义一个注解 @EnableUser 其实实现的功能和 @Import的一样的，只不过指定了所需要加载Class。

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(UserConfig.class)
public @interface EnableUser {
}
```

用的时候，就变得非常简单

```java
@SpringBootApplication
// @ComponentScan("com.bingo.other")
// @Import(UserConfig.class)
@EnableUser
public class SpringbootEnableApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootEnableApplication.class, args);
        Object user = context.getBean("user");
        System.out.println(user);
    }
}
```

加上 `@EnableUser` 就相当于 开启了让你获得User对象的功能。



### 3.@Import 

@Enable*底层依赖于@Import注解导入一些类，使用@Import导入的类会被Spring加载到IOC容器中。

@Import提供四种获取Bean的实现方式:

> 1. 通过导入Bean直接获取
> 2. 通过导入配置类获取
> 3. 通过导入 `ImportSelector` 实现类。一般用于加载配置文件中的类。
> 4. 通过导入 `ImportBeanDefinitionRegistrar` 实现类。

我们使用上面的 enable 和 other 的案例，来分别说明下 @Import 的四种用法



#### （1）导入Bean 

```java
@SpringBootApplication
@Import(User.class)
public class SpringbootEnableApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootEnableApplication.class, args);

        // Object user = context.getBean("user");
        // System.out.println(user);
      	// 打印 User 在容器中的信息
        Map<String, User> map = context.getBeansOfType(User.class);
        System.out.println(map);
				
      	// 获取User 对象
        User user = context.getBean(User.class);
        System.out.println(user);
    }
}
```

执行结果

```java
{com.bingo.other.domain.User=com.bingo.other.domain.User@15dcfae7}
com.bingo.other.domain.User@15dcfae7
```

> TIP
>
> 不能使用 `context.getBean("user");` 来获取，通过结果可知， User 在IOC容器中的id 并不是 `user` 而是其全路径名：`com.bingo.other.domain.User`
>
> 当然你可以写成：`Object user = context.getBean("com.bingo.other.domain.User");` 



#### （2）导入配置类

```java
@SpringBootApplication
// @ComponentScan("com.bingo.other")
// @Import(UserConfig.class)
// @EnableUser
// @Import(User.class)
@Import(UserConfig.class)
public class SpringbootEnableApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootEnableApplication.class, args);

        User user = (User) context.getBean("user");
        System.out.println(user);
    }
}
```

很简单，和上面类似，就不做赘述了。



#### （3）导入 `ImportSelector` 实现类



`ImportSelector`

> ```java
> public interface ImportSelector {
> 
> 	String[] selectImports(AnnotationMetadata importingClassMetadata);
> 
> 	@Nullable
> 	default Predicate<String> getExclusionFilter() {
> 		return null;
> 	}
> }
> ```

首先，为了便于更好的测试，在 other 中另外创建一个 Role 类，并在配置类中配置 

`Role`

```java
public class Role {
}
```

`UserConfig`

```java
@Configuration
public class UserConfig {
    @Bean
    public User user() {
        return new User();
    }
    @Bean
    public Role role(){
        return new Role();
    }
}
```



enable 项目中创建一个该接口的实现类 `MyImportSelector` 

```java
public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[0];
    }
}
```

改造下，让其返回 User 和 Role 的全限定类名

```java
public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{"com.bingo.other.domain.Role","com.bingo.other.domain.User"};
    }
}
```

使用方法

```java
@SpringBootApplication
// @Import(User.class)
// @Import(UserConfig.class)
@Import(MyImportSelector.class)
public class SpringbootEnableApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootEnableApplication.class, args);
        User user = context.getBean(User.class);
        Role role = context.getBean(Role.class);
        System.out.println(user);
        System.out.println(role);
    }
}
```

运行测试结果如下

```
com.bingo.other.domain.User@1852a3ff
com.bingo.other.domain.Role@7203c7ff
```

这种方式的好处在于，由于`return new String[]{"com.bingo.other.domain.Role","com.bingo.other.domain.User"};` 里都是写类的全限定名的字符串，所以完全可以通过配置文件来配置提供。



#### （4）通过导入 `ImportBeanDefinitionRegistrar` 实现类

> ```java
> public interface ImportBeanDefinitionRegistrar {
> 
> 	default void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry,
> 			BeanNameGenerator importBeanNameGenerator) {
> 		registerBeanDefinitions(importingClassMetadata, registry);
> 	}
> 
> 	default void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
> 	}
> }
> 
> ```
>
> 



创建该接口实现类

```java
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AbstractBeanDefinition userDefinition = BeanDefinitionBuilder.rootBeanDefinition(User.class).getBeanDefinition();
        registry.registerBeanDefinition("user",userDefinition);
        AbstractBeanDefinition roleDefinition = BeanDefinitionBuilder.rootBeanDefinition(Role.class).getBeanDefinition();
        registry.registerBeanDefinition("Role",roleDefinition);
    }
}
```

使用测试

```java
@SpringBootApplication
// @Import(User.class)
// @Import(UserConfig.class)
// @Import(MyImportSelector.class)
@Import(MyImportBeanDefinitionRegistrar.class)
public class SpringbootEnableApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootEnableApplication.class, args);

        User user = context.getBean(User.class);
        Role role = context.getBean(Role.class);
        System.out.println(user);
        System.out.println(role);
    }
}
```

执行结果

```
com.bingo.other.domain.User@93cf163
com.bingo.other.domain.Role@1852a3ff
```

#### （5）小结

其实以上几种方式就是springboot 自动装配的实现方式

比如 我们看下 引导类 

```java
@SpringBootApplication
public class SpringbootEnableApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootEnableApplication.class, args);
    }
}
```

他所使用的注解 @SpringBootApplication

![image-20211215172330199](https://tva1.sinaimg.cn/large/008i3skNly1gxen6i8hbfj30qf05g754.jpg)



![](https://tva1.sinaimg.cn/large/008i3skNly1gxen94sowbj30p9028wel.jpg)



![image-20211215172532391](https://tva1.sinaimg.cn/large/008i3skNly1gxen8mmrxuj310606lt9g.jpg)



其实就是使用的第三种方式实现的。



### 4.@EnableAutoConfiguration

#### （1）原理分析

```java
...
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {
	...
}
```

重点看 `AutoConfigurationImportSelector` 的 `selectImports`



```java
public class AutoConfigurationImportSelector implements DeferredImportSelector, BeanClassLoaderAware,
		ResourceLoaderAware, BeanFactoryAware, EnvironmentAware, Ordered {
	...
  ...
    
	@Override
	public String[] selectImports(AnnotationMetadata annotationMetadata) {
		if (!isEnabled(annotationMetadata)) {
			return NO_IMPORTS;
		}
		AutoConfigurationEntry autoConfigurationEntry = getAutoConfigurationEntry(annotationMetadata);
		return StringUtils.toStringArray(autoConfigurationEntry.getConfigurations());
	}
  ...
  
}
```

上面的代码和我们第三种方式一样，返回的是字符串的全限类名，那么这些类名是怎么获取的呢？

然后定位到 `getAutoConfigurationEntry` 可以看到 configurations 来自 `getCandidateConfigurations`

```java
  
   protected AutoConfigurationEntry getAutoConfigurationEntry(AnnotationMetadata annotationMetadata) {
		if (!isEnabled(annotationMetadata)) {
			return EMPTY_ENTRY;
		}
		AnnotationAttributes attributes = getAttributes(annotationMetadata);
		List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
		configurations = removeDuplicates(configurations);
		Set<String> exclusions = getExclusions(annotationMetadata, attributes);
		checkExcludedClasses(configurations, exclusions);
		configurations.removeAll(exclusions);
		configurations = getConfigurationClassFilter().filter(configurations);
		fireAutoConfigurationImportEvents(configurations, exclusions);
		return new AutoConfigurationEntry(configurations, exclusions);
	}
```

定位到 `getCandidateConfigurations`

```java
protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
		List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),
				getBeanClassLoader());
		Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you "
				+ "are using a custom packaging, make sure that file is correct.");
		return configurations;
	}
```

继续进到 `loadFactoryNames`

```java
public static List<String> loadFactoryNames(Class<?> factoryType, @Nullable ClassLoader classLoader) {
        ClassLoader classLoaderToUse = classLoader;
        if (classLoader == null) {
            classLoaderToUse = SpringFactoriesLoader.class.getClassLoader();
        }

        String factoryTypeName = factoryType.getName();
        return (List)loadSpringFactories(classLoaderToUse).getOrDefault(factoryTypeName, Collections.emptyList());
    }

    private static Map<String, List<String>> loadSpringFactories(ClassLoader classLoader) {
        Map<String, List<String>> result = (Map)cache.get(classLoader);
        if (result != null) {
            return result;
        } else {
            HashMap result = new HashMap();

            try {
                Enumeration urls = classLoader.getResources("META-INF/spring.factories");

                while(urls.hasMoreElements()) {
                    URL url = (URL)urls.nextElement();
                    UrlResource resource = new UrlResource(url);
                    Properties properties = PropertiesLoaderUtils.loadProperties(resource);
                    Iterator var6 = properties.entrySet().iterator();

                   ....
                }

               ...
            } catch (IOException var14) {
                throw new IllegalArgumentException("Unable to load factories from location [META-INF/spring.factories]", var14);
            }
        }
    }
```

最终我们发现了一个文件名：

```java
Enumeration urls = classLoader.getResources("META-INF/spring.factories");
...
 while(urls.hasMoreElements()) {
   ...
   UrlResource resource = new UrlResource(url);
   Properties properties = PropertiesLoaderUtils.loadProperties(resource);
   ....
 }
```

也就是关键就在 `META-INF/spring.factories` 也就是我们之前提到的通过配置文件的方式指定需要加载的类，而这个就是 SpringBoot 常用的方式，通过读取第三方框架中的该配置文件来自动加载其类，完成自动装配功能

> 可以在项目中的 External Libraries 里找一个带有 autoconfigure的包，比如我这里有一个
>
> ![image-20211215183147152](https://tva1.sinaimg.cn/large/008i3skNly1gxep5kp7h7j30n4065t99.jpg)
>
> 
>
> 点开 spring.factories
>
> ![image-20211215183351010](https://tva1.sinaimg.cn/large/008i3skNly1gxep7s4olgj30mh02iwew.jpg)
>
> 就可以看到可以被加载的配置类了。



#### （2）案例实现

原理已经分析清楚了，接下来我们根据原理来模拟实现下整个过程。

> 需求：自定义user-starter。要求当导入user坐标时，SpringBoot自动创建User的Bean。

在空项目中创建两个模块 `user-spring-boot-starter` 和 `user-spring-boot-autoconfigure` ,后面简称 stater 和 config

其中 config 只负责提供bean：提供User的bean。

而 stater 就是当做第三方依赖 提供给springboot 使用的。



创建后，删除掉无用的目录和文件，最终目录结构如下



`config` 项目



├── pom.xml
├── redis-spring-boot-autoconfigure.iml
└── src
    └── main
        ├── java
        │   └── com
        │       └── bingo
        │           └── learn
        │               └── config
        │                   ├── User.java
        │                   ├── UserAutoConfiguration.java
        │                   └── UserProperties.java
        └── resources
            ├── META-INF
            │   └── spring.factories
            └── application.properties



`pom.xml` 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.6.1</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.bingo.learn.config</groupId>
    <artifactId>user-spring-boot-autoconfigure</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>user-spring-boot-autoconfigure</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
      
    </dependencies>
</project>
```



`User`

```java
public class User {
    private String userName;
    private  Integer age;

    public User(String userName, Integer age) {
        this.userName = userName;
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", age=" + age +
                '}';
    }
}
```



`UserAutoConfiguration`

```java
@Configuration
public class UserAutoConfiguration {
    @Bean
    public User user(){
        return new User("法外狂徒",33);
    }
}
```

User 的 userName 和 age 目前是写死的，这当然不合适。这个应该是使用方也就是springboot项目来配置的， 而不是我们自己写死的。

所以合理的方式应该是 在springboot项目中配置该参数，也就是在application.properties类似的配置文件中配置。

我们负责预先写好加载方法即可，所以我们需要提供一个配置信息读取的类

```java
@ConfigurationProperties(prefix = "user")
public class UserProperties {
    private String name ;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
```



此时，UserProperties 其实还并不能作为bean，你或许想 那就加上一个 @Component 注解呗，但会存在一个之前说过的问题，当提供给 springboot 项目时，由于包名不一致，也是无法扫描到该bean的，所以一个好的办法就是在 UserAutoConfiguration 中@Import UserProperties,也就会自动被spring 容器扫描并加载创建。

```java
@Configuration
@EnableConfigurationProperties(UserProperties.class)
public class UserAutoConfiguration {
    @Bean
    public User user(UserProperties userProperties){
        String name = userProperties.getName()!=null? userProperties.getName():"法外狂徒";
        int age = userProperties.getAge()!=null?userProperties.getAge():30;
        return new User(name,age);
    }
}
```

> TIP
>
> 1. @EnableConfigurationProperties 底层本质上就是@Import ，通过@Import 引入JedisProperties 就会自动创建 bean 并放入容器中。这属于前面讲解的的内容
> 2. name,age提供默认值 ，当使用方不提供配置的话，就以默认的值，创建bean



上面代码部分基本已经完成，但是根据上面原理的分析，我们还需要完成一个配置才可以实现自动配置功能，也就是 `META-INF/spring.factories`  你需要通过该配置告诉springboot 哪些类可以自动配置。而springboot 在引入的时候也会去读取该配置文件，决定哪些类自动配置。

创建配置文件，在 resources 目录下创建 该配置文件



`spring.factoris`

```factoris
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.bingo.learn.config.UserAutoConfiguration
```

> TIP
>
> 1. `org.springframework.boot.autoconfigure.EnableAutoConfiguration` 固定写法
>
> 2. 等号后面的`\ `表示换行
> 3. 等号后面是你定义的配置类

这样就配置完成了，然后在 starter 里引入即可



`starter`



├── pom.xml
├── redis-spring-boot-starter.iml
└── src
    └── main
        ├── java
        │   └── com
        │       └── bingo
        │           └── learn
        │               └── config
        └── resources
            └── application.properties





`pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.6.1</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.bingo.learn.stater</groupId>
    <artifactId>user-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>user-spring-boot-starter</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
				
        <dependency>
            <groupId>com.bingo.learn.config</groupId>
            <artifactId>user-spring-boot-autoconfigure</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>

```

至此，我们的 User 的 依赖库就算写完了。下面就是提供给springboot项目来使用了。

这里我们新创建一个 springboot 项目 springboot_test_auto 

首先 pom.xml 中引入 starter 的依赖

```xml
 <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.bingo.learn.stater</groupId>
            <artifactId>redis-spring-boot-starter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
```

然后在引导类中看能否获取到User 

```java
@SpringBootApplication
public class SpringbootTestAutoApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootTestAutoApplication.class, args);
        User user = context.getBean(User.class);
        System.out.println(user);
    }
}
```

然后执行，控制台输出结果

```
User{userName='法外狂徒', age=30}

Process finished with exit code 0
```

可见，User 成功获取到了。只不过这里是获取的默认值，因为我们并没有重新配置，如何配置？很简单

`application.properties`

```properties
user.userName= bingo
user.age = 33
```



重新执行，控制台输出

```
User{userName='bingo', age=33}

Process finished with exit code 0
```

至此，基本的原理流程我们都已经复现出来了。

不过一般还会添加一些条件，比如 如果你已经有了 User bean 了，那么就使用你自己的。我们可以模拟下这种情况



```java
@Configuration
@EnableConfigurationProperties(UserProperties.class)
public class UserAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(name = "user")
    public User user(UserProperties userProperties){
        System.out.println("UserAutoConfiguration -- user");
        String name = userProperties.getUserName()!=null? userProperties.getUserName():"法外狂徒";
        int age = userProperties.getAge()!=null?userProperties.getAge():30;
        return new User(name,age);
    }
}
```

> @ConditionalOnMissingBean 注解的作用就是如果你已经有了名字叫 user 的bean 了 就不再加载创建。

同时在使用方 `SpringbootTestAutoApplication` 中添加创建一个相同名称的 bean

```java
@SpringBootApplication
public class SpringbootTestAutoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootTestAutoApplication.class, args);
        User user = context.getBean(User.class);
        System.out.println(user);
    }

    @Bean
    public User user() {
        return new User("wangwu", 22);
    }
}
```

> SpringbootTestAutoApplication 本身就是一个配置类，所以可以配置创建 bean 

此时，运行结果

```
User{userName='wangwu', age=22}

Process finished with exit code 0
```

可以发现，这里使用的就是自身创建的了，而不是 user-spring-boot-stater 提供的了。

