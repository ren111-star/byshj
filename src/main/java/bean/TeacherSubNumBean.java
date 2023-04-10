/**
 * 教师申报的总题目数
 */
package bean;

import lombok.Data;

import java.util.ArrayList;

@Data
public class TeacherSubNumBean {
    String tid;//教师编号
    int subsum;//总题目数
    ArrayList<String> subjects;//所有题目


    public TeacherSubNumBean() {
        subsum = 0;
        subjects = new ArrayList<String>();
    }

}
