package smurf.processors;

import com.google.auto.service.AutoService;
import smurf.annotations.DAO;
import smurf.annotations.DAOCRUDEnum;
import smurf.data.ColumnData;
import smurf.utils.SQLQueryHelper;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class DAOProcessor extends AbstractProcessor {

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> supportedAnnotationTypes = new HashSet<>();
    supportedAnnotationTypes.add(DAO.class.getCanonicalName());
    return supportedAnnotationTypes;
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

    String packageName;

    for (TypeElement annotation : annotations) {
      Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);

      for (Element annotatedElement : annotatedElements) {
        if (annotatedElement.getKind().isClass()) {
          String sourceSimpleClassName = annotatedElement.getSimpleName().toString();
          packageName = annotatedElement.getEnclosingElement().toString();
          String tableName = annotatedElement.getAnnotation(DAO.class).value();
          DAOCRUDEnum[] queries = annotatedElement.getAnnotation(DAO.class).queries();
          List<ColumnData> columnDataList = annotatedElement.getEnclosedElements().stream()
                  .filter(element -> element.getKind().isField())
                  .map(ColumnData::new)
                  .collect(Collectors.toList());
          try {
            writeDaoFile(packageName, sourceSimpleClassName, tableName, columnDataList, queries);
          } catch (IOException e) {
            debug(e.getMessage());
          }
        }
      }
    }
    return true;
  }

  private void writeDaoFile(String packageName, String sourceSimpleClassName, String tableName, List<ColumnData> columnDataList, DAOCRUDEnum[] queries) throws IOException {

    String daoSimpleClassName = sourceSimpleClassName + "DAO";

    if (columnDataList.stream().filter(ColumnData::isPrimaryKey).count() > 1) {
      throw new IllegalArgumentException("There can be only 1 field annotated with @PrimaryKey annotation");
    }

    SQLQueryHelper builder = new SQLQueryHelper(columnDataList, tableName, sourceSimpleClassName, queries);

    JavaFileObject daoFile = processingEnv.getFiler().createSourceFile(daoSimpleClassName);
    try (PrintWriter out = new PrintWriter(daoFile.openWriter())) {

      out.println("package " + packageName + ";");
      out.println();
      out.println("import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;");
      out.println("import org.jdbi.v3.sqlobject.customizer.Bind;");
      out.println("import org.jdbi.v3.sqlobject.customizer.BindBean;");
      out.println("import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;");
      out.println("import org.jdbi.v3.sqlobject.statement.SqlQuery;");
      out.println("import org.jdbi.v3.sqlobject.statement.SqlUpdate;");
      out.println();
      out.println("import java.math.BigDecimal;");
      out.println("import java.sql.Timestamp;");
      out.println("import java.util.List;");
      out.println();
      out.println("@RegisterBeanMapper(" + sourceSimpleClassName + ".class)");
      out.println("public interface " + daoSimpleClassName + " {");
      out.println(builder.generate());
      out.println("}");
      out.println();

    } catch (IOException e) {
      debug(e.getMessage());
    }

  }

  private void debug(String data) {
    String marker = "[DEBUG]";
    processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, marker + data);
  }

}
