package smurf.data;

public enum SQLQueryTypeTemplates {

  SELECT_LIST(
          "\n\t@SqlQuery(\"SELECT * FROM {table} {where} {limited}\")" +
                      "\n\tList<{returnType}> {methodName}({incomingArgs});"
  ),
  SELECT_SINGLE(
          "\n\t@SqlQuery(\"SELECT * FROM {table} {where}\")" +
                      "\n\t{returnType} {methodName}({incomingArgs});"
  ),

  INSERT(
          "\n\t@GetGeneratedKeys" +
                      "\n\t@SqlUpdate(\"INSERT INTO {table} ({keys}) VALUES ({values})\")" +
                      "\n\tLong {methodName}({incomingObj});"
  ),
  UPDATE(
          "\n\t@SqlUpdate(\"UPDATE {table} SET {keyValuePairs} {where}\")" +
                      "\n\tvoid {methodName}({incomingObj});"
  ),
  DELETE(
          "\n\t@SqlUpdate(\"DELETE FROM {table} {where}\")" +
                      "\n\tvoid {methodName}({incomingArgs});"
  );

  private final String queryTemplate;

  SQLQueryTypeTemplates(String queryTemplate) {
    this.queryTemplate = queryTemplate;
  }

  public String getQueryTemplate() {
    return queryTemplate;
  }

}
