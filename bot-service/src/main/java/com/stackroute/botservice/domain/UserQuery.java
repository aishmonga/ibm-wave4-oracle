package com.stackroute.botservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQuery {

    @Id
    private String id;
    private String concept;
    private List<Query> query;

}
