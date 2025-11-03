package cmdotender.TaskLine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerIT extends AbstractPostgresIT {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void post_then_get_task() throws Exception {
        var createJson = """
                {
                  "title": "From controller IT",
                  "description": "desc from controller test",
                  "status": "OPEN"
                }
                """;

        // 1) POST /api/tasks
        MvcResult postResult = mvc.perform(
                        post("/api/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createJson)
                )
                .andExpect(status().isCreated())              // @ResponseStatus(CREATED)
                .andExpect(jsonPath("$.id").isNumber())       // TaskDto.id
                .andExpect(jsonPath("$.title").value("From controller IT"))
                .andExpect(jsonPath("$.description").value("desc from controller test"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andReturn();

        String postBody = postResult.getResponse().getContentAsString();
        Number idNum = JsonPath.read(postBody, "$.id");
        Long id = idNum.longValue();

        mvc.perform(get("/api/tasks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.intValue()))
                .andExpect(jsonPath("$.title").value("From controller IT"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void post_validation_error_returns_400() throws Exception {
        var invalidJson = """
                {
                  "title": "",
                  "description": "missing title",
                  "status": "OPEN"
                }
                """;

        mvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
