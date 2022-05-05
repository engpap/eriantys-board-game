package it.polimi.ingsw.triton.launcher.utils.message.servermessage;

import it.polimi.ingsw.triton.launcher.server.ServeOneClient;
import it.polimi.ingsw.triton.launcher.utils.message.Message;

public class BroadcastMessageVisitor {

    private ServeOneClient serveOneClient;
    private String username;

   public BroadcastMessageVisitor(ServeOneClient serveOneClient, String username){
       this.serveOneClient = serveOneClient;
       this.username = username;
   }

    public void visit(Message message){
        serveOneClient.sendMessage(message);
    }

    /*public void visit(BroadcastServerMessage broadcastServerMessage){
       serveOneClient.sendMessage(broadcastServerMessage);
    }

    public void visit(WinMessage winMessage){
       if (winMessage.getReceiverUsername().equals(username))
            serveOneClient.sendMessage(winMessage);
       else serveOneClient.sendMessage(new LoseMessage(winMessage.getReceiverUsername()));
    }*/


}
