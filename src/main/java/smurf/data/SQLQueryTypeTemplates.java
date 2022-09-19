package smurf.data;

public enum SQLQueryTypeTemplates {

  SELECT_LIST(
          "\n\t@SqlQuery(\"SELECT * FROM {table} {where} {limited}\")" +
                      "\n\tList<{returnType}> {methodName}({incomingArgs});\n\t"
  ),
  SELECT_SINGLE(
          "\n\t@SqlQuery(\"SELECT * FROM {table} {where}\")" +
                      "\n\t{returnType} {methodName}({incomingArgs});\n\t"
  ),

  INSERT(
          "\n\t@GetGeneratedKeys" +
                      "\n\t@SqlUpdate(\"INSERT INTO {table} ({keys}) VALUES ({values})\")" +
                      "\n\tLong {methodName}({incomingObj});\n\t"
  ),
  UPDATE(
          "\n\t@SqlUpdate(\"UPDATE {table} SET {keyValuePairs} {where}\")" +
                      "\n\tvoid {methodName}({incomingObj});\n\t"
  ),
  DELETE(
          "\n\t@SqlUpdate(\"DELETE FROM {table} {where}\")" +
                      "\n\tvoid {methodName}({incomingArgs});\n\t"
  );

  private final String queryTemplate;

  SQLQueryTypeTemplates(String queryTemplate) {
    this.queryTemplate = queryTemplate;
  }

  public String getQueryTemplate() {
    return queryTemplate;
  }

}
