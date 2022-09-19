package smurf.data;

import java.util.List;

public class SQLQueryConfig {

  private String table;
  private List<ColumnData> where;
  private boolean isLimited;
  private String returnType;

  public String getTable() {
    return table;
  }

  public SQLQueryConfig setTable(String table) {
    this.table = table;
    return this;
  }

  public List<ColumnData> getWhere() {
    return where;
  }

  public SQLQueryConfig setWhere(List<ColumnData> where) {
    this.where = where;
    return this;
  }

  public boolean isLimited() {
    return isLimited;
  }

  public SQLQueryConfig setLimited(boolean limited) {
    isLimited = limited;
    return this;
  }

  public String getReturnType() {
    return returnType;
  }

  public SQLQueryConfig setReturnType(String returnType) {
    this.returnType = returnType;
    return this;
  }

}
