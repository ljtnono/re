package cn.rootelement.es.service.impl;

import cn.rootelement.es.repository.ReBlogEsRepository;
import cn.rootelement.es.service.ReBlogEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReBlogEsServiceImpl implements ReBlogEsService {

    @Autowired
    private ReBlogEsRepository reBlogEsRepository;



}
