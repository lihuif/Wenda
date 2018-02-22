package com.example.Test.service;

import org.springframework.stereotype.Service;

@Service
public class WendaService {
    public String getMessage(int userId){
        return "hello "+ userId;
    }
}
