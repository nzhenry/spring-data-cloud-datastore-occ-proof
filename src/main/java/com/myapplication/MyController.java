package com.myapplication;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("my-entity")
public class MyController {

    private final MyService myService;

    public MyController(MyService myService) {
        this.myService = myService;
    }

    @PutMapping("/{id}")
    public MyEntity put(
            @PathVariable String id,
            @RequestParam(defaultValue = "false") boolean transactional) {
        return transactional
                ? myService.putWithTransaction(id)
                : myService.putEntity(id);
    }
}
