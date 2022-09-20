package smurf.data;

import org.jdbi.v3.core.mapper.reflect.ColumnName;
import smurf.annotations.Chronicle;
import smurf.annotations.PrimaryKey;
import smurf.annotations.SearchCriteria;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

import static java.util.Objects.nonNull;

public class ColumnData {

  private final Element field;
  private final String type;
  private final String typeShort;
  private final String codeName;
  private final String dbName;
  private final boolean isPrimaryKey;
  private final boolean isChronicle;
  private final boolean isSearchCriteria;

  public ColumnData(Element fieldElement) {
    this.field = fieldElement;
    this.type = fieldElement.asType().toString();
    this.typeShort = getLastStringElement(type.split("\\."));
    this.codeName = fieldElement.getSimpleName().toString();
    this.dbName = getDBColumnNameOfField(fieldElement);
    this.isPrimaryKey = hasFieldPrimaryKeyAnnotation();
    this.isChronicle = hasFieldChronicleAnnotation();
    this.isSearchCriteria = hasFieldSearchCriteriaAnnotation();
  }

  public String getTypeShort() {
    return typeShort;
  }

  public String getCodeName() {
    return codeName;
  }

  public String getDbName() {
    return dbName;
  }

  public boolean isPrimaryKey() {
    return isPrimaryKey;
  }

  public boolean isChronicle() {
    return isChronicle;
  }

  public boolean isSearchCriteria() {
    return hasFieldSearchCriteriaAnnotation();
  }

  private String getDBColumnNameOfField(Element fieldElement) {
    if (hasFieldAnnotation(ColumnName.class)) {
      return fieldElement.getAnnotation(ColumnName.class).value();
    } else {
      return fieldElement.getSimpleName().toString();
    }
  }

  private boolean hasFieldPrimaryKeyAnnotation() {
    return hasFieldAnnotation(PrimaryKey.class);
  }

  private boolean hasFieldChronicleAnnotation() {
    return hasFieldAnnotation(Chronicle.class);
  }

  private boolean hasFieldSearchCriteriaAnnotation() {
    return hasFieldAnnotation(SearchCriteria.class);
  }

  private boolean hasFieldAnnotation(Class<? extends Annotation> annotation) {
    return nonNull(field.getAnnotation(annotation));
  }

  private String getLastStringElement(String[] array) {
    return array[array.length - 1];
  }

  @Override
  public String toString() {
    return "ColumnData{" +
            "field=" + field +
            ", type='" + type + '\'' +
            ", typeShort='" + typeShort + '\'' +
            ", codeName='" + codeName + '\'' +
            ", dbName='" + dbName + '\'' +
            ", isPrimaryKey=" + isPrimaryKey +
            ", isChronicle=" + isChronicle +
            ", isSearchCriteria=" + isSearchCriteria +
            '}';
  }

}
