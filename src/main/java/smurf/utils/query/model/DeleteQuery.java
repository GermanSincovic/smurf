package smurf.utils.query.model;

import smurf.data.ColumnData;
import smurf.data.SQLQueryConfig;
import smurf.utils.SQLQueryBuilder;

import java.util.List;

import static smurf.data.SQLQueryTypeTemplates.DELETE;

public class DeleteQuery extends SQLQuery implements SQLQueryBuilder {

  public DeleteQuery(SQLQueryConfig config, List<ColumnData> columnList) {
    super(config, columnList);
  }

  public String build() {
    String query = DELETE.getQueryTemplate();
    return query.replace("{table}", getTableReplacement())
            .replace("{where}", getWhereReplacement())
            .replace("{methodName}", getMethodNameReplacement("delete"))
            .replace("{incomingArgs}", getIncomingArgsReplacement());
  }
}
