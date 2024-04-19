package cn.itcast.job.utils;


import com.baomidou.mybatisplus.annotation.TableField;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;

//@Component
public class ChromeDownloader implements Downloader {

    //声明驱动
    private WebDriver driver;

    public ChromeDownloader() {
        System.setProperty("webdriver.chrome.driver", "D:\\reallyD\\browse\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe"); // 替换为您的 Chrome 驱动程序路径
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless","--disable-cache"); // 启用无头模式
        this.driver = new ChromeDriver(options);
    }

    @Override
    public Page download(Request request, Task task) {
        try {
//            driver = getChromeDriver();
//            driver.manage().deleteAllCookies();
//            driver.get("chrome://settings/clearBrowserData");
            driver.get(request.getUrl());
            Thread.sleep(2000); // 等待页面加载
            // 获取页面 HTML 内容
            String htmlContent = driver.getPageSource();
            Page page = new Page();
            page.setRawText(htmlContent);
            page.setUrl(new PlainText(request.getUrl()));
            page.setRequest(request);
            page.setDownloadSuccess(true);
            return page;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setThread(int threadNum) {
    }

    private WebDriver getChromeDriver() {
        System.setProperty("webdriver.chrome.driver", "D:\\reallyD\\browse\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe"); // 替换为您的 Chrome 驱动程序路径
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-cache"); // 启用无头模式和禁用缓存
        // 添加其他配置选项...
        return new ChromeDriver(options);
    }

}