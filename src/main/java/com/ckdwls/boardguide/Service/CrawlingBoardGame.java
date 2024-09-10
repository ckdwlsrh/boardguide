package com.ckdwls.boardguide.Service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ckdwls.boardguide.Entity.BoardGame;
import com.ckdwls.boardguide.Persistence.BoardGameRepository;

@Service
public class CrawlingBoardGame {
    
    @Value("${crawling.url}")
    private String rootUrl;

    @Autowired
    private BoardGameRepository boardGameRepository;

    public void BoardLife() {
        List<String> urlList = new ArrayList<>();
        try {
            for (int i = 1; i <= 10; i ++) {
                Connection connection = Jsoup.connect(rootUrl + "rank.php?pg=" + Integer.toString(i));
                Document document = connection.get();
                Elements elements = document.select("div.grid-wrapper").select("div.game-rank-div-wrapper.no-border").select("a.storage-title-div.flex-1");
                for (Element element: elements) {
                    urlList.add(element.attr("href"));
                }
            }
            for(String url : urlList) {
                Connection connection = Jsoup.connect(rootUrl + url + "&page=credits");
                Document document = connection.get();

                Elements elements = document.select("div.top-info-box.flex");
                Element firstElements = elements.select("div.flex-div").select("div.info-row").first();

                String rating = firstElements.select("div.main-play-rate-svg.me-2").text();
                String level = firstElements.nextElementSibling().select("span#game-weight").text();

                Element secondElements = elements.select("div.game-sub-title").select("dl.info-row").first();
                String players = secondElements.nextElementSibling().select("dd.data").text();
                Long age = Long.parseLong(secondElements.nextElementSibling().nextElementSibling().select("dd.data").text().replaceAll("[^0-9]", ""));
                String playingTime = secondElements.nextElementSibling().nextElementSibling().nextElementSibling().select("dd.data").text();

                String[] playerTemp = players.replaceAll("[^0-9]"," ").split(" ");
                Long playerMin = Long.parseLong(playerTemp[0]);
                Long playerMax = Long.parseLong(playerTemp[1]);

                String[] playingTimeTemp = playingTime.replaceAll("[^0-9]"," ").split(" ");
                Long playingTimeAverage = (Long.parseLong(playingTimeTemp[0]) + Long.parseLong(playingTimeTemp[1])) / 2;

                BoardGame boardGame = BoardGame.builder()
                                               .boardGame(elements.select("h1#game-title").text())
                                               .age(age)
                                               .playerMin(playerMin)
                                               .playerMax(playerMax)
                                               .playingTime(playingTimeAverage)
                                               .rating(rating)
                                               //.imageAddress()
                                               .level(level)
                                               .build();
                
                Elements themes = document.select("div#game-main-content-box").select("div.title-wrapper.credit.flex");
                for(Element theme : themes) {
                    if(theme.select("div.title.flex").text().trim() == "테마") {
                        Elements genres = theme.select("div.credits-row");
                        for(Element genre: genres) {
                            boardGame.setGenre(genre.select("a").text());
                            boardGameRepository.save(boardGame);
                        }
                        break;
                    }
                }
            }

        }catch(Exception e) {
            System.out.println(e);
        }
    }
}
