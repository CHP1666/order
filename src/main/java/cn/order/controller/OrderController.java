package cn.order.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import cn.order.model.domain.Order;
import cn.order.model.dto.AddOrderRequestBody;
import cn.order.model.dto.PatchOrderRequestBody;
import cn.order.service.OrderService;
import cn.order.util.ValidateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JesseChen
 */
@Slf4j
@Api(tags = "订单管理")
@RestController
@RequestMapping("/orders")
public class OrderController {

  @Resource
  OrderService orderService;

  @Value("${geo.distance-url}")
  private String distanceUrl;

  @Value("${geo.api-key}")
  private String apiKey;

  /**
   * 存放不同id对应的锁
   */
  private final ConcurrentHashMap<Integer, ReentrantLock> locks = new ConcurrentHashMap<>();

  @ApiOperation("添加订单")
  @PostMapping
  public Object addOrder(@RequestBody AddOrderRequestBody requestBody) {
    // 参数校验
    if (requestBody.getOrigin().length != 2 || requestBody.getDestination().length != 2) {
      return MapUtil.of("error", "ERROR_DESCRIPTION");
    }
    String[] origin = requestBody.getOrigin();
    String[] destination = requestBody.getDestination();
    if (!ValidateUtil.isValidLocation(origin) || !ValidateUtil.isValidLocation(destination)) {
      return MapUtil.of("error", "ERROR_DESCRIPTION");
    }
    // 查询距离
    Map<String, Object> param = new HashMap<>();
    param.put("origins", origin);
    param.put("destinations", destination);
    param.put("key", apiKey);

    Order order = new Order();
    try (HttpResponse response = HttpRequest.get(distanceUrl)
//        .setHttpProxy("127.0.0.1", 7890)
        .form(param)
        .execute()) {
      JSON json = JSONUtil.parse(response.body());
      String distance = json.getByPath("rows[0].elements[0].distance.value", String.class);

      order.setDistance(distance);
      order.setStatus("UNASSIGNED");
      orderService.save(order);
    }
    return order;
  }


  @ApiOperation("获取订单")
  @PatchMapping("/{id}")
  public Map<String, Object> patchOrder(@PathVariable Integer id,
      @RequestBody PatchOrderRequestBody requestBody) {
    ReentrantLock lock = locks.computeIfAbsent(id, key -> new ReentrantLock());
    // 该id的订单有线程在访问，直接return
    if (!lock.tryLock()) {
      return MapUtil.of("error", "ERROR_DESCRIPTION");
    }
    try {
      // 参数校验
      if (requestBody == null || !"TAKEN".equals(requestBody.getStatus())) {
        return MapUtil.of("error", "ERROR_DESCRIPTION");
      }

      // 业务逻辑
      Order order = orderService.getById(id);
      if (order == null || "TAKEN".equals(order.getStatus())) {
        return MapUtil.of("error", "ERROR_DESCRIPTION");
      }
      order.setStatus("TAKEN");
      orderService.updateById(order);
      return MapUtil.of("status", "SUCCESS");
    } finally {
      lock.unlock();
      // 忘记在处理完业务逻辑后将对应id的锁对象删除了，有内存泄露的问题
      locks.remove(id);
    }
  }

  @ApiOperation("订单列表")
  @GetMapping
  public Object getOrderList(@RequestParam Integer page,
      @RequestParam Integer limit) {
    if (page == null || limit == null || page < 1 || limit < 1) {
      return MapUtil.of("error", "ERROR_DESCRIPTION");
    }
    Page<Order> orderPage = orderService.lambdaQuery()
        .page(new Page<>(page, limit));
    return orderPage.getRecords();
  }

}
