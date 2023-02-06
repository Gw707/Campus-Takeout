package com.gw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gw.mapper.AddressBookMapper;
import com.gw.pojo.AddressBook;
import com.gw.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description TODO
 * @Author ygw
 * @Date 2022/9/29 11:13
 * @Version 1.0
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    @Autowired
    private AddressBookService addressBookService;

    @Override
    @Transactional
    public void updateDefault(Long addressBookId, Long userId) {

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId);
        List<AddressBook> addressBooks = addressBookService.list(queryWrapper);
        for (AddressBook addressBook : addressBooks) {
            addressBook.setIsDefault(Integer.valueOf(0));
        }
        addressBookService.updateBatchById(addressBooks);

        queryWrapper.eq(AddressBook::getId, addressBookId);
        AddressBook defaultAddress = addressBookService.getOne(queryWrapper);
        defaultAddress.setIsDefault(Integer.valueOf(1));
        addressBookService.updateById(defaultAddress);

    }
}
