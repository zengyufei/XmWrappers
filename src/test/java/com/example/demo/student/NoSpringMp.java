package com.example.demo.student;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @formatter:off
 *
 * pom.xml
 *
 * <properties>
 *         <maven.compiler.source>8</maven.compiler.source>
 *         <maven.compiler.target>8</maven.compiler.target>
 *         <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
 *         <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
 *         <skipTests>true</skipTests>
 *         <mybatis-plus.version>3.4.3</mybatis-plus.version>
 *     </properties>
 *     <dependencies>
 *         <dependency>
 *             <groupId>ch.qos.logback</groupId>
 *             <artifactId>logback-classic</artifactId>
 *             <version>1.2.3</version>
 *         </dependency>
 *         <dependency>
 *             <groupId>org.slf4j</groupId>
 *             <artifactId>slf4j-api</artifactId>
 *             <version>1.7.31</version>
 *         </dependency>
 *         <dependency>
 *             <groupId>com.baomidou</groupId>
 *             <artifactId>mybatis-plus</artifactId>
 *             <version>${mybatis-plus.version}</version>
 *         </dependency>
 *         <dependency>
 *             <groupId>com.baomidou</groupId>
 *             <artifactId>mybatis-plus-extension</artifactId>
 *             <version>${mybatis-plus.version}</version>
 *         </dependency>
 *         <dependency>
 *             <groupId>mysql</groupId>
 *             <artifactId>mysql-connector-java</artifactId>
 *             <version>8.0.23</version>
 *         </dependency>
 *         <dependency>
 *             <groupId>com.zaxxer</groupId>
 *             <artifactId>HikariCP</artifactId>
 *             <version>4.0.3</version>
 *         </dependency>
 *         <dependency>
 *             <groupId>org.projectlombok</groupId>
 *             <artifactId>lombok</artifactId>
 *             <version>1.18.22</version>
 *         </dependency>
 *     </dependencies>
 * @formatter:on
 *
 *
 * @formatter:off
 *
 * run Main method
 *
 *
 * import com.baomidou.mybatisplus.core.toolkit.Wrappers;
 * import com.youlintech.longbao_human_resources.webs.employee_info.mapper.EmployeeInfoMapper;
 * import org.jetbrains.annotations.NotNull;
 * import org.springframework.core.io.Resource;
 * import org.springframework.core.io.support.ResourcePatternResolver;
 *
 * import java.io.IOException;
 *
 * public class TestQueryEmp extends NoSpringMp3 {
 *
 * @NotNull
 * @Override
 * public Resource[] getResources(ResourcePatternResolver resourceResolver) throws IOException {
 * //        return resourceResolver.getResources("classpath*:mapper*\/employee_info/*.xml");
 *         return new Resource[] {
 *                 resourceResolver.getResource("classpath:mapper/employee_info/EmployeeInfoMapper.xml"),
 *                 resourceResolver.getResource("classpath:mapper/employee_info/EmployeeInfoMapper-ext.xml"),
 *         };
 *     }
 *
 *     public static void main(String[] args) throws Exception {
 *         final TestQueryEmp testQueryEmp = new TestQueryEmp();
 *
 *         testQueryEmp.run(EmployeeInfoMapper.class, mapper -> {
 *             final Integer count = mapper.selectCount(Wrappers.emptyWrapper());
 *             System.out.println("结果: " + count);
 *         });
 *
 *         final Integer run = testQueryEmp.run(EmployeeInfoMapper.class, mapper -> {
 *             final Integer count = mapper.selectCount(Wrappers.emptyWrapper());
 *             System.out.println("结果: " + count);
 *             return count;
 *         });
 *         System.out.println(run);
 *
 *         testQueryEmp.run(sqlSession -> {
 *             final EmployeeInfoMapper mapper = sqlSession.getMapper(EmployeeInfoMapper.class);
 *             final Integer count = mapper.selectCount(Wrappers.emptyWrapper());
 *             System.out.println("结果: " + count);
 *         });
 *
 *     }
 * }
 * @formatter:on
 */
public abstract class NoSpringMp {

    ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    private Dict defaultYml;
    private Dict activeYml;

    public NoSpringMp() {
        defaultYml = YamlUtil.loadByPath("application.yml");
        final String active = defaultYml.getByPath("spring.profiles.active");
        if (StrUtil.isNotBlank(active)) {
            activeYml = YamlUtil.loadByPath(StrUtil.format("application-{}.yml", active));
        }
    }

    private String getYmlValue(String expression) {
        String value = null;
        if (activeYml != null) {
            value = activeYml.getByPath(expression);
        }
        if (StrUtil.isNotBlank(value)) {
            return value;
        }
        return defaultYml.getByPath(expression);
    }

    private <S> S getYmlBeanValue(String expression) {
        S value = null;
        if (activeYml != null) {
            value = activeYml.getBean(expression);
        }
        if (value != null) {
            return value;
        }
        return defaultYml.getBean(expression);
    }

    private Integer getYmlIntValue(String expression) {
        Integer value = null;
        if (activeYml != null) {
            value = activeYml.getByPath(expression);
        }
        if (value == null) {
            return value;
        }
        return defaultYml.getByPath(expression);
    }

    private Boolean getYmlBoolValue(String expression) {
        Boolean value = null;
        if (activeYml != null) {
            value = activeYml.getByPath(expression);
        }
        if (value == null) {
            return value;
        }
        return defaultYml.getByPath(expression);
    }


    public SqlSessionFactory getSqlSessionFactory() throws Exception {
        String url = getYmlValue("spring.datasource.url");
        String username = getYmlValue("spring.datasource.username");
        String password = getYmlValue("spring.datasource.password");
        String packageName = getYmlValue("mybatis-plus.typeAliasesPackage");
        String typeHandlersPackage = getYmlValue("mybatis-plus.typeEnumsPackage");
        String driverClassName = getYmlValue("spring.datasource.driver-class-name");
        // db-config
        String idType = getYmlValue("mybatis-plus.global-config.db-config.id-type");
        Integer logicDeleteValue = getYmlIntValue("mybatis-plus.global-config.db-config.logic-delete-value");
        Integer logicNotDeleteValue = getYmlIntValue("mybatis-plus.global-config.db-config.logic-not-delete-value");
        Boolean columnUnderline = getYmlBoolValue("mybatis-plus.global-config.db-config.column-underline");
        // configuration
        Boolean mapUnderscoreToCamelCase = getYmlBoolValue("mybatis-plus.configuration.map-underscore-to-camel-case");
        Boolean cacheEnabled = getYmlBoolValue("mybatis-plus.configuration.cache-enabled");
        Class<? extends TypeHandler> defaultEnumTypeHandler = getYmlBeanValue("mybatis-plus.configuration.default-enum-type-handler");
        final Interceptor interceptor = initInterceptor();


        MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
        mybatisPlus.setDataSource(initDataSource(driverClassName, url, username, password));
        mybatisPlus.setVfs(SpringBootVFS.class);
        mybatisPlus.setPlugins(interceptor);
        if (StrUtil.isNotBlank(typeHandlersPackage)) {
            mybatisPlus.setTypeHandlersPackage(typeHandlersPackage);
        }
        if (StrUtil.isNotBlank(packageName)) {
            mybatisPlus.setTypeAliasesPackage(packageName);
        }
        final Resource[] resources = getResources(resourceResolver);
        mybatisPlus.setMapperLocations(resources);

        //这是mybatis-plus的配置对象，对mybatis的Configuration进行增强
        MybatisConfiguration configuration = new MybatisConfiguration();
        mybatisPlus.setConfiguration(configuration);
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        if (mapUnderscoreToCamelCase != null) {
            //开启驼峰大小写转换
            configuration.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);
        }
        if (cacheEnabled != null) {
            configuration.setCacheEnabled(cacheEnabled);
        }
        if (defaultEnumTypeHandler != null) {
            configuration.setDefaultEnumTypeHandler(defaultEnumTypeHandler);
        }
        //配置添加数据自动返回数据主键
        configuration.setUseGeneratedKeys(true);
        //配置日志实现
        configuration.setLogImpl(Slf4jImpl.class);
        final GlobalConfig globalConfig = new GlobalConfig();
        GlobalConfig.DbConfig dbConfig = globalConfig.getDbConfig();
        if (dbConfig == null) {
            dbConfig = new GlobalConfig.DbConfig();
            globalConfig.setDbConfig(dbConfig);
        }
        if (StrUtil.isNotBlank(idType)) {
            dbConfig.setIdType(IdType.valueOf(idType.toUpperCase()));
        }
        if (logicDeleteValue != null) {
            dbConfig.setLogicDeleteValue(String.valueOf(logicDeleteValue));
        }
        if (logicNotDeleteValue != null) {
            dbConfig.setLogicNotDeleteValue(String.valueOf(logicNotDeleteValue));
        }
        if (columnUnderline != null) {
            dbConfig.setTableUnderline(columnUnderline);
        }
        mybatisPlus.setGlobalConfig(globalConfig);
        return mybatisPlus.getObject();
    }

    public Resource[] getResources(ResourcePatternResolver resourceResolver) throws IOException {
        String mapperLocation = getMapperLocation();
        final Resource[] resources = resourceResolver.getResources(mapperLocation);
        return resources;
    }

    public String getMapperLocation() {
        return getYmlValue("mybatis-plus.mapper-locations");
    }


    /**
     * 初始化数据源
     *
     * @return
     */
    private DataSource initDataSource(String driverClassName, String url, String username, String password) {
        if (StrUtil.isBlank(driverClassName)) {
            driverClassName = "com.mysql.cj.jdbc.Driver";
        }
        if (StrUtil.isBlank(username)) {
            username = "root";
        }
        if (StrUtil.isBlank(password)) {
            password = "root";
        }
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setIdleTimeout(60000);
        dataSource.setAutoCommit(true);
        dataSource.setMaximumPoolSize(5);
        dataSource.setMinimumIdle(1);
        dataSource.setMaxLifetime(60000 * 10);
        dataSource.setConnectionTestQuery("SELECT 1");
        try (Connection connection = dataSource.getConnection()) {
            {
                final ClassPathResource classPathResource = new ClassPathResource("db/schema.sql");
                final String createTableSql = IoUtil.readUtf8(classPathResource.getInputStream());
                final PreparedStatement preparedStatement = connection.prepareStatement(createTableSql);
                preparedStatement.execute();
            }
            {
                final ClassPathResource classPathResource = new ClassPathResource("db/data.sql");
                final String createTableSql = IoUtil.readUtf8(classPathResource.getInputStream());
                final PreparedStatement preparedStatement = connection.prepareStatement(createTableSql);
                preparedStatement.execute();
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        return dataSource;
    }

    /**
     * 初始化拦截器
     *
     * @return
     */
    private Interceptor initInterceptor() {
        //创建mybatis-plus插件对象
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //构建分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.POSTGRE_SQL);
        paginationInnerInterceptor.setOverflow(true);
//        paginationInnerInterceptor.setMaxLimit(500L);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }


    protected <S, R> R run(Class<S> mapperClass, Function<S, R> consumer) throws Exception {
        final SqlSessionFactory sqlSessionFactory = this.getSqlSessionFactory();
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final S mapper = session.getMapper(mapperClass);
            return consumer.apply(mapper);
        }
    }


    protected <S> void run(Class<S> mapperClass, Consumer<S> consumer) throws Exception {
        final SqlSessionFactory sqlSessionFactory = this.getSqlSessionFactory();
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            final S mapper = session.getMapper(mapperClass);
            consumer.accept(mapper);
        }
    }

    protected void run(Consumer<SqlSession> consumer) throws Exception {
        final SqlSessionFactory sqlSessionFactory = this.getSqlSessionFactory();
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            consumer.accept(session);
        }
    }


}
