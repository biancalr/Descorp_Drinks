package test.com.mycompany.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.operation.DatabaseOperation;

/**
 *
 * @author Bianca
 */
public class DbUnitUtil {
    private static final String XML_FILE = "C:\\Users\\bibil\\OneDrive\\Documentos\\NetBeansProjects\\idrink\\src\\main\\resources\\dbUnit\\dataset.xml";
    
    @SuppressWarnings("UseSpecificCatch")
    public static void inserirDados() {
        Connection conn = null;
        IDatabaseConnection db_conn = null;
        
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/idrink?zeroDateTimeBehavior=convertToNull", 
                    "root", "1730");
            db_conn = new DatabaseConnection(conn, "idrink");
            DatabaseConfig dbConfig = db_conn.getConfig();
            dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
            dbConfig.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());
            FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
            builder.setColumnSensing(true);
            File file = new File(XML_FILE);
            InputStream in = new FileInputStream(file);
            //InputStream in = DbUnitUtil.class.getResourceAsStream(XML_FILE); Nao funciona no Windows
            IDataSet dataset = builder.build(in);
            DatabaseOperation.CLEAN_INSERT.execute(db_conn, dataset);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }finally{
            try {
                if (conn != null) {
                    conn.close();
                }
                if (db_conn != null) {
                    db_conn.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    
    }
    
}
