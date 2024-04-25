package cn.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.order.model.domain.Order;
import cn.order.service.OrderService;
import cn.order.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
* @author JesseChen
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

}




