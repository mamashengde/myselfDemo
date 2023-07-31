package xiaopeng.zdys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaopeng.zdys.mapper.BookMapper;
import xiaopeng.zdys.pojo.Books;
import xiaopeng.zdys.service.BookService;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    /**
     * 添加书籍
     * @param books
     */
    public void addBooks(Books books) {
        bookMapper.addBooks(books);
    }

    /**
     * 根据id来删除书籍
     * @param id
     */
    @Override
    public void deleteById(int id) {
        bookMapper.deleteById(id);
    }

    /**
     * 根据id来查询书籍
     * @param id
     * @return
     */
    @Override
    public Books getbookById(int id) {
        Books books = bookMapper.getById(id);
        return books;
    }

    /**
     * 查询全部书籍
     * @return
     */
    @Override
    public List<Books> getAll() {
        List<Books> booksList = bookMapper.getAllBook();
        return booksList;
    }

    /**
     * 更新书籍信息
     * @param books
     */
    @Override
    public void updateBook(Books books) {
        bookMapper.updateBook(books);
    }
}
