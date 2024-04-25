package cn.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.*;


import cn.hutool.json.JSONUtil;
import cn.order.controller.OrderController;
import cn.order.model.dto.AddOrderRequestBody;
import cn.order.model.dto.PatchOrderRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author JesseChen
 */
@Slf4j
@SpringBootTest
public class OrderControllerTest {

    MockMvc mockMvc;

    // 初始化MockMvc
    @BeforeEach
    void setUp(WebApplicationContext wac) {
        // 方式1：明确指定需要测试的“Controller”类
        this.mockMvc = MockMvcBuilders.standaloneSetup(new OrderController()).build();

        // 方式2：基于Spring容器进行配置，包含了Spring MVC环境和所有“Controller”类。
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }


    /**
     * 查询订单测试
     */
    @Test
    public void getOrderListTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
            get("/orders?page=1&limit=10")
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        log.info(mvcResult.getResponse().getContentAsString());

        mvcResult = this.mockMvc.perform(
            get("/orders?page=-1&limit=-10")
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        log.info(mvcResult.getResponse().getContentAsString());
    }


    /**
     * 添加订单测试
     */
    @Test
    public void addOrderTest() throws Exception {
        // 调用content()方法传递json字符串参数
        AddOrderRequestBody requestBody = new AddOrderRequestBody();
        requestBody.setOrigin(new String[]{ "23", "114"});
        requestBody.setDestination(new String[]{ "23.55", "114.2"});
        String requestJsonStr = JSONUtil.toJsonPrettyStr(requestBody);
        MvcResult mvcResult = this.mockMvc.perform(post("/orders")
                .content(requestJsonStr)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        log.info(mvcResult.getResponse().getContentAsString());
    }


    /**
     * 获取订单测试
     */
    @Test
    public void patchOrderTest() throws Exception {
        // 调用content()方法传递json字符串参数
        PatchOrderRequestBody requestBody = new PatchOrderRequestBody();
        requestBody.setStatus("TAKEN");
        String requestJsonStr = JSONUtil.toJsonPrettyStr(requestBody);
        MvcResult mvcResult = this.mockMvc.perform(patch("/orders/1")
                .content(requestJsonStr)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        log.info(mvcResult.getResponse().getContentAsString());
    }
}
