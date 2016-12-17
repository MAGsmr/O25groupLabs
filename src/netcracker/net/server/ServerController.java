package netcracker.net.server;

import netcracker.json.Json;
import netcracker.net.ISettings;
import netcracker.tree.ITreeNode;
import netcracker.tree.TreeController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by ВладПК on 17.12.2016.
 */
public class ServerController implements IServerController {
    private Socket socket;

    public ServerController(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Соединение с новым пользователем выполнено успешно!");
        String help ="Создать дерево с корневым узлом: create <key> <value>#";
        help+="Открыть дерево: open <key>#";
        help+="Открыть узел текущего дерева: show <key>#";
        help+="Закрыть узел текущего дерева: hide <key>#";
        help+="Добавить узел в открытое дерево: add <key> <value>#";
        help+="Изменить значение узла по ключу в открытом дереве: set <key> <value>#";
        help+="Раcщепление(удалить 1 узел в открытом дереве): split <key>#";//расщепление
        help+="Удалить узел с потомками в открытом дереве: remove <key>#";
        help+="Поиск узла в открытом дереве: find <key>#";
        help+="Удалить открытое дерево: removeTree#";
        help+="Клонировать открытое дерево: CloneTree#";
        help+="Сохранить текущее открытое дерево: save <file_name>#";
        help+="Загрущить дерево: load <file_name>#";
        help+="Отсортировать текущее дерево: sortTree <value_for_sorting> //key или data#";

        Integer tree = null;
        String command = "";
        String[] components;
        BufferedReader in = null;
        PrintStream out = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(in == null || out == null){
            System.out.println("Ошибка получения потоков чтения/записи... Сессия остановлена.");
            return;
        }

        while(isConnected() && !command.equals("stop")){
            components = command.trim().split("\\s+");

            if(components.length>=1 && !components[0].equals("")){
                switch(components[0]){
                    case "help":{

                        out.println(help);
                        break;
                    }
                    case "create":{
                        int key = 0;
                        if(components.length == 3) {
                            try {
                                key = Integer.parseInt(components[1]);
                                Object value = components[2];
                                if (TreeController.getInstance().create(key, value) != null) {
                                    out.println(ISettings.SUCCESSFUL + "Дерево с корнем " + key + "было успешно создано!");
                                    tree =key;
                                }
                                else
                                    out.println(ISettings.ERROR+"Дерево не было создано, проверьте правильность входных параметров (справка help).");
                            } catch (Exception e) {
                                out.println(ISettings.ERROR+"Функция create в качестве первого параметра должна принимать целое число (справка help).");
                            }
                        }
                        else
                            out.println(ISettings.ERROR+"Функция create должна принимать 2 параметра (справка help).");

                        break;
                    }
                    case "add":{
                        int key = 0;
                        if(components.length == 3) {
                            try {
                                key = Integer.parseInt(components[1]);
                                Object value = components[2];
                                if (tree != null) {
                                    if (TreeController.getInstance().addNode(TreeController.getInstance().getTree(tree), key, value) == null){
                                        out.println(ISettings.ERROR+"Узел с таким ключом существует! Не добавлен.");

                                    }
                                    else{
                                        out.println(ISettings.SUCCESSFUL+Json.toJson(TreeController.getInstance().getTree(tree)));
                                    }
                                } else
                                    out.println(ISettings.ERROR+"Не открыто дерево для выполнения команды (справка help).");
                            } catch (Exception e) {
                                out.println(ISettings.ERROR+"Функция add в качестве первого параметра должна принимать целое число (справка help).");
                            }
                        }
                        else
                            out.println(ISettings.ERROR+"Функция add должна принимать 2 параметра (справка help).");
                        break;
                    }
                    case "set":{
                        int key = 0;
                        if(components.length == 3) {
                            try {
                                key = Integer.parseInt(components[1]);
                                Object value = components[2];
                                if (tree != null) {
                                    if (TreeController.getInstance().setNode(TreeController.getInstance().getTree(tree), key, value) == null){
                                        out.println(ISettings.ERROR+"Узел с таким ключом не существует.");
                                        break;
                                    }
                                    else{
                                        out.println(ISettings.SUCCESSFUL+Json.toJson(TreeController.getInstance().getTree(tree)));
                                    }
                                } else
                                    out.println(ISettings.ERROR+"Не открыто дерево для выполнения команды (справка help).");
                            } catch (Exception e) {
                                out.println(ISettings.ERROR+"Функция set в качестве первого параметра должна принимать целое число (справка help).");
                            }
                        }
                        else
                            out.println(ISettings.ERROR+"Функция set должна принимать 2 параметра (справка help).");
                        break;
                    }
                    case "split":{
                        int key = 0;
                        if(components.length == 2) {
                            try {
                                key = Integer.parseInt(components[1]);

                                if (tree != null) {
                                    if (TreeController.getInstance().getTree(tree).getKey() != key) {
                                        TreeController.getInstance().splitNode(TreeController.getInstance().getTree(tree), key);
                                        out.println(ISettings.SUCCESSFUL+Json.toJson(TreeController.getInstance().getTree(tree)));
                                    } else
                                        out.println(ISettings.ERROR+"Воспользуйтесь удалением дерева целиком");
                                } else
                                    out.println(ISettings.ERROR+"Не открыто дерево для выполнения команды (справка help).");
                            } catch (Exception e) {
                                out.println(ISettings.ERROR+"Функция split должна принимать целочисленный параметр (справка help).");
                            }
                        }
                        else
                            out.println(ISettings.ERROR+"Функция remove должна принимать 1 параметра (справка help).");
                        break;
                    }
                    case "remove":{
                        int key = 0;
                        if(components.length == 2) {
                            try {
                                key = Integer.parseInt(components[1]);

                                if (tree != null) {
                                    if (TreeController.getInstance().getTree(tree).getKey() != key) {
                                        TreeController.getInstance().removeNode(TreeController.getInstance().getTree(tree), key);
                                        out.println(ISettings.SUCCESSFUL+Json.toJson(TreeController.getInstance().getTree(tree)));
                                    } else
                                        out.println(ISettings.ERROR+"Воспользуйтесь удалением дерева целиком");
                                } else
                                    out.println(ISettings.ERROR+"Не открыто дерево для выполнения команды (справка help).");
                            } catch (Exception e) {
                                out.println(ISettings.ERROR+"Функция remove должна принимать целочисленный параметр (справка help).");
                            }
                        }
                        else
                            out.println(ISettings.ERROR+"Функция remove должна принимать 1 параметра (справка help).");
                        break;
                    }
                    case "find":{
                        int key = 0;
                        if(components.length == 2) {
                            try {
                                key = Integer.parseInt(components[1]);
                                if (tree != null) {
                                    if (TreeController.getInstance().findNode(TreeController.getInstance().getTree(tree), key)!=null) {
                                        out.println(ISettings.SUCCESSFUL+Json.toJson(TreeController.getInstance().findNode(TreeController.getInstance().getTree(tree), key)));
                                    } else
                                        out.println(ISettings.ERROR+"Узел с таким ключом не найден.");
                                } else
                                    out.println(ISettings.ERROR+"Не открыто дерево для выполнения команды (справка help).");
                            } catch (Exception e) {
                                out.println(ISettings.ERROR+"Функция find должна принимать целочисленный параметр (справка help).");
                            }
                        }
                        else
                            out.println(ISettings.ERROR+"Функция find должна принимать 1 параметра (справка help).");
                        break;
                    }

                    case "open":{
                        if(components.length == 2){
                            try{
                                Integer buf = Integer.parseInt(components[1]);
                                if(buf!=null && TreeController.getInstance().getTree(buf)!=null){
                                    tree = buf;
                                    out.println(ISettings.SUCCESSFUL+Json.toJson(TreeController.getInstance().getTree(tree)));
                                }
                                else
                                    out.println(ISettings.ERROR+"Дерево с таким ключом не найдено (справка help).");
                            }
                            catch(Exception e){
                                out.println(ISettings.ERROR+"Функция open должна принимать целочисленный параметр (справка help).");
                            }
                        }
                        else
                            out.println(ISettings.ERROR+"Функция open должна принимать 1 параметр (справка help).");
                        break;
                    }
                    case "show":{
                        int key = 0;
                        if(components.length == 2) {
                            try {
                                key = Integer.parseInt(components[1]);
                                if (tree != null)
                                    if(TreeController.getInstance().findNode(TreeController.getInstance().getTree(tree), key)!=null)
                                        out.println(ISettings.SUCCESSFUL + ""+key+" "+Json.toJson(TreeController.getInstance().getTree(tree)));
                                    else
                                        out.println(ISettings.ERROR+"Невозможно раскрыть несуществующий узел (справка help):");
                                else
                                    out.println(ISettings.ERROR+"Не открыто дерево для выполнения команды (справка help):");
                            } catch (Exception e) {
                                out.println(ISettings.ERROR+"Функция show должна принимать целочисленный параметр (справка help).");
                            }
                        }
                        else
                            out.println(ISettings.ERROR+"Функция show должна принимать 1 параметр (справка help).");
                        break;
                    }
                    case "hide":{
                        int key = 0;
                        if(components.length == 2) {
                            try {
                                key = Integer.parseInt(components[1]);
                                if (tree != null)
                                    if(TreeController.getInstance().findNode(TreeController.getInstance().getTree(tree), key)!=null)
                                        out.println(ISettings.SUCCESSFUL + ""+key+" "+Json.toJson(TreeController.getInstance().getTree(tree)));
                                    else
                                        out.println(ISettings.ERROR+"Невозможно свернуть несуществующий узел (справка help):");
                                else
                                    out.println(ISettings.ERROR+"Не открыто дерево для выполнения команды (справка help):");
                            } catch (Exception e) {
                                out.println(ISettings.ERROR+"Функция hide должна принимать целочисленный параметр (справка help).");
                            }
                        }
                        else
                            out.println(ISettings.ERROR+"Функция hide должна принимать 1 параметр (справка help).");
                        break;
                    }
                    case "removeTree":{
                        if (tree != null) {
                            TreeController.getInstance().removeTree(tree);
                            tree= null;
                            out.println(ISettings.SUCCESSFUL+"Операция удаления дерева выполнена успешно.");
                        } else
                            out.println(ISettings.ERROR+"Не открыто дерево для выполнения команды (справка help).");
                        break;
                    }
                    case "CloneTree":{
                        int key = 0;
                        if (tree == null) {
                            out.println(ISettings.ERROR+"Не открыто дерево для выполнения команды (справка help).");
                            break;
                        }
                        if(components.length > 1)
                            out.println(ISettings.ERROR+"Функция CloneTree не должна принимать параметров (справка help). Выполнена без учета параметров.");
                        TreeController.getInstance().addTreeInPool( TreeController.getInstance().cloneTree(TreeController.getInstance().getTree(tree)));
                        out.println(ISettings.SUCCESSFUL+"Операция выполнена успешно.");
                        break;
                    }
                    case "load":{
                        if(components.length == 2) {
                            try {

                                Integer key = TreeController.getInstance().load(components[1]).getKey();
                                if(key!=null){
                                    tree = key;
                                    TreeController.getInstance().addTreeInPool(TreeController.getInstance().load(components[1]));
                                    out.println(ISettings.SUCCESSFUL+Json.toJson(TreeController.getInstance().getTree(key)));
                                }
                                else
                                    out.println(ISettings.ERROR+"Не удалось открыть дерево (справка help).");
                            } catch (IOException e) {
                                out.println(ISettings.ERROR+"Не удалось открыть/прочитать файл с таким именем (справка help).");
                            }
                        }
                        else
                            out.println(ISettings.ERROR+"Функция load должна принимать 1 параметр (справка help).");
                        break;
                    }
                    case "save":{
                        if(components.length == 2) {
                            try {
                                if(TreeController.getInstance().save(TreeController.getInstance().getTree(tree), components[1]))
                                    out.println(ISettings.SUCCESSFUL+"Дерево успешно сохранено (справка help).");
                                else
                                    out.println(ISettings.ERROR+"Не удалось сохранить дерево (справка help).");
                            } catch (IOException e) {
                                out.println(ISettings.ERROR+"Не удалось сохранить дерево (справка help).");
                            }
                        }
                        else
                            out.println(ISettings.ERROR+"Функция save должна принимать 1 параметр (справка help).");
                        break;
                    }
                    case "sortTree":{
                        String strComparator = "key";
                        if(components.length == 2) {
                            try {
                                strComparator = components[1];
                                if ( strComparator.equals("data") || strComparator.equals("key") ) {
                                    if (tree != null) {
                                        out.println(ISettings.SUCCESSFUL+Json.toJson(TreeController.getInstance().sortTree(TreeController.getInstance().getTree(tree), strComparator)));
                                    } else
                                        out.println(ISettings.ERROR+"Не открыто дерево для выполнения команды (справка help).");
                                } else
                                    out.println(ISettings.ERROR+"Неверный критерий сортировки");
                            } catch (Exception e) {
                                out.println(ISettings.ERROR+"Функция sortTree в качестве первого параметра должна принимать cтроку(key или data) (справка help).");
                            }
                        }
                        else
                            out.println(ISettings.ERROR+"Функция add должна принимать 2 параметра (справка help).");
                        break;
                    }


                    default:
                        out.println(ISettings.ERROR+"Неизвестная команда "+components[0] +" (справка help).");
                        break;

                }
                //out.flush();

            }
            try {
                command = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean isConnected() {
        return socket!=null && socket.isConnected();
    }
}
