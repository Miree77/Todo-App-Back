package com.example.todo.controller;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.TestRequestDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

  @GetMapping("/testGetMapping")
  public String testController() {
    return "Hello Controller get mapping";
  }

  @GetMapping("/testReP")
  public String test1(@PathVariable(required = false) int id) {
    return "Hello Mapping" + id;
  }

  @GetMapping("/testRequestParam")
  public String test2(@RequestParam(required = false) int id) {
    return "Hello Mapping " + id;
  }

  @GetMapping("/testRequestBody")
  public String testControllerRequestBody(@RequestBody TestRequestDTO requestDTO) {
    return "Hello Mapping " + requestDTO.getId() + " " + requestDTO.getMessage();
  }

  @GetMapping("/testResponseBody")
  public ResponseDTO<String> test3() {
    List<String> list = new ArrayList<>();
    list.add("Hello I!!");
    list.add("23455");

    ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
    //ResponseDTO<String> response = ResponseDTO.builder().data(list).build();

    return response;
  }

  @GetMapping("/testResponseEntity")
  public ResponseEntity<?> test4() {

    List<String> list = new ArrayList<>();

    list.add("HI 12");
    ResponseDTO<String> responseDTO = ResponseDTO.<String>builder().data(list).build();

    return ResponseEntity.badRequest().body(responseDTO);
    //return ResponseEntity.ok().body(responseDTO);
  }


}
