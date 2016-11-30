package com.yhsjedu.datacloud.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;

import com.yhsjedu.datacloud.utils.DateUtility;

@Entity
@Table(name = "replacePage")
@DynamicUpdate(true)
public class Replace implements Serializable {

	/**
	 * @author yangjin
	 */
	private static final long serialVersionUID = -3458300853268268071L;
	private Long id;
	private Long type;
	private String img;
	private String title;
	private String content;
	private String url;
	private String colour;
	private String hoverColour;
	private Long state;
	private Long pageOrder;
	private Date createTime;

	public Replace() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getHoverColour() {
		return hoverColour;
	}

	public void setHoverColour(String hoverColour) {
		this.hoverColour = hoverColour;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public Long getPageOrder() {
		return pageOrder;
	}

	public void setPageOrder(Long pageOrder) {
		this.pageOrder = pageOrder;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Transient
	public String getCreateTimeStr() {
		return DateUtility.toString(createTime,
				DateUtility.DATE_PATTERN_SLASH_YMDHM);
	}

	@Transient
	public String getCreateTimeYmdStr() {
		return DateUtility.toString(createTime,
				DateUtility.DATE_PATTERN_SLASH_YMD);
	}

}
