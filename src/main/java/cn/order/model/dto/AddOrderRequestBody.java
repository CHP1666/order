package cn.order.model.dto;

import lombok.Data;

/**
 * @author JesseChen
 */
@Data
public class AddOrderRequestBody {

  private String[] origin;

  private String[] destination;
}
