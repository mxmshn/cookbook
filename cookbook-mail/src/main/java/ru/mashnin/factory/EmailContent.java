package ru.mashnin.factory;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailContent {
    private String mailTo;
    private String subject;
    private String text;
}
