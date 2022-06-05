package smurf.utils;

public enum QueryTypeTemplates {
  SELECT_ALL(
          "\n\t@SqlQuery(\"SELECT * FROM %s\")" +
                      "\n\tList<%s> %s();"
  ),
  SELECT_BY_PRIMARY_KEY(
          "\n\t@SqlQuery(\"SELECT * FROM %s WHERE %s\")" +
                      "\n\tList<%s> %s(@BindBean %s %s);"
  ),
  INSERT(
          "\n\t@GetGeneratedKeys" +
                      "\n\t@SqlUpdate(\"INSERT INTO %s (%s) VALUES (%s)\")" +
                      "\n\t%s %s(@BindBean %s %s);"
  ),
  UPDATE_BY_PRIMARY_KEY(
          "\n\t@SqlUpdate(\"UPDATE %s SET %s WHERE %s\")" +
                      "\n\tvoid %s(@BindBean %s %s);"
  ),
  DELETE_BY_PRIMARY_KEY(
          "\n\t@SqlUpdate(\"DELETE FROM %s WHERE %s\")" +
                      "\n\tvoid %s(@BindBean %s %s);"
  );

  private final String queryTemplate;

  QueryTypeTemplates(String queryTemplate) {
    this.queryTemplate = queryTemplate;
  }

  public String getQueryTemplate() {
    return queryTemplate;
  }

}
