package info.scholarsportal.dao;

public interface MillDbDAO {

	public Integer activeFiles();
	public Integer deletedFiles();
	public String lastModified();
}
