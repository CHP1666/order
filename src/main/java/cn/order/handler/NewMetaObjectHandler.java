package cn.order.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author ：JesseChen
 */
@Component
public class NewMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
      this.setFieldValByName("updateTime", new Date(), metaObject);
      this.setFieldValByName("createTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
      this.setFieldValByName("updateTime", new Date(), metaObject);
    }


}
