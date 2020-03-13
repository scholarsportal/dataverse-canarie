package info.scholarsportal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MillDbDAOImpl implements MillDbDAO {
	
    @Autowired
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Integer getNumberOfDataverses() {
		String sql = "select count(id) as count_id from dvobject where dtype = 'Dataverse'";
		
		Connection conn = null;
		Integer numFiles = 0;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				 numFiles = rs.getInt("count_id");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally { 
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return numFiles;
	}

	public Integer getNumberOfDatasets() {
	  String sql = "select count(id) as count_id from dvobject where dtype = 'Dataset'";
        
        Connection conn = null;
        Integer numFiles = 0;
        
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                 numFiles = rs.getInt("count_id");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { 
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return numFiles;
	}

	public Integer getNumberOfDownloads() {
		  String sql = "select count(*) as count_id from filedownload where downloadtype = 'Download'";
	        
	        Connection conn = null;
	        Integer numFiles = 0;
	        
	        try {
	            conn = dataSource.getConnection();
	            PreparedStatement ps = conn.prepareStatement(sql);
	            ResultSet rs = ps.executeQuery();
	            if (rs.next()) {
	                 numFiles = rs.getInt("count_id");
	            }
	            rs.close();
	            ps.close();
	        } catch (SQLException e) {
	            throw new RuntimeException(e);
	        } finally { 
	            if (conn != null) {
	                try {
	                    conn.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        return numFiles;
	}

}
