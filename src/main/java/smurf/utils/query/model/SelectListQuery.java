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
    return query.replace("{table}", getTableReplacement())
            .replace("{where}", getWhereReplacement(columnData -> !columnData.isPrimaryKey() && !columnData.isUniqueKey()))
            .replace("{returnType}", getReturnTypeReplacement())
            .replace("{limited}", getLimitedReplacement())
            .replace("{methodName}", getMethodNameReplacement("selectList"))
            .replace("{incomingArgs}", getIncomingArgsReplacement());
  }

}
