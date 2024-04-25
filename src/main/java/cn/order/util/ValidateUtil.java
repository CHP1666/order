package cn.order.util;

import cn.hutool.core.collection.CollUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 对象参数校验工具类
 *
 * @author JesseChen
 * @date 2023/01/14
 */
public class ValidateUtil {

  /**
   * 对单个对象进行参数校验
   *
   * @param o 待校验对象
   * @return 错误信息列表
   */
  public static List<String> validate(Object o, Class<?>... groups) {
    List<String> messageList = new ArrayList<>();
    ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
    Validator validator = vf.getValidator();
    Set<ConstraintViolation<Object>> set = validator.validate(o, groups);
    for (ConstraintViolation<Object> constraintViolation : set) {
      messageList.add(constraintViolation.getMessage());
    }
    return CollUtil.isEmpty(messageList) ? null : messageList;
  }

  public static boolean isValidLocation(String[] location) {
    double longitude = Double.parseDouble(location[0]);
    double latitude = Double.parseDouble(location[0]);
    return isValidLongitude(longitude) && isValidLatitude(latitude);
  }

  // 校验经度是否合法
  public static boolean isValidLongitude(double longitude) {
    return longitude >= -180 && longitude <= 180;
  }

  // 校验纬度是否合法
  public static boolean isValidLatitude(double latitude) {
    return latitude >= -90 && latitude <= 90;
  }
}
