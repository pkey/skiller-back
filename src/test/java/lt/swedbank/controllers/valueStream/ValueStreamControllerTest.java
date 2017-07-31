package lt.swedbank.controllers.valueStream;

import lt.swedbank.beans.entity.ValueStream;
import lt.swedbank.beans.response.valueStream.ValueStreamResponse;
import lt.swedbank.handlers.RestResponseEntityExceptionHandler;
import lt.swedbank.services.valueStream.ValueStreamService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ValueStreamControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;


    @InjectMocks
    private ValueStreamController valueStreamController;

    @Mock
    private ValueStreamService valueStreamService;

    private ValueStream testValueStream;
    private ValueStreamResponse testValueStreamResponse;
    private List<ValueStreamResponse> testValueStreamResponseList;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(valueStreamController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        testValueStream = new ValueStream();
        testValueStream.setName("Value Stream");
        testValueStream.setId(new Long(1));
        testValueStreamResponseList = new ArrayList<>();
        testValueStreamResponseList.add(new ValueStreamResponse(testValueStream));
    }

    @Test
    public void getAll() throws Exception {
        testValueStreamResponse = new ValueStreamResponse(testValueStream);

        Mockito.when(valueStreamService.getAllValueStreamEntityResponseList()).thenReturn(testValueStreamResponseList);

        mockMvc.perform(get("/stream")
                .header("Authorization", "Bearer")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").isString());
    }
}
