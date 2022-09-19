package smurf.utils.query.model;

import smurf.data.ColumnData;
import smurf.data.SQLQueryConfig;
import smurf.utils.SQLQueryBuilder;

import java.util.List;

import static smurf.data.SQLQueryTypeTemplates.UPDATE;

public class UpdateQuery extends SQLQuery implements SQLQueryBuilder {

  public UpdateQuery(SQLQueryConfig config, List<ColumnData> columnList) {
    super(config, columnList);
  }

  @Override
  public String build() {
    String query = UPDATE.getQueryTemplate();
    return query.replace("{table}", getTableReplacement())
            .replace("{keyValuePairs}", getKeyPairList())
            .replace("{where}", getWhereReplacement())
            .replace("{methodName}", getMethodNameReplacement("update"))
            .replace("{incomingObj}", getIncomingObjReplacement());
  }
}
