package xiaopeng.zdys;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import xiaopeng.zdys.mapper.BookMapper;
import xiaopeng.zdys.pojo.Books;
import xiaopeng.zdys.service.BookService;

import java.util.List;

@SpringBootTest
@Slf4j
class Demo1ApplicationTests {

	@Value("${spring.datasource.url}")
	private String dataSourceUrl;

	@Autowired
	private BookService bookService;

	@Autowired
	private BookMapper bookMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void readApplication(){
		System.out.println("helloWorld");
		System.out.println(dataSourceUrl);
	}

	/**
	 * 增加书籍测试
	 */
	@Test
	void addBooks(){
		Books books = new Books();
		books.setBookName("JavaScript");
		books.setBookCounts(4);
		books.setDetail("如果可以重来，我愿只学JavaScript");
//		bookService.addBooks(books);
		bookMapper.addBooks(books);
	}

	@Test
	void deleteById(){
		bookMapper.deleteById(6);
	}

	@Test
	void updateBook(){
		Books books = new Books();
		books.setBookID(1);
		books.setDetail("从入门到入土");
		bookMapper.updateBook(books);
	}

	@Test
	void findById(){
		Books books = bookMapper.getById(1);
		System.out.println(books);
	}

	@Test
	void findAllBook(){
		List<Books> booksList = bookMapper.getAllBook();
		for(Books book : booksList){
			System.out.println(book);
		}
	}

}
