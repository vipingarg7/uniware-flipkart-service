package com.uniware.integrations.web.exception;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author Abhishek Kumar on 28/11/18.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BadRequest extends RuntimeException {

    private String message;

}
