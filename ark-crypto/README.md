# Ark-Crypto

加密模块，采用 JPBC 实现

## Java 环境

主要是用 JPBC 模拟认证过程，包括密钥生成、加密、解密、密钥更新等功能

- JDK 17
- JPBC 2.0.0
- Maven 3.9.1
- Springboot 3.0.2
- Thymeleaf 3.1.1
- Data JPA 3.0.2
- Spring Security 6.0.1
- Redis 3.0.1

准备实现的功能：多线程认证测试（加密线程池）

- 对认证时间的测定：用户可选择认证次数和线程个数（有限的选择）

## Thymeleaf 交互

前端：通过`<input>`标签`name`标识

```html
<form th:action="@{/submit}" method="post">
    <input type="text" name="key1" placeholder="Enter value for key1" />
    <input type="text" name="key2" placeholder="Enter value for key2" />
    <button type="submit">Submit</button>
</form>
```

后端：通过`@RequestParam`进行取值，前端传来的参数将根据`<name, value>`自动封装为 map

```java
@RequestMapping("/auth/protocol")
public ResultUtil auth(@RequestParam Map<String, String> params){
    Map<String, String> data = authService.auth(params);
    return ResultUtil.success(data);
}
```

## Jar 部署

### 打包

jpbc 导出需要配置 maven

```xml
<!-- JPBC 打包 -->
<dependency>
    <groupId>jpbc.api</groupId>
    <artifactId>api</artifactId>
    <version>2.0.0</version>
    <scope>system</scope>
    <systemPath>${pom.basedir}/lib/jpbc-api-2.0.0.jar</systemPath>
</dependency>

<dependency>
    <groupId>jpbc-plaf</groupId>
    <artifactId>plaf</artifactId>
    <version>2.0.0</version>
    <scope>system</scope>
    <systemPath>${pom.basedir}/lib/jpbc-plaf-2.0.0.jar</systemPath>
</dependency>
```

除此以外，Maven 打包将默认不包含`<scope>`为 system 的包，所以还要额外在 build 插件中配置

```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <version>${spring-boot.version}</version>
            <configuration>
                <mainClass>cia.arkrypto.auth.AuthApplication</mainClass>
                <skip>false</skip>
                <includeSystemScope>true</includeSystemScope>
            </configuration>
            <executions>
                <execution>
                    <id>repackage</id>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

后台运行

```bash
nohup java -jar Auth-Platform-0.0.1-SNAPSHOT.jar &
```

查看进程

```bash
ps -ef | grep java
```

根据进程号查询

```bash
netstat -anop | grep 3802588
```

根据端口查询状态

```bash
netstat -tuln | grep 8080
```

杀死进程

```bash
kill -9 3802588
```

### 防火墙

防火墙配置，首先是控制台要打开，然后是 iptables 规则

```bash
sudo iptables -L -n
```

确保 8080 端口允许外部访问，若不允许用以下命令开放

```bash
sudo iptables -A INPUT -p tcp --dport 8080 -j ACCEPT
sudo iptables-save
```

最后检查 ufw

```bash
sudo ufw status
sudo ufw allow 8080/tcp
```

### HTTPS

其实是因为我打算从博客里请求`/image`接口去动态的展示随机图片，这个接口从数据库表中随机取一个图片路径，返回图片的二进制流，非常简单捏

但是 GitHub Page 的默认域名，即`xxx.github.io`强制使用 HTTPS，即我直接在服务器上部署 jar 包（HTTP），他会打不过去，从而加载不了图片

所以需要配置 HTTPS，又因为我没域名，所以只能用自生成的证书，步骤如下

服务器上生成证书

```sh
keytool -genkeypair -alias springboot -keyalg RSA -keysize 2048 \
        -dname "CN=localhost, OU=MyOrg, O=MyCompany, L=City, ST=State, C=US" \
        -validity 365 -keystore keystore.p12 -storetype PKCS12 \
        -storepass 123456
```

会在当前目录得到一个 PKCS12 格式的证书 keystore.p12，将这个证书拷入 resources 目录，对 SpringBoot 配置

```yaml
server:
  port: 8443
  ssl:
    key-store: classpath:keystore.p12
    key-store-password: "123456"
    key-store-type: PKCS12
    key-alias: springboot
```

打包上传部署，通过`ip:8443`进行访问，成功，经测试 GitHub Page 同样允许自编译的 SSL 证书

### 时区

后端全局设置时区为东八区

```java
@SpringBootApplication
public class AuthApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(AuthApplication.class, args);
    }
}
```

同时设置 JPA 和 JDBC 时区

```yaml
spring:
  jpa:
    properties:
      hibernate:
        jdbc.time_zone: Asia/Shanghai
  datasource:
        url: jdbc:mysql://43.156.105.197:3306/auth_platform?useUnicode=true&characterEncoding=utf-8&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai
```

