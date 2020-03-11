package info.scholarsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import info.scholarsportal.dao.MillDbDAO;

@Service
public class MillDbServiceImpl implements MillDbService {

    @Autowired
    private MillDbDAO millDb;
    
    @Transactional
    public Integer activeFiles() {
        return millDb.activeFiles();
    }

    public Integer deletedFiles() {
        return millDb.deletedFiles();
    }

    public String lastModified() {
        return millDb.lastModified();
    }

}
