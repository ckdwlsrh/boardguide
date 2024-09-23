package com.ckdwls.boardguide.DTO;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseDTO<T>{
    private String error;
    private List<T> data;
}