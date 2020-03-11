package info.scholarsportal.service;

public interface MillDbService {
    
    public Integer activeFiles();
    public Integer deletedFiles();
    public String lastModified();

}
