package lt.swedbank.services.valueStream;

import lt.swedbank.beans.entity.ValueStream;
import lt.swedbank.beans.response.valueStream.ValueStreamResponse;
import lt.swedbank.repositories.ValueStreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ValueStreamService {

    @Autowired
    private ValueStreamRepository valueStreamRepository;

    public Iterable<ValueStream> getAllValueStreams() {
        return valueStreamRepository.findAll();
    }

    public Iterable<ValueStreamResponse> getAllValueStreamEntityResponseList() {
        return ((List<ValueStream>) getAllValueStreams()).stream().map(ValueStreamResponse::new)
                .collect(Collectors.toList());
    }
}
