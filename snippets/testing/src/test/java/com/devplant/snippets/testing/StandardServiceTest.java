package com.devplant.snippets.testing;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestingApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StandardServiceTest {

    @Autowired
    private StandardService standardService;

    @SpyBean
    private StandardService.SubService subService;


    @Test
    public void verifyInvocationMockResult(){
        when(subService.makeItPretty(Mockito.anyString())).thenReturn("can't do that boss");
        String pretty =standardService.echoPretty("Make it pretty!");
        verify(subService).makeItPretty("Make it pretty!");
        assertThat(pretty).isEqualTo("Echo: can't do that boss");
    }


    @Test
    public void verifyRealInvocation(){
        String pretty =standardService.echoPretty("Make it pretty!");
        assertThat(pretty).isEqualTo("Echo: MAKE IT PRETTY! Uppercase Sparkle !");
    }
}
