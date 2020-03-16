package cn.ljtnono.re.es.service.impl;

import cn.ljtnono.re.es.repository.ReBlogEsRepository;
import cn.ljtnono.re.es.service.ReBlogEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReBlogEsServiceImpl implements ReBlogEsService {

    @Autowired
    private ReBlogEsRepository reBlogEsRepository;



}
