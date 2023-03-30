package ua.servicedesk;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.servicedesk.controllers.UsersController;
import ua.servicedesk.dao.UserRepository;
import ua.servicedesk.domain.CurrentRoleHolder;
import ua.servicedesk.domain.requestfields.User;
import ua.servicedesk.services.controllerservices.UsersControllerService;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DisplayName("TEST USER:")
public class TestUser {
    @Autowired
    UsersController usersController;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UsersControllerService controllerService;

    CurrentRoleHolder roleHolder;
    MockMvc mockMvcController;

    public void buildRequestControllerService(String user){
        User dbUser = userRepository.findUserByName(user);

        roleHolder = new CurrentRoleHolder();
        roleHolder.setRole(dbUser.getRole());

        controllerService.setRoleHolder(roleHolder);
        usersController.setControllerService(controllerService);
    }

    public void buildMock(String user){
        buildRequestControllerService(user);
        mockMvcController = MockMvcBuilders.standaloneSetup(usersController).build();
    }

    @Test
    @DisplayName("save users test:")
    public void testTakeToWorkRequest() throws Exception {

        buildMock("user");
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("id","");
        paramsMap.put("roleid","2");
        paramsMap.put("name","John");
        paramsMap.put("email","email");
        paramsMap.put("password","1");
        paramsMap.put("isEnabled","true");
        mockMvcController.perform(MockMvcRequestBuilders.post("/save_user"))
                .andExpect(status().isOk());

    }
}
