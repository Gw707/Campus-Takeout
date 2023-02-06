package com.gw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gw.pojo.AddressBook;

public interface AddressBookService extends IService<AddressBook> {
    public void updateDefault(Long addressBookId, Long userId);
}
