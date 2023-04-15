# 登录后进入教师模块



## 刷新教师课题到列表中

1. 根据教师编号获取该教师的课题列表

   - 参数
     ```java
     int tutorid; // 教师编号
     ```

   - api（findSubjectByTutorid）
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

   - api (findSubjectBySubid)
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

   - api (findStudentByStuid)
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

- api (addSubjectByStuid)
  ```java
  url: "../SubjectServlet/edt";
  method: "post";
  params:
  {
      subid: subid;
      othertid: othertid;  // 其他教师编号
      othertname: othertname;  // 其他教师姓名
      subname: subname;  // 课题名称
      subsort: subsort;  // 课题类别
      subkind: subkind;  // 题目性质
      subsource: subsource;  // 题目来源
      subtype: subtype;  // 题目类型
      oldargu: oldargu;  // 设计概述
      workcontent: workcontent;  // 工作内容
      requirement: requirement;  // 工作基本要求
      // 工作日程
      refpapers: refpapers;  // 主要参考资料及文献
      speciality: speciality;  // 课题适合专业
      subdirectionselect: subdirectionselect;  // 课题方向
  }
  return: "学生信息";
  ```
  





































