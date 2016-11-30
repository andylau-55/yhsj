package com.yhsjedu.datacloud.utils;

public class DataTableBean {

    private int draw;
    private int start = 0;
    private int length = 20;
    private String search;
    private int pageNo = 0;
    private String[] orderColumn;
    private String[] orderDir;
    private String ordername;
    private String[] columnsdata;

    public DataTableBean() {

    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public int getPageNo() {
        if (pageNo == 0)
            pageNo = (start / length) + 1;
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        if (length <= 0) {
            length = 20;
        }
        start = length * (pageNo - 1);
        this.pageNo = pageNo;
    }

    public String[] getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(String[] orderColumn) {
        this.orderColumn = orderColumn;
    }

    public String[] getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(String[] orderDir) {
        this.orderDir = orderDir;
    }

    public String getOrdername() {
        return ordername;
    }

    public void setOrdername(String ordername) {
        this.ordername = ordername;
    }

    public String[] getColumnsdata() {
        return columnsdata;
    }

    public void setColumnsdata(String[] columnsdata) {
        this.columnsdata = columnsdata;
    }

    public String getOrderSql() {
        StringBuffer ordersql = new StringBuffer();
        if (orderColumn != null && orderColumn.length > 0) {
            ordersql.append(" ORDER BY ");
            for (int i = 0; i < orderColumn.length; i++) {
                if (i == 0) {
                    ordersql.append(" " + columnsdata[Integer.parseInt(orderColumn[i])] + " " + orderDir[i]);
                } else {
                    ordersql.append(", " + columnsdata[Integer.parseInt(orderColumn[i])] + " " + orderDir[i]);
                }
            }
        }
        return ordersql.toString();
    }

    public String getOrderSql(String tableAlias) {
        StringBuffer ordersql = new StringBuffer();
        if (orderColumn != null && orderColumn.length > 0) {
            ordersql.append(" ORDER BY ");
            for (int i = 0; i < orderColumn.length; i++) {
                if (i == 0) {
                    ordersql.append(" " + tableAlias + "." + columnsdata[Integer.parseInt(orderColumn[i])] + " "
                            + orderDir[i]);
                } else {
                    ordersql.append(", " + tableAlias + "." + columnsdata[Integer.parseInt(orderColumn[i])] + " "
                            + orderDir[i]);
                }
            }
        }
        return ordersql.toString();
    }

    public String getDateOrderSql() {
        StringBuffer ordersql = new StringBuffer();
        if (orderColumn != null && orderColumn.length > 0) {
            ordersql.append(" ORDER BY ");
            for (int i = 0; i < orderColumn.length; i++) {
                String tablename = " vo.";
                if (columnsdata[Integer.parseInt(orderColumn[i])].equals("statDate")) {
                    tablename = " d.";
                }
                if (i == 0) {
                    ordersql.append(tablename + columnsdata[Integer.parseInt(orderColumn[i])] + " " + orderDir[i]);
                } else {
                    ordersql.append("," + tablename + columnsdata[Integer.parseInt(orderColumn[i])] + " " + orderDir[i]);
                }
            }
        }
        return ordersql.toString();
    }

    public String getSearchSql() {
        StringBuffer searchsql = new StringBuffer();
        if (search != null && search.length() > 0) {
            searchsql.append(" and ( ");
            for (int i = 0; i < columnsdata.length; i++) {
                if (i == 0) {
                    searchsql.append(" vo." + columnsdata[i] + " like '%" + search + "%'");
                } else {
                    searchsql.append(" or vo." + columnsdata[i] + " like '%" + search + "%'");
                }
            }
            searchsql.append(" ) ");
        }
        return searchsql.toString();
    }

    @Override
    public String toString() {
        String data = "";
        if (columnsdata != null && columnsdata.length > 0) {
            data = ", columnsdata=" + ArrytoString(columnsdata);

        }
        if (orderColumn != null && orderColumn.length > 0) {
            data = data + ", orderColumn=" + ArrytoString(orderColumn);

        }
        if (orderDir != null && orderDir.length > 0) {
            data = data + ", orderDir=" + ArrytoString(orderDir);

        }
        return "DataTableBean [draw=" + draw + ", length=" + length + ", search=" + search + ", start=" + start + data
                + "]";
    }

    public String ArrytoString(String[] strs) {
        if (strs == null) {
            return ",";
        } else {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < strs.length; i++) {
                sb.append("-" + strs[i]);
            }
            return sb.toString();
        }
    }

}
