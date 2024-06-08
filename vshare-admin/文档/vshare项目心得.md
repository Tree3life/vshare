异常是Java中的一种反馈机制



如何保证一个方法内 redis和mysql 两种操作整体的事务性，原子性；比如@PutMapping("/played/{id}")中。





todo了解MySQL数据库自身的锁，多线程。。。。。。。。

![image-20240317180846541](https://tree3.oss-cn-hangzhou.aliyuncs.com/md/photo/1710670126-image-20240317180846541.png)





springboot接管redis的事务机制





保证redis和mysql的数据一致性





mybatis中的mapper。xml中使用cache标签





```
// servlet service httpServletRequest  httpServletResponse 传统web springmvc   springwebflux new web模型
```





springboot中测试类的使用

```
@RunWith(SpringRunner.class)
@SpringBootTest(classes = 启动类.class)
public class 测试类 {}
```

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```





```
abstract class
```