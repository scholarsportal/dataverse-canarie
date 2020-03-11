package info.scholarsportal.config;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("info.scholarsportal")
@PropertySource({ "classpath:persistence-mysql.properties" })
public class ApplicationContextConfig implements WebMvcConfigurer  {
    
    @Autowired
    private Environment env;
    
    @Bean
    public DataSource dbDataSource() {
        ComboPooledDataSource dbDataSource = new ComboPooledDataSource();
        
        try {
            dbDataSource.setDriverClass(env.getProperty("jdbc.driver"));
        } catch (PropertyVetoException exc) {
            throw new RuntimeException(exc);
        }
        
        dbDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
        dbDataSource.setUser(env.getProperty("jdbc.user"));
        dbDataSource.setPassword(env.getProperty("jdbc.password"));
        
        Integer initSize = Integer.parseInt(env.getProperty("connection.pool.initialPoolSize"));
        Integer minSize = Integer.parseInt(env.getProperty("connection.pool.minPoolSize"));
        Integer maxSize = Integer.parseInt(env.getProperty("connection.pool.maxPoolSize"));
        Integer maxIdleTime = Integer.parseInt(env.getProperty("connection.pool.maxIdleTime"));
        
        dbDataSource.setInitialPoolSize(initSize);
        dbDataSource.setMinPoolSize(minSize);
        dbDataSource.setMaxPoolSize(maxSize);     
        dbDataSource.setMaxIdleTime(maxIdleTime);
        
        return dbDataSource;
        
    }
    
    @Bean
    @Autowired
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        final DataSourceTransactionManager txManager = new DataSourceTransactionManager();
        txManager.setDataSource(dataSource);

        return txManager;
    }
    
    @Bean
    @Autowired
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/view/");
        viewResolver.setSuffix(".jsp");
        
        return viewResolver;
    }
    
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
    }
}
