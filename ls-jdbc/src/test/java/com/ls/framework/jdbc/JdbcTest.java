package com.ls.framework.jdbc;

import com.ls.framework.jdbc.base.SimpleDataSource;
import com.ls.framework.jdbc.binding.MapperData;
import com.ls.framework.jdbc.config.Constants;
import com.ls.framework.jdbc.config.LSDbConfiguration;
import com.ls.framework.jdbc.exception.LSJdbcException;
import com.ls.framework.jdbc.executor.DefaultJdbcExecutor;
import com.ls.framework.jdbc.executor.JdbcExecutor;
import com.ls.framework.jdbc.session.ConnectionContext;
import com.ls.framework.jdbc.session.DefaultSqlSession;
import com.ls.framework.jdbc.session.SqlSession;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.*;


public class JdbcTest {

    private DataSource dataSource;
    private SqlSession sqlSession;
    private JdbcExecutor executor;
    @Before
    public void init() {
        String uri = "jdbc:mysql://127.0.0.1:3306/stu_info?characterEncoding=UTF-8";
        String user = "root";
        String pwd = "";
        String driver = "com.mysql.cj.jdbc.Driver";
        dataSource = new SimpleDataSource(uri, user, pwd, driver);
        LSDbConfiguration configuration = new LSDbConfiguration();
        configuration.getDataSources().put(Constants.DEFAULT_DATASOURCE_NAME, dataSource);

        executor = new DefaultJdbcExecutor(configuration.isMapUnderscoreToCamelCase());

        sqlSession = new DefaultSqlSession(configuration, executor);
    }

    @Test
    public void testPattern() {
        String sql = "insert into user values(${das}, ${das}, ${das})";
        MapperData mapperData = new MapperData(null, sql, true, Constants.DEFAULT_DATASOURCE_NAME);

        System.out.println(mapperData.buildSql(new Object[]{"dadsa", 1, new int[] {1,2,3}}));
    }

    @Test
    public void testApp() {
        TestMapper2 testMapper2 = sqlSession.getMapper(TestMapper2.class);
        System.out.println(testMapper2.selectOne());
        System.out.println("==========");
        for (ResultBean resultBean : testMapper2.selectArray()) {
            System.out.println(resultBean);
        }
        System.out.println("==========");
        System.out.println(testMapper2.selectList());

        System.out.println("==========");
        System.out.println(testMapper2.selectCount());

    }

    @Test
    public void testSelectOneSpeed() throws SQLException {
        //测试JDBC模块查询单条数据10000次耗时
        String sql = "SELECT * FROM  tbl_result limit 1";

        TestMapper2 testMapper2 = sqlSession.getMapper(TestMapper2.class);

        Connection connection = dataSource.getConnection();
        ConnectionContext.beginTransaction();
        // 100次连接初始化预热
        for (int i = 0; i < 100; i++) {
            testMapper2.selectOne();
//            executor.executeQuery(connection, sql, null);
//            sqlSession.selectOne(ResultBean.class, sql, null);
        }

        long start = System.currentTimeMillis();
        System.out.println("start jdbc module: " + start);

        int count = 0;

        for (int i = 0; i < 10000; i++) {
            testMapper2.selectOne();
//            executor.executeQuery(connection, sql, null);
//            sqlSession.selectOne(ResultBean.class, sql, null);
            count++;
        }
        long end = System.currentTimeMillis();
        ConnectionContext.commitTransaction();
        System.out.println(String.format("select %d record, took %d ms", count, end - start));

        start = System.currentTimeMillis();
        System.out.println("start jdbc module: " + start);
        count = 0;

        for (int i = 0; i < 10000; i++) {
            jdbcSelectOne(connection, sql);
            count++;
        }
        if (connection != null) {
            connection.close();
        }
        end = System.currentTimeMillis();
        System.out.println(String.format("select %d record, took %d ms", count, end - start));

    }
//
    private void jdbcSelectOne(Connection connection, String sql) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            ResultBean resultBean = new ResultBean();
            while (resultSet.next()) {
                resultBean.id = resultSet.getLong("id");
                resultBean.day = resultSet.getString("day");
                resultBean.result = resultSet.getString("result");
            }
//            System.out.println(resultBean);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 测试有返回值的更新
     */
    @Test
    public void testUpdateWithReturn() {
        String day = "2017-5-23";
        TestMapper2 testMapper2 = sqlSession.getMapper(TestMapper2.class);
        ResultBean resultBean = testMapper2.selectOneByDay(day);
        System.out.println(resultBean);
        String val;
        if ("胜".equals(resultBean.result)) {
            val = "负";
        } else {
            val = "胜";
        }
        long result = testMapper2.update(val, day);
        System.out.println(result);
        resultBean = testMapper2.selectOneByDay(day);
        System.out.println(resultBean);
    }

    /**
     * 测试无返回值的更新
     */
    @Test
    public void testUpdateWithoutReturn() {
        String day = "2017-5-23";
        TestMapper2 testMapper2 = sqlSession.getMapper(TestMapper2.class);
        ResultBean resultBean = testMapper2.selectOneByDay(day);
        System.out.println(resultBean);
        String val;
        if ("胜".equals(resultBean.result)) {
            val = "负";
        } else {
            val = "胜";
        }
        testMapper2.updateWithoutReturn(val, day);
        resultBean = testMapper2.selectOneByDay(day);
        System.out.println(resultBean);
    }

    @Test
    public void testInsert() {
        TestMapper2 testMapper2 = sqlSession.getMapper(TestMapper2.class);
        long result = testMapper2.insert("胜","2017-5-23");
        System.out.println(result);

        System.out.println(testMapper2.selectList());
    }

    @Test
    public void testDelete() {
        TestMapper2 testMapper2 = sqlSession.getMapper(TestMapper2.class);
        long result = testMapper2.delete("2017-5-23");
        System.out.println(result);
        System.out.println(testMapper2.selectList());
    }

    @Test
    public void testTransaction() throws SQLException {
        TestMapper2 testMapper2 = sqlSession.getMapper(TestMapper2.class);
        ConnectionContext.beginTransaction();//开启事务
        try {
//            testMapper2.insert("胜","2017-5-23");//测试新增数据事务
//            testDelete();//测试删除事务
            testUpdateWithReturn();//测试更新事务
            throw new LSJdbcException("");//抛个运行时异常试试
//            ConnectionContext.commitTransaction(); //提交事务
        } catch (Exception e) {
            e.printStackTrace();
            ConnectionContext.rollbackTransaction();
        }

        System.out.println(testMapper2.selectList());
    }

}
