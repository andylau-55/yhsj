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
public class Select2Bean implements Serializable {
    private String id;
    private String text;
    private Object children = new String();
    private String parentId;
    private String groupNm;
    private Object data;

    public Select2Bean() {

    }

    public Select2Bean(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getChildren() {
        return children;
    }

    public void setChildren(Object children) {
        this.children = children;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getGroupNm() {
        return groupNm;
    }

    public void setGroupNm(String groupNm) {
        this.groupNm = groupNm;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static List<Select2Bean> removeBranch(List<Select2Bean> lst, String removeId) {
        if (!CommonUtil.isEmpty(removeId)) {
            List<String> removeKey = new ArrayList<String>();
            for (int i = lst.size() - 1; i >= 0; i--) {
                Select2Bean bean = lst.get(i);
                // 去掉本身
                if (removeId.equals(bean.getId())) {
                    lst.remove(i);
                } else
                // 去掉父节点为该节点的值
                if (removeId.equals(bean.getParentId())) {
                    removeKey.add(bean.getId());
                    lst.remove(i);
                }
            }
            // 递归去掉下层树枝
            for (String key : removeKey) {
                removeBranch(lst, key);
            }
        }
        return lst;
    }

    public static List<Select2Bean> orderByParent(List<Select2Bean> lst, String root, int level) {
        ArrayList<Select2Bean> retVal = new ArrayList<Select2Bean>();

        for (Select2Bean bean : lst) {
            String parentId = CommonUtil.null2str(bean.getParentId());

            if (parentId.equals(root)) {
                for (int i = 0; i < level; i++) {
                    bean.setText(bean.getText());
                }

                retVal.add(bean);

                List<Select2Bean> childLst = orderByParent(lst, bean.getId(), level + 1);

                retVal.addAll(childLst);
            }
        }

        return retVal;
    }
}
