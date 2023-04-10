package bean;

import lombok.Data;

import java.util.Collection;


@Data
public class InformBean {
	private int infid;
	private String title;
	private String content;
	private String affixpath;
	private int viewtimes;
	private String author;
	private String  modifytime;
	private String releasetime;
	private Collection<?> AffixFiles;

}
