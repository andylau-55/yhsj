package com.yhsjedu.datacloud.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 页面Jquery.select专用Bean
 * 
 * @author Guoxk
 */
@SuppressWarnings("serial")
public class Select2GroupBean implements Serializable {
    private String text;
    private ArrayList children = new ArrayList();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList getChildren() {
        return children;
    }

    public void setChildren(ArrayList children) {
        this.children = children;
    }

    public static ArrayList<Select2GroupBean> toGroupLst(List<Select2Bean> lst) {
        ArrayList<Select2GroupBean> retVal = new ArrayList<Select2GroupBean>();
        Select2GroupBean gBean = null;
        String parentId = null;
        for (Select2Bean bean : lst) {
            if (gBean == null || !parentId.equals(bean.getParentId())) {
                gBean = new Select2GroupBean();
                gBean.setText(bean.getText());
                parentId = bean.getId();
                retVal.add(gBean);
            } else {
                gBean.getChildren().add(bean);
            }
        }

        return retVal;
    }
}
