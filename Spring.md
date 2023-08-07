







 

！！！！图片记得修改一下

## 一. Spring 简介

### 1.1 概念

> Spring是分层的 Java SE/EE应用 full-stack 轻量级开源框架，以 IoC(Inverse Of Control:反转控制)和 AOP(Aspect Oriented Programming:面向切面编程)为内核。 
>
> 提供了展现层 SpringMVC 和持久层 Spring JDBCTemplate 以及业务层事务管理等众多的企业级应用技术 ，还能整合开源世界众多著名的第三方框架和类库，逐渐成为使用最多的Java EE 企业应用开源框架。 

###  1.2 优势

* 方便解耦，简化开发 

* 通过 Spring 提供的 IoC容器，可以将对象间的依赖关系交由 Spring 进行控制，避免硬编码所造成的过度耦合。 用户也不必再为单例模式类、属性文件解析等这些很底层的需求编写代码，可以更专注于上层的应用

* AOP 编程的支持

  通过 Spring的 AOP 功能，方便进行面向切面编程，许多不容易用传统 OOP 实现的功能可以通过 AOP 轻松实现

* 声明式事务的支持

  可以将我们从单调烦闷的事务管理代码中解脱出来，通过声明式方式灵活的进行事务管理，提高开发效率和质量。

* 方便程序的测试

  可以用非容器依赖的编程方式进行几乎所有的测试工作，测试不再是昂贵的操作，而是随手可做的事情。

* 方便集成各种优秀框架

  Spring对各种优秀框架(Struts、Hibernate、Hessian、Quartz等)的支持。

* 降低 JavaEE API 的使用难度

  Spring对 JavaEE API(如 JDBC、JavaMail、远程调用等)进行了薄薄的封装层，使这些 API 的使用难度大为降低。

* Java 源码是经典学习范例

  Spring的源代码设计精妙、结构清晰、匠心独用，处处体现着大师对Java 设计模式灵活运用以及对 Java技术的高深 造诣。它的源代码无意是 Java 技术的最佳实践的范例。

### 1.3 体系结构

![image-20211118180348643](https://tva1.sinaimg.cn/large/008i3skNgy1gwk936yghwj30r40jswga.jpg)

引言 

> 首先通过maven创建一个java项目,用来模拟web项目的持久层，业务层以及表现层的代码。旨在说明解耦的思路



项目主要目录（java层）

```
.
├── java
│   └── com
│       └── bingo
│           └── learn
│               ├── dao
│               │   ├── UserDao.java
│               │   └── impl
│               │       └── UserDaoImpl.java
│               ├── service
│               │   ├── UserService.java
│               │   └── impl
│               │       └── UserServiceImpl.java
│               └── ui
│                   └── Client.java
└── resources
```

相关代码

<span id="dao"> `Dao.java` </span>

```java
/**
 * 持久层接口
 */
public interface UserDao {
    /**
     * 模拟持久层保存操作
     */
    void save();
}

```



<span id="userDaoImpl">`UserDaoImpl`</span>

```java
/**
 * 持久层实现类
 */
public class UserDaoImpl implements UserDao {
    //模拟持久层操作
		@Override
    public void save() {
        System.out.println("保存用户成功");
    }
}
```

<span id="userService">`UserService`</span>

```java
/**
 * 业务层的实现
 */
public interface UserService {
    /**
     * 模拟保存
     */
    void save();
}
```

<span id="userServiceImpl">`UserServiceImpl`</span>

```java

/**
 * 模拟业务层实现类
 */
public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();
		@Override
    public void save() {
        userDao.save();
    }
}
```

<span id="client">`Client.java`</span>

```java
/**
 * 模拟一个表现层，用来调用业务层
 */
public class Client {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.save();
    }
}
```



运行后表现层的main方法

```
保存用户成功

Process finished with exit code 0
```

这样，一个简单的模拟web的java项目就算完成了。



存在的问题：

各个层之间，也就是类与类之间耦合度太高了，也就是依赖关系太紧密了。

业务层（UserServiceImpl）依赖持久层（UserDaoImpl），而表现层有依赖业务层（UserServiceImpl），如果把其中的某个文件删掉就会发现，在程序编译期就会报错。根本无法运行。

而什么是解耦呢，解耦的意思不是说让彼此直接彻底没了关系，而是尽量降低他们之间的依赖关系，即便依赖关系丢失。

而Spring第一个优势就是解耦，他是怎么实现的呢？答案就是底层采用的工厂设计模式。



接下来，就来实现一个工厂类，来看下如何解耦的

上面项目中新建一个类`factory/BeanFactory.java`，一个创建Bean对象的工厂

```java
/**
* 创建Bean对象的工厂
**/
public class BeanFactory {

}
```

你会好奇，`Bean`不就是实体类么？ Bean :在计算机英语中，可重用组建的含义。而JavaBean就是用java编写的可重用组件，可理解为就是创建service和dao对象的。

所以不能单纯的与实体类划等号。

如何实现一个工厂呢？

1. 需要一个配置文件<span id="bean.properties">`bean.properties`</span> ，里面以key-value形式配置我们service和dao的全限定类名,放到`resources`目录下

   ```properties
   userService=com.bingo.learn.service.impl.UserServiceImpl
   userDao=com.bingo.learn.dao.impl.UserDaoImpl
   ```

1. 通过读取配置文件中配置的内容，反射创建service和dao的对象

①读取配置文件

```java
// 使用静态代码块为Properties对象赋值
static {
  try {
    props = new Properties();
    // 利用反射拿到类加载器，返回文件的输入流对象
    InputStream rs = BeanFactory.class.getClassLoader().getResourceAsStream("bean.properties");
    props.load(rs);
  } catch (Exception e) {
    e.printStackTrace();
  }
}
```

② 反射创建bean对象

```java
/**
 * 根据beanName获取bean对象
 *
 * @param beanName
 * @return
 */
public static Object getBean(String beanName) {
  Object bean = null;
  try {
    String beanPath = props.getProperty(beanName);
    //通过获取的全限定类名，反射得到bean对象
    bean = Class.forName(beanPath).newInstance();
  } catch (Exception e) {
    e.printStackTrace();
  } 
  return bean;
}
```

至此，一个简单的工厂就有了

```java
package com.bingo.learn.factory;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 创建Bean对象的工厂
 * Bean :在计算机英语中，可重用组建的含义
 * JavaBean:用java编写的可重用组件
 * 可以理解为就是创建service和dao对象的。
 */
public class BeanFactory{
    // 定义一个Properties对象
    private static Properties props;
    // 定义一个Map,存放要创建的对象，也就是容器的概念
    private static Map<String ,Object> beans;
    // 使用静态代码块为Properties对象赋值
    static {
        try {
            props = new Properties();
            // 利用反射拿到类加载器，返回文件的输入流对象
            InputStream rs = BeanFactory.class.getClassLoader().getResourceAsStream("bean.properties");
            props.load(rs);
            // 实例化容器
            beans = new HashMap<>();
            // 取出配置文件中所有的key
            Enumeration keys = props.keys();
            // 遍历
            while (keys.hasMoreElements()){
                // 取出每个key
                String key = keys.nextElement().toString();
                // 根据key获取value
                String beanPath = props.getProperty(key);
                // 反射创建对象
                Object value = Class.forName(beanPath).newInstance();
                // 将key和value存入容器，因为是在静态代码块中，只会创建一次对象，实现了单例。
                beans.put(key,value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据beanName获取bean对象
     *
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        return  beans.get(beanName);
    }
}
```

然后利用工厂方法来替代之前的写法

`UserServiceImpl.java`

```java
/**
 * 模拟业务层实现类
 */
public class UserServiceImpl implements UserService {
    UserDao userDao = (UserDao) BeanFactory.getBean("userDao");
    public void save() {
        userDao.save();
    }
}

```



`Client.java`

```java
/**
 * 模拟一个表现层，用来调用业务层
 */
public class Client {
    public static void main(String[] args) {
        UserService userService = (UserService) BeanFactory.getBean("userService");
        userService.save();
    }
}
```

测试结果

```
保存用户成功

Process finished with exit code 0
```

如果我们这时候将`UserServiceImpl.java`删除掉，会发现项目依旧可以编译，只会在运行期报错`java.lang.ClassNotFoundException: com.bingo.learn.service.impl.UserServiceImpl`,也就做到了类与类之间的依赖关系：**编译期不依赖，运行期依赖** 这就是工厂模式的解耦。





# 二、IOC 



## （一）概念



之前我们获取对象都是采用主动`new`的方式去获取对象，变成了工厂从容器中获取指定的对象的类，帮我们查找或者创建对象提供给我们使用，我们的角色有主动变成了被动。

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gwzixilhlij30y40kwtal.jpg" alt="image-20211202153248219" style="zoom:50%;" />

也就是对象创建的权力交给了框架去完成，我们被动的接受容器创建的对象--这就是控制反转的概念，也就是IOC(Inversion Of Control)，这种思想就是Spring框架的核心之一。Spring的IOC就是为了降低耦合关系、依赖关系。

其主要包括

> * 依赖注入DI（Dependency Injection） 
> * 依赖查找DL (Dependency Lookup)

## （二）入门案例

### 1. 创建项目

接下来，就用代码的方式来认识下spring 的IOC

项目还是用最初的

.
├── java
│   └── com
│       └── bingo
│           └── learn
│               ├── dao
│               │   ├── [UserDao.java](#dao)
│               │   └── impl
│               │       └── [UserDaoImpl.java](#userDaoImpl)
│               ├── service
│               │   ├── [UserService.java](#userService)
│               │   └── impl
│               │       └── [UserServiceImpl.java](#userServiceImpl)
│               └── ui
│                   └── [Client.java](#client)
└── resources
    └── applicationContext.xml



多了一个`applicationContext.xml`,这个主要用来配置ioc相关的，先不用管。



`pom.xml`引入spring的基础核心依赖

```xml
<properties>
  <spring.version>5.3.12</spring.version>
</properties>
<dependencies>
  <!--导入spring的context坐标，context依赖了core、beans、expression-->
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>${spring.version}</version>
  </dependency>
</dependencies>
```

### 2. 配置applicationContext.xml

#### （1）创建IOC核心配置文件

> `resources`目录上右键选择【XMLConfiguration File】- 【Spring Config】,输入文件名applicationContext.xml,确认即可

创建好后，自带了所需要的基本的命名空间

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

</beans>
```

#### （2）将对象交给Spring管理



接下来就是到了开始表演的时候了，也就是将**对象的创建交给Spring来管理** 了。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!--将对象的创建交给Spring来管理-->
    <bean id="userService" class="com.bingo.learn.service.impl.UserServiceImpl"/>
    <bean id="userDao" class="com.bingo.learn.dao.impl.UserDaoImpl"/>
</beans>
```

看着眼熟么，<bean>里的`id`和`class` 是不是就是之前实现工厂类时所需要的在[bean.properties](#bean.properties)中配置的key-value键值对呢。也就是`唯一标识`+`全限定类名`

### 3. 获取Spring IOC容器，并通过id来获取对象

```java
public class Client {
    public static void main(String[] args) {
        // 1.读取配置文件，获取容器对象
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 2.根据id来获取对象
        // ① 一种是通过字节码进行强转得到UserDao的对象
        UserDao userDao = app.getBean("userDao", UserDao.class);
      	// ② 一种是获取Object，进行强转得到UserService对象
        UserService userService = (UserService) app.getBean("userService");
        System.out.println("userService: " + userService);
        System.out.println("userDao: " + userDao);
    }
}
>>>
userDao: com.bingo.learn.dao.impl.UserDaoImpl@64bfbc86
userService: com.bingo.learn.service.impl.UserServiceImpl@192b07fd

Process finished with exit code 0
```

然后运行`main`方法得到上面打印结果。说明，事实证明创建对象的活Spring干的不错，我们只需要拿到对象，去使用就好了。

> 注意，上面获取对象的两种方式：资源名和字节码



#### （1）**补充一 applicationContext继承关系**



① `AppicationContext`是什么呢？其实就是应用的上下文，负责加载配置文件的接口并获得对象的bean。



![类关系图](https://tva1.sinaimg.cn/large/008i3skNly1gx44kq484uj313v0a23zm.jpg)

​										

上图是有关`AppicationContext`的类继承关系图这里我标注出了常用的三个实现类,以及`BeanFactory`，也就是Spring容器的顶层接口

* ClassPathXmlApplicationContext

  > 从类的根路径下加载配置文件，要求配置文件必须在类路径下 -- **和FileSystemXmlApplicationContext 比较而言，更推荐使用这种**

* FileSystemXmlApplicationContext

  > 从磁盘路径上加载配置文件，配置文件可以在磁盘的任意位置。

* <span id="annotationConfigApplicationContext">AnnotationConfigApplicationContext </span>

  > 当使用注解配置容器对象时，需要使用此类来创建 spring 容器,它用来读取注解。

  **注意！**

  > FileSystemXmlApplicationContext 在写绝对路径的时候
  >
  > ```java
  > ApplicationContext app = new FileSystemXmlApplicationContext("/Users/ing/github/spring_ioc/src/main/resources/applicationContext.xml");
  > ```
  >
  > Exception in thread "main" org.springframework.beans.factory.BeanDefinitionStoreException: IOException parsing XML document from file [/src/main/resources/applicationContext.xml]; nested exception is java.io.FileNotFoundException: /src/main/resources/applicationContext.xml
  >
  > 找不到该文件，原因在源码中
  >
  > ```java
  > protected Resource getResourceByPath(String path) {
  >  if (path.startsWith("/")) {
  >      path = path.substring(1);
  >  }
  > 
  >  return new FileSystemResource(path);
  > }
  > ```
  >
  > 这里会自动去掉路径的第一个"/"。
  >
  > 所以要添加上一个
  >
  > ```java
  > ApplicationContext app = new FileSystemXmlApplicationContext("/"+"/Users/ing/github/spring_ioc/src/main/resources/applicationContext.xml");
  > ```

  ② `ApplicationContext ` 和 `BeanFactory`的区别

  ApplicationContext 是 BeanFactory 的子接口，他们之间的区别在于

  > 1. ApplicationContext:立即加载的策略，只要一读取配置文件，默认情况下就会创建对象 -- 单例对象适用。
  >
  > 2. BeanFactory 延迟加载的策略，什么时候使用什么时候创建对象 -- 非单例对象适用
  >
  >    
  >
  > `Spring会根据我们的配置的不同，智能的选择哪种方式，yyds ,当然更常用的还是第一种方式！！！`

  验证方法也很简单，给`UserServiceImpl`加一个无参构造函数，里面打印一句话

```java
public UserServiceImpl() {
  System.out.println("UserServiceImpl对象创建了...");
}
```

然后在`Client` 加断点测试即可，分别对两种方式加断点调试即可，很容易就得出结论,这里就不详述了，自己动动小爪吧。

```java
public class Client {
    public static void main(String[] args) {
        // 1.读取配置文件，获取容器对象
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 2.根据id来获取对象
        // ① 一种是通过字节码进行强转得到UserDao的对象
        UserDao userDao = app.getBean("userDao", UserDao.class);
        // ② 一种是获取Object，进行强转得到UserService对象
        UserService userService = (UserService) app.getBean("userService");
        System.out.println("userService: " + userService);
        System.out.println("userDao: " + userDao);

        Resource resource = new ClassPathResource("applicationContext.xml");
        BeanFactory factory = new XmlBeanFactory(resource);
        UserService userService1 = (UserService) factory.getBean("userService");
        System.out.println(userService1);
    }
}
```

#### （2）**补充二 getBean方法**

主要有两种

```java
Object getBean(String var1) throws BeansException;

<T> T getBean(Class<T> var1) throws BeansException;
```

对应示例代码

```java
UserService userService = (UserService) app.getBean("userService");

UserService userService =  app.getBean(UserService.class);
```

对比

* 当参数的数据类型是字符串时，表示根据Bean的id从容器中获得Bean实例，返回是Object，需要强转。 

* 当参数的数据类型是Class类型时，表示根据类型从容器中匹配Bean实例，当容器中相同类型的Bean有多个时， 则此方法会报错





## （三）IOC 中 bean 标签和对象管理的细节

bean标签

### 1. 作用

用于配置对象让 Spring 来创建的。默认情况下调用的是类中的无参构造函数,没有无参构造函数则不能创建成功。

### 2. 属性

* id: 给对象在容器中提供一个唯一标识

  用于获取对象。

* class: 指定类的全限定类名

  用于反射创建对象。默认情况下调用无参构造函数。

* scope: 指定对象的作用域

* Init-method: 指定类中的初始化方法名称

* destroy-method: 指定类中销毁方法名称

### 3. Bean创建的三种方式

一共有三种方式

* 使用默认无参构造函数创建对象
* 使用某个类中的方法创建对象 
* 使用某个类中静态方法创建对象

准备工作

> 将之前项目改造一下，为了便于测试，减少干扰，保留Service，删掉Dao层的代码。


├── java
│   └── com
│       └── bingo
│           └── learn
│               ├── service
│               │   ├── [UserService.java](#userService)
│               │   └── impl
│               │       └── [UserServiceImpl.java](#userServiceImpl)
│               └── ui
│                   └── [Client.java](#client)
└── resources
    └── applicationContext.xml







`UserService.java`

```java
public class UserServiceImpl implements UserService {
    public UserServiceImpl() {
        System.out.println("UserServiceImpl 无参构造函数...");
    }

    public void save() {
        System.out.println("UserServiceImpl 的 save方法执行...");
    }
}
```



`Client.java`

```java
public class Client {
    public static void main(String[] args) {
        // 1.读取配置文件，获取容器对象
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService userService = (UserService) app.getBean("userService");
        System.out.println("userService: " + userService);
      	userService.save();
    }
}
```

`applicationContext.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="userService" class="com.bingo.learn.service.impl.UserServiceImpl"/>
</beans>
```

#### （1）默认无参构造函数的方式

>   默认情况下,Spring在创建对象的时候是调用的是类中的无参构造函数。如果没有无参构造函数则不能创建成功。

执行Client的main方法，打印结果

![](https://tva1.sinaimg.cn/large/008i3skNly1gwzwa4c2b9j30k203mglv.jpg)

可以看到无参构造函数被执行了并打印出了对象的地址，说明成功创建了对象。

接着，将UserServiceImpl 改为

```java
public class UserServiceImpl implements UserService {
    public UserServiceImpl(String name) {
        System.out.println("UserServiceImpl 无参构造函数...");
    }

    public void save() {
        System.out.println("UserServiceImpl 的 save方法执行...");
    }
}
```

也就是没有了无参构造函数。

然后再次执行，就会发现报错了

`警告: Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'userService' defined in class path resource [applicationContext.xml]: Instantiation of bean failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.bingo.learn.service.impl.UserServiceImpl]: No default constructor found; nested exception is java.lang.NoSuchMethodException: com.bingo.learn.service.impl.UserServiceImpl.<init>()`

很明显，意思就是**没有找到默认构造函数（ No default constructor found）无法创建对象。**



#### （2）使用某个类中的方法创建对象 

模拟一个工厂类，假设其是一个第三方jar包里的类，我们无法通过修改源码的方式提供默认构造函数，但是该类提供了一个获取bean对象的方法，那么该如何使用呢？

项目中增加一个类 BeanFactory

```java
package com.bingo.learn.factory;

import com.bingo.learn.service.UserService;
import com.bingo.learn.service.impl.UserServiceImpl;

public class BeanFactory {
    // 提供UserService对象
    public UserService getUserService() {
        return new UserServiceImpl();
    }
}
```

Spring 该如何通过该工厂类中的方法来创建对象呢？当然还是需要配置，问题是怎么配置，这时就用到了其他的bean标签中的另外两个属性： factory-bean 和 factory-method 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
   
  	<!--通过工厂类的方法创建，存入spring容器-->
    <bean id="beanFactory" class="com.bingo.learn.factory.BeanFactory"/>
    <bean id="userService" factory-bean="beanFactory" factory-method="getUserService"/>
</beans>
```

* `factory-bean` 用来指定 哪个工厂类的bean 
* `factory-method` 用来指定 用指定工厂bean中的哪个方法获得目标bean 

如此，利用工厂中的方法创建的bean,然后存入Spring容器，交由容器去管理

运行结果

![](https://tva1.sinaimg.cn/large/008i3skNly1gwzw9ygb79j30k203mglv.jpg)



#### （3）使用某个类中静态方法创建对象

这种方式和第二种类似，只不过就是变成了静态方法而已,创建一个新的类叫 StaticFactory

```java
public class StaticFactory {
    public static UserService getUserService(){
        return new UserServiceImpl();
    }
}
```

配置applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!--通过工厂类的静态方法创建，存入spring容器-->
    <bean id="userService" class="com.bingo.learn.factory.StaticFactory" factory-method="getUserService"/>
</beans>
```

> 注意 和第二种方式配置上的区别 。 第二种和第三种方式，常用于创建第三方jar包中的对象。

### 4. Bean的作用域 

* singleton :单例的，也是默认值
* prototype: 多例的
* request : WEB项目中,Spring创建一个Bean的对象,将对象存入到request域中. 
* session : WEB项目中,Spring创建一个Bean的对象,将对象存入到session域中. 
* global session: WEB项目中,应用在Portlet环境.如果没有Portlet环境那么globalSession 相当于 session.

其中常用的就是前两种。

以上个项目为例，来验证下前两种。

```java
public class Client {
    public static void main(String[] args) {
        // 1.读取配置文件，获取容器对象
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService us1 = (UserService) app.getBean("userService");
        UserService us2 = (UserService) app.getBean("userService");
        System.out.println("us1: " + us1);
        System.out.println("us2: " + us2);
        System.out.println(us1==us2);
    }
}
```

上面代码，获取了两次对象，那就比较两次获取的对象是否是同一个对象

首先 singleton

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="userService" class="com.bingo.learn.service.impl.UserServiceImpl" scope="singleton"/>

</beans>
```

运行结果

![image-20211202233439944]()

![image-20211202233506986](https://tva1.sinaimg.cn/large/008i3skNly1gwzwv62wc4j30jr04mq39.jpg)

说明是同一个对象，也就是说明，默认情况下Spring在创建bean的对象的时候是单例的。

同理 protype

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="userService" class="com.bingo.learn.service.impl.UserServiceImpl" scope="prototype"/>

</beans>
```

运行结果

![image-20211202233406595](https://tva1.sinaimg.cn/large/008i3skNly1gwzwu4dpw2j30jq05074o.jpg)





### 5. bean的生命周期

Bean 的生命周期涉及到两个属性

* init-method: 指定类中的初始化方法名称
* destory-method: 指定类中的销毁方法名称

#### （1）模拟方法

```java
public class UserServiceImpl implements UserService {
    public UserServiceImpl() {
        System.out.println("UserServiceImpl 无参构造函数...");
    }

    public void save() {
        System.out.println("UserServiceImpl 的 save方法执行...");
    }
  	// 模拟初始化
    public void init(){
        System.out.println("对象初始化...");
    }
  	// 模拟销毁
    public void destory(){
        System.out.println("对象销毁...");
    }
}
```

#### （2）配置



① 单例模式下的情况

```xml
<bean id="userService" class="com.bingo.learn.service.impl.UserServiceImpl" init-method="init" destroy-method="destory" scope="singleton"/>
```

测试

```java
public class Client {
    public static void main(String[] args) {
        // 1.读取配置文件，获取容器对象
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService userService = (UserService) app.getBean("userService");
        userService.save();
      	// 模拟容器的关闭
        app.close();
    }
}
```

结果

![image-20211203000043796](https://tva1.sinaimg.cn/large/008i3skNly1gwzxltl1u8j30l204hmxc.jpg)





② 多例模式下的情况

```xml
<bean id="userService" class="com.bingo.learn.service.impl.UserServiceImpl" init-method="init" destroy-method="destory" scope="prototype"/>
```

结果

![image-20211203000200224](https://tva1.sinaimg.cn/large/008i3skNly1gwzxn64exqj30ke03ndfz.jpg)



对比来看，单例情况下，随着main入口方法的执行结束，也就是应用实例的销毁而销毁，多例的情况下，并没看到销毁方法的调用。原因

* 单例对象，一个应用只有一个对象的实例，它的作用范围就是整个应用。

  > 生命周期:
       对象出生:当应用加载，创建容器时，对象就被创建了。
       对象活着:只要容器在，对象一直活着。
       对象死亡:当应用卸载，销毁容器时，对象就被销毁了。

* 多例对象，每次访问对象时，都会重新创建对象实例。

  > 生命周期:
  >   对象出生:当使用对象时，创建新的对象实例。
  >   对象活着:只要对象在使用中，就一直活 着。
  >   对象死亡:当对象长时间不用时，被 Java 的GC垃圾回收器回收了。



## （四）Bean的依赖注入

### 1. 概念

> 依赖注入:Dependency Injection。是spring框架核心ioc的具体实现。
>
> 之前说过，Spring的IOC目的在于降低削减耦合，也就是依赖关系，但是再怎么削减，也不可能消除依赖关系的存在。
>
> 在程序编写时，通过控制反转，对象的创建交给了Spring，我们只需要在配置文件中做出说明即可。

### 2. 依赖注入的数据类型

* 基本数据类型 
* 引用数据类型
* 集合数据类型

### 3. 依赖注入的三种方式

* 构造函数注入

* set方法注入

* 使用p命名空间注入

  

下面就演示下，每种方式对应的多种数据类型的注入方式

#### （1）构造函数注入

> 使用类中的构造函数，给成员变量赋值，赋值的操作是通过配置的方式，让Spring框架帮我们完成

要求

* 类中需要提供一个对应参数列表的构造函数

* 标签：`constructor-arg` 

  该标签包含几种属性

  * index 指定参数在构造函数参数列表的索引位置

  * type:指定参数在构造函数中的数据类型

  * name:指定参数在构造函数中的名称 【常用】

    

  * value:它能赋的值是基本数据类型和 String 类型

  * ref: 它能赋的值是其他 bean 类型，也就是说，必须得是在配置文件中配置过的 bean



下面用代码来实现

① 改造`UserSerivceImpl` ，增加几个类成员变量，

```java
public class UserServiceImpl implements UserService {
  	//基本数据类型
    private String name; 
    private Integer age;
  	//引用类型数据
    private Date birthday;
  	//集合类型数据
    private String[] mStrs;
    private List<String> mList;
    private Set<String> mSet;
    private Map<String, User> userMap;
    private Properties props;

    public UserServiceImpl(String name, Integer age, Date birthday, String[] mStrs, List<String> mList, Set<String> mSet, Map<String, User> userMap, Properties props) {
        this.name = name;
        this.age = age;
        this.birthday = birthday;
        this.mStrs = mStrs;
        this.mList = mList;
        this.mSet = mSet;
        this.userMap = userMap;
        this.props = props;
    }

    @Override
    public void save() {
        System.out.println("name: " + name);
        System.out.println("age: " + age);
        System.out.println("birthday: " + birthday);
        System.out.println("mStrs: " + Arrays.toString(mStrs));
        System.out.println("mList: " + mList);
        System.out.println("mSet: " + mSet);
        System.out.println("userMap: " + userMap);
        System.out.println("props: " + props);
    }
}
```

并增加一个实体类 `User`

```java
public class User {
    private String name;
    private Integer age;
    private Date birthday;
		...
      getter ,setter ,toString 省略，请自行添加
    ...
}

```



② 配置核心文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="userService" class="com.bingo.learn.service.impl.UserServiceImpl">
        <constructor-arg name="name" value="法外狂徒"/>
        <constructor-arg name="age" value="22"/>
        <!--ref 引用一个日期对象-->
        <constructor-arg name="birthday" ref="date"/>

        <!--Array数据注入配置-->
        <constructor-arg name="mStrs" >
            <array>
                <value>Hello</value>
                <value>Array</value>
            </array>
        </constructor-arg>

        <!--List数据注入配置-->
        <constructor-arg name="mList">
            <list>
                <value>Hello</value>
                <value>List</value>
            </list>
        </constructor-arg>

        <!--Set数据注入配置-->
        <constructor-arg name="mSet">
            <set>
                <value>Hello</value>
                <value>Set</value>
            </set>
        </constructor-arg>

        <!--Map数据注入，且value值为引用数据类型的配置-->
        <constructor-arg name="userMap">
            <map>
                <entry key="u1" value-ref="user1"/>
                <entry key="u2" value-ref="user2"/>
            </map>
        </constructor-arg>

        <!--Properties数据注入配置-->
        <constructor-arg name="props">
            <props>
                <prop key="p1">Hello</prop>
                <prop key="p1">Properties</prop>
            </props>
        </constructor-arg>
    </bean>
    
    <!--配置一个日期对象-->
    <bean id="date" class="java.util.Date"/>
    <bean id="user1" class="com.bingo.learn.domain.User">
        <property name="name" value="Tom"/>
        <property name="age" value="22"/>
        <property name="birthday" ref="date"/>
    </bean>

    <!--实体类注入-->
    <bean id="user2" class="com.bingo.learn.domain.User">
        <property name="name" value="Jerry"/>
        <property name="age" value="30"/>
        <property name="birthday" ref="date"/>
    </bean>
</beans>
```

运行结果

![image-20211203105452476](https://tva1.sinaimg.cn/large/008i3skNly1gx0gifoapnj311m06emxp.jpg)





#### （2）set方法注入 【常用】

> 使用默认构造函数创建对象，然后调用类中的set方法，给属性赋值，赋值的操作是通过配置的方式，让Spring框架帮我们完成

要求

* 类中需要提供属性的set的方法

* 标签：`property` 

  该标签包含几种属性

  * name: 指定所调用的set方法名称 【set方法名去掉set后，首字符小写的部分】
  * value:它能赋的值是基本数据类型和 String 类型
  * ref: 它能赋的值是其他 bean 类型，也就是说，必须得是在配置文件中配置过的 bean

代码实现



① 改造`UserServiceImpl`

```java

public class UserServiceImpl implements UserService {
    private String name;
    private Integer age;
    private Date birthday;
    private String[] mStrs;
    private List<String> mList;
    private Set<String> mSet;
    private Map<String, User> userMap;
    private Properties props;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setmStrs(String[] mStrs) {
        this.mStrs = mStrs;
    }

    public void setmList(List<String> mList) {
        this.mList = mList;
    }

    public void setmSet(Set<String> mSet) {
        this.mSet = mSet;
    }

    public void setUserMap(Map<String, User> userMap) {
        this.userMap = userMap;
    }

    public void setProps(Properties props) {
        this.props = props;
    }

    @Override
    public void save() {
        System.out.println("name: " + name);
        System.out.println("age: " + age);
        System.out.println("birthday: " + birthday);
        System.out.println("mStrs: " + Arrays.toString(mStrs));
        System.out.println("mList: " + mList);
        System.out.println("mSet: " + mSet);
        System.out.println("userMap: " + userMap);
        System.out.println("props: " + props);
    }
}

```



② 配置核心文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="userService" class="com.bingo.learn.service.impl.UserServiceImpl">
        <property name="name"  value="法外狂徒"/>
        <property name="age"   value="22"/>
        <property name="birthday" ref="date"/>
    </bean>
    <!--配置一个日期对象-->
    <bean id="date" class="java.util.Date"/>

</beans>
```

执行结果

![image-20211203105452476](https://tva1.sinaimg.cn/large/008i3skNly1gx0gjdr60pj311m06emxp.jpg)





> 注意：`<property name="xxx" ></property>`中的name的值对应的是`set方法名`去掉set后部分，首字符小写。
>
> 比如：方法名为 `setABC(){}` 那么其对应配置为`<property name="aBC" ></property>`



上面没啥可讲的，差不多各种数据类型的注入通用写法都包含了，个别写法自己对照文档，举一反三即可。



#### （3）使用p命名空间注入

配置文件引入P命名空间` xmlns:p="http://www.springframework.org/schema/p"`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="userService" class="com.bingo.learn.service.impl.UserServiceImpl" p:name="Jerry" p:age="33" p:birthday-ref="date"/>

    <!--配置一个日期对象-->
    <bean id="date" class="java.util.Date"/>

</beans>
```

至于集合类型，没找到具体方法，有知晓请告知。



#### （4）小结

> <bean>标签
>
> ​	id属性:在容器中Bean实例的唯一标识，不允许重复 
>
> ​	class属性:要实例化的Bean的全限定名 
>
> ​	scope属性:Bean的作用范围，常用是Singleton(默认)和prototype 
>
> ​	<property>标签:属性注入
>
> ​		name属性:属性名称    value属性:注入的普通属性值      ref属性:注入的对象引用值 
>
> ​		<list>标签
>
> ​		<map>标签
>
> ​				<properties>标签 
>
> ​		<constructor-arg>标签
>
> <import>标签:导入其他的Spring的分文件

上述的方式，其实都可以看做是通过xml配置的方式来完成的注入，下面我们介绍下，如何使用注解的方式来实现同样的效果，也就是IOC的注解开发



### 4. 注解方式注入

项目恢复到最初的结构，把删除的Dao层再补上，结构如下

├── java
│   └── com
│       └── bingo
│           └── learn
│               ├── dao
│               │   ├── [UserDao.java](#dao)
│               │   └── impl
│               │       └── [UserDaoImpl.java](#userDaoImpl)
│               ├── service
│               │   ├── [UserService.java](#userService)
│               │   └── impl
│               │       └── [UserServiceImpl.java](#userServiceImpl)
│               └── ui
│                   └── [Client.java](#client)
└── resources
    └── applicationContext.xml





便于后续测试，改造下 `UserServiceImpl`

```java
@Component("userService")
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    @Override
    public void save() {
        userDao.save();
    }
}
```



另外 applicationContext.xml 清除干净

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

</beans>
```

准备好了上面，我们就开始表演了。

#### （1）对象的注入

以UserServiceImpl为例，之前通过XML配置的注入的格式为

```xml
<bean id="userService" class="com.bingo.learn.service.impl.UserServiceImpl" 
      scope="" init-method="" destroy-method="">
  <property name=""  value="" | ref="" ></property>
</bean>
```

我们拆分下之前的xml配置

* <bean></bean>标签：作用是创建对象
* scope属性：作用是指定对象作用域
* init-method 和 destory-method:和对象的生命周期有关
* <property></property>标签：作用是注入数据的

那么注解方式实现的话，对应这些功能的都是哪些注解字段呢？



##### ① `@Component`

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface Component {
    String value() default "";
} 
```

> 作用：用于把当前类对象存入Spring容器中，也就是让Spring来为我们生成对象
>
> 其`value`  的值对应的就是XML中的id

比如我们在`UserServiceImpl`上加上该注解`@Component` 

```java
@Component(value="userService")
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    @Override
    public void save() {
        userDao.save();
    }
}
```

> Tip
>
> 1. 如果没有指定`value` 那么默认**以当前类名，首字母小写**的字符串作为 value,也就是这里的 `@Component` 相当于 `@Component(value = "userServiceImpl")`
>
> 2. 如果只有一个value属性，value也可以省略不写 -- `@Component("userService")`



加上注解就可以了么？试一试

```java
public class Client {
    public static void main(String[] args) {
        // 1.读取配置文件，获取容器对象
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService userService = (UserService) app.getBean("userService");
        System.out.println("userService: "+userService);
    }
}
```



你发现运行后报错：

`Exception in thread "main" org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'userService' available`

why?

原因在于，虽然你给当前类加上了注解，也放入了Spring容器中，但是Spring容器是通过解析配置文件 `applicationContext.xml` 来确定你要注入的对象的，当前我们的配置文件里什么都没有...Spring容器也懵逼呀，你根本没告知它 在哪加了注解，它也就不会去为你生成对象。所以采用注解方式注入的时候，有个非常非常重要的事情要做，就是告诉Spring容器我在某些地方加了 `@Component` 注解了，你得干活了，他怎么知道呢？通过`组件扫描 `:**使用注解进行开发时，需要在applicationContext.xml中配置组件扫描，作用是指定哪个包及其子包下的Bean 需要进行扫描以便识别使用注解配置的类、字段和方法。**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
  			
        <!--开启组件扫描，指定所需要扫描的包-->
        <context:component-scan base-package="com.bingo.learn"/>
</beans>
```

> Tip
>
> 在配置文件中 输入 `<scan` ,idea 会自动提示你，直接敲回车，所需要引入的命名空间会自动引入的，不需要你手动从其他地方拷贝。

再次运行，打印了对象地址，说明对象已经创建了。

![image-20211203141157041](https://tva1.sinaimg.cn/large/008i3skNly1gx0m7h6j20j30t302q3yn.jpg)



除了`@Component` 外，还有另外三种注解和 @Component 的作用以及属性完全一样的

* `@Service` : 一般用在业务层

* `@Controller`: 一般用在控制层

* `@Repository`: 一般用在持久层

  

接下来，我们使用下@Repository 

```java
@Repository("userDao")
public class UserDaoImpl implements UserDao {

    //模拟持久层操作
    @Override
    public void save() {
        System.out.println("保存用户成功");
    }
}
```

验证下

```java
public class Client {
    public static void main(String[] args) {
        // 1.读取配置文件，获取容器对象
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService userService = (UserService) app.getBean("userService");
        UserDao userDao = (UserDao)app.getBean("userDao");
        System.out.println("userService: "+userService);
        System.out.println("userDao: "+userDao);
    }
}
```

运行结果

![image-20211203142357105](https://tva1.sinaimg.cn/large/008i3skNly1gx0mjy7vs1j30qb036dg3.jpg)



> Tip
>
> 如果是属于三层，建议使用@Service，@Controller,@Repository来进行注解，如果不属于就用@Component



#### （2）数据的注入

上面的案例，如果我们调用下save方法

```java
public class Client {
    public static void main(String[] args) {
        // 1.读取配置文件，获取容器对象
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService userService = (UserService) app.getBean("userService");
        UserDao userDao = (UserDao)app.getBean("userDao");
        System.out.println("userService: "+userService);
        System.out.println("userDao: "+userDao);
        userService.save();
    }
}
```

很快就会发现，报错了

`Exception in thread "main" java.lang.NullPointerExceptionat com.bingo.learn.service.impl.UserServiceImpl.save(UserServiceImpl.java:15)
at com.bingo.learn.ui.Client.main(Client.java:19)`

这个空指针很容易理解，UserServiceImpl里的userDao并么有初始化

```java
@Component("userService")
public class UserServiceImpl implements UserService {
    private UserDao userDao; // 这里并没有初始化
    @Override
    public void save() {
        userDao.save();
    }
}
```

也许你会说，在这里给他初始化不就可以了，反正对象已经注入了么？

可以是可以，但如果在这里初始化，岂不是又回到了最初，我们为了解耦才走到这里的，你这么一干，立马回到了解放前。

所以不能从这里搞，那怎么搞？也就是数据怎么注入呢



通过之前基于XML的数据类型注入，这里如果用XML配置来解决的话，很简单 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
        
        <bean id="userDao" class="com.bingo.learn.dao.impl.UserDaoImpl"/>
  
        <bean id="serviceService" class="com.bingo.learn.service.impl.UserServiceImpl">
                <property name="userDao" ref="userDao"/>
        </bean>
</beans>
```

然后采用`默认构造函数`或者`set方法`注入即可。



那么注解如何做呢？这就用到了下面几个注解

##### ① @Autowired` 

```java
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    boolean required() default true;
}
```

通过源码可知，其可以用在构造函数，方法，参数，属性，注解类型上，不过常用的还是用在`方法`和`属性`上

> 作用
>
> 自动按照**类型**注入，只要容器中有**唯一**的一个bean对象类型和要注入的变量类型匹配，就可以注入成功。
>
> 
>
> Tip
>
> 在使用此注解后，set方法可省略



**匹配规则原理分析**



我们知道 Spring IOC容器本质上是一个Map<String,Object>集合，里面存放了 bean类的唯一标识id 和 bean实例对象。

我们给 UserDaoImpl 加上了注解，并修改save方法的输出

```java
@Repository("userDao")
public class UserDaoImpl implements UserDao {
    @Override
    public void save() {
        System.out.println("【UserDaoImpl】 保存用户成功");
    }
}
```



此时此刻，IOC容器中就有了一个实现了UserDao接口类型的`UserDaoImpl` bean实例 -- A 



![](https://tva1.sinaimg.cn/large/008i3skNly1gx12661aiij31g205c0td.jpg)





接下来，我们在UserServiceImpl中想注入UserDao 的变量

```java
@Component("userService")
public class UserServiceImpl implements UserService {
  
    @Autowired
    private UserDao userDao ;
		...
    ...
}
```

那么当加上 `@Autowired` 就会依据变量的类型：`UserDao` 接口类型自动地去IOC容器中匹配有没有和这个变量类型一样的bean实例存在，很显然这时候是存在一个implement 了 `UserDao`接口类的实例A ，我们就认为是同属于`UserDao`类型，那么就会取出然后注入到userDao变量中，完成了初始化。

运行不再报空指针

![image-20211203232019554](https://tva1.sinaimg.cn/large/008i3skNly1gx1225jlojj30kh02e3yj.jpg)









那你就会问了，如果相同类型的实例不唯一呢，那什么结果呢？



我们在项目中添加一个类 `UserDaoImpl1` 并且添加注解 `@Repository("userDao1")`

```java
@Repository("userDao1")
public class UserDaoImpl1 implements UserDao {
    @Override
    public void save() {
        System.out.println("【UserDaoImpl-1】 保存用户成功");
    }
}
```



此时此刻，IOC容器中就有了一个实现了UserDao接口类型的`UserDaoImpl1 `bean实例  -- B

![](https://tva1.sinaimg.cn/large/008i3skNly1gx128l0jwcj31f206mjso.jpg)



当加上`@Autowired` 还是会依据变量类型自动地去IOC容器中匹配有没有和这个变量类型一样的实例存在，很显然,这时候有两个A和B ，此时就会依据变量名称也就是 `userDao` 作为bean的id去匹配A和B的key值，是否有相同的，有就注入，没有就报错。很显然，bean实例A 类型相同，key也相同，所以注入的是A



![image-20211203233632347](https://tva1.sinaimg.cn/large/008i3skNly1gx12ixawy1j30n102g3yj.jpg)



如果此时，将变量名称改为 `userDao1`

```java
@Component("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao1 ;

    @Override
    public void save() {
        userDao1.save();
    }
}
```

那么就会是匹配的实例B,也就是B被注入。运行结果也证明了这一点

![image-20211203233926904](https://tva1.sinaimg.cn/large/008i3skNly1gx12lzbuagj30ml02djre.jpg)



那么如果将变量名修改为 `userDao2`呢？

```java
@Component("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao2 ;

    @Override
    public void save() {
        userDao2.save();
    }
}
```

结果就是报错了

`Caused by: org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type 'com.bingo.learn.dao.UserDao' available: expected single matching bean but found 2: userDao,userDao1`

意思就是，期望的是有一个唯一匹配的bean,但是却有2个类型匹配的，但名称又都不匹配，所以不知道选择哪一个了，此时就无法注入成功，Spring索性破罐子破摔扔出来一个错误，并不忘抱怨道：你喵的就不能告诉我选哪一个么？

好吧，这时 `@Qualifier`就登场了。





##### ② <span id="qualifier">`@Qualifier` </span>

> 作用：在按照类型注入的基础上指定名称注入
>
> 
>
> Tip
>
> 1. 在给类成员注入时不能单独使用，通常是和 `@Autowired` 联用
> 1. 在给方法参数注入时可以单独使用

使用 `@Qualifier` 指定名称“userDao”, 意思就是告诉Spring 我要找的是`UserDao`类型的，并且名称是 “userDao” 的bean ，此时你变量名想叫什么就叫什么，与我无关。

```java
@Component("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    @Qualifier("userDao")
    private UserDao userDao2 ;

    @Override
    public void save() {
        userDao2.save();
    }
}
```

很显然，此时A 被注入进去，同时也解决了上述问题。

![image-20211204001033106](https://tva1.sinaimg.cn/large/008i3skNly1gx13ibeoptj30mm02l3yj.jpg)





此时你说了，好麻烦哦，竟然需要配置两个注解，有没有更简单的办法呢，有！ 那就是`@Resource`





##### ③ `@Resource` 

> 作用
>
> 指定bean的id来注入
>
> 
>
> Tip
>
> 1. 其默认属性是 name ，不能省略

已知的，已经有两个bean![image-20211204002947308](https://tva1.sinaimg.cn/large/008i3skNly1gx14441iv0j31f206mjso.jpg)

使用 @Resource 指定的哪个 ，哪个就会被注入到变量中

```java
@Component("userService")
public class UserServiceImpl implements UserService {
    // @Autowired
    // @Qualifier("userDao")
    @Resource(name="userDao1")
    private UserDao userDao2 ;

    @Override
    public void save() {
        userDao2.save();
    }
}
```

运行结果

![image-20211204003116019](https://tva1.sinaimg.cn/large/008i3skNly1gx143xlqsnj30ml02djre.jpg)



**注意!!!**

> 1. 上述三种注解，只能用在注入其他bean类型的数据，而不能用于基本数据类型和String类型的数据
> 1. 基本数据类型和String类型的数据使用@Value注解来注入
> 1. 集合数据类型只能通过XML配置的方式来注入



##### ④ `@Value`

> 作用
>
> 属性值注入
>
> 
>
> Tip
>
> 也可以使用SpEl表达式写法 ${}  -- 后续补充

简单使用

```java
@Component("userService")
public class UserServiceImpl implements UserService {
    @Value("Hello IOC")
    private String str;
    @Resource(name="userDao1")
    private UserDao userDao2 ;

    @Override
    public void save() {
        System.out.println(str);
        userDao2.save();
    }
}
```

运行结果

![image-20211204011322367](https://tva1.sinaimg.cn/large/008i3skNly1gx15bol1g7j30mo02z74c.jpg)



#### （3）作用域相关注解

##### ① `@Scope`

> 作用
>
> 常用于指定bean的作用域范围
>
> 
>
> 属性
>
> value: 指定取值，常用取值：singleton和prototype 
>
> 
>
> Tip
>
> 如果不指定值，默认为singleton

```java
@Component("userService")
@Scope
public class UserServiceImpl implements UserService {
    @Resource(name="userDao1")
    private UserDao userDao2 ;
    @Override
    public void save() {
        userDao2.save();
    }
}
```

```java
public class Client {
    public static void main(String[] args) {
        // 1.读取配置文件，获取容器对象
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService userService = (UserService) app.getBean("userService");
        UserService userService1 = (UserService) app.getBean("userService");
        System.out.println(userService==userService1);
    }
}
```

运行结果： true  说明是单例的



```java
@Component("userService")
@Scope("prototype")
public class UserServiceImpl implements UserService {

    @Resource(name="userDao1")
    private UserDao userDao2 ;

    @Override
    public void save() {
        userDao2.save();
    }
}
```

运行结果：false 说明是多例的



#### （4）生命周期相关注解



##### ① `@PostConstruct`

> 作用
>
> 和在bean标签中使用 `destroy-method` 作用一样，用于指定销毁方法	

##### ② `@PreDestroy`

> 作用
>
> 和在bean标签中使用 `init-method` 的作用一样，用于指定初始化方法

测试

改造`UserServiceImpl`添加两个方法，并添加相应注解`@PostConstrut`和`@PreDestroy`分别指定初始化方法和销毁方法

> Tip 
>
> 需要将`UserServiceImpl`的scope改为单例 -- singleton ，不然无法测试`@Prestroy`的作用，因为如果是多例，bean实例的销毁不是Spring来管理的，而是Java 回收机制。

```java
@Component("userService")
@Scope
public class UserServiceImpl implements UserService {

    @Resource(name = "userDao1")
    private UserDao userDao2;

    @PostConstruct
    public void init() {
        System.out.println("UserServiceImpl 初始化方法执行");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("UserServiceImpl 销毁方法执行");
    }

    @Override
    public void save() {
        userDao2.save();
    }
}

```



```java
public class Client {
    public static void main(String[] args) {
        // 1.读取配置文件，获取容器对象
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService userService = (UserService) app.getBean("userService");
        userService.save();
        // 容器释放
        app.close();
    }
}
```

运行结果

![image-20211205002908241](https://tva1.sinaimg.cn/large/008i3skNly1gx29o321szj30mn03kdg0.jpg)







#### （5）小复习

为了便于讲解新注解，我们新建一个项目，实现简单的CRUD操作(案例中只实现查询全部和插入数据，其他自行实现)，顺便复习下之前的注解；

数据库操作的工具我们使用Apache的`DBUtils` ，数据源采用`Druid`

首先建表 `account`

```sql
CREATE TABLE account(
id int PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(20),
balance DOUBLE 
)ENGINE=INNODB DEFAULT CHARSET=utf8;

+----------+-------------+------+-----+---------+----------------+
| Field    | Type        | Null | Key | Default | Extra          |
+----------+-------------+------+-----+---------+----------------+
| id       | int         | NO   | PRI | NULL    | auto_increment |
| name | varchar(20) | YES  |     | NULL    |                |
| balance  | double      | YES  |     | NULL    |                |
+----------+-------------+------+-----+---------+----------------+


插入一条数据
INSERT into account values(null,"Alice",3000);

mysql> select * from account;
+----+----------+---------+
| id | name | balance |
+----+----------+---------+
|  1 | Alice    |    3000 |
+----+----------+---------+
1 row in set (0.00 sec)

```

添加必要的依赖 pom.xml

```xml
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-context</artifactId>
  <version>5.3.12</version>
</dependency>
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>8.0.26</version>
</dependency>
<!--DbUtils-->
<dependency>
  <groupId>commons-dbutils</groupId>
  <artifactId>commons-dbutils</artifactId>
  <version>1.7</version>
</dependency>
<!--druid-->
<dependency>
  <groupId>com.alibaba</groupId>
  <artifactId>druid</artifactId>
  <version>1.2.8</version>
</dependency>
<dependency>
  <groupId>junit</groupId>
  <artifactId>junit</artifactId>
  <version>4.12</version>
  <scope>test</scope>
</dependency>
```

项目结构



├── java
│   └── com
│       └── bingo
│           └── learn
│               ├── dao
│               │   ├── [AccountDao.java](#accountDao)
│               │   └── impl
│               │       └── [AccountDaoImpl.java](#accountDaoImpl)
│               ├── domain
│               │   └── [Account.java](#account)
│               └── service
│                   ├── [AccountService.java](#accountService)
│                   └── impl
│                       └── [AccountServiceImpl.java](#accountServiceImpl)
├── resources
│   ├── [applicationContext.xml](#applicationContext)





我们先以 XML配置的方式实现一遍，然后改造成注解方式



<span id="account">`Account` </span>

```java
public class Account {

    private Integer id;
    private String name;
    private Float balance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }
}
```



<span id="accountDao">`AccountDao`</span>

```java
public interface AccountDao {

    List<Account> findAllAccount();

    void saveAccount(Account account);
}
```



<span id="accountDaoImpl">`AccountDaoImpl`</span>

```java
public class AccountDaoImpl implements AccountDao {
	
    private QueryRunner runner;

    public void setRunner(QueryRunner runner) {
        this.runner = runner;
    }

    @Override
    public List<Account> findAllAccount() {
        try{
            return runner.query("select * from account",new BeanListHandler<Account>(Account.class));
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAccount(Account account) {
        try{
            runner.update("insert into account(name,balance)values(?,?)",account.getName(),account.getBalance());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

<span id="accountService">`AccountService`</span>

```java
public interface AccountService {

    List<Account> findAllAccount();

    void saveAccount(Account account);
}

```

<span id="accountServiceImpl">`AccountServiceImpl`</span>

```java
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public List<Account> findAllAccount() {
        return accountDao.findAllAccount();
    }

    @Override
    public void saveAccount(Account account) {
        accountDao.saveAccount(account);
    }
}
```

<span id="applicationContext">`applicationContext.xml`</span>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
        <context:component-scan base-package="com.bingo.learn"/>
        <!-- 配置Service对象 -->
        <bean id="accountService" class="com.bingo.learn.service.impl.AccountServiceImpl">
                <!-- 注入dao -->
                <property name="accountDao" ref="accountDao"></property>
        </bean>

        <!--配置Dao对象-->
        <bean id="accountDao" class="com.bingo.learn.dao.impl.AccountDaoImpl">
                <!-- 注入QueryRunner -->
                <property name="runner" ref="runner"></property>
        </bean>
        <!--配置QueryRunner对象-->
        <bean id="runner" class="org.apache.commons.dbutils.QueryRunner" scope="prototype">
                <constructor-arg name="ds" ref="dataSource"/>
        </bean>
        <!-- 数据源 -->
        <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
                <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/spring"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
        </bean>

</beans>
```

> Tip
>
> 1. QueryRunner配置的scope为多例（prototype），每次使用都是一个新的对象。原因在于防止单例情况下，多个Dao使用同一个对象，一个在用，一个还没有用完，导致线程互相干扰。
>
> 2. `com.mysql.jdbc.Driver`` 是 mysql-connector-java 5及以下版本的
>
>    `com.mysql.cj.jdbc.Driver` 是 mysql-connector-java 6以上的
>
>    版本要对应上mysql-connector-java 的版本一定要和你本地环境的mysql版本对应，不然会报错：
>
>    `Could not load driverClass com.mysql.jdbc.Driver`
>
>    其他错误，诸如拼写错误，空格之类的，自己多检查下吧

接下来写我们的测试类 `AccountServiceTest`  ，测试查询所有和插入一条数据

```java
public class AccountServiceTest {

    private AccountService as;

    @Test
    public void testFindAll() {
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        AccountService accountService = app.getBean(AccountService.class);
        List<Account> accounts = accountService.findAllAccount();
        for(Account account : accounts){
            System.out.println(account);
        }
    }

    @Test
    public void testSave() {
      	ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        AccountService accountService = app.getBean(AccountService.class);
        Account account = new Account();
        account.setName("test");
        account.setBalance(12345f);
        accountService.saveAccount(account);
    }
}

```

以上就是采用XML配置的方式实现的简单增删改查，这个是应该掌握，自己能盲写出来的。

接下来，我们改造成使用注解的方式实现,其他类不变只需要改动以下几个

`AccountDaoImpl`

```java
@Repository("accountDao")
public class AccountDaoImpl implements AccountDao {
    @Autowired
    private QueryRunner runner;

    @Override
    public List<Account> findAllAccount() {
        try{
            return runner.query("select * from account",new BeanListHandler<Account>(Account.class));
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAccount(Account account) {
        try{
            runner.update("insert into account(name,balance)values(?,?)",account.getName(),account.getBalance());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

```

`AccountServiceImpl`

```java
@Service("accountService")
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;


    @Override
    public List<Account> findAllAccount() {
        return accountDao.findAllAccount();
    }

    @Override
    public void saveAccount(Account account) {
        accountDao.saveAccount(account);
    }
}
```

然后，applicationContext.xml里的这个类的bean配置就可以去掉了

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
        <!--配置组件扫描-->
        <context:component-scan base-package="com.bingo.learn"/>

        <!--配置QueryRunner-->
        <bean id="runner" class="org.apache.commons.dbutils.QueryRunner" scope="prototype">
                <constructor-arg name="ds" ref="dataSource"/>
        </bean>
        <!-- 配置数据源 -->
        <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
                <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/spring"/>
                <property name="username" value="root"/>
                <property name="password" value="Bingoing"/>
        </bean>
</beans>
```



> Tip
>
> QueryRunner 是第三方库里的方法，我们不可能修改源码。因为他没有set方法，没法使用set方法注入，但是他正好有参数为DataSource的构造方法
>
> ```java
> public QueryRunner(DataSource ds) {
>   super(ds);
> }
> ```
>
> 所以这里使用的是带参构造方法的注入方法。



然后运行AccountServiceTest里的单元测试，正常情况下应该是可以查询和插入数据的。

以上代码虽然采用注解也实现了简单的CRUD，但我们发现还是有几处让人跃跃欲试的地方，能否也能用注解代替

> 1. 配置文件剩下的几个配置：组件扫描、第三方库对象QueryRunner、数据源
>
>    ```xml
>    <!--配置组件扫描-->
>    <context:component-scan base-package="com.bingo.learn"/>
>          
>    <!--配置QueryRunner-->
>    <bean id="runner" class="org.apache.commons.dbutils.QueryRunner" scope="prototype">
>      <constructor-arg name="ds" ref="dataSource"/>
>    </bean>
>    <!-- 配置数据源 -->
>    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
>      <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
>      <property name="url" value="jdbc:mysql://localhost:3306/spring"/>
>      <property name="username" value="root"/>
>      <property name="password" value="Bingoing"/>
>    </bean>
>    ```
>
>    
>
> 2. 测试类中的重复代码
>
>    ```java
>    ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
>    AccountService accountService = app.getBean(AccountService.class);
>    ```



mmp,铺垫了这么久,新注解终于要闪亮登场了



##### ① `@Configuration`

> 作用
>
> 指定当前类是一个配置类，用来取代applicationContext.xml ,当创建容器时，从该类加载注解

创建一个类 config包下的SpringConfiguration ,加上注解 `@Configuration`就可以将其看成applicationContext.xml了

```java
@Configuration
public class SpringConfiguration {
}
```



虽然已经配置文件已经用类来代替了，但是里面的配置内容可不会因为你类上带个@Configuration 就自动装配的，他们有自己对应的注解的



##### ② `@ComponentScan`

> 用于指定Spring在初始化容器时要扫描的包。和Spring配置文件中的<context:component-scan base-package="com.bingo.learn"/>作用是一样的。

```java
@Configuration
@ComponentScan("com.bingo.learn")
public class SpringConfiguration {
}
```

配置好了要扫描的包，剩下的QueryRunner对象和数据源用什么来替代呢？接下来就得看@Bean了



##### ③ `@Bean`

> 作用
>
> 该注解只能写在方法上，用于将当前方法的返回值做为bean对象存入Spring的IOC容器中
>
> Tip
>
> 属性name: 给当前@Bean注解方法创建的对象指定一个名称(即 bean的 id)，当不写时，默认值为当前方法的名称



再看配置文件 

```xml
<!--配置QueryRunner-->
<bean id="runner" class="org.apache.commons.dbutils.QueryRunner" scope="prototype">
  <constructor-arg name="ds" ref="dataSource"/>
</bean>
<!-- 配置数据源 -->
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
  <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
  <property name="url" value="jdbc:mysql://localhost:3306/spring"/>
  <property name="username" value="root"/>
  <property name="password" value="Bingoing"/>
</bean>
```

其中数据源是以set方法注入的，且根据之前所学，知道它其实调用的无参构造函数生成的bean对象,然后将数据通过set方法装配。

而runner是以有参构造注入，其中以数据源作为参数，那么我们就可以反向推到出以下实现：

```java
@Configuration
@ComponentScan("com.bingo.learn")
public class SpringConfiguration {
    @Bean(name = "dataSource")
    public DataSource createDataSource() {
        DruidDataSource dds = new DruidDataSource();
        dds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dds.setUsername("root");
        dds.setPassword("Bingoing");
        dds.setUrl("jdbc:mysql://localhost:3306/spring");
        return dds;
    }

    @Bean(name = "runner")
  	@Scope("prototype")
    public QueryRunner createQueryRunner(DataSource dataSource) {
        return new QueryRunner(dataSource);
    }
}
```

上面的代码，createDataSource 对应的就是

```xml
<!-- 配置数据源 -->
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
  <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
  <property name="url" value="jdbc:mysql://localhost:3306/spring"/>
  <property name="username" value="root"/>
  <property name="password" value="root"/>
</bean>
```

通过默认构造函数得到对象，然后调用set方法将属性值装配，通过@Bean将方法返回值,也就是DataSource的对象，id = "dataSource" 注入到容器中。

而createQueryRunner 则是对应

```xml
<!--配置QueryRunner-->
<bean id="runner" class="org.apache.commons.dbutils.QueryRunner" scope="prototype">
  <constructor-arg name="ds" ref="dataSource"/>
</bean>
```

QueryRunner 通过其构造函数，接收的参数就是刚注入的id为`dataSource`的数据源对象，返回一个QueryRunner对象，然后再通过@Bean注解注入到容器中，bean 的id为"runner" 。

至此，applicationContext.xml 就完全被`SpringConfiguration`替代了。

我猜，这时候手速如闪电般的你可能已经把这个配置文件删除了，信心满满的运行单元测试发现，报错了

`Caused by: java.io.FileNotFoundException: class path resource [applicationContext.xml] cannot be opened because it does not exist`

原因在于 

```java
ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
```

这里的上下文的获取还需要用到配置文件，但此时找不到了。

删除没毛病，毕竟applicationContext.xml已经空囊一个，留着无用了。

那ApplicationContext 该怎么获取呢，你是否还记得大明湖畔的 [AnnotationConfigApplicationContext](#annotationConfigApplicationContext)？

> 当使用注解配置容器对象时，需要使用此类来创建 Spring 容器并读取注解

所以，修改下单元测试

```java
public class AccountServiceTest {

    @Test
    public void testFindAll() {
        ApplicationContext app = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        AccountService accountService = app.getBean(AccountService.class);
        List<Account> accounts = accountService.findAllAccount();
        for(Account account : accounts){
            System.out.println(account);
        }
    }
    ...
    ...
}
```

运行单元测试，结果如下

![image-20211206160537201](https://tva1.sinaimg.cn/large/008i3skNly1gx46co2844j30oe03qaag.jpg)

说明applicationContext.xml可以彻底说拜拜了。





那么完事了么，显然创建数据源的四个基本参数配置又都写死在类中了，怎么把他们也给摘出来呢，那就得用到下一个注解了

##### ④ `@PropertySource`

> 作用
>
> 用于加载 xxx.properties 文件中的配置。例如我们配置数据源时，可以把连接数据库的信息写到 properties 配置文件中，就可以使用此注解指定 properties 配置文件的位置
>
> Tip
>
> value[]:用于指定 properties 文件位置。如果是在类路径下，需要写上 `classpath:`

创建一个jdbc.properties文件，放置到resources目录下，内容就是数据源的四个基本配置参数

```properties
jdbc.driver = com.mysql.cj.jdbc.Driver
jdbc.url = jdbc:mysql://localhost:3306/spring
jdbc.username = root
jdbc.password = root
```

然后使用方法如下

```java
@Configuration
@ComponentScan("com.bingo.learn")
@PropertySource("classpath:jdbc.properties")
public class SpringConfiguration {
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    @Bean(name = "dataSource")
    public DataSource createDataSource() {
        DruidDataSource dds = new DruidDataSource();
        dds.setDriverClassName(driver);
        dds.setUsername(username);
        dds.setPassword(password);
        dds.setUrl(url);
        return dds;
    }

    @Bean(name = "runner")
    @Scope("prototype")
    public QueryRunner createQueryRunner(DataSource dataSource) {
        return new QueryRunner(dataSource);
    }
}
```

单元测试结果也正常，说明没有问题

至此，其实已经就算是完全用注解方式代替了之前的XML配置方式。



##### ⑤ `@Import`

> 作用
>
> 用于导入其他配置类
>
> Tip
>
> 属性value: 用于指定其他配置类的字节码
>
> 当使用了@Import后，有@Import注解的类就是父配置类，导入的就是子配置类



实际开发中，Spring的配置内容非常多，如果都放到一个里面，就会繁杂臃肿，解决办法就是将部分配置拆解拆解后，各个配置文件之间如何关联的呢？

在XML中是通过一个叫`<import/>`的标签引入

> 补充
>
> `applicationContext.xml`
>
> ```xml
> <?xml version="1.0" encoding="UTF-8"?>
> <beans xmlns="http://www.springframework.org/schema/beans"
>        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
>        xmlns:context="http://www.springframework.org/schema/context"
>        xsi:schemaLocation="http://www.springframework.org/schema/beans
>        http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
>         <!--配置组件扫描-->
>         <context:component-scan base-package="com.bingo.learn"/>
>   			<!--引入其他配置文件-->
> 				<import resource="applicationContext-jdbc.xml"/>
> </beans>
> ```
>
> `applicationContext-jdbc.xml`
>
> ```xml
> <?xml version="1.0" encoding="UTF-8"?>
> <beans xmlns="http://www.springframework.org/schema/beans"
>        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
>        xmlns:context="http://www.springframework.org/schema/context"
>        xsi:schemaLocation="http://www.springframework.org/schema/beans
>        http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
>        
>         <!--配置QueryRunner-->
>         <bean id="runner" class="org.apache.commons.dbutils.QueryRunner" scope="prototype">
>                 <constructor-arg name="ds" ref="dataSource"/>
>         </bean>
>         <!-- 配置数据源 -->
>         <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
>                 <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
>                 <property name="url" value="jdbc:mysql://localhost:3306/spring"/>
>                 <property name="username" value="root"/>
>                 <property name="password" value="Bingoing"/>
>         </bean>
> </beans>
> ```
>
> 



而注解方式是利用 `@Import` 

比如现在的数据库连接相关的配置抽离出来，单独作为一个配置`JdbcConfiguration` 

```java
@Configuration
@PropertySource("classpath:jdbc.properties")
public class JdbcConfiguration {
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    @Bean(name = "dataSource")
    public DataSource createDataSource() {
        DruidDataSource dds = new DruidDataSource();
        dds.setDriverClassName(driver);
        dds.setUsername(username);
        dds.setPassword(password);
        dds.setUrl(url);
        return dds;
    }

    @Bean(name = "runner")
    @Scope("prototype")
    public QueryRunner createQueryRunner(DataSource dataSource) {
        return new QueryRunner(dataSource);
    }
}
```



最终 `SpringConfiguration` 变成

```java
@Configuration
@ComponentScan("com.bingo.learn")
@Import({JdbcConfiguration.class})
public class SpringConfiguration {
    
}
```

> Tip
>
> 1. 当配置类作为AnnotationConfigApplicationContext对象创建的参数时，可以省略 `@Configuration` 不写
>
>    所以 `SpringConfiguration` 可以不用写
>
> 1. 此时，`SpringConfiguration`用 @Import 引入其他配置类后，其他配置类也可以不用写 @Configuration 注解

至此，我们才算真正的改造结束，实现了完全注解方式开发。





#### （6）填坑

之前留了个小坑，就是 [`@Qualifier `](#qualifier) 如何在给方法参数注入时可以单独使用？

我们以`JdbcConfiguration`为例，假如我有两个数据源 一个之前的`spring`表，另有一个`spring2`表，稍作改造，示例代码如下：

```java
@Configuration
@PropertySource("classpath:jdbc.properties")
public class JdbcConfiguration {
    ...
    ...

    @Bean(name = "dataSource1")
    public DataSource createDataSource() {
        ...
        ...
        dds.setUrl(url);
        return dds;
    }

  	@Bean(name = "dataSource2")
    public DataSource createDataSource2() {
        ...
        ...
        dds.setUrl("jdbc:mysql://localhost:3306/spring2");
        return dds;
    }
    @Bean(name = "runner")
    @Scope("prototype")
    public QueryRunner createQueryRunner(DataSource dataSource) {
        return new QueryRunner(dataSource);
    }
}
```

这时候，IOC容器有两个数据源的bean示例 dataSource1 和 dataSource2 ,但是此时 `createQueryRunner` 的参数名为 dataSource ,那么之前讲过，这种情况下，IOC容器里有两个类型相同但是名称不同的bean对象存在，Spring容器不知道该用哪一个给你注入到参数dataSource中了，这个时候就需要你利用 @Qualifier 来指定了，也就是如下示例代码

```java
@Bean(name = "runner")
@Scope("prototype")
public QueryRunner createQueryRunner(@Qualifier("dataSource2") DataSource dataSource) {
  return new QueryRunner(dataSource);
}
```

这时候连接的就是`spring2`表了

⑥ 小结 

原始注解表

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gx49wnyhftj30uq0kp0v0.jpg" alt="img" style="zoom:50%;" />

### 5. XML和注解方式的选择

基本原则是

* 如果是自定义的类对象，建议采用注解
* 如果是第三方的类对象，建议采用xml

总而言之

> 小孩子才做选择题，成年人当然是全都要,怎么方便怎么来







# 三、AOP

### 11.1 概念

> AOP 为 Aspect Oriented Programming 的缩写，意思为面向切面编程，是通过预编译方式和运行期动态代理 实现程序功能的统一维护的一种技术。
>
> AOP 是 OOP 的延续，是软件开发中的一个热点，也是Spring框架中的一个重要内容，是函数式编程的一种衍 生范型。利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序 的可重用性，同时提高了开发的效率。

### 11.2 AOP 的作用及其优势

> 作用:在程序运行期间，在不修改源码的情况下对方法进行功能增强 
>
> 优势:减少重复代码，提高开发效率，并且便于维护

### 11.3 AOP 的底层实现

假如说我想买一辆二手车，而你想卖一辆车，先不说单纯信息匹配上彼此中间经历的波折，就是后续一系列的车子过户流程也够彼此折腾的。我不过想买辆车，你不过想卖一辆车，遇到个一年一个小目标的人，中间耗费的人力时间成本就太高了。于是出现了一个职业--二手车中介，他负责为我们彼此匹配客户和房源，最终我买到了合适的车子，你卖出了预期的价格。

整个过程，我们就可以理解为是一个代理模式：代理模式给某一个对象提供一个代理对象，并由代理对象控制对被代理对象的引用，其特征是代理类和被代理类都实现相同的接口，通俗将就是 你中介得有二手车可以卖，不然我找你干嘛。

代理模式的好处

> - **中介隔离作用：**在某些情况下，一个客户类不想或者不能直接引用一个被代理对象，而代理类对象可以在客户类和被代理对象之间起到中介的作用
> - **开闭原则，增加功能：**代理类除了是客户类和被代理类的中介之外，我们还可以通过给代理类增加额外的功能来扩展被代理类的功能，这就是功能增强。这样只需要修改代理类而不需要再修改被代理类，符合代码设计的开闭原则



实际上，AOP 的底层是通过 Spring 提供的的动态代理技术实现的。在运行期间，Spring通过动态代理技术动态的生成代理对象，代理对象方法执行时进行增强功能的介入，在去调用目标对象的方法，从而完成功能的增强。

### 11.4 AOP 的动态代理技术

其常用的动态代理技术

> * JDK 代理 : 基于接口的动态代理技术 
>
> * cglib 代理: 基于子类的动态代理技术

![image-20211124160620950](https://tva1.sinaimg.cn/large/008i3skNgy1gwqaxt2prxj31cs0g2wgd.jpg)

#### 11.4.1 基于JDK的动态代理实现

就以上面举例说明

> 你找到中介，要卖车，中介负责去找合适的买车客户，中间繁杂的过程交由中介处理

① 代理类和被代理类共同实现的接口 （都有卖车的能力）

```java
public interface ISeller {
    void sellCars(float money);
}

```

② 被代理类（卖家）

```java
public class Seller implements ISeller {
    /**
     * 卖车
     * @param money
     */
    public void sellCars(float money){
        System.out.println("卖车，拿到钱："+money);
    }
}
```

③ 增强功能 （中介干的事）

```java
/**
 * 增强功能
 */
public class Enhancements {
    public float before(float money){
        System.out.println("找客户,过户手续等");
        System.out.println("收取中介费："+money*0.1f);
    return money*0.9f;
    }
    public void after(){
        System.out.println("售后服务等");
    }
}
```

④ 模拟客户（买车的过程）

原先的方式，买家直接找到你，你把车卖给客户，你拿到钱。

```java
public class Client {
    public static void main(String[] args) {
        Seller seller = new Seller();
        seller.sellCars(1000000f);
}
```



动态代理模式 -- 基于接口的动态代理

```java
public class Client {
    public static void main(String[] args) {
        // 原先的方式，直接找生产者购买
        Seller seller = new Seller();
        Enhancements enhancements = new Enhancements();
        //现在的方式 ,通过代理商
        /*
         * 动态代理
         * 特点：字节码随用随创建，随用随加载
         * 作用：不修改源码的基础上对方法增强
         * 分类：基于接口的动态代理
         *      基于子类的动态代理
         * 基于接口的动态代理
         *      涉及的类：Proxy
         *      提供者：JDK官方
         * 如果创建代理对象
         *      使用Proxy类中的newProxyInstance方法
         * 创建代理对象的要求
         *      被代理类至少实现一个接口，没有则不能使用
         *  newProxyInstance方法的参数：
         *  Classloader: 用于加载代理对象字节码的，和被代理对象使用的类加载器相同，所以这里你代理谁就写谁的类加载器：producer.getClass().getClassLoader() -固定写法
         *  Class[]: 字节码数组 用于然代理对象和被代理对象有相同的方法，怎么有相同的方法：就是两者都实现相同的接口 ，所以你代理谁就写谁实现的接口字节码数组 - 固定写法
         *  InvocationHandler:用于写增强的代码，这里实现具体的代理内容，一般都是写该接口的实现类，通常是匿名内部类，不必须
         *
         */
        ISeller iSeller = (ISeller) Proxy.newProxyInstance(seller.getClass().getClassLoader(), seller.getClass().getInterfaces(), new InvocationHandler() {
            /**
             * 作用：执行被代理对象的任何接口方法都会经过该方法
             * @param proxy 代理对象的引用
             * @param method 当前执行的方法
             * @param args 当前执行方法所需参数
             * @return 和被代理对象方法有相同的返回值
             * @throws Throwable
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //提供增强代码
                Object ret = null;
                // 1.获取方法执行的参数
                Float money = (Float) args[0];
                money = enhancements.before(money);
                /**
                 * invoke方法参数
                 * Object:指的是谁的方法，是被代理对象的方法。所以这里是被代理对象
                 * args ：方法参数
                 */
                ret = method.invoke(seller, money);
                enhancements.after();
                return ret;
            }
        });
        iSeller.sellCars(100000f);

    }
}

```

![image-20211207151945466](https://tva1.sinaimg.cn/large/008i3skNly1gx5ana4l6rj30n104b3yn.jpg)

#### 11.4.2 基于cglib的动态代理实现

接口已经不是必须的了，即可以对普通的非final类进行增强



① 被代理类（卖家）

```java
public class Seller implements ISeller {
    /**
     * 卖车
     * @param money
     */
    public void sellCars(float money){
        System.out.println("卖车，拿到钱："+money);
    }
}
```

② 增强功能 （中介干的事）

```java
/**
 * 增强功能
 */
public class Enhancements {
    public float before(float money){
        System.out.println("找客户,过户手续等");
        System.out.println("收取中介费："+money*0.1f);
    return money*0.9f;
    }
    public void after(){
        System.out.println("售后服务等");
    }
}
```

④ 模拟客户（买车的过程）

```java
public class Client {
    public static void main(String[] args) {
        Seller seller = new Seller();
        Enhancements enhancements = new Enhancements();
        /**
         * 动态代理 cglib
         * 特点：字节码随用随创建，随用随加载
         * 作用：不修改源码的基础上对方法增强
         * 分类：基于接口的动态代理 和 基于子类的动态代理
         * 基于子类的动态代理
         *      涉及的类：Enhancer
         *      提供者：cglib
         * 如果创建代理对象
         *      使用Enhancer类中的create方法
         * 创建代理对象的要求
         *      被代理类不能是最终类(final)，也就是必须有子类
         *  create方法的参数：
         *  Class: 字节码
         *      用于指定被代理对象的字节码 想代理谁就写谁的class
         *
         *  Callback:
         *         用于写增强的代码，这里实现具体的代理内容，一般都是写该接口的实现类，通常是匿名内部类，不必须
         *          一般写的都是该接口的子接口实现类：MethodInterceptor
         */
        Seller proxySeller = (Seller) Enhancer.create(seller.getClass(), new MethodInterceptor() {
            /**
             * 执行被代理对象的任何方法都会经过该方法
             *
             * @param  proxy 代理对象的引用
             * @param method 当前执行的方法
             * @param args 当前执行方法所需参数
             * @param methodProxy 当前执行方法的代理对象
             * @return
             * @throws Throwable
             */
            @Override
            public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                //提供增强代码
                Object ret = null;
                // 1.获取方法执行的参数
                Float money = (Float) args[0];
                enhancements.before(money);
                /**
                 * invoke方法参数
                 * Object:指的是谁的方法，是被代理对象的方法。所以这里是被代理对象
                 * args ：方法参数
                 */
                ret = method.invoke(seller, money * 0.9f);
                enhancements.after();
                return ret;
            }
        });
        proxySeller.sellCars(100000f);
    }
}
```

![image-20211207151945466](https://tva1.sinaimg.cn/large/008i3skNly1gx5ana4l6rj30n104b3yn.jpg)





### 11.5 Spring的AOP 简介

#### 11.5.1 AOP 相关概念

> Spring 的 AOP 实现底层就是对上面的动态代理的代码进行了封装，封装后我们只需要对需要关注的部分进行代码编 写，并通过配置的方式完成指定目标的方法增强。

Spring中AOP的常用术语

* Target(目标对象):代理的目标对象
 * Proxy (代理):一个类被 AOP 织入增强后，就产生一个结果代理类
 * Joinpoint(连接点):**所谓连接点是指那些可以被拦截到的点，也就是可以被增强的方法** 。在spring中,这些点指的是方法，因为spring只支持方法类型的连接点

 * Pointcut(切入点):所谓切入点是指我们要对哪些 Joinpoint 进行拦截的定义-- **真正被增强的方法**  （需要配置）
 * Advice(通知/ 增强):所谓通知是指拦截到 Joinpoint 之后所要做的事情就是通知 （需要代码实现，并配置）
 * Aspect(切面):是切入点和通知(引介)的结合 （需要配置）
 * Weaving(织入):是指把增强应用到目标对象来创建新的代理对象的过程。spring采用动态代理织入，而

AspectJ采用编译期织入和类装载期织入 （简单理解：就是配置的过程）

#### 11.5.2 开发过程

① 需要编写的内容

> * 编写核心业务代码(目标类的目标方法)
> * 编写切面类，切面类中有通知(增强功能方法)
> * 在配置文件中，配置织入关系，即将哪些通知与哪些连接点进行结合

② AOP 技术实现的内容

切入点配置后，Spring 框架就开始监控切入点方法的执行。一旦监控到切入点方法被运行，使用代理机制，动态创建目标对象的 代理对象，根据通知类别，在代理对象的对应位置，将通知对应的功能织入，完成完整的代码逻辑运行。

③ AOP底层采用哪种代理方式（jdk & cglib）

在 spring 中，框架会根据目标类是否实现了接口来判断决定采用哪种动态代理的方式。

* 若实现了接口，就是基于jdk的动态代理方式

* 若未实现接口，就采用基于cglib的动态代理方式

#### 11.5.3 AOP知识要点

> * aop:面向切面编程
>
> * aop底层实现:基于JDK的动态代理 和 基于Cglib的动态代理 
> * aop的重点概念:Pointcut(切入点):被增强的方法 Advice(通知/ 增强):封装增强业务逻辑的方法 Aspect(切面):切点+通知 Weaving(织入):将切点与通知结合的过程
> * 开发明确事项: 谁是切点(切点表达式配置) 谁是通知(切面类中的增强方法) 将切点和通知进行织入配置

### 11.6 基于XML的AOP开发

#### 11.6.1 快速入门

① 导入 AOP 相关坐标

```xml
<!--导入AOP相关坐标--> 
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-context</artifactId>
  <version>5.3.12</version>
</dependency>
<dependency>
  <groupId>org.aspectj</groupId>
  <artifactId>aspectjweaver</artifactId>
  <version>1.8.6</version>
</dependency>
```

② 创建目标接口和目标类(内部有切点)

```java
/**
 * 目标接口
 */
public interface TargetInterface {
    void save();
}

/**
 * 目标类
 */
public class Target implements TargetInterface {
    public void save() {
        System.out.println("save running ...");
      	// int i = 1/0; // 测试异常抛出增强
    }
}

```



③ 创建切面类(内部有增强方法)

```java
/**
 * 切面类
 */
public class MyAspect {
   // 前置通知
    public void before(){
        System.out.println("前置增强...");
    }
}

```



④ 将目标类和切面类的对象创建权交给 spring  , 在 applicationContext.xml 中配置织入关系 

```xml
<!--目标对象-->
<bean id="target" class="com.bingo.learn.aop.Target"/>
<!--通知对象-->
<bean id="myAspect" class="com.bingo.learn.aop.MyAspect"></bean>
<!--配置织入 告诉spring框架 哪些方法（切点） 需要进行哪些增强（前置、后置...）-->
<aop:config>
  <!--声明切面类 告诉spring框架哪一个bean是切面-->
  <aop:aspect ref="myAspect">
    <!--切面：切点+通知  method 指定增强方法 pointcut指定 要被增强的方法-->
    <!--前置切面-->
    <aop:before method="before" pointcut="execution(* com.bingo.learn.aop.*.*(..))"/>
</aop:config>
```

⑥ 测试代码

```java
@RunWith(SpringJUnit4ClassRunner.class) // 指定测试引擎
@ContextConfiguration("classpath:applicationContext.xml") //指定配置文件
public class AopTest {
    @Autowired //测试谁就注入谁
    private TargetInterface target;
    @Test
    public void test1(){
        target.save();
    }
}

>>
前置增强...
save running ...

Process finished with exit code 0
```

看，好处显而易见

> 目标方法专注于自身的核心业务代码，辅助功能定义在切面方法，且切面方法可复用

#### 11.6.2  XML 配置 AOP 

① 配置

切点表达式语法:

**execution([修饰符] 返回值类型 包名.类名.方法名(参数))** 

> * 访问修饰符可以省略
>
> * 返回值类型、包名、类名、方法名可以使用星号* 代表任意
> * 包名与类名之间一个点 . 代表当前包下的类，两个点 .. 表示当前包及其子包下的类
> * 参数列表可以使用两个点 .. 表示任意个数，任意类型的参数列表 

```xml
execution(public void com.bingo.aop.Target.method()) 
execution(void com.bingo.aop.Target.*(..)) 
execution(* com.bingo.aop.*.*(..))  // 最常用
execution(* com.bingo.aop..*.*(..))
execution(* *..*.*(..)) // 代表所有方法
```

上例配置也可以为

```xml
<!--目标对象-->
<bean id="target" class="com.bingo.learn.aop.Target"/>
<!--通知对象-->
<bean id="myAspect" class="com.bingo.learn.aop.MyAspect"></bean>
<!--配置织入 告诉spring框架 哪些方法（切点） 需要进行哪些增强（前置、后置...）-->
<aop:config>
  <!--声明切面类 告诉spring框架哪一个bean是切面类-->
  <aop:aspect ref="myAspect">
    <!--切面：切点+通知  method 指定增强方法 pointcut 切点表达式 指定 要被增强的方法 -->
    <!--<aop:before method="before" pointcut="execution(public void com.bingo.learn.aop.Target.save())"/>-->
    <!--切点表达式的意思：aop包下的任意类的方法-->
    <aop:before method="before" pointcut="execution(* com.bingo.learn.aop.*.*(..))"/>
  </aop:aspect>
</aop:config>
```

② 通知的类型

通知的配置语法:

> <aop:通知类型 method=“切面类中方法名” pointcut=“切点表达式"/>

| 名称         | 标签                    | 说明                                                         |
| ------------ | ----------------------- | ------------------------------------------------------------ |
| 前置通知     | `<aop:before>`          | 用于配置前置通知。指定增强的方法在切入点方法之前执行         |
| 后置通知     | `<aop:after-returning>` | 用于配置后置通知。指定增强的方法在切入点方法之后执行         |
| 环绕通知     | `<aop:around>`          | 用于配置环绕通知。指定增强的方法 在切入点方法之前和之后都执行 |
| 异常抛出通知 | `<aop:throwing>`        | 用于配置异常抛出通知。指定增强的方法在出现异常时执行         |
| 最终通知     | `<aop:after>`           | 用于配置正常返回通知。无论增强方式执行是否有异常都会执行     |

> Tip
>
> 后置通知与异常通知，两者互斥



③ 完整示例

```java
/**
* 切面类
*/
public class MyAspect {
    // 前置通知
    public void before(){
        System.out.println("前置增强...");
    }
    // 正常返回通知
    public void afterReturning(){
        System.out.println("正常返回通知...");
    }
    // 环绕通知
    // Proceeding JoinPoint 正在执行的连接点 == 切点
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("环绕前置通知...");
        Object proceed = proceedingJoinPoint.proceed();
        System.out.println("环绕后置通知...");
        return proceed;
    }
    // 异常通知
    public void afterThrowing(){
        System.out.println("异常抛出通知...");
    }
    // 后置通知
    public void after(){
        System.out.println("后置通知...");
    }
}

```

配置文件

```xml
<!--目标对象-->
<bean id="target" class="com.bingo.learn.aop.Target"/>
<!--通知对象-->
<bean id="myAspect" class="com.bingo.learn.aop.MyAspect"></bean>
<!--配置织入 告诉spring框架 哪些方法（切点） 需要进行哪些增强（前置、后置...）-->
<aop:config>
  <!--声明切面类 告诉spring框架哪一个bean是切面-->
  <aop:aspect ref="myAspect">
    <!--切面：切点+通知  method 指定增强方法 pointcut指定 要被增强的方法-->
    <!--前置切面-->
    <aop:before method="before" pointcut="execution(* com.bingo.learn.aop.*.*(..))"/>
    <!--后置切面-->
    <aop:after-returning method="afterReturning" pointcut="execution(* com.bingo.learn.aop.*.*(..))"/>
    <!--环绕切面-->
    <aop:around method="around" pointcut="execution(* com.bingo.learn.aop.*.*(..))"/>
    <!--异常切面-->
    <aop:after-throwing method="afterThrowing" pointcut="execution(* com.bingo.learn.aop.*.*(..))"/>
    <!--最终切面-->
    <aop:after method="after" pointcut="execution(* com.bingo.learn.aop.*.*(..))"/>
  </aop:aspect>
</aop:config>
```



④ 切点表达式的抽取

当多个增强的切点表达式相同时，可以将切点表达式进行抽取，在增强中使用 pointcut-ref 属性代替 pointcut 属性来引用抽取后的切点表达式。

```xml
<!--目标对象-->
<bean id="target" class="com.bingo.learn.aop.Target"/>
<!--通知对象-->
<bean id="myAspect" class="com.bingo.learn.aop.MyAspect"></bean>
<!--配置织入 告诉spring框架 哪些方法（切点） 需要进行哪些增强（前置、后置...）-->
<aop:config>
  <!--声明切面类 告诉spring框架哪一个bean是切面类-->
  <aop:aspect ref="myAspect">
    <!--抽取切点表达式-->
    <aop:pointcut id="myPointcut" expression="execution(* com.bingo.learn.aop.*.*(..))"/>
    <!--前置切面-->
    <aop:before method="before" pointcut-ref="myPointcut"/>
    <!--后置返回切面-->
    <aop:after-returning method="afterReturning" pointcut-ref="myPointcut"/>
    <!--环绕切面-->
    <aop:around method="around" pointcut-ref="myPointcut"/>
    <!--异常切面-->
    <aop:after-throwing method="afterThrowing" pointcut-ref="myPointcut"/>
    <!--后置切面-->
    <aop:after method="after" pointcut-ref="myPointcut"/>

  </aop:aspect>
</aop:config>
```

#### 11.6.3 知识要点

* aop织入的配置

```xml
<aop:config>
  <aop:aspect ref=“切 面 类 ”>
  	<aop:before method=“通 知 方 法 名 称” pointcut=“切 点 表 达 式 "/>
  </aop:aspect>
</aop:config>
```

* 通知的类型:前置通知、正常返回通知、环绕通知、异常抛出通知、后置通知 
* 切点表达式的写法



### 11.7 基于注解的AOP开发

#### 11.7.1 快速入门

基于注解的aop开发步骤:
① 创建目标接口和目标类(内部有切点)

```java
/**
 * 目标接口
 */
public interface TargetInterface {
    void save();
}

/**
 * 目标类
 */
public class Target implements TargetInterface {
    public void save() {
        System.out.println("save running ...");
      	// int i = 1/0; // 测试异常抛出增强
    }
}
```



② 创建切面类(内部有增强方法)

```java
/**
 * 切面类
 */
public class MyAspect {
   // 前置通知
    public void before(){
        System.out.println("前置增强...");
    }
}
```



③ 将目标类和切面类的对象创建权交给 spring

```java
/**
 * 目标类
 */
@Component("target") //交给spring容器
public class Target implements TargetInterface {
    public void save() {
        System.out.println("save running ...");
    }
}
/**
 * 切面类
 */
@Component("myAspect")
@Aspect //告知spring 该类是一个切面类
public class MyAspect {
    // 前置通知
    public void before(){
        System.out.println("前置通知...");
    }
    ...
    ...
}

```



④ 在切面类中使用注解配置织入关系

```java
/**
 * 切面类
 */
@Component("myAspect")
@Aspect //告知spring 该类是一个切面类
public class MyAspect {
    // 配置前置通知
    @Before("execution(* com.bingo.learn.anno.*.*(..))")
    public void before() {
        System.out.println("前置通知...");
    }

    // 正常返回通知
    @AfterReturning("execution(* com.bingo.learn.anno.*.*(..))")
    public void afterReturning() {
        System.out.println("正常返回通知...");
    }

    // 环绕通知
    // Proceeding JoinPoint 正在执行的连接点 == 切点
    @Around("execution(* com.bingo.learn.anno.*.*(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("环绕前置通知...");
        Object proceed = proceedingJoinPoint.proceed();
        System.out.println("环绕后置通知...");
        return proceed;
    }

    // 异常返回通知
    @AfterThrowing("execution(* com.bingo.learn.anno.*.*(..))")
    public void afterThrowing() {
        System.out.println("异常通知...");
    }

    // 后置通知
    @After("execution(* com.bingo.learn.anno.*.*(..))")
    public void after() {
        System.out.println("后置通知...");
    }
}
```



⑤ 在配置文件中开启组件扫描和 AOP 的自动代理 

```xml
<!--applicationContext-anno.xml 开启组件扫描-->
<context:component-scan base-package="com.bingo.learn.anno"/>
<!--aop自动代理 加上这句，aop相关注解才起作用-->
<aop:aspectj-autoproxy/>
```



⑥ 测试

```java
/**
 * 测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-anno.xml")
public class AnnoTest {
    @Autowired
    private TargetInterface targetInterface;
    @Test
    public void test(){
        targetInterface.save();
    }
}



>>>

环绕前置通知...
前置通知...
save running ...
正常返回通知...
后置通知...
环绕后置通知...

Process finished with exit code 0
```

![在这里插入图片描述](https://tva1.sinaimg.cn/large/008i3skNly1gwqo4aeenhj30s10d5dhu.jpg)

## @TODO 补充 上图不是很正确 后续更改

#### 11.7.2 切点表达式的抽取

同xml配置aop一样，我们可以将切点表达式抽取。抽取方式是在切面内定义方法，在该方法上使用@Pointcut注解定义切点表达式，然后在在增强注解中进行引用 :

```java
@Component("myAspect")
@Aspect //告知spring 该类是一个切面类
public class MyAspect {
    // 配置前置通知
    @Before("MyAspect.pointcut()")
    public void before() {
        System.out.println("前置通知...");
    }
  
		...
    ...
    ...
      
    //定义切点表达式
    @Pointcut("execution(* com.bingo.learn.anno.*.*(..))")
    public void pointcut() {

    }

```

#### 11.7.5 各类型的执行顺序探究

## @TODO ！！！！待补充！！！！！

#### 11.7.4 知识要点

① 注解aop开发步骤
1. 使用@Aspect标注切面类

2. 使用@通知注解标注通知方法
2. 在配置文件中配置aop自动代理`<aop:aspectj-autoproxy/>`

② 通知注解类型





# 四、Spring Web开发

## （一）三层架构介绍



> 我们的开发架构一般都是基于两种形式C/S架构（客户端/服务器），B/S 架构（浏览器服务器），在 JavaEE 开发中，几乎全都是基于 B/S 架构的开发。

B/S 架构中采用系统标准的三层架构表现层、业务层、持久 层。

* 表现层 (SpringMVC)

  也就是我们常说的 web 层。它负责接收客户端请求，向客户端响应结果，通常客户端使用 http 协议请求 web 层，web 需要接收 http 请求，完成 http 响应。
  表现层又包括

  * 展示层：负责结果的展示
  * 控制层:负责接收请求

  表现层依赖业务层，接收到客户端请求一般会调用业务层进行业务处理，并将处理结果响应给客户端。
  表现层的设计一般都使用 MVC 模型。(MVC 是表现层的设计模型，和其他层没有关系) 

* 业务层 (Spring)
  也就是我们常说的 service 层。它负责业务逻辑处理，和我们开发项目的需求息息相关。web 层依赖业 务层，但是业务层不依赖 web 层。
  业务层在业务处理时可能会依赖持久层，如果要对数据持久化需要保证事务一致性。(也就是我们说的，事务应该放到业务层来控制)

* 持久层 (MyBatis)
  也就是我们是常说的 dao 层。负责数据持久化，包括数据层即数据库和数据访问层，数据库是对数据进 行持久化的载体，数据访问层是业务层和持久层交互的接口，业务层需要通过数据访问层将数据持久化到数据库中。通俗的讲，持久层就是和数据库交互，对数据库表进行曾删改查的。

如图所示，使用Spring 开发web的三层架构图

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gx5c4rpkt6j316c0j4421.jpg" alt="image-20211207161102313" style="zoom:50%;" />

## （二）MVC模型

> MVC 全名是 Model View Controller，是模型(model)-视图(view)-控制器(controller)的缩写， 是一种用于设计创建 Web 应用程序表现层的模式。MVC 中每个部分各司其职

* Model(模型): 通常指的就是我们的数据模型,JavaBean的类，用来进行数据封装。作用一般情况下用于封装数据。 
* View(视图):  通常指的就是我们的 jsp 或者 html。作用一般就是展示数据的。 通常视图是依据模型数据创建的。 	
* Controller(控制器): 是应用程序中处理用户交互的部分。作用一般就是处理程序逻辑的。



## （三） web开发--使用之前方式 【了解】

在讲SpringMVC之前，我们先来回顾下传统的的web 开发模式:



首先创建一个webapp工程，目录主要结构如下



├── java
│   └── com
│       └── bingo
│           └── learn
│               ├── dao
│               │   ├── UserDao.java
│               │   └── impl
│               │       └── UserDaoImpl.java
│               ├── service
│               │   ├── UserService.java
│               │   └── impl
│               │       └── UserServiceImpl.java
│               └── web
│                   └── UserServlet.java
├── resources
│   └── applicationContext.xml
└── webapp
    ├── WEB-INF
    │   └── web.xml
    └── success.jsp



### 1. pom.xml 引入依赖坐标

```xml
<dependencies>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.3.12</version>
    </dependency>
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>servlet-api</artifactId>
      <version>2.5</version>
    </dependency>
    <dependency>
      <groupId>javax.servlet.jsp</groupId>
      <artifactId>jsp-api</artifactId>
      <version>2.1</version>
    </dependency>
  </dependencies>
```



### 2. 编写持久层和业务代码

`User`

```java
public class User {
    private String name;
    private int age;
    ...
    ...
}
```

`UserDao`

```java
public interface UserDao {
    void say(String name);
}
```

`UserDaoImpl`

```java
public class UserDaoImpl implements UserDao {
    public void save() {
        System.out.println("save user...");
    }
}
```

`UserService`

```java
public interface UserService {
    void save();
}
```

`UserServiceImpl`

```java
public class UserServiceImpl implements UserService {
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    
    public void save() {
        userDao.save();
    }
}
```



### 3. 配置applicationContext.xml 

> 注册UserDao和UserService

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="userDao" class="com.bingo.learn.dao.impl.UserDaoImpl"/>
    <bean id="userService" class="com.bingo.learn.service.impl.UserServiceImpl">
        <property name="userDao" ref="userDao"/>
    </bean>
</beans>
```



### 5.编写控制层



`UserServlet`

```java
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ApplicationContext app = new  ClassPathXmlApplicationContext("applicationContext.xml");
        UserService userService = app.getBean(UserService.class);
        userService.save();
        resp.getWriter().println("Hello Spring.");
    }
}
```

### 6. Web.xml中配置注册servlet组件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="2.4"
         xmlns="http://java.sun.com/xml/ns/j2ee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd">
    <servlet>
        <servlet-name>userServlet</servlet-name>
        <servlet-class>com.bingo.learn.web.UserServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>userServlet</servlet-name>
        <url-pattern>/userServlet</url-pattern>
    </servlet-mapping>
</web-app>
        
```

### 7. 测试

通过Tomcat去发布，运行访问请求 http://localhost:8080/userServlet 测试，显示



<img src="https://tva1.sinaimg.cn/large/008i3skNly1gx67l2zgp8j30wq05m3yx.jpg" alt="image-20211208101918746" style="zoom:50%;" />



### 8. 优化点

#### （1）使用监听器来解决多次加载配置文件，多次创建上下文对象

这里我们是简单的模拟了一个mvc框架模式，实际项目中，会有很多servlet组件，那么问题就来了，每次去调用方法，都需要通过`new ClasspathXmlApplicationContext(spring配置文件)` 方式获取应用上下文，从而从容器中获取bean ，这样的弊端是配置文件加载多次，应用上下文对象创建多次。

怎么解决呢？ -- 使用监听器。



> 在Web项目中，可以使用ServletContextListener监听Web应用的启动，我们可以在Web应用启动时，就加载Spring的配置文件，创建应用上下文对象ApplicationContext，在将其存储到最大的域servletContext域中，这样就可以在任意位置从域中获得应用上下文ApplicationContext对象了。

新建一个监听类 `ContextLoaderListener` 实现 `ServletContextListener`,在其初始化方法里实现读取配置文件，然后将Spring的应用上下文对象存入ServletContext域中

```java
public class ContextLoaderListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.setAttribute("app",app);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
```

当然还需要在web.xml 配置该监听器，不然没法使用

```xml
<web-app version="2.4"
         xmlns="http://java.sun.com/xml/ns/j2ee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd">
    <!--servlet注册-->
    <servlet>
        <servlet-name>userServlet</servlet-name>
        <servlet-class>com.bingo.learn.web.UserServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>userServlet</servlet-name>
        <url-pattern>/userServlet</url-pattern>
    </servlet-mapping>
    <!--配置监听器-->
    <listener>
        <listener-class>com.bingo.learn.listener.ContextLoaderListener</listener-class>
    </listener>
</web-app>
```

使用方法

```java
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = req.getSession().getServletContext();
        ApplicationContext app = (ApplicationContext) servletContext.getAttribute("app");
        UserService us = app.getBean(UserService.class);
        us.save();
        resp.getWriter().println("Hello Spring.");
    }
}
```

重新发布，访问请求 http://localhost:8080/userServlet 测试，结果和上面一致表明一切ok。



#### （2）配置web 全局初始化参数抽离配置文件的读取

一般可以通过全局初始化参数的配置，来替代`ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");`这个配置文件的名称是随意定义的，如果后续修改，代码中修改就不如直接在配置文件修改来的方便，也算是一个小解耦操作,配置也很简单，在web.xml加上

```xml
<web-app version="2.4"
         xmlns="http://java.sun.com/xml/ns/j2ee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd">
    ...
    ...
    <!--配置全局初始化参数-->
    <!--配置全局初始化参数-->
    <context-param>
        <!--名称可自定义-->
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:applicationContext.xml</param-value>
    </context-param>
    ...
    ...
</web-app>
```

然后通过ServletContext读取web.xml中全局初始化参数即可

```java
public class ContextLoaderListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
      	// 读取web.xml中的全局初始化参数
        String contextConfigLocation = servletContext.getInitParameter("contextConfigLocation");
        ApplicationContext app = new ClassPathXmlApplicationContext(contextConfigLocation);
        servletContext.setAttribute("app",app);
    }
   	...
    ...
}
```



#### （3）封装获取上下文的工具类

上述代码中，在取 `ApplicationContext `时的key，也需要封装下，毕竟很多后续会有很多地方用到这个，也存在修改的情况，每次改代码也麻烦不是，所以可以通过封装一个工具类来隐藏，如果修改就该工具类即可

```java
public class WebApplicationContextUtils {
    public static ApplicationContext getWebApplicationContext(ServletContext context){
        return (ApplicationContext) context.getAttribute("app");
    }
}
```

然后调用处修改

```java
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = req.getSession().getServletContext();
      	// 通过工具类来获取上下文
        ApplicationContext app = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        UserService us = app.getBean(UserService.class);
        us.save();
        resp.getWriter().println("Hello Spring.");
    }
}
```

优化完以上几点，发布测试，结果和上面一致，说明一切ok。





### 9. Spring集成web环境

其实上述的那些优化代码，Spring的web开发框架（spring-web）已然想到并解决了，spring-web提供了一个监听器ContextLoaderListener就是对上述功能的封装，该监听器内部加载Spring配置文件，创建应用上下文对象，并存储到ServletContext域中，提供了一个客户端工具WebApplicationContextUtils供使用者获得应用上下文对象。

所以我们需要做的只有两件事:

① 导入spring-web依赖坐标，并在web.xml中配置ContextLoaderListener监听器

② 使用WebApplicationContextUtils获得应用上下文对象ApplicationContext

#### （1）导入spring-web依赖坐标

```xml
<dependencies>
    ...
  	...
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-web</artifactId>
      <version>5.3.12</version>
    </dependency>
    ...
  	...
  </dependencies>
```

#### （2）使用Spring的`ContextLoaderListener`

web.xml中将我们自己实现的ContextLoaderListener替换为Spring提供的,

```xml
<web-app>
  ...
  <!--配置监听器-->
  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>
  ...
</web-app>
```

然后将 `UserServlet`中使用的 `WebApplicationContextUtils`替换成Spring-web为我们封装好的

这里只是介绍了Spring web框架某些用法，其实

而 spring-web 提供核心的HTTP集成，包括一些方便的Servlet过滤器，Spring HTTP Invoker，与其他Web框架和HTTP技术集成的基础结构，也就是说集成了spring-web ，即可完成web项目的开发。但是通常情况下，web开发中表现层一般都是采用MVC模式，而Spring也为我们提供了它的MVC框架(其依赖坐标spring-webmvc,其内部也集成了spring-web)，这也是目前主流的。接下来让我们开始继续卷吧。



## （四）SpringMVC

> SpringMVC 是一种基于 Java 的实现 MVC 设计模型的请求驱动类型的轻量级 Web 框架，属于 SpringFrameWork 的后续产品，已经融合在 Spring Web Flow 中。SpringMVC 已经成为目前最主流的MVC框架之一，并且随着Spring3.0 的发布，全面超越 Struts2，成为最优 秀的 MVC 框架。它通过一套注解，让一个简单的 Java 类成为处理请求的控制器，而无须实现任何接口。同时 它还支持 RESTful 编程风格的请求。

### 1. 快速入门案例

> 需求:客户端发起请求，服务器端接收请求，执行逻辑并进行视图跳转

开发步骤:

Spring mvc 简单的开发步骤

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gx6evq4wk4j31rq0mkwje.jpg" alt="image-20211208143137650" style="zoom: 33%;" />



① 导入SpringMVC相关坐标
② 配置SpringMVC核心控制器DispathcerServlet 

③ 创建Controller类和视图页面
④ 使用注解配置Controller类中业务方法的映射地址 

⑥ 配置SpringMVC核心文件 spring-mvc.xml
⑦ 请求测试

#### （1）导入SpringMVC相关坐标

pom.xml中导入必要的依赖做标

```xml
<dependencies>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.3.12</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>5.3.12</version>
    </dependency>
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>4.0.1</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>javax.servlet.jsp</groupId>
      <artifactId>jsp-api</artifactId>
      <version>2.2</version>
    </dependency>
  </dependencies>
```

> 因为spring-webmvc 内部集成了spring-web，所以这里只需导入spring-webmvc的坐标即可

#### （2）配置SpringMVC前置控制器

我们作为Web应用程序的开发者，最想从以下这些枯燥乏味的工作中抽身出来，只关注真正的业务逻辑实现。

- 把一个`HTTP request`交给它真正的处理方法
- 解析`HTTP request`的header和body中的数据，并把它们转换为DTO(数据传输对象)
- `Model-View-Controller`三方的交互
- 再把业务逻辑返回的DTO转换成`HTTP response`

等等这些工作就是由前置控制器 -- `DispatcherServlet` 来完成的, 称之为Spring MVC框架的心脏也不为过，当然这里不去深入，你现在只需要知道 SpringMVC中的`DispatcherServlet`正正就是提供这些服务的，这个核心组件接收所有传输到你应用的`HTTP request` 并帮你把请求分发到该去的地方。



web.xml中添加前置控制器的配置

```xml
<?xml version="1.0" encoding="UTF-8"?>

<web-app version="2.4"
         xmlns="http://java.sun.com/xml/ns/j2ee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd">
    <!--旧版的web 配置的 servlet注册-->
    <servlet>
        <servlet-name>userServlet</servlet-name>
        <servlet-class>com.bingo.learn.web.UserServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>userServlet</servlet-name>
        <url-pattern>/userServlet</url-pattern>
    </servlet-mapping>
    <!--配置监听器-->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    <!--配置全局初始化参数-->
    <context-param>
        <!--名称可自定义-->
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:applicationContext.xml</param-value>
    </context-param>
  
		 <!--配置前置控制器-->
    <servlet>
        <servlet-name>dispatcherServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--服务启动即加载创建对象-->
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>dispatcherServlet</servlet-name>
        <!--匹配规则，默认缺省-->
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

#### （3）创建controller层和视图页面

接下来我们就正式使用Spring MVC 来进行开发，之前项目中的的web层 就不用了。

① 新建一个视图页面 `success.jsp` 放到webapp/jsp目录下

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    Hello SpringMVC!
</body>
</html>
```



② 新建一个controller目录，创建 <span id="userController">`UserController`</span>

```java
@Controller
public class UserController {
    @RequestMapping("/save")
    public String save(){
        System.out.println("user saved...");
        return "jsp/success.jsp";
    }
}
```



#### （4）配置SpringMVC核心文件 spring-mvc.xml

因为我们使用到了注解，所以需要配置注解组件扫描

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
    <!--controller层的组件扫描-->
    <context:component-scan base-package="com.bingo.learn.controller"/>
</beans>
```

配置完肯定得加载呀，谁来用？答案就是DispatcherServlet ，配置的方法

`web.xml`

```xml
<!--配置前置控制器-->
<servlet>
  <servlet-name>dispatcherServlet</servlet-name>
  <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  <init-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:spring-mvc.xml</param-value>
  </init-param>
  <!--服务启动即加载创建对象-->
  <load-on-startup>1</load-on-startup>
</servlet>
```

#### （5）测试

运行发布，然后访问 http://localhost:8080/user/save 即可

> 控制台输出 `user saved...`

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gx6iu85jrxj30xo064mxm.jpg" alt="image-20211208164841789"  />



#### （6）小结

Spring MVC的执行流程

![image-20211208163526606](/Users/ing/Library/Application Support/typora-user-images/image-20211208163526606.png)



### 2. SpringMVC的组件执行流程

#### （1）请求过程中的组件流程



<img src="https://tva1.sinaimg.cn/large/008i3skNly1gx6j3phi9zj31eg0ls43l.jpg" alt="image-20211208165749605" style="zoom:50%;" />

大致流程

> 1. 用户发送请求至前端控制器DispatcherServlet。
> 2. DispatcherServlet收到请求调用HandlerMapping处理器映射器。
> 3. 处理器映射器找到具体的处理器(可以根据xml配置、注解进行查找)，生成处理器对象及处理器拦截器(如果有则生成)一并返回给DispatcherServlet。
> 4. DispatcherServlet调用HandlerAdapter处理器适配器。
> 5. HandlerAdapter经过适配调用具体的处理器(Controller，也叫后端控制器)。
> 6. Controller执行完成返回ModelAndView。
> 7. HandlerAdapter将controller执行结果ModelAndView返回给DispatcherServlet。
> 8. DispatcherServlet将ModelAndView传给ViewReslover视图解析器。
> 9. ViewReslover解析后返回具体View。
> 10. DispatcherServlet根据View进行渲染视图(即将模型数据填充至视图中)。DispatcherServlet响应用户。

#### （2）SpringMVC组件解析

##### ① 前置控制器:DispatcherServlet

> 用户请求到达前端控制器，它就相当于 MVC 模式中的 C，DispatcherServlet 是整个流程控制的中心，由 它调用其它组件处理用户的请求，DispatcherServlet 的存在降低了组件之间的耦合性。

##### ② 处理器映射器:HandlerMapping

>HandlerMapping 负责根据用户请求找到 Handler 即处理器，SpringMVC 提供了不同的映射器实现不同的 映射方式，例如:配置文件方式，实现接口方式，注解方式等。

##### ③ 处理器适配器:HandlerAdapter

> 通过 HandlerAdapter 对处理器进行执行，这是适配器模式的应用，通过扩展适配器可以对更多类型的处理器进行执行。

##### ④ 处理器:Handler

> 它就是我们开发中要编写的具体业务控制器。由 DispatcherServlet 把用户请求转发到 Handler。由 Handler 对具体的用户请求进行处理。

##### ⑤ 视图解析器:View Resolver

> View Resolver 负责将处理结果生成 View 视图，View Resolver 首先根据逻辑视图名解析成物理视图名，即 具体的页面地址，再生成 View 视图对象，最后对 View 进行渲染将处理结果通过页面展示给用户。

##### ⑥ 视图:View

> SpringMVC 框架提供了很多的 View 视图类型的支持，最常用的视图就是 jsp。一般情况下需要通过页面标签或页面模版技术将模型数据通过页面展示给用户，需要由程序员根据业务需求开发具体的页面

#### （3）Spring MVC 注解



这里以我们之前的 [UserController](#userController) 为例，我们稍作修改

```java
@Controller
@RequestMapping("/user")
public class UserController {
    @RequestMapping(value = "/save",method = RequestMethod.GET,params = {"userName"})
    public String save(){
        System.out.println("user saved...");
        return "/jsp/success.jsp";
    }
}
```

##### ① `@RequestMapping`

> 作用: 映射URL路径，将http的请求地址映射到控制器(controller)类的处理方法上。
>
> 使用: 可以定义在控制器类上，也可以定义在类里面的方法上。
>
> * 定义类上：将http请求映射到该控制器上，规定请求URL的第一级访问目录，相当于请求地址的父路径（http://localhost:8080/user/）。此处不写的话，就相当于应用的根目录(http://localhost:8080/)
>
>   * 类上不加的话，需 return "jsp/success.jsp" 
>
>   * 类上加的话，需 return "/jsp/success.jsp"  
>
>     `/` 表示应用根路径
>
> * 定义类方法上：请求 URL 的第二级访问目录，指定到控制器处理方法的映射关系，与类上的使用@ReqquestMapping标注的一级目录一起组成访问虚拟路径，如果在控制器类上没有定义该注解，则直接将请求地址映射到处理方法上。
>
> 属性:
>
> * value:用于指定请求的URL。它和path属性的作用是一样的
> * method:用于指定请求的方式
> * params:用于指定限制请求参数的条件。它支持简单的表达式。要求请求参数的key和value必须和配置的一模一样
>
> 例如:params = {"userName"}，表示请求参数必须有userName





。。。

##### ② @TODO 其他的注解 ， 后续补充



#### （4）组件-- 视图解析器配置 【了解】

视图解析器

> SpringMVC有默认组件配置，默认组件都是DispatcherServlet.properties配置文件中配置的，该配置文件地址 org/springframework/web/servlet/DispatcherServlet.properties，该文件中配置了默认的视图解析器，如下:
>
> `org.springframework.web.servlet.ViewResolver=org.springframework.web.servlet.view.I nternalResourceViewResolver`

翻看该解析器源码，可以看到该解析 器的默 认设置 ，如下 :

```java
REDIRECT_URL_PREFIX = "redirect:" --重定向前缀
FORWARD_URL_PREFIX = "forward:" --转发前缀(默认值) 
prefix = ""; --视图名称前缀
suffix = ""; --视图名称后缀
```

我们可以通过属性注入的方式修改视图的的前后缀

`spring-mvc.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
    <!--controller层的组件扫描-->
    <context:component-scan base-package="com.bingo.learn.controller"/>

    <!--配置内部资源视图解析器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/jsp/"></property>
        <property name="suffix" value=".jsp"></property>
    </bean>

</beans>
```

这时候，我之前的案例中在返回视图的时候，就可以直接写文件名了

```java
@Controller
@RequestMapping("/user")
public class UserController {
    @RequestMapping(value = "/save",method = RequestMethod.GET)
    public String save(){
        System.out.println("user saved...");
        return "success";
    }
}
```

运行发布，访问 http://localhost:8080/user/save 

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gx6ltnlg7zj30sq05y74q.jpg" alt="image-20211208175327994" style="zoom:50%;" />





## （五）SpringMVC 数据请求和响应

### 1. 数据响应

web端数据响应一般分为两种方式

> * 视图展示，也就是页面跳转
>   * 直接返回字符串
>   * 通过ModelAndView对象返回（jsp）
> * 数据回写
>   * 直接返回字符串
>   * 返回对象或者集合

#### （1）页面跳转

##### ① 返回字符串形式

> 直接返回字符串:此种方式会将返回的字符串与视图解析器的前后缀拼接后跳转。

```java
@Controller
@RequestMapping("/user")
public class UserController {
    @RequestMapping(value = "/save",method = RequestMethod.GET)
    public String save(){
        System.out.println("user saved...");
        return "success";
    }
}
```

```xml
<!--配置内部资源视图解析器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/jsp/"/>
        <property name="suffix" value=".jsp"/>
    </bean>
```



比如上个案例中，`return "success"` ; 最后拼接为 `/jsp/success.jsp`



##### ② 返回ModelAndView对象

#### （五）SpringMVC 拦截器

### 9.1 拦截器(interceptor)的作用

Spring MVC 的拦截器类似于 Servlet 开发中的过滤器 Filter，用于对处理器进行预处理和后处理。

将拦截器按一定的顺序联结成一条链，这条链称为拦截器链(Interceptor Chain)。在访问被拦截的方 法或字段时，拦截器链中的拦截器就会按其之前定义的顺序被调用。拦截器也是AOP思想的具体实现。

### 9.2 拦截器和过滤器区别

| 区别     | 过滤器(Filter)                                             | 拦截器(Interceptor)                                          |
| -------- | ---------------------------------------------------------- | ------------------------------------------------------------ |
| 使用范围 | 是 servlet 规范中的一部分，任何 Java Web 工程都可以        | 是 SpringMVC 框架自己的，只有使用了 SpringMVC 框架的工程才能用 |
| 拦截范围 | 在 url-pattern 中配置了/*之后， 可以对所有要访问的资源拦截 | 在<mvc:mapping path=“”/>中配置了/**之 后，也可以多所有资源进行拦截，但 是可以 通 过<mvc:exclude-mapping path=“”/>标签 排除不需要拦截的资源 |

### 9.3 拦截器是快速入门

自定义拦截器很简单，只有如下三步:

① 创建拦截器类实现HandlerInterceptor接口

```java
public class MyInterceptor implements HandlerInterceptor {
    // 在目标方法执行前执行
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle...");
        String allow = request.getParameter("allow");
        if ("yes".equals(allow)){
            return  true;
        }else{
            request.getRequestDispatcher("/error.jsp").forward(request,response);
            return false;
        }
        // return false; // 不放行
        // return true; //放行
    }

    // 在目标方法执行之后，视图对象返回之前执行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle...");
        modelAndView.addObject("name","李时珍");

    }

    // 在流程都执行完毕后执行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion...");
    }
}

```

 

② 配置拦截器

```xml
<!--spring-mvc.xml中配置拦截器-->
<mvc:interceptors>
  <mvc:interceptor>
    <!--对哪些资源执行拦截操作，这里是对所有的资源都执行-->
    <mvc:mapping path="/**"/>
    <!--指定拦截器-->
    <bean class="com.bingo.learn.interceptor.MyInterceptor"/>
  </mvc:interceptor>
</mvc:interceptors>
```



③ 测试拦截器的拦截效果

测试类

```java
@Controller
public class TargetController {
    @RequestMapping("/show")
    public ModelAndView show(){
        System.out.println("目标资源执行...");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name","李银河");
        modelAndView.setViewName("index");
        return modelAndView;
    }
}
```

访问：

http://localhost:8080/show?allow=yes 

访问http://localhost:8080/show?allow=yes ，结果输出

```text 
preHandle...
目标资源执行...
postHandle...
afterCompletion...
```

访问http://localhost:8080/show?allow=no，结果输出



## 十.Spring 异常处理

### 10.1 异常处理的思路

系统中异常包括两类:预期异常和运行时异常RuntimeException，前者通过捕获异常从而获取异常信息，后 者主要通过规范代码开发、测试等手段减少运行时异常的发生。

系统的Dao、Service、Controller出现都通过throws Exception向上抛出，最后由SpringMVC前端控制器交 由异常处理器进行异常处理，如下图:

![image-20211124112121836](https://tva1.sinaimg.cn/large/008i3skNgy1gwq2pb7xn5j319m0ei75v.jpg)

### 10.2 1.2 异常处理两种方式

* 使用Spring MVC提供的简单异常处理器SimpleMappingExceptionResolver 
* 实现Spring的异常处理接口HandlerExceptionResolver 自定义自己的异常处理器

#### 10.2.1 简单异常处理器SimpleMappingExceptionResolver

```xml
<!--spring-mvc.xml 配置异常处理器-->
<bean class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
  <!--默认的异常映射的错误视图-->
  <property name="defaultErrorView" value="error"/>
  <property name="exceptionMappings">
    <map>
      <entry key="java.lang.ClassCastException" value="error1"/>
      <entry key="com.bingo.learn.exception.MyException" value="error2"/>
    </map>
  </property>
</bean>

```

#### 10.2.2 自定义异常处理

> 1 创建异常处理器类实现HandlerExceptionResolver 
>
> 2 配置异常处理器
>
> 3 编写异常页面
>
> 4 测试异常跳转

① 创建异常处理器类实现HandlerExceptionResolver

```java
public class MyExceptionResolver implements HandlerExceptionResolver {
    /**
     * @param request
     * @param response
     * @param handler
     * @param ex       异常对象
     * @return ModelView 跳转到错误视图
     */
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        if (ex instanceof MyException) {
            modelAndView.addObject("info", "自定义异常");
        }else if (ex instanceof  ClassCastException){
            modelAndView.addObject("info","类转换异常");
        }
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
```

② 配置异常处理器

```xml
<!--spring-mvc.xml 配置自定义异常处理器-->
<bean class="com.bingo.learn.resolver.MyExceptionResolver"/>
```

> 注意，经过测试，如果自定义异常处理和spring提供的简单处理器都配置了的话，两者谁先配置谁起作用。

③ 异常展示页面

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>通用的异常</h1>
<h1>${info}</h1>
</body>
</html>
```

④ 测试 访问 http://localhost:8080/parse

```java
@RequestMapping("/parse")
@ResponseBody
public void quickMethod22() throws ParseException {
  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
  simpleDateFormat.parse("abc");
}
```



## 十二. Spring的事务控制

### 12.1 编程式事务控制

#### 12.1.1 PlatformTransactionManager接口 【了解】

PlatformTransactionManager接口是spring的事务管理器，它负责操作事务行为。

| 方法                                                         | 说明               |
| ------------------------------------------------------------ | ------------------ |
| TransactionStatus getTransaction(TransactionDefination defination) | 获取事务的状态信息 |
| void commit(TransactionStatus status)                        | 提交事务           |
| void rollback(TransactionStatus status)                      | 回滚事务           |

> 注意:
>
> PlatformTransactionManager是接口，不同的 Dao 层技术则有不同的实现类.【这部分并不需要我们自己去实现，因为spring mvc已经帮我实现】
>
> 例如:
>
> * Dao 层技术是jdbc 或 mybatis 时:org.springframework.jdbc.datasource.DataSourceTransactionManager
>
> * Dao 层技术是hibernate时:org.springframework.orm.hibernate5.HibernateTransactionManager

#### 12.2 TransactionDefinition

TransactionDefinition 是事务的定义对象，负责设置事务的属性。里面有如下方法:

| 方法                         | 说明               |
| ---------------------------- | ------------------ |
| int getIsolationLevel()      | 获得事务的隔离级别 |
| int getPropogationBehavior() | 获得事务的传播行为 |
| int getTimeout()             | 获得超时时间       |
| boolean isReadOnly()         | 是否只读           |

① 事务隔离级别

设置隔离级别，可以解决事务并发产生的问题，如脏读、不可重复读、虚读（幻读）、序列化读。

* ISOLATION_DEFAULT

* ISOLATION_READ_UNCOMMITTED

* ISOLATION_READ_COMMITTED
* ISOLATION_REPEATABLE_READ 
* ISOLATION_SERIALIZABLE

② 事务的传播行为

> 作用：主要解决，业务方法在调用业务方法时，他们的事务统一性。

假设存在两个业务A和B,且A会调用B,此时不同的传播行为会对应着不同的处理方式：

* REQUIRED:如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中。一般的选择(默认值)  SUPPORTS:支持当前事务，如果当前没有事务，就以非事务方式执行(没有事务)

  > B会看A中有没有事务，如果A有事务就使用这个事务，如果没有就新建一个事务

* SUPPORTS:支持当前事务，如果当前没有事务，就以非事务方式执行(没有事务)

  > B看A有没有事务，有就使用，没有就以非事务方式执行

* MANDATORY:使用当前的事务，如果当前没有事务，就抛出异常

  > B看A有没有事务，有就是使用当前事务，没有就抛出异常 （有房有车就好说，没房没车，滚犊子）

* REQUERS_NEW:新建事务，如果当前在事务中，把当前事务挂起。

* NOT_SUPPORTED:以非事务方式执行操作，如果当前存在事务，就把当前事务挂起

* NEVER:以非事务方式运行，如果当前存在事务，抛出异常

* NESTED:如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行 REQUIRED 类似的操作 

* 超时时间:默认值是-1，没有超时限制。如果有，以秒为单位进行设置

* 是否只读:建议查询时设置为只读

#### 12.1.3 TransactionStatus 【了解】

TransactionStatus 接口，负责提供事务具体的运行状态，方法如下。

| 方法                       | 说明           |
| -------------------------- | -------------- |
| boolean hasSavepoint()     | 是否存储回滚点 |
| boolean isCompleted()      | 事务是否完成   |
| boolean isNewTransaction() | 是否是新事务   |
| boolean isRollbackOnly()   | 事务是否回滚   |

### 12.2 声明式事务控制

#### 12.2.1 什么是声明式事务控制

> Spring 的声明式事务顾名思义就是采用声明的方式来处理事务。这里所说的声明，就是指在配置文件中声明 ，用在 Spring 配置文件中声明式的处理事务来代替代码式的处理事务。

#### 12.2.2 声明式事务处理的作用

* 事务管理不侵入开发的组件。具体来说，业务逻辑对象就不会意识到正在事务管理之中，事实上也应该如 此，因为事务管理是属于系统层面的服务，而不是业务逻辑的一部分，如果想要改变事务管理策划的话， 也只需要在定义文件中重新配置即可

* 在不需要事务管理的时候，只要在设定文件上修改一下，即可移去事务管理服务，无需改变代码重新编译 ，这样维护起来极其方便

> 注意:Spring 声明式事务控制底层就是AOP。

#### 12.2.2 声明式事务控制的实现

① 创建测试项目,主要目录

```
├── java
│   └── com
│       └── bingo
│           └── learn
│               └── tx
│                   ├── controller
│                   │   └── AccountController.java
│                   ├── dao
│                   │   ├── AccountDao.java
│                   │   └── impl
│                   │       └── AccountDaoImpl.java
│                   ├── domain
│                   │   └── Account.java
│                   └── service
│                       ├── AccountService.java
│                       └── impl
│                           └── AccountServiceImpl.java
├── resources
│   ├── applicationContext.xml
│   └── jdbc.properties
└── webapp
    └── WEB-INF
        └── web.xml
```

② 对应代码

```java
// Dao层
public interface AccountDao {
    /**
     *
     * @param outMan
     * @param money
     */
    void out(String outMan,double money);

    /**
     *
     * @param inMan
     * @param money
     */
    void in(String inMan,double money);
}

public class AccountDaoImpl implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void out(String outMan, double money) {
        jdbcTemplate.update("update account set balance = balance - ? where username = ?;", money, outMan);
    }


    public void in(String inMan, double money) {
        jdbcTemplate.update("update account set balance = balance + ? where username = ?;",money,inMan);
    }
}

// service层
public interface AccountService {
    /**
     * 转账
     * @param outMan 转账人
     * @param inMan  收款人
     * @param money  转账金额
     */
    void transfer(String outMan,String inMan,double money);
}

public class AccountServiceImpl implements AccountService {
    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void transfer(String outMan, String inMan, double money) {
        accountDao.out(outMan, money);
      	int i = 1/0; // 自造异常，用来测试事务控制
        accountDao.in(inMan, money);
    }
}
// 模拟web层，测试类
public class AccountController {
    public static void main(String[] args) {
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        AccountService accountService = app.getBean(AccountService.class);
        accountService.transfer("Alice","Bob",500);
    }
}

// domain
public class Account {
    private int id;
    private String username;
    private double balance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", balance=" + balance +
                '}';
    }
}
数据表account
  
+----+----------+---------+
| id | username | balance |
+----+----------+---------+
|  1 | Alice    |    5000 |
|  2 | Bob      |    5000 |
+----+----------+---------+


```

③ 配置文件 

applicationContext.xml

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
    <!--1.加载jdbc.properties-->
    <context:property-placeholder location="classpath:jdbc.properties"/>
    <!--2.配置数据源对象-->
    <bean id="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${jdbc.driver}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="druidDataSource"/>
    </bean>
    <bean id="accountDao" class="com.bingo.learn.tx.dao.impl.AccountDaoImpl">
        <property name="jdbcTemplate" ref="jdbcTemplate"/>
    </bean>
    <bean id="accountService" class="com.bingo.learn.tx.service.impl.AccountServiceImpl">
        <property name="accountDao" ref="accountDao"/>
    </bean>
</beans>
```

jdbc.properties

```properties
jdbc.driver = com.mysql.cj.jdbc.Driver
jdbc.url = jdbc:mysql://localhost:3306/test
jdbc.username = root
jdbc.password = root
```

pom.xml 

```xml
<dependencies>
    <!--spring框架-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.3.12</version>
    </dependency>
    <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjweaver</artifactId>
      <version>1.8.6</version>
    </dependency>
    <!--jdbcTemplate-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <version>5.3.12</version>
    </dependency>
    <!--spring事务管理-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-tx</artifactId>
      <version>5.3.12</version>
    </dependency>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-test</artifactId>
      <version>5.3.12</version>
    </dependency>
    <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>druid</artifactId>
      <version>1.2.8</version>
    </dependency>
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.26</version>
    </dependency>
  </dependencies>
```



④ 分析

声明式事务控制明确事项: 

* 谁是切点? 

  > 业务方法，这里就是转账方法transfer  

* 谁是通知?

  > 事务控制，增强功能

* 配置切面

```xml
<!--目标对象，内部方法transfer就是切点-->
<bean id="accountService" class="com.bingo.learn.tx.service.impl.AccountServiceImpl">
  <property name="accountDao" ref="accountDao"/>
</bean>
<!--配置平台事务管理器-->
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
  <!--注入dataSource transactionManager会从中获取connection 进行事务控制-->
  <property name="dataSource" ref="druidDataSource"/>
</bean>
<!--配置通知 事务的增强-->
<tx:advice id="txAdvice" transaction-manager="transactionManager">
  <tx:attributes>
     <tx:method name="transfer" isolation="REPEATABLE_READ" propagation="REQUIRED" timeout="-1" read-only="false"/>
  </tx:attributes>
</tx:advice>
<!--配置事务的aop织入-->
<aop:config>
  <!--advisor 是 spring专门为事务增强定义的 ，其他的用aspect-->
  <aop:advisor advice-ref="txAdvice" pointcut="execution(* com.bingo.learn.service.impl.*.*(..))"/>
</aop:config>
```



> 错误：“transaction-manager Attribute transaction-manager is not allowed here” 或者 “通配符的匹配很全面, 但无法找到元素 'tx:advice' 的声明”
>
> 原因是命名空间引入错误，可以将<tx 之类全部删除干净，重新引入 以 /tx 结尾的命名空间即可。
>
> ![image-20211125132059755](https://tva1.sinaimg.cn/large/008i3skNgy1gwrbsu087qj30ng07ymz6.jpg)
>
> 



切点方法的事务参数的配置

<!--事务增强配置-->

 ```xml
  <!--配置通知 事务的增强-->
 <tx:advice id="txAdvice" transaction-manager="transactionManager">
   <!--设置事务属性信息-->
   <tx:attributes>
     <!--哪些方法被增强-->
     <tx:method name="transfer" isolation="REPEATABLE_READ" propagation="REQUIRED" timeout="-1" read-only="false"/>
   </tx:attributes>
 </tx:advice>
 ```

其中，`<tx:method>` 代表切点方法的事务参数的配置:

* name:切点方法名称

* isolation:事务的隔离级别

* propogation:事务的传播行为 
* timeout:超时时间
* read-only:是否只读



下面进行测试 ,预期结果：： 程序报错，且金额没有变化。 



![image-20211125132430353](/Users/ing/Library/Application Support/typora-user-images/image-20211125132430353.png)

```sql
mysql> select * from account;
+----+----------+---------+
| id | username | balance |
+----+----------+---------+
|  1 | Alice    |    4500 |
|  2 | Bob      |    5000 |
+----+----------+---------+
2 rows in set (0.00 sec)
```



符合我们的预期结果,说明事务控制起效。

### 12.3 注解式事务控制

对前面项目进行注解式改造

> 主要是对AccountDaoImpl、AccountServiceImpl、applicationContext.xml 改造

#### 12.3.1 添加注解

`AccountDaoImpl.java`

```java
@Repository("accountDao")
public class AccountDaoImpl implements AccountDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    //     this.jdbcTemplate = jdbcTemplate;
    // }


    public void out(String outMan, double money) {
        jdbcTemplate.update("update account set balance = balance - ? where username = ?;", money, outMan);
    }


    public void in(String inMan, double money) {
        jdbcTemplate.update("update account set balance = balance + ? where username = ?;",money,inMan);
    }
}


```



`AccountServiceImpl.java`

```java

@Service("accountService")
//① 可以在类上加注解
//@Transactional(isolation = Isolation.REPEATABLE_READ) 
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;

    // public void setAccountDao(AccountDao accountDao) {
    //     this.accountDao = accountDao;
    // }
		// ②在切点方法上加注解
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED) //事务增强
    public void transfer(String outMan, String inMan, double money) {
        accountDao.out(outMan, money);
        int i = 1/0;
        accountDao.in(inMan, money);
    }
}
```

> 两种方式
>
> ① 在类上注解，表示该类下的切点方法都采用这一事务控制属性
>
> ② 在方法上单独加注解，表示该方法的事务控制属性
>
> **两者优先级采用就近原则 方法单独设置的优先级高**

#### 12.3.2 applicationContext.xml配置

```xml
<!--1.加载jdbc.properties-->
<context:property-placeholder location="classpath:jdbc.properties"/>

<!--配置数据源对象-->
<bean id="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource">
  <property name="driverClassName" value="${jdbc.driver}"/>
  <property name="url" value="${jdbc.url}"/>
  <property name="username" value="${jdbc.username}"/>
  <property name="password" value="${jdbc.password}"/>
</bean>

<!--配置jdbc模板-->
<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
  <property name="dataSource" ref="druidDataSource"/>
</bean>

<!--配置平台事务管理器-->
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
  <!--注入dataSource transactionManager会从中获取connection 进行事务控制-->
  <property name="dataSource" ref="druidDataSource"/>
</bean>


<!--组件扫描-->
<context:component-scan base-package="com.bingo.learn.tx"/>

<!--事务控制的注解驱动-->
<tx:annotation-driven transaction-manager="transactionManager"/>
```

> 基本原则：
>
> **自定义的bean 使用注解， 第三方的bean使用xml配置。所以这里保留DataSource、JDBC模板及平台事务管理器的配置**

#### 12.3.3 小结 

>  注解配置声明式事务控制解析
>
>  * 使用@Transactional 在需要进行事务控制的类或是方法上修饰，注解可用的属性同xml 配置方式，例如隔离 级别、传播行为等。
>  * 注解使用在类上，那么该类下的所有方法都使用同一套注解参数配置。 
>  * 使用在方法上，不同的方法可以采用不同的事务参数配置
>  * 类和方法上的注解同时存在时，优先级采用就近原则。
>  * XML配置文件中要开启事务的注解驱动 `<tx:annotation-driven transaction-manager="transactionManager"/>`





Spring整合Junit

![image-20211206181102633](https://tva1.sinaimg.cn/large/008i3skNly1gx49z7xc03j316a0hwgnz.jpg)
