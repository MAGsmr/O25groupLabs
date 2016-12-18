package netcracker.net.client;

import netcracker.json.Json;
import netcracker.net.ServerSettings;
import netcracker.tree.TreeView;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by ВладПК on 17.12.2016.
 */
public class ClientView extends TreeView implements IClientView {



    @Override
    public void input() throws  IOException{
        if(!ClientController.getInstance().connect(ServerSettings.host, ServerSettings.PORT)){
            System.out.println("Соединение не установлено, завершение работы...");
            return;
        }

        Scanner sc = new Scanner(System.in);
        String command;
        String[] components;
        System.out.println("Введите команду (справка help):");
        Integer tree = null;


        while(!(command = sc.nextLine()).equals("stop")){
            components = command.trim().split("\\s+");
            if(components.length>=1 && !components[0].equals("")){
                switch(components[0]){
                    case "help":{
                        ClientController.getInstance().send(components[0]);
                        String help = "";
                        String[] list = ClientController.getInstance().getResponse().split("#");
                        for(int i = 0; i < list.length; i++)
                            help+=list[i]+"\n";
                        System.out.println(help);
                        break;
                    }
                    case "create":{
                        ClientController.getInstance().send(command);
                        String response = ClientController.getInstance().getResponse();
                        System.out.println(getMessage(response));
                        break;
                    }
                    case "reg":{
                        ClientController.getInstance().send(command);
                        String response = ClientController.getInstance().getResponse();
                        System.out.println(getMessage(response));
                        break;
                    }
                    case "auth":{
                        ClientController.getInstance().send(command);
                        String response = ClientController.getInstance().getResponse();
                        System.out.println(getMessage(response));
                        break;
                    }
                    case "add":{
                        ClientController.getInstance().send(command);
                        String response = ClientController.getInstance().getResponse();
                        String message = getMessage(response);
                        if(ServerSettings.compareCode(response, ServerSettings.SUCCESSFUL))
                            showTree(Json.parseJson(message), 0, 2);
                        else
                            System.out.println(message);
                        break;
                    }
                    case "set":{
                        ClientController.getInstance().send(command);
                        String response = ClientController.getInstance().getResponse();
                        String message = getMessage(response);
                        if(ServerSettings.compareCode(response, ServerSettings.SUCCESSFUL))
                            showTree(Json.parseJson(message), 0, 2);
                        else
                            System.out.println(message);
                        break;
                    }
                    case "split":{
                        ClientController.getInstance().send(command);
                        String response = ClientController.getInstance().getResponse();
                        String message = getMessage(response);
                        if(ServerSettings.compareCode(response, ServerSettings.SUCCESSFUL))
                            showTree(Json.parseJson(message), 0, 2);
                        else
                            System.out.println(message);
                        break;
                    }
                    case "remove":{
                        ClientController.getInstance().send(command);
                        String response = ClientController.getInstance().getResponse();
                        String message = getMessage(response);
                        if(ServerSettings.compareCode(response, ServerSettings.SUCCESSFUL))
                            showTree(Json.parseJson(message), 0, 2);
                        else
                            System.out.println(message);
                        break;
                    }
                    case "find":{
                        ClientController.getInstance().send(command);
                        String response = ClientController.getInstance().getResponse();
                        String message = getMessage(response);
                        if(ServerSettings.compareCode(response, ServerSettings.SUCCESSFUL))
                            printNode(Json.parseJson(message), 2);
                        else
                            System.out.println(message);
                        break;
                    }

                    case "open":{
                        ClientController.getInstance().send(command);
                        String response = ClientController.getInstance().getResponse();
                        String message = getMessage(response);
                        if(ServerSettings.compareCode(response, ServerSettings.SUCCESSFUL)){
                            showTree(Json.parseJson(message), 0, 2);
                        }
                        else
                            System.out.println(message);
                        break;
                    }
                    case "show":{
                        ClientController.getInstance().send(command);
                        String response = ClientController.getInstance().getResponse();
                        String message = getMessage(response);
                        if(ServerSettings.compareCode(response, ServerSettings.SUCCESSFUL))
                            showNode(Json.parseJson(message.substring(message.indexOf(" ")+1)),
                                    Integer.parseInt(message.substring(0, message.indexOf(" "))), 0,2);
                        else
                            System.out.println(message);
                        break;
                    }
                    case "hide":{
                        ClientController.getInstance().send(command);
                        String response = ClientController.getInstance().getResponse();
                        String message = getMessage(response);
                        if(ServerSettings.compareCode(response, ServerSettings.SUCCESSFUL))
                            hideNode(Json.parseJson(message.substring(message.indexOf(" ")+1)),
                                    Integer.parseInt(message.substring(0, message.indexOf(" "))), 0,2);
                        else
                            System.out.println(message);
                        break;
                    }
                    case "removeTree":{
                        ClientController.getInstance().send(command);
                        String response = ClientController.getInstance().getResponse();
                        String message = getMessage(response);
                        System.out.println(message);
                        break;
                    }
                    case "CloneTree":{
                        ClientController.getInstance().send(command);
                        String response = ClientController.getInstance().getResponse();
                        String message = getMessage(response);
                        System.out.println(message);
                        break;
                    }
                    case "load":{
                        ClientController.getInstance().send(command);
                        String response = ClientController.getInstance().getResponse();
                        String message = getMessage(response);
                        if(ServerSettings.compareCode(response, ServerSettings.SUCCESSFUL))
                            showTree(Json.parseJson(message), 0, 2);
                        else
                            System.out.println(message);
                        break;
                    }
                    case "save":{
                        ClientController.getInstance().send(command);
                        String response = ClientController.getInstance().getResponse();
                        String message = getMessage(response);
                        System.out.println(message);
                        break;
                    }
                    case "sortTree":{
                        ClientController.getInstance().send(command);
                        String response = ClientController.getInstance().getResponse();
                        String message = getMessage(response);
                        if(ServerSettings.compareCode(response, ServerSettings.SUCCESSFUL))
                            showTree(Json.parseJson(message), 0, 2);
                        else
                            System.out.println(message);
                        break;
                    }


                    default:
                        System.out.println("Неизвестная команда "+components[0] +" (справка help).");
                        break;
                }
                System.out.println("\nВведите команду (stop - выход из программы):");
            }
        }
        ClientController.getInstance().disconnect();
    }

    private String getMessage(String s){
        return s.length()>2 ? s.substring(3) : null;
    }
}
