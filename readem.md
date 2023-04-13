在当前版本将使用新版的jdbc

版本 0.0.1

修改了jdbc，已经可以正常运行

版本 1.0.0

当前版本引入了lombok，删除了部分的get和set方法，使得bean文件更加整洁

修改了bug，ResultSet的first方法时遇到了`Operation not allowed for a result set of type ResultSet.TYPE_FORWARD_ONLY.`，原因是默认情况下，ResultSet只能使用next遍历，为Connection的prepareStatement添加权限，做如下修改

```java
PreparedStatement 
            pstmt = con.prepareStatement(vsql1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
```

版本1.0.1

修复了一些兼容性的bug

版本1.0.2

添加了api设计文档
