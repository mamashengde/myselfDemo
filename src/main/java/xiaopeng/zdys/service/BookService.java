package xiaopeng.zdys.service;

import xiaopeng.zdys.pojo.Books;

import java.util.List;


public interface BookService {

    /**
     * 增加书籍
     * @param books
     */
    public void addBooks(Books books);

    /**
     * 根据id删除书籍
     * @param id
     */
    void deleteById(int id);

    /**
     * 根据id来查询书籍
     * @param id
     * @return
     */
    Books getbookById(int id);

    /**
     * 查询所有书籍
     * @return
     */
    List<Books> getAll();

    /**
     * 修改书籍信息
     * @param books
     */
    void updateBook(Books books);
}
