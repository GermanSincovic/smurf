package smurf.utils;

import com.google.common.collect.Sets;
import smurf.annotations.DAOCRUDEnum;
import smurf.data.ColumnData;
import smurf.data.SQLQueryConfig;
import smurf.utils.query.model.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SQLQueryHelper {

  private final List<ColumnData> columnDataList;
  private final SQLQueryConfig config;
  private final Set<DAOCRUDEnum> queries;

  public SQLQueryHelper(List<ColumnData> columnList, String tableName, String className, DAOCRUDEnum[] queries) {
    this.queries = Sets.newHashSet(queries);
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
    String result = "";
    if (queries.contains(DAOCRUDEnum.SELECT)) {
      result = result.concat(new SelectListQuery(config, columnDataList).build());
      result = result.concat(new SelectSingleQuery(config, columnDataList).build());
    }
    if (queries.contains(DAOCRUDEnum.INSERT)) {
      result = result.concat(new InsertQuery(config, columnDataList).build());
    }
    if (queries.contains(DAOCRUDEnum.UPDATE)) {
      result = result.concat(new UpdateQuery(config, columnDataList).build());
    }
    if (queries.contains(DAOCRUDEnum.DELETE)) {
      result = result.concat(new DeleteQuery(config, columnDataList).build());
    }
    return result;
  }

}
