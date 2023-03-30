package ua.servicedesk;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.servicedesk.domain.FieldsToCheckHolder;
import ua.servicedesk.domain.RequiredField;
import ua.servicedesk.services.FieldsChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

//@SpringBootTest
////@WebMvcTest
//////@DataJpaTest
////@SpringJUnitConfig(TestConfig.class)
//@WithMockUser(username = "admin")
@DisplayName("TEST FIELDS CHECKER:")
public class TestFieldChecker {

//    MockMvc mockMvc;
//
//    @Autowired
//    FieldsChecker checker;
//    @Autowired
//    CustomHttpSecurityConfig customHttpSecurityConfig;
//    @Autowired
//    UsersController usersController;
    @Test
    @DisplayName("check user fields test:")
    public void testUsersFieldsChecker(){

        List<RequiredField> rfList = new ArrayList<>();
        RequiredField rf = new RequiredField();
        rf.setEntityName("User");
        rf.setFieldName("name");
        rfList.add(rf);

        rf = new RequiredField();
        rf.setEntityName("User");
        rf.setFieldName("email");
        rfList.add(rf);

        FieldsToCheckHolder checkHolder = new FieldsToCheckHolder();
        checkHolder.setFieldsToCheck(rfList);

        Map<String, String> fieldsMap = new HashMap<>();
        fieldsMap.put("name", "user");
        fieldsMap.put("email", "user@user.ua");

        FieldsChecker fieldsChecker = new FieldsChecker();
        fieldsChecker.setFieldsToCheckHolder(checkHolder);
        assertTrue(fieldsChecker.checkFields("User", fieldsMap).isEmpty());

    }
    @Test
    @DisplayName("check requests fields test:")
    public void testRequestsFieldsChecker(){

        List<RequiredField> rfList = new ArrayList<>();
        RequiredField rf = new RequiredField();
        rf.setEntityName("SupportRequest");
        rf.setFieldName("date");
        rfList.add(rf);

        rf = new RequiredField();
        rf.setEntityName("SupportRequest");
        rf.setFieldName("customer");
        rfList.add(rf);

        rf = new RequiredField();
        rf.setEntityName("SupportRequest");
        rf.setFieldName("project");
        rfList.add(rf);

        rf = new RequiredField();
        rf.setEntityName("SupportRequest");
        rf.setFieldName("status");
        rfList.add(rf);

        FieldsToCheckHolder checkHolder = new FieldsToCheckHolder();
        checkHolder.setFieldsToCheck(rfList);

        Map<String, String> fieldsMap = new HashMap<>();
        fieldsMap.put("date", "");
        fieldsMap.put("customer", "1");
        fieldsMap.put("project", "2");
        fieldsMap.put("status", "1");

        FieldsChecker fieldsChecker = new FieldsChecker();
        fieldsChecker.setFieldsToCheckHolder(checkHolder);
        assertFalse(fieldsChecker.checkFields("SupportRequest", fieldsMap).isEmpty());

    }

}
