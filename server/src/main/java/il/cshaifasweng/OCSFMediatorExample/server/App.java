package il.cshaifasweng.OCSFMediatorExample.server;

import org.hibernate.SessionFactory;

import java.io.IOException;

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
        SessionFactory sessionFactory = SimpleServer.getSessionFactory();
        DemoDataInitializer.initialize(sessionFactory);
        server.listen();
        System.out.println("Server is now listening on port 3000");
    }





}
