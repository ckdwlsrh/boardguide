package com.ckdwls.boardguide.Service;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ckdwls.boardguide.Entity.BoardGame;
import com.ckdwls.boardguide.Persistence.BoardGameRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CrawlingBoardGame {
    
    @Value("${crawling.url}")
    private String rootUrl;

    @Autowired
    private BoardGameRepository boardGameRepository;

    public void BoardLife() {
        try {
            List<String> urlList = boardGameUrlList();
            // 하나하나 정보 가져오기 
            for(String url : urlList) {
                Connection connection = Jsoup.connect(rootUrl + url);
                Document document = connection.get();
                Elements body = document.select("div.grid-right.radius-5");
                Elements elements = body.select("div.top-info-box.flex");
                
                String boardGameTitle = elements.select("h1#boardgame-title").text();
                // image 
                String imageAddress = rootUrl + elements.select("div.img").select("img").first().attr("src");
                imageAddress = downImage(boardGameTitle,imageAddress);

                Element firstElements = elements.select("div.info-main.flex-1").select("dl.info-row").first();

                String rating = firstElements.select("div.main-play-rate-svg.me-2").text();
                log.info(rating);
                String level = firstElements.nextElementSibling().select("span#game-weight").text();
                log.info(level);


                Element secondElements = elements.select("div.game-sub-title").select("dl.info-row").first();
                String players = secondElements.nextElementSibling().select("dd.data").text();
                log.info(players);

                Long age = Long.parseLong(secondElements.nextElementSibling().nextElementSibling().select("dd.data").text().replaceAll("[^0-9]", ""));
                log.info(age.toString());
                String playingTime = secondElements.nextElementSibling().nextElementSibling().nextElementSibling().select("dd.data").text();
                log.info(playingTime);
                String[] playerTemp = players.replaceAll("[^0-9]"," ").split(" ");
                Long playerMin = Long.parseLong(playerTemp[0]);
                log.info(playerMin.toString());
                Long playerMax = Long.parseLong(playerTemp[playerTemp.length - 1]);
                log.info(playerMax.toString());

                String[] playingTimeTemp = playingTime.replaceAll("[^0-9]"," ").split(" ");
                Long playingTimeAverage = (Long.parseLong(playingTimeTemp[0]) + Long.parseLong(playingTimeTemp[playingTimeTemp.length - 1])) / 2;

                
                
                List<String> themes = getThemes(url);
                for (String theme : themes) {
                    BoardGame boardGame = BoardGame.builder()
                                                    .boardGame(boardGameTitle)
                                                    .age(age)
                                                    .playerMin(playerMin)
                                                    .playerMax(playerMax)
                                                    .playingTime(playingTimeAverage)
                                                    .rating(rating)
                                                    .imageAddress(imageAddress)
                                                    .level(level)
                                                    .genre(theme)
                                                    .build();
                    boardGameRepository.save(boardGame);
                }
                
            }

        }catch(Exception e) {
            System.out.println(e);
        }
    }

    private List<String> boardGameUrlList() {
        List<String> urlList = new ArrayList<>();
        final int TOTALPAGES = 1;
        try {
            // 페이지 별 제목 크롤링
            for (int i = 1; i <= TOTALPAGES; i ++) {
                Connection connection = Jsoup.connect(rootUrl + "rank.php?pg=" + Integer.toString(i));
                Document document = connection.get();
                Elements elements = document.select("div.grid-wrapper").select("div.game-rank-div-wrapper.no-border").select("a.storage-title-div.flex-1");
                for (Element element: elements) {
                    urlList.add(element.attr("href"));
                }
            }
            
            return urlList;
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return null;
    }

    
    private String downImage(String title, String url) {
        String downLoadUrl = "/Users/kochangjin/Downloads/";
        String imageAddress = downLoadUrl + title + ".jpg";
        log.info(imageAddress);
        try {
            Response response = Jsoup.connect(url).ignoreContentType(true).execute();
            FileOutputStream out = new FileOutputStream(new File(imageAddress));
            out.write(response.bodyAsBytes());
            out.close();
            return imageAddress;
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return null;
    }
    private List<String> getThemes(String url) {
        List<String> themesList = new ArrayList<>();
        System.setProperty("webdriver.chrome.driver", "/Users/kochangjin/chromedriver-mac-arm64/chromedriver");
        try {
            WebDriver webDriver = new ChromeDriver();

            webDriver.get(rootUrl + url + "&page=credits");

            Thread.sleep(2000);

            List<WebElement> elements = webDriver.findElements(By.cssSelector("div.title-wrapper.credit.flex"));

            for(WebElement element: elements) {
                String factor = element.findElement(By.cssSelector("div.title.flex")).getText().trim().split("\n")[0].trim();
                log.info(factor);
                if (factor.equals("테마")) {
                    log.info("a");
                    List<WebElement> themes = element.findElements(By.cssSelector("div.credits-row"));

                    for (WebElement theme: themes) {
                        log.info(theme.getText().trim());
                        themesList.add(theme.getText().trim());
                    }
                    break;
                }
            }

            webDriver.close();
            return themesList;
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
