/**
 * ��ʦ�걨������Ŀ��
 */
package bean;

import lombok.Data;

import java.util.ArrayList;

@Data
public class TeacherSubNumBean {
    String tid;//��ʦ���
    int subsum;//����Ŀ��
    ArrayList<String> subjects;//������Ŀ


    public TeacherSubNumBean() {
        subsum = 0;
        subjects = new ArrayList<String>();
    }

}
