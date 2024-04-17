package com.example.homeworkm3v1;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
public class Contact {
    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private int phone;

}
