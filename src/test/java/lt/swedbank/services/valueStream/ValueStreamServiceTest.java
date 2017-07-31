package lt.swedbank.services.valueStream;

import lt.swedbank.beans.entity.ValueStream;
import lt.swedbank.beans.response.valueStream.ValueStreamResponse;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.repositories.ValueStreamRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class ValueStreamServiceTest {

    @InjectMocks
    private ValueStreamService valueStreamService;

    @Mock
    private ValueStreamRepository valueStreamRepository;

    private List<ValueStream> valueStreamsList;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        valueStreamsList = TestHelper.fetchValueStreams();
    }

    @Test
    public void getAllValueStreams() throws Exception {
        Mockito.when(valueStreamRepository.findAll()).thenReturn(valueStreamsList);

        List<ValueStream> resultValueStreamsList = (List<ValueStream>) valueStreamService.getAllValueStreams();

        Assert.assertEquals(valueStreamsList, resultValueStreamsList);
    }

    @Test
    public void getAllValueStreamEntityResponseList() {
        Mockito.when(valueStreamRepository.findAll()).thenReturn(valueStreamsList);
        Mockito.when(valueStreamService.getAllValueStreams()).thenReturn(valueStreamsList);

        List<ValueStreamResponse> resultValueStreamResponses = (List<ValueStreamResponse>) valueStreamService.getAllValueStreamEntityResponseList();

        Assert.assertEquals(valueStreamsList.get(0).getName(), resultValueStreamResponses.get(0).getName());
        Assert.assertEquals(valueStreamsList.get(0).getId(), resultValueStreamResponses.get(0).getId());
        Assert.assertEquals(valueStreamsList.size(), resultValueStreamResponses.size());

    }
}
