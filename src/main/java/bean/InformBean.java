package bean;

import java.util.Collection;

public class InformBean {
	private int infid;
	private String title;
	private String content;
	private String affixpath;
	private int viewtimes;
	private String author;
	private String  modifytime;
	private String releasetime;
	private Collection AffixFiles;
	public Collection getAffixFiles() {
		return AffixFiles;
	}
	public void setAffixFiles(Collection collection) {
		AffixFiles = collection;
	}
	public String getAffixpath() {
		return affixpath;
	}
	public void setAffixpath(String affixpath) {
		this.affixpath = affixpath;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getInfid() {
		return infid;
	}
	public void setInfid(int infid) {
		this.infid = infid;
	}
	public String getModifytime() {
		return modifytime;
	}
	public void setModifytime(String modifytime) {
		this.modifytime = modifytime;
	}
	public String getReleasetime() {
		return releasetime;
	}
	public void setReleasetime(String releasetime) {
		this.releasetime = releasetime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getViewtimes() {
		return viewtimes;
	}
	public void setViewtimes(int viewtimes) {
		this.viewtimes = viewtimes;
	}

}
