# 登录后进入教师模块



## 刷新教师课题到列表中

1. 根据教师编号获取该教师的课题列表

   - 参数
     ```java
     int tutorid; // 教师编号
     ```

   - api
     ```json
     // 根据教师编号获取该教师的课题列表
     url: "../SubjectServlet/gets";
     params:
     {
         tutorid: tutorid;  // 教师编号
     }
     return: 课题信息（包括错误信息）
     ```

2. 根据课题id查看课题详情

   - 参数
     ```java
     int subid; // 课题id
     ```

   - api
     ```json
     // 课题id查看课题详情 SyscodeBpo getcode
     url: "SubjectView.jsp";
     params:
     {
         subid: subid;
     }
     return: 当前课题详情（包括错误信息）
     ```

3. 查看选择的课题的学生信息

   - 参数
     ```java
     int stuid;  // 学生id
     ```

   - api
     ```json
     url: "../StudentServlet/get";
     params:
     {
         stuid: stuid;
     }
     return: 学生信息
     ```

2. 查看设计情况（loading）

## 申报新课题功能

```tex
打开课题新增、修改窗口
```

1、判断是否可以继续申请课题

- 参数
  ```java
  int currsubnum; // 当前课题数
  int maxsubnum;  // 最多可以申报的课题数
  ```

- api
  ```java
  ```

  