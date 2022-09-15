package smurf.utils;

public enum QueryTypeTemplates {

  SELECT_ALL(
          "\n\t@SqlQuery(\"SELECT * FROM %s\")" +
                      "\n\tList<%s> %s();"
  ),
  SELECT_LIMITED(
          "\n\t@SqlQuery(\"SELECT * FROM %s LIMIT ? OFFSET ?\")" +
                      "\n\tList<%s> %s(long limit, long offset);"
  ),


  SELECT_BY_UNIQUE_KEY(
          "\n\t@SqlQuery(\"SELECT * FROM %s WHERE %s = ?\")" +
                      "\n\t%s %s(%s %s);"
  ),
  SELECT_BY_KEY(
          "\n\t@SqlQuery(\"SELECT * FROM %s WHERE %s = ?\")" +
                      "\n\tList<%s> %s(%s %s);"
  ),


  SELECT_BY_KEY_LIMITED(
          "\n\t@SqlQuery(\"SELECT * FROM %s WHERE %s = ? LIMIT ? OFFSET ?\")" +
                      "\n\tList<%s> %s(%s %s, long limit, long offset);"
  ),


  SELECT_BY_CHRONICLE_EARLIER(
          "\n\t@SqlQuery(\"SELECT * FROM %s WHERE %s < ? \")" +
                      "\n\tList<%s> %s(%s upperTime);"
  ),
  SELECT_BY_CHRONICLE_LATER(
          "\n\t@SqlQuery(\"SELECT * FROM %s WHERE %s > ? \")" +
                      "\n\tList<%s> %s(%s lowerTime);"
  ),
  SELECT_BY_CHRONICLE_RANGE(
          "\n\t@SqlQuery(\"SELECT * FROM %s WHERE %s > ? AND %s < ?\")" +
                      "\n\tList<%s> %s(%s lowerTime, %s upperTime);"
  ),


  SELECT_BY_KEY_AND_CHRONICLE_EARLIER(
          "\n\t@SqlQuery(\"SELECT * FROM %s WHERE %s = ? AND %s < ? \")" +
                      "\n\tList<%s> %s(%s %s, %s upperTime);"
  ),
  SELECT_BY_KEY_AND_CHRONICLE_LATER(
          "\n\t@SqlQuery(\"SELECT * FROM %s WHERE %s = ? AND %s > ? \")" +
                      "\n\tList<%s> %s(%s %s, %s lowerTime);"
  ),
  SELECT_BY_KEY_AND_CHRONICLE_RANGE(
          "\n\t@SqlQuery(\"SELECT * FROM %s WHERE %s = ? AND %s > ? AND %s < ?\")" +
                      "\n\tList<%s> %s(%s %s, %s lowerTime, %s upperTime);"
  ),


  SELECT_BY_KEY_AND_CHRONICLE_EARLIER_LIMITED(
          "\n\t@SqlQuery(\"SELECT * FROM %s WHERE %s = ? AND %s < ? LIMIT ? OFFSET ?\")" +
                  "\n\tList<%s> %s(%s %s, %s upperTime, long limit, long offset);"
  ),
  SELECT_BY_KEY_AND_CHRONICLE_LATER_LIMITED(
          "\n\t@SqlQuery(\"SELECT * FROM %s WHERE %s = ? AND %s > ? LIMIT ? OFFSET ?\")" +
                  "\n\tList<%s> %s(%s %s, %s lowerTime, long limit, long offset);"
  ),
  SELECT_BY_KEY_AND_CHRONICLE_RANGE_LIMITED(
          "\n\t@SqlQuery(\"SELECT * FROM %s WHERE %s = ? AND %s > ? AND %s < ? LIMIT ? OFFSET ?\")" +
                  "\n\tList<%s> %s(%s %s, %s lowerTime, %s upperTime, long limit, long offset);"
  ),


  INSERT(
          "\n\t@GetGeneratedKeys" +
                      "\n\t@SqlUpdate(\"INSERT INTO %s (%s) VALUES (%s)\")" +
                      "\n\t%s %s(@BindBean %s %s);"
  ),
  UPDATE_BY_UNIQUE_KEY(
          "\n\t@SqlUpdate(\"UPDATE %s SET %s WHERE %s\")" +
                      "\n\tvoid %s(@BindBean %s %s);"
  ),
  DELETE_BY_UNIQUE_KEY(
          "\n\t@SqlUpdate(\"DELETE FROM %s WHERE %s = ?\")" +
                      "\n\tvoid %s(%s %s);"
  );

  private final String queryTemplate;

  QueryTypeTemplates(String queryTemplate) {
    this.queryTemplate = queryTemplate;
  }

  public String getQueryTemplate() {
    return queryTemplate;
  }

}
