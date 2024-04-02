package processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Measurement;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private final Logger logger = LoggerFactory.getLogger(ResourcesFileLoader.class);
    private final File file;
    private final ObjectMapper reader;

    public ResourcesFileLoader(String fileName) {
        this.file = new File(fileName);
        this.reader = JsonMapper.builder().build();
    }

    @Override
    public List<Measurement> load() {
        try {
            return reader.readValue(file, new TypeReference<List<Measurement>>() {
            });
        } catch (IOException exc) {
            logger.error("Error while reading value from file:", exc);
            throw new FileProcessException(exc);
        }
    }
}
