package smurf.utils.query.model;

import smurf.data.ColumnData;
import smurf.data.SQLQueryConfig;
import smurf.utils.SQLQueryBuilder;

import java.util.List;

import static smurf.data.SQLQueryTypeTemplates.SELECT_LIST;

public class SelectListQuery extends SQLQuery implements SQLQueryBuilder {

  public SelectListQuery(SQLQueryConfig config, List<ColumnData> columnList) {
    super(config, columnList);
  }

  public String build() {
    String query = SELECT_LIST.getQueryTemplate();
    query = query.replace("{table}", getTableReplacement())
            .replace("{where}", getWhereReplacement())
            .replace("{returnType}", getReturnTypeReplacement())
            .replace("{limited}", getLimitedReplacement())
            .replace("{methodName}", getMethodNameReplacement("select"))
            .replace("{incomingArgs}", getIncomingArgsReplacement());
    return query;
  }

}
