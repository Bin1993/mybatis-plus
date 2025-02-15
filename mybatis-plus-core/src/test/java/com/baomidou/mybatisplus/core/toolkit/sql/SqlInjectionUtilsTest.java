package com.baomidou.mybatisplus.core.toolkit.sql;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * SQL 注入验证工具类测试
 *
 * @author hubin
 * @since 2021-08-15
 */
public class SqlInjectionUtilsTest {

    @Test
    public void sqlTest() {
        assertSql(false, "insert abc");
        assertSql(true, "insert user (id,name) value (1, 'qm')");
        assertSql(true, "SELECT * FROM user");
        assertSql(true, "delete from user");
        assertSql(true, "drop TABLE user");
        assertSql(true, ";TRUNCATE from user");
        assertSql(false, "update");
        assertSql(false, "trigger");
        assertSql(true, "and name like '%s123%s'");
        assertSql(false, "convert(name using GBK)");

        // 无空格
        assertSql(false, "insert_into");
        assertSql(true, "SELECT aa FROM user");
        // 无空格
        assertSql(true, "SELECT*FROM user");
        // 左空格
        assertSql(true, "SELECT *FROM user");
        // 右空格
        assertSql(true, "SELECT* FROM user");
        // 左tab
        assertSql(true, "SELECT                 *FROM user");
        // 右tab
        assertSql(true, "SELECT*        FROM user");
        assertSql(false, "SELECT*FROMuser");
        // 该字符串里包含 setT or
        assertSql(false, "databaseType desc,orderNum desc)");

        assertSql(false, "insert");
        assertSql(true, "insert user (id,age) values (1, 18)");
        assertSql(false, "union");
        assertSql(false, "or");
        assertSql(false, "delete");
        assertSql(false, "drop");
        assertSql(true, "and age not in (1,2,3)");
        assertSql(true, "and age <> 1");
        assertSql(false,"ORDER BY field(status,'SUCCESS','FAILED','CLOSED')");
        assertSql(true,"ORDER BY id,'SUCCESS',''-- FAILED','CLOSED'");
        assertSql(true, "or 1 = 1");
    }

    private void assertSql(boolean injection, String sql) {
        Assertions.assertEquals(injection, SqlInjectionUtils.check(sql));
    }
}
