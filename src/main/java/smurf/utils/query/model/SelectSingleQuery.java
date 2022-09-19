package smurf.utils.query.model;

import smurf.data.ColumnData;
import smurf.data.SQLQueryConfig;
import smurf.utils.SQLQueryBuilder;

import java.util.List;

import static smurf.data.SQLQueryTypeTemplates.SELECT_SINGLE;

public class SelectSingleQuery extends SQLQuery implements SQLQueryBuilder {

  public SelectSingleQuery(SQLQueryConfig config, List<ColumnData> columnList) {
    super(config, columnList);
  }

  @Override
  public String build() {
    String query = SELECT_SINGLE.getQueryTemplate();
    return query.replace("{table}", getTableReplacement())
            .replace("{where}", getWhereReplacement())
            .replace("{returnType}", getReturnTypeReplacement())
            .replace("{methodName}", getMethodNameReplacement("select"))
            .replace("{incomingArgs}", getIncomingArgsReplacement());
  }
}
