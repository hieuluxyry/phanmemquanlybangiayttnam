package com.Utilities;

import com.Models.*;

import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

    private static final SessionFactory FACTORY;

    static {
        Configuration conf = new Configuration();

        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.SQLServerDialect");
        properties.put(Environment.DRIVER, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        properties.put(Environment.URL, "jdbc:sqlserver://localhost:1433;databaseName=PhanMemQuanLyBanGiayTTNam");
        properties.put(Environment.USER, "sa");
        properties.put(Environment.PASS, "123");
        properties.put(Environment.SHOW_SQL, "true");
//        gen DB tự động
//        properties.put(Environment.HBM2DDL_AUTO, "create");

        conf.setProperties(properties);
        conf.addAnnotatedClass(Bill.class);
        conf.addAnnotatedClass(BillDetails.class);
        conf.addAnnotatedClass(Color.class);
        conf.addAnnotatedClass(Customer.class);
        conf.addAnnotatedClass(ProductDetails.class);
        conf.addAnnotatedClass(ProductType.class);
        conf.addAnnotatedClass(Promotion.class);
        conf.addAnnotatedClass(PromotionDetails.class);
        conf.addAnnotatedClass(Size.class);
        conf.addAnnotatedClass(Staff.class);
        conf.addAnnotatedClass(Substance.class);

        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(conf.getProperties()).build();
        FACTORY = conf.buildSessionFactory(registry);

    }

    public static SessionFactory getFACTORY() {
        return FACTORY;
    }

    public static void main(String[] args) {
        getFACTORY();
    }

}
