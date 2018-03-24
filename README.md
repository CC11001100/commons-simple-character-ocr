## 通用简单字符验证码识别框架

### 使用流程
1. 在pom.xml中添加依赖
添加仓库：
```
<repositories>
    <repository>
        <id>maven-repo-cc11001100</id>
        <url>https://raw.github.com/cc11001100/maven-repo/commons-simple-character-ocr/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>
```
添加dependency：
```
<dependency>
    <groupId>cc11001100</groupId>
    <artifactId>commons-simple-character-ocr</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
2. 下载
2. blabla
2. blabla
2. blabla
2. blabla

---

### 适用场景
略鸡肋，适用于某些特殊情况：   
1. 比如代理ip网站端口用图片表示的可以直接识别 
2. 比如过于简单的验证码可以直接识别 
3. 编不出的其它场景...  

---

### 对输入图片的要求
具有提下特征的字符型验证码可以直接识别：  
1. 无噪音或噪音可以完美擦除  
2. 字符无粘连，无变形，单个字符始终只有一种形态  



