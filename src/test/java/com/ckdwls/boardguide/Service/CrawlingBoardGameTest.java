package com.ckdwls.boardguide.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CrawlingBoardGameTest {
    @Autowired
    private CrawlingBoardGame crawlingBoardGame;

    @Test
    @DisplayName("크롤링 데이터 확인")
    public void testCrawling() {
        crawlingBoardGame.BoardLife();
    }
}
