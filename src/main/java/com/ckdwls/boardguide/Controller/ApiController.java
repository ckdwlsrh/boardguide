package com.ckdwls.boardguide.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ckdwls.boardguide.Service.CrawlingBoardGame;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private CrawlingBoardGame CrawlingBoardGame;

    @RequestMapping(value = "/crawling", method = RequestMethod.GET)
    public void CrawlingTest() {
        CrawlingBoardGame.BoardLife();
    }
    
    @RequestMapping(value = "/boardlist", method=RequestMethod.GET)
    public String requestMethodName(@RequestParam String param) {
        return new String();
    }
    
}
