package com.samsonan.counters.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

/**
 * These are FULL integration tests!
 * 
 * We invoking controller method and check if the correct data is read from DB or if it is updated correctly
 *
 * There should be much more tests, but due to the lack of time i've left just a couple for demo purposes 
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DatabaseSetup("/db_init.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    WithSecurityContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@ActiveProfiles("test")
public class CounterServiceTest {

    @Autowired
    private MockMvc mvc;
    
    @Test
    @WithMockUser(username = "demo", roles = "USER")
    public void testVisibilityScopeOfUser() throws Exception {

        /**
         * TODO: the proper way of course is to compare not the strings, rather transform JSON -> object and analyze it
         */
        
        String expectedContent = "[{\"id\":1,\"name\":\"one\",\"type\":3,\"owner\":\"demo\",\"value\":{\"id\":1,\"value\":123.45,\"valueDate\":null}},{\"id\":3,\"name\":\"three\",\"type\":2,\"owner\":\"demo\",\"value\":{\"id\":3,\"value\":9876.01,\"valueDate\":null}}]";
        
        mvc.perform(get("/api/counters")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedContent));
    
    }  

    @Test
    @WithMockUser(username = "demo", roles = "ADMIN")
    public void testVisibilityScopeOfADMIN() throws Exception {

        /**
         * TODO: the proper way of course is to compare not the strings, rather transform JSON -> object and analyze it
         */
        
        String expectedContent = "[{\"id\":1,\"name\":\"one\",\"type\":3,\"owner\":\"demo\",\"value\":{\"id\":1,\"value\":123.45,\"valueDate\":null}},{\"id\":2,\"name\":\"two\",\"type\":3,\"owner\":\"user\",\"value\":{\"id\":2,\"value\":456.222,\"valueDate\":null}},{\"id\":3,\"name\":\"three\",\"type\":2,\"owner\":\"demo\",\"value\":{\"id\":3,\"value\":9876.01,\"valueDate\":null}}]";
        
        mvc.perform(get("/api/counters")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedContent));
        
    }  
    
    @Test
    @WithMockUser(username = "demo", roles = "USER")
    public void testUpdateWithWrongParams() throws Exception {
        mvc.perform(post("/counters/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "")
                .param("value", "123.23")
                .param("type", "2"))
                .andExpect(view().name("counter/edit")); // error -> return to edit
    }

    @Test
    @WithMockUser(username = "demo", roles = "USER")
    @ExpectedDatabase(value="/db_after_delete.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void testSuccessfulDelete() throws Exception {
        mvc.perform(delete("/api/counters/3")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());
    }
    
    
    
}
