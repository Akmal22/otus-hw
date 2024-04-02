package processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FileSerializer implements Serializer {
    private final Logger logger = LoggerFactory.getLogger(FileSerializer.class);
    private final ObjectMapper mapper;
    private final File file;

    public FileSerializer(String fileName) {
        this.file = new File(fileName);
        this.mapper = JsonMapper.builder().build();
    }

    @Override
    public void serialize(Map<String, Double> data) {
        Map<String, Double> sortedData = data.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        try {
            mapper.writeValue(file, sortedData);
        } catch (IOException exc) {
            logger.error("Error while serializing list of objects:", exc);
            throw new FileProcessException(exc);
        }
    }
}
