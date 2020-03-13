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
    public Integer getNumberOfDataverses() {
        return millDb.getNumberOfDataverses();
    }

    public Integer getNumberOfDatasets() {
        return millDb.getNumberOfDatasets();
    }

    public Integer getNumberOfDownloads() {
        return millDb.getNumberOfDownloads();
    }

}
