package smurf.utils;

import smurf.data.ColumnData;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class SQLQueryBuilder {

  private final String sourceSimpleClassName;
  private final List<ColumnData> columnList;
  private final String tableName;
  private final ColumnData primaryKey;

  public SQLQueryBuilder(List<ColumnData> columnList, String tableName, String sourceSimpleClassName) throws IllegalArgumentException {
    this.sourceSimpleClassName = sourceSimpleClassName;
    this.columnList = columnList;
    this.primaryKey = initPrimaryKey();
    this.tableName = tableName;
  }

  public ColumnData getPrimaryKey() {
    return this.primaryKey;
  }

  public String getSelectWithLimit() {
    return String.format(
            QueryTypeTemplates.SELECT_LIMITED.getQueryTemplate(),
            tableName,
            sourceSimpleClassName,
            "selectWithLimit"
    );
  }

  public String getSelectAllQuery() {
    return String.format(
            QueryTypeTemplates.SELECT_ALL.getQueryTemplate(),
            tableName,
            sourceSimpleClassName,
            "selectAll"
    );
  }

  public String getSelectByPrimaryKeyQuery() {
    return String.format(
            QueryTypeTemplates.SELECT_BY_PRIMARY_KEY.getQueryTemplate(),
            tableName,
            getKeyPair(primaryKey),
            sourceSimpleClassName,
            "selectAllBy" + makeFirstLetterUpperCase(primaryKey.getCodeName()),
            sourceSimpleClassName,
            makeFirstLetterLowerCase(sourceSimpleClassName)
    );
  }

  public String getInsertQuery() {
    return String.format(
            QueryTypeTemplates.INSERT.getQueryTemplate(),
            tableName,
            getColumnDBNamesList(),
            getColumnCodeNamesList(),
            nonNull(primaryKey) ? primaryKey.getTypeShort() : "void",
            "insert",
            sourceSimpleClassName,
            makeFirstLetterLowerCase(sourceSimpleClassName)
    );
  }

  public String getUpdateByPrimaryKeyQuery() {
    return String.format(
            QueryTypeTemplates.UPDATE_BY_PRIMARY_KEY.getQueryTemplate(),
            tableName,
            getKeyPairList(),
            getKeyPair(primaryKey),
            "updateBy" + makeFirstLetterUpperCase(primaryKey.getCodeName()),
            sourceSimpleClassName,
            makeFirstLetterLowerCase(sourceSimpleClassName)
    );
  }

  public String getDeleteQueryByPrimaryKey() {
    return String.format(
            QueryTypeTemplates.DELETE_BY_PRIMARY_KEY.getQueryTemplate(),
            tableName,
            getKeyPair(primaryKey),
            "deleteBy" + makeFirstLetterUpperCase(primaryKey.getCodeName()),
            sourceSimpleClassName,
            makeFirstLetterLowerCase(sourceSimpleClassName)
    );
  }

  private String getColumnCodeNamesList() {
    return columnList.stream()
            .filter(columnData -> !columnData.isPrimaryKey())
            .map(columnData -> ":" + columnData.getCodeName())
            .collect(Collectors.joining(", "));
  }

  private String getColumnDBNamesList() {
    return columnList.stream()
            .filter(columnData -> !columnData.isPrimaryKey())
            .map(ColumnData::getDbName)
            .collect(Collectors.joining(", "));
  }

  private String getKeyPair(ColumnData column) {
    return String.format("%s = :%s", column.getDbName(), column.getCodeName());
  }

  private String getKeyPairList() {
    return columnList.stream()
            .filter(columnData -> !columnData.isPrimaryKey())
            .map(this::getKeyPair)
            .collect(Collectors.joining(", "));
  }

  private ColumnData initPrimaryKey() throws IllegalArgumentException {
    if (getPrimaryKeyColumnList().size() > 1) {
      throw new IllegalArgumentException("There can be only 1 field annotated with @PrimaryKey annotation");
    } else if (getPrimaryKeyColumnList().isEmpty()) {
      return null;
    } else {
      return getPrimaryKeyColumnList().get(0);
    }
  }

  private List<ColumnData> getPrimaryKeyColumnList(){
    return columnList.stream()
            .filter(ColumnData::isPrimaryKey)
            .collect(Collectors.toList());
  }

  private String makeFirstLetterUpperCase(String string) {
    return string.substring(0, 1).toUpperCase() + string.substring(1);
  }

  private String makeFirstLetterLowerCase(String string) {
    return string.substring(0, 1).toUpperCase() + string.substring(1);
  }

}
