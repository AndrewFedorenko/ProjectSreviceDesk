package ua.servicedesk;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.servicedesk.controllers.RequestsController;
import ua.servicedesk.dao.RequestsRepository;
import ua.servicedesk.dao.RoleRepository;
import ua.servicedesk.dao.UserRepository;
import ua.servicedesk.domain.CurrentRoleHolder;
import ua.servicedesk.domain.SupportRequest;
import ua.servicedesk.domain.requestfields.User;
import ua.servicedesk.mailUtils.InformingReason;
import ua.servicedesk.mailUtils.InformingReasonsFactory;
import ua.servicedesk.services.controllerservices.RequestsControllerService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@WithMockUser(username = "admin")
@DisplayName("TEST REQUEST:")
public class TestRequest {
    @Autowired
    RequestsController requestsController;
    @Autowired
    RequestsRepository requestsRepository;

    @Autowired
    RequestsControllerService requestsControllerService;

    CurrentRoleHolder roleHolder;
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    MockMvc mockMvcController;

    public void buildRequestControllerService(String user){
        User dbUser = userRepository.findUserByName(user);

        roleHolder = new CurrentRoleHolder();
        roleHolder.setRole(dbUser.getRole());

        requestsRepository.setRoleHolder(roleHolder);
        requestsControllerService.setRoleHolder(roleHolder);
        requestsControllerService.setRequestsRepository(requestsRepository);
        requestsController.setControllerService(requestsControllerService);

    }

    public void buildMock(String user){
        buildRequestControllerService(user);
        mockMvcController = MockMvcBuilders.standaloneSetup(requestsController).build();
    }


    @Test
    @DisplayName("save request test error:")
    public void testSaveRequestFalse() throws Exception {

        buildRequestControllerService("user");

        SupportRequest request = new SupportRequest();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("data", String.valueOf(LocalDateTime.now()));
        paramsMap.put("executorid","3");
        paramsMap.put("authorid","2");
        paramsMap.put("projectid","1");
        paramsMap.put("content","test content");

        JSONObject json = (JSONObject) new JSONParser()
                .parse(requestsControllerService.processSaveRequest(request, paramsMap));
        assertFalse(json.get("errorlist")!=null && ((String)json.get("errorlist")).isEmpty());
    }

    @Test
    @DisplayName("take to work test:")
    public void testTakeToWorkRequest() throws Exception {

            buildMock("admin");
            mockMvcController.perform(MockMvcRequestBuilders.get("/take_to_work"))
                    .andExpect(content().bytes("".getBytes()));

    }

    @Test
    @DisplayName("find reason New Request to send mail:")
    public void testNewRequestFilter() throws Exception {
        buildRequestControllerService("user");
        Map<String, String> paramsMap = new HashMap<>();

        List<InformingReason> reasons = InformingReasonsFactory.getInforminfReason(new SupportRequest(),
                requestsRepository,
                requestsControllerService.getReasonsService());
        assertTrue(reasons.size()==1);
    }

}
