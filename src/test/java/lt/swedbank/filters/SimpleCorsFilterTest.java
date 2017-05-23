package lt.swedbank.filters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleCorsFilterTest {


    @InjectMocks
    private SimpleCorsFilter filter;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void doFilterHappyPath() throws Exception {
        MockFilterChain mockFilterChain = new MockFilterChain();
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();

        filter.doFilter(req, res, mockFilterChain);

        assertEquals(res.getHeader("Access-Control-Allow-Origin"), "*");
        assertEquals(res.getHeader("Access-Control-Allow-Methods"), "POST, GET, OPTIONS, DELETE");
        assertEquals(res.getHeader("Access-Control-Max-Age"), "3600");
        assertEquals(res.getHeader("Access-Control-Allow-Headers"), "x-requested-with, authorization, Content-Type");


    }

    @Test
    public void doFilterOptionsRequest() throws Exception {
        MockFilterChain mockFilterChain = new MockFilterChain();
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        req.setMethod("OPTIONS");

        filter.doFilter(req, res, mockFilterChain);

        assertEquals(res.getHeader("Access-Control-Allow-Origin"), "*");
        assertEquals(res.getHeader("Access-Control-Allow-Methods"), "POST, GET, OPTIONS, DELETE");
        assertEquals(res.getHeader("Access-Control-Max-Age"), "3600");
        assertEquals(res.getHeader("Access-Control-Allow-Headers"), "x-requested-with, authorization, Content-Type");
        assertEquals(res.getStatus(), HttpServletResponse.SC_OK);


    }

    @Test
    public void init() throws Exception {

    }

}