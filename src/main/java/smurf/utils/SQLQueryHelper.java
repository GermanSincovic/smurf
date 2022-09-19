package smurf.utils;

import smurf.data.ColumnData;
import smurf.data.SQLQueryConfig;
import smurf.utils.query.model.*;

import java.util.List;
import java.util.stream.Collectors;

public class SQLQueryHelper {

  private final List<ColumnData> columnDataList;
  private final SQLQueryConfig config;

  public SQLQueryHelper(List<ColumnData> columnList, String tableName, String className) {
    this.columnDataList = columnList;
    List<ColumnData> filteringColumns = columnList.stream()
            .filter(c -> c.isPrimaryKey() || c.isSearchCriteria())
            .collect(Collectors.toList());
    this.config = new SQLQueryConfig()
            .setTable(tableName)
            .setReturnType(className)
            .setWhere(filteringColumns)
            .setLimited(true);
  }

  public String generate() {
    return String.join("",
            new SelectListQuery(config, columnDataList).build(),
            new SelectSingleQuery(config, columnDataList).build(),
            new InsertQuery(config, columnDataList).build(),
            new UpdateQuery(config, columnDataList).build(),
            new DeleteQuery(config, columnDataList).build()
    );
  }

}
