package com.naronco.minigames;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class TcpManager
{
    private ServerSocket server;
    private Vector<Func<Socket>> connectionEvents = new Vector<Func<Socket>>();
    private Thread thread;
    
    public TcpManager(int port)
    {
        try
        {
            server = new ServerSocket(port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void start()
    {
        thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(true)
                {
                    try
                    {
                        emitConnection(server.accept());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
    
    public void stop()
    {
        thread.interrupt();
    }
    
    public void onConnection(Func<Socket> func)
    {
        connectionEvents.add(func);
    }
    
    protected void emitConnection(Socket socket)
    {
        for(Func<Socket> func : connectionEvents)
            func.run(socket);
    }
}
