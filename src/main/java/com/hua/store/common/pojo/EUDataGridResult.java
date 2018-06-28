package com.hua.store.common.pojo;

import java.util.List;

public class EUDataGridResult {

    private long total;
    private List<?> rows;

    public EUDataGridResult() {
    }

    public EUDataGridResult(long total, List<?> dataGrid) {
        this.total = total;
        this.rows = dataGrid;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
