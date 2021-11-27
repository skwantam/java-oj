# java-oj
记录刷题-使用java

# git 操作
在当前目录生成ssh key（ssh-keygen)

# 同一主机 多个git账号，分别指定ssh key
- 全局账户不需指定（使用~/.ssh下的密钥）
- 其余账户：新建~/.ssh/config文件，写入以下内容
```shell
Host myHostName
    HostName github.com
    IdentityFile /path/to/id_rsa # 可用相对路劲
```

# Maven 
## 指定运行类
mvn exec:java -Dexec.mainClass="test.TestMain"
## 指定测试类/方法
mvn test -Dtest=tsj.deserializer.DeserializerProcessTest#fun6
## 新建项目
mvn archetype:generate