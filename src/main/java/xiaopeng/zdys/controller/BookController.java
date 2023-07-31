package xiaopeng.zdys.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xiaopeng.zdys.pojo.Books;
import xiaopeng.zdys.result.Result;
import xiaopeng.zdys.service.BookService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("books")
@Api(tags = "书本管理")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * 增加书籍
     * @param books
     * @return
     */
    @PostMapping
    @ApiOperation("新增书籍")
    public Result save(@RequestBody Books books){
        log.info("新增书籍: {}",books);
        bookService.addBooks(books);
        return Result.success();
    }

    /**
     * 根据id来删除书籍
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除书籍")
    public Result delete(@RequestParam int id){
        log.info("删除书籍id: {}",id);
        bookService.deleteById(id);
        return Result.success();
    }

    /**
     * 根据id来查询书籍
     * @param id
     * @return
     */
    @GetMapping("/id")
    @ApiOperation("根据id来查询书籍")
    public Result<Books> getById(@PathVariable int id) {
        log.info("根据id来查询书籍: ",id);
        Books books = bookService.getbookById(id);
        return Result.success(books);
    }

    /**
     * 查询书籍信息
     * @return
     */
    @GetMapping
    @ApiOperation("查询所有书籍")
    public Result<List<Books>> getAll(){
        log.info("查询所有书籍");
        List<Books> booksList = bookService.getAll();
        return Result.success(booksList);
    }

    /**
     * 修改书籍信息
     * @param books
     * @return
     */
    @PutMapping
    @ApiOperation("修改书籍信息")
    public Result update(@RequestBody Books books){
        log.info("修改书籍信息");
        bookService.updateBook(books);
        return Result.success();
    }
}
