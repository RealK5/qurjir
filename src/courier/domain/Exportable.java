package courier.domain;

import java.io.IOException;
import java.util.List;

public interface Exportable
{
    String exportToCSV() throws IOException;
    List<String> getExportableData();
}
