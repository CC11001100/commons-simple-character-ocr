## 通用简单字符验证码识别框架

### 使用流程
1. 在pom.xml中添加依赖  
    - 添加仓库：
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
    - 添加dependency：
    ```
    <dependency>
        <groupId>cc11001100</groupId>
        <artifactId>commons-simple-character-ocr</artifactId>
        <version>1.1-SNAPSHOT</version>
    </dependency>
    ```
    所有版本可以去这里看[commons-simple-character-ocr maven repository](https://github.com/CC11001100/maven-repo/tree/commons-simple-character-ocr)
2. 下载一些图片用于生成标注数据，大致有两种情况 
    -  有一个地址，发送请求就可以直接下载图片  
        ``init(String url, int times, String saveBaseDir)``  
    - 图片下载比较麻烦，自己下载完再指定所下载的目录   
        ``init(String fromBasePath, String toBasePath)``
3. 打开生成的标注图片所在的目录手动将文件名改为图片所表示的意思
    ![](https://images2018.cnblogs.com/blog/784924/201803/784924-20180326020539301-1103491062.png)
4. 告诉ocrUtil去哪里加载上面的文件  
    ``ocrUtil.loadDictionaryMap("E:/test/proxy/ant/char/")``
5. 然后就可以直接使用了  
    ``String ocr(BufferedImage img)``
6. 这里是几个具体的例子：  
    - [蚂蚁代理免费代理ip爬取（端口图片显示）](http://www.cnblogs.com/cc11001100/p/8648169.html)  
    - [酷伯伯实时免费HTTP代理ip爬取（端口图片显示+document.write）](http://www.cnblogs.com/cc11001100/p/8647555.html)

---

### 适用场景
略鸡肋，只适用于某些特殊情况：   
1. 比如代理ip网站端口用图片表示的可以直接识别 
    - [蚂蚁代理免费代理ip爬取（端口图片显示）](http://www.cnblogs.com/cc11001100/p/8648169.html)  
    - [酷伯伯实时免费HTTP代理ip爬取（端口图片显示+document.write）](http://www.cnblogs.com/cc11001100/p/8647555.html)
2. 比如过于简单的验证码可以直接识别 
3. 比如电商网站的价格字段的识别 
4. 比如编不出的其它场景...  

---

### 对输入图片的要求
具有提下特征的字符型验证码可以直接识别：  
1. 无噪音或噪音可以完美擦除，默认提供了几种擦除噪音的实现  
2. 字符无粘连，单个字符始终只有一种形态  




