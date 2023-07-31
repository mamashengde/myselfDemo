package xiaopeng.zdys.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import xiaopeng.zdys.pojo.Books;

import java.util.List;

@Mapper
public interface BookMapper {

    /**
     * 添加书籍
     * @param books
     */
    void addBooks(Books books);

    /**
     * 根据主键删除id
     * @param id
     */
    void deleteById(int id);

    /**
     * 修改书籍信息
     * @param books
     */
    void updateBook(Books books);

    /**
     * 通过id进行查找
     * @param id
     * @return
     */
    Books getById(int id);

    /**
     * 查询所有书籍
     * @return
     */
    List<Books> getAllBook();
}
