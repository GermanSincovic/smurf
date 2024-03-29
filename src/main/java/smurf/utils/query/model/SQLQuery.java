package smurf.utils.query.model;

import smurf.data.ColumnData;
import smurf.data.SQLQueryConfig;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class SQLQuery {

  protected final SQLQueryConfig config;
  protected final List<ColumnData> columnList;

  protected SQLQuery(SQLQueryConfig config, List<ColumnData> columnList) {
    this.config = config;
    this.columnList = columnList;
  }

  protected final String getWhereReplacement(Predicate<ColumnData> exceptionRule) {
    if (config.getWhere().isEmpty()) {
      return "";
    } else {
      return "WHERE " + config.getWhere().stream()
              .filter(exceptionRule)
              .map(col -> col.getDbName() + " = :" + col.getCodeName())
              .collect(Collectors.joining(" AND "));
    }
  }

  protected final String getWhereReplacement() {
    return getWhereReplacement(x -> true);
  }

  protected String getTableReplacement() {
    return config.getTable();
  }

  protected String getMethodNameReplacement(String prefix) {
    return (!config.getWhere().isEmpty())
            ? prefix.concat("By").concat(config.getWhere().stream()
            .map(col -> makeFirstLetterUpperCase(col.getCodeName()))
            .collect(Collectors.joining("And")))
            : prefix;
  }

  protected String getLimitedReplacement() {
    return (config.isLimited()) ? " LIMIT :limit OFFSET :offset" : "";
  }

  protected String getReturnTypeReplacement() {
    return config.getReturnType();
  }

  protected String getIncomingArgsReplacement() {
    String rIncomingArgs = config.getWhere().stream()
            .map(col -> String.format("@Bind(\"%s\") %s %s", col.getCodeName(), col.getTypeShort(), col.getCodeName()))
            .collect(Collectors.joining(", "));
    return (config.isLimited())
            ? rIncomingArgs.concat(", @Bind(\"limit\") long limit, @Bind(\"offset\") long offset")
            : rIncomingArgs;
  }

  protected String getIncomingObjReplacement() {
    return String.format("@BindBean %s %s", getReturnTypeReplacement(), makeFirstLetterLowerCase(getReturnTypeReplacement()));
  }

  protected String makeFirstLetterUpperCase(String string) {
    return string.substring(0, 1).toUpperCase() + string.substring(1);
  }

  protected String makeFirstLetterLowerCase(String string) {
    return string.substring(0, 1).toUpperCase() + string.substring(1);
  }

  protected String getColumnCodeNamesList() {
    return columnList.stream()
            .filter(columnData -> !columnData.isPrimaryKey())
            .map(columnData -> ":" + columnData.getCodeName())
            .collect(Collectors.joining(", "));
  }

  protected String getColumnDBNamesList() {
    return columnList.stream()
            .filter(columnData -> !columnData.isPrimaryKey())
            .map(ColumnData::getDbName)
            .collect(Collectors.joining(", "));
  }

  protected String getKeyPair(ColumnData column) {
    return String.format("%s = :%s", column.getDbName(), column.getCodeName());
  }

  protected String getKeyPairList() {
    return columnList.stream()
            .filter(columnData -> !columnData.isPrimaryKey())
            .map(this::getKeyPair)
            .collect(Collectors.joining(", "));
  }
}
