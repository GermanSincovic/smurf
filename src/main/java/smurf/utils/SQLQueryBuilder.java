package smurf.utils;

import smurf.data.ColumnData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class SQLQueryBuilder {

  private final String sourceSimpleClassName;
  private final List<ColumnData> columnList;
  private final String tableName;
  private final ColumnData primaryKey;

  private static final String SELECT_BY = "selectBy";

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

  public String getSelectByUniqueKeyQueryList() {
    return columnList.stream()
            .filter(col -> col.isUniqueKey() || col.isPrimaryKey())
            .map(this::getSelectByUniqueKeyQuery)
            .collect(Collectors.joining("\n\n"));
  }

  public String getSelectByNonUniqueKeyQueryList() {
    List<String> queryList = new ArrayList<>();
    List<ColumnData> chronicleColumns = columnList.stream()
            .filter(ColumnData::isChronicle)
            .collect(Collectors.toList());
    List<ColumnData> regularColumns = columnList.stream()
            .filter(col -> !col.isUniqueKey() && !col.isPrimaryKey() && !col.isChronicle())
            .collect(Collectors.toList());
    queryList.add(chronicleColumns.stream()
            .map(col ->
                    this.getSelectByChronicleEarlierQuery(col) +
                            "\n\n" +
                            this.getSelectByChronicleLaterQuery(col) +
                            "\n\n" +
                            this.getSelectByChronicleRangeQuery(col)
            )
            .collect(Collectors.joining("\n\n")));
    regularColumns
            .forEach(rc -> {
              queryList.add(getSelectByKeyQuery(rc));
              queryList.add(getSelectByKeyWithLimitQuery(rc));
              queryList.add(
                      chronicleColumns.stream()
                              .map(cc ->
                                      this.getSelectByKeyAndChronicleEarlierQuery(rc, cc)
                                              + "\n\n"
                                              + this.getSelectByKeyAndChronicleLaterQuery(rc, cc)
                                              + "\n\n"
                                              + this.getSelectByKeyAndChronicleRangeQuery(rc, cc)
                                              + "\n\n"
                                              + this.getSelectByKeyAndChronicleEarlierQueryLimited(rc, cc)
                                              + "\n\n"
                                              + this.getSelectByKeyAndChronicleLaterQueryLimited(rc, cc)
                                              + "\n\n"
                                              + this.getSelectByKeyAndChronicleRangeQueryLimited(rc, cc)
                              ).collect(Collectors.joining("\n\n")));
            });
    return String.join("\n\n", queryList);
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
            QueryTypeTemplates.UPDATE_BY_UNIQUE_KEY.getQueryTemplate(),
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
            QueryTypeTemplates.DELETE_BY_UNIQUE_KEY.getQueryTemplate(),
            tableName,
            primaryKey.getDbName(),
            "deleteBy" + makeFirstLetterUpperCase(primaryKey.getCodeName()),
            primaryKey.getTypeShort(),
            primaryKey.getCodeName()
    );
  }

  private String getSelectByKeyQuery(ColumnData column) {
    return String.format(
            QueryTypeTemplates.SELECT_BY_KEY.getQueryTemplate(),
            tableName,
            column.getDbName(),
            sourceSimpleClassName,
            SELECT_BY + makeFirstLetterUpperCase(column.getCodeName()),
            column.getTypeShort(),
            column.getCodeName()
    );
  }

  public String getSelectByUniqueKeyQuery(ColumnData column) {
    return String.format(
            QueryTypeTemplates.SELECT_BY_UNIQUE_KEY.getQueryTemplate(),
            tableName,
            column.getDbName(),
            sourceSimpleClassName,
            SELECT_BY + makeFirstLetterUpperCase(column.getCodeName()),
            column.getTypeShort(),
            column.getCodeName()
    );
  }

  private String getSelectByChronicleEarlierQuery(ColumnData column) {
    return String.format(
            QueryTypeTemplates.SELECT_BY_CHRONICLE_EARLIER.getQueryTemplate(),
            tableName,
            column.getDbName(),
            sourceSimpleClassName,
            SELECT_BY + makeFirstLetterUpperCase(column.getCodeName()) + "Earlier",
            column.getTypeShort()
    );
  }

  private String getSelectByChronicleLaterQuery(ColumnData column) {
    return String.format(
            QueryTypeTemplates.SELECT_BY_CHRONICLE_LATER.getQueryTemplate(),
            tableName,
            column.getDbName(),
            sourceSimpleClassName,
            SELECT_BY + makeFirstLetterUpperCase(column.getCodeName()) + "Later",
            column.getTypeShort()
    );
  }

  private String getSelectByChronicleRangeQuery(ColumnData column) {
    return String.format(
            QueryTypeTemplates.SELECT_BY_CHRONICLE_RANGE.getQueryTemplate(),
            tableName,
            column.getDbName(),
            column.getDbName(),
            sourceSimpleClassName,
            SELECT_BY + makeFirstLetterUpperCase(column.getCodeName()) + "InRange",
            column.getTypeShort(),
            column.getTypeShort()
    );
  }

  private String getSelectByKeyAndChronicleEarlierQuery(ColumnData key, ColumnData chronicle) {
    return String.format(
            QueryTypeTemplates.SELECT_BY_KEY_AND_CHRONICLE_EARLIER.getQueryTemplate(),
            tableName,
            key.getDbName(),
            chronicle.getDbName(),
            sourceSimpleClassName,
            SELECT_BY + makeFirstLetterUpperCase(key.getCodeName()) + "And" + makeFirstLetterUpperCase(chronicle.getCodeName()) + "Earlier",
            key.getTypeShort(),
            key.getCodeName(),
            chronicle.getTypeShort()
    );
  }

  private String getSelectByKeyAndChronicleLaterQuery(ColumnData key, ColumnData chronicle) {
    return String.format(
            QueryTypeTemplates.SELECT_BY_KEY_AND_CHRONICLE_LATER.getQueryTemplate(),
            tableName,
            key.getDbName(),
            chronicle.getDbName(),
            sourceSimpleClassName,
            SELECT_BY + makeFirstLetterUpperCase(key.getCodeName()) + "And" + makeFirstLetterUpperCase(chronicle.getCodeName()) + "Later",
            key.getTypeShort(),
            key.getCodeName(),
            chronicle.getTypeShort()
    );
  }

  private String getSelectByKeyAndChronicleRangeQuery(ColumnData key, ColumnData chronicle) {
    return String.format(
            QueryTypeTemplates.SELECT_BY_KEY_AND_CHRONICLE_RANGE.getQueryTemplate(),
            tableName,
            key.getDbName(),
            chronicle.getDbName(),
            chronicle.getDbName(),
            sourceSimpleClassName,
            SELECT_BY + makeFirstLetterUpperCase(key.getCodeName()) + "And" + makeFirstLetterUpperCase(chronicle.getCodeName()) + "InRange",
            key.getTypeShort(),
            key.getCodeName(),
            chronicle.getTypeShort(),
            chronicle.getTypeShort()
    );
  }

  private String getSelectByKeyAndChronicleEarlierQueryLimited(ColumnData key, ColumnData chronicle) {
    return String.format(
            QueryTypeTemplates.SELECT_BY_KEY_AND_CHRONICLE_EARLIER_LIMITED.getQueryTemplate(),
            tableName,
            key.getDbName(),
            chronicle.getDbName(),
            sourceSimpleClassName,
            SELECT_BY + makeFirstLetterUpperCase(key.getCodeName()) + "And" + makeFirstLetterUpperCase(chronicle.getCodeName()) + "EarlierWithLimit",
            key.getTypeShort(),
            key.getCodeName(),
            chronicle.getTypeShort()
    );
  }

  private String getSelectByKeyAndChronicleLaterQueryLimited(ColumnData key, ColumnData chronicle) {
    return String.format(
            QueryTypeTemplates.SELECT_BY_KEY_AND_CHRONICLE_LATER_LIMITED.getQueryTemplate(),
            tableName,
            key.getDbName(),
            chronicle.getDbName(),
            sourceSimpleClassName,
            SELECT_BY + makeFirstLetterUpperCase(key.getCodeName()) + "And" + makeFirstLetterUpperCase(chronicle.getCodeName()) + "LaterWithLimit",
            key.getTypeShort(),
            key.getCodeName(),
            chronicle.getTypeShort()
    );
  }

  private String getSelectByKeyAndChronicleRangeQueryLimited(ColumnData key, ColumnData chronicle) {
    return String.format(
            QueryTypeTemplates.SELECT_BY_KEY_AND_CHRONICLE_RANGE_LIMITED.getQueryTemplate(),
            tableName,
            key.getDbName(),
            chronicle.getDbName(),
            chronicle.getDbName(),
            sourceSimpleClassName,
            SELECT_BY + makeFirstLetterUpperCase(key.getCodeName()) + "And" + makeFirstLetterUpperCase(chronicle.getCodeName()) + "InRangeWithLimit",
            key.getTypeShort(),
            key.getCodeName(),
            chronicle.getTypeShort(),
            chronicle.getTypeShort()
    );
  }

  private String getSelectByKeyWithLimitQuery(ColumnData column) {
    return String.format(
            QueryTypeTemplates.SELECT_BY_KEY_LIMITED.getQueryTemplate(),
            tableName,
            column.getDbName(),
            sourceSimpleClassName,
            SELECT_BY + makeFirstLetterUpperCase(column.getCodeName()) + "WithLimit",
            column.getTypeShort(),
            column.getCodeName()
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

  private List<ColumnData> getPrimaryKeyColumnList() {
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
