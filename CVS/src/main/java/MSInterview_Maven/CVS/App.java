package MSInterview_Maven.CVS;
import MSInterview_Maven.CVS.CSVInterview;

public class App 
{
    public static void main( String[] args )
    {
        CSVInterview CSVCreate = new CSVInterview();
        CSVCreate.SetDBname();
        CSVCreate.SetTableName();
        CSVCreate.SetCSVname();
        CSVCreate.ConnectDB();
        CSVCreate.readCSVline();
    }
}
