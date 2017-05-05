package lt.swedbank.interceptors.auth;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import lt.swedbank.SkillerApplication;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Arrays;

import static org.junit.Assert.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class AuthInterceptorTest {

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

//    @Test
//    public void cacheResourcesConfiguration() throws Exception {
//        AuthInterceptor interceptor = new AuthInterceptor();
//
//        interceptor.preHandle(request, response, null);
//
//        Iterable<String> cacheControlHeaders = response.getHeaders("Cache-Control");
//        assertThat(cacheControlHeaders, Matchers.hasItem("max-age=10"));
//    }
    @Test
    public void preHandle() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test");
        request.setMethod("GET");


        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecutionChain handlerExecutionChain = handlerMapping.getHandler(request);

        HandlerInterceptor[] interceptors = handlerExecutionChain.getInterceptors();

        for(HandlerInterceptor interceptor : interceptors){
            interceptor.preHandle(request, response, handlerExecutionChain.getHandler());
        }

        ModelAndView mav = handlerAdapter. handle(request, response, handlerExecutionChain.getHandler());

        for(HandlerInterceptor interceptor : interceptors){
            interceptor.postHandle(request, response, handlerExecutionChain.getHandler(), mav);
        }

        assertEquals(200, response.getStatus());
    }

    @Test
    public void postHandle() throws Exception {
    }

    @Test
    public void afterCompletion() throws Exception {
    }

}