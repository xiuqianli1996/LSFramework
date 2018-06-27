# LSFramework

#### 项目介绍
手写山寨版"spring"，学习aop、ioc思想的demo，没看过spring的源码，因为实在是太庞大了，参考部分网上博客及开源代码完成。


#### 进度记录

2018.06.27 完成jdbc模块，支持查询实体、数组、List集合，单元测试基本完成，但是在测试模块里怎么都引入不了，暂时放下

2018.06.26 完成了拥有基础功能的web模块，注解式路由、request参数和path参数绑定、jsp视图、json视图、全局异常处理，详见test-example/OneController（事实证明基础还是很弱，基础骨架半天完成，填坑倒是花了一天多）

2018.06.22  继续抽离部分代码逻辑，新增Loader接口，为新增其他组件做准备，新增LSConfiguration注解，实现从注解的类中加载bean实例，AOP、IOC基本完善。


#### TODO

1. cache模块

2.扫描jar包class

#### 主要依赖包

1. [gson](http://mvnrepository.com/artifact/com.google.code.gson/gson) 解析bean配置文件，实现通过配置文件初始化bean实例
2. [cglib-nodep](http://mvnrepository.com/artifact/cglib/cglib-nodep) 使用动态代理实现AOP


#### 程序说明

1.配置文件
```
app.scanPackage=demo.web #需要扫描的包，递归扫描，通常只需要一个，多个包名使用逗号分隔
app.beansConfig=beans.json #初始化bean的配置文件
```

2. IOC

先扫描注解装配，后加载配置文件

注解方式:
```java
@LSBean
public class Service {

    @LSAutowired
    Service2 service2;
    @LSAutowired("service3")
    Service3 service3;

    @LSAround(Action2.class)
    public void test() {
        service2.test();
    }

    public void test2() {
        service3.test3();
    }
}
```
初始化时会扫描@LSBean注解修饰的类实例化到bean容器中，必须有无参构造函数，目前需要依赖注入的类成员变量只支持@LSAutowired注解注入。
因为是先全部实例化后进行依赖注入，所以不会有循环依赖

@LSConfiguration注解，会扫描LSConfiguration注解的类里的所有@LSBean注解修饰的方法，将返回值注入bean容器，方法参数是需要注入的参数, @LSParam标注注入的beanName

```java
@LSConfiguration
public class TestConfig {

    @LSBean
    public TestConfigDI getTestConfigDI(@LSParam("service3") IService service2, Service3 service3) {
        return new TestConfigDI(service2, service3); 
    }
}
```

配置文件方式
```
//beans.json
[
  {
    "name": "testBean2", //在bean容器中的名字
    "className": "demo.web.TestBean2", //类名
    "constructor": [10, "${testBean}"], //构造函数的参数，个数和顺序必须一一对应
    "properties": { //成员变量，键值对结构
      "val1": 1,
      "val2": "123",
//      "testBean": "${testBean1}"
    }
  }
]

```
数组格式，${xxx}表示从bean容器中取名为xxx的bean，需要AOP强化的化必须含有无参构造函数，所以得把被依赖的bean放在上面先实例化，暂未解决循环依赖

3.AOP

```java
@LSBean
@LSAspect(value = wdemo.webBean2")
public class Action3 extends AopAction {
    @Override
    public void invoke(Invocation invocation) throws Throwable {
        if (invocation.getMethod().isAnnotationPresent(LSAround.class)){
            return;
        }
        System.out.println("aop3 before");
        invocation.invoke();
        System.out.println("aop3 after");
    }

}
```
继承AopAction实现invoke方法并且加上@LSBean注解即可实现一个切面类实现简单的环切，调用invocation.invoke();继续执行拦截链

@LSAspect注解定义被切入的类，value为包名（必填），cls为切入的类名（选填），cls为空的情况下此拦截器切入包下的所有类

被切入的类必须被实例化在bean容器中

@LSAround可用于类和方法之上，参数为切面类，参数为数组，可传入多个切面类，按顺序执行

优先级为@LSAspect > 类级@LSAround > 方法级@LSAround

@LSClear注解作用于方法上，用于清除作用于的类上的指定切面（传入需要清除的类数组）或清空切面（不传参数）

4.JDBC

具体可查看单元测试

#### 感谢

1.[JFinal](https://gitee.com/jfinal/jfinal)

2.[smart-framework](https://gitee.com/huangyong/smart-framework)

3.[coody-icop](https://gitee.com/coodyer/coody-icop)