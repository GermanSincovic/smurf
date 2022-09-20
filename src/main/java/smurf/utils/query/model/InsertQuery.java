package smurf.utils.query.model;

import smurf.data.ColumnData;
import smurf.data.SQLQueryConfig;
import smurf.utils.SQLQueryBuilder;

import java.util.List;

import static smurf.data.SQLQueryTypeTemplates.INSERT;

public class InsertQuery extends SQLQuery implements SQLQueryBuilder {

  public InsertQuery(SQLQueryConfig config, List<ColumnData> columnList) {
    super(config, columnList);
  }

  public String build() {
    String query = INSERT.getQueryTemplate();
    return query.replace("{table}", getTableReplacement())
            .replace("{keys}", getColumnDBNamesList())
            .replace("{values}", getColumnCodeNamesList())
            .replace("{methodName}", getMethodNameReplacement("insert"))
            .replace("{incomingObj}", getIncomingObjReplacement());
  }
}
