package il.cshaifasweng.OCSFMediatorExample.server;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class App
{

    private static SimpleServer server;
    public static void main( String[] args ) throws IOException
    {
        server = new SimpleServer(3000);
        server.listen();
        System.out.println("Server is now listening on port 3000");
    }





}

