package com.beyond.basic.b1_hello.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Student {
    private String name;
    private String email;
    private List<Score> scores;



    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    static class Score {
        private String subject;
        private int point;
    }

}


