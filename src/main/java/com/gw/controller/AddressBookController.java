package com.gw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gw.common.Status;
import com.gw.pojo.AddressBook;
import com.gw.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Description TODO
 * @Author ygw
 * @Date 2022/9/30 13:23
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/{addressBookId}")
    public Status<AddressBook> getOne(@PathVariable Long addressBookId){
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getId, addressBookId);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        return Status.success(addressBook);
    }

    @PutMapping
    public Status<String> updateAddress(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);

        return Status.success("修改成功");
    }

    @GetMapping("/default")
    public Status<AddressBook> getDefault(HttpSession session){
        Long userId = (Long) session.getAttribute("user");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId);
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        return Status.success(addressBook);
    }

    @PutMapping("/default/{id}")
    public Status<String> updateDefault(@PathVariable("id") Long addressBookId,
                                        HttpSession session){

        Long userId = (Long) session.getAttribute("user");
        log.info("addressBookId:{}, userId{}", addressBookId, userId);

        addressBookService.updateDefault(addressBookId, userId);

        return Status.success("设置成功");
    }

    @GetMapping("/list")
    public Status<List<AddressBook>> getList(HttpSession session){
        Long userId = (Long) session.getAttribute("user");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId);
        List<AddressBook> addressBooks = addressBookService.list(queryWrapper);
        return Status.success(addressBooks);
    }

    @PostMapping
    public Status<String> save(@RequestBody AddressBook addressBook,
                               HttpSession session){
        Long userId = (Long) session.getAttribute("user");

        addressBook.setUserId(userId);

        addressBookService.save(addressBook);

        return Status.success("新增地址成功");
    }




}
