package netcracker.net.server;

import netcracker.json.Json;
import netcracker.net.ServerSettings;
import netcracker.tree.Controllers.TreeController;

import java.io.*;
import java.net.Socket;

/**
 * Created by ВладПК on 17.12.2016.
 */
public class ServerController implements IServerController {
    private Socket socket;
    private boolean isAuth;

    public ServerController(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Соединение с новым пользователем выполнено успешно!");
        String help ="Авторизация: auth <login> <password>#";
        help+="Регистрация: reg <login> <password>#";
        help+="Создать дерево с корневым узлом: create <key> <value>#";
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
                    case "reg":{
                        if(components.length == 3) {
                            try {
                                ServerSettings.reg(components[1], components[2]);
                                out.println(ServerSettings.SUCCESSFUL+"Регистрация прошла успешно.");
                            } catch (FileNotFoundException e) {
                                out.println(ServerSettings.ERROR+"Регистрация не выполнена.");
                                e.printStackTrace();
                            }
                        }
                        else
                            out.println(ServerSettings.ERROR+"Функция reg должна принимать 2 параметра (справка help).");

                        break;
                    }
                    case "auth":{
                        if(components.length == 3) {
                            try {
                                isAuth = ServerSettings.auth(components[1], components[2]);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            out.println(ServerSettings.SUCCESSFUL+"Вход " +(isAuth?"":"не")+ " выполнен.");
                        }
                        else
                            out.println(ServerSettings.ERROR+"Функция auth должна принимать 2 параметра (справка help).");
                        break;
                    }
                    case "help":{
                        out.println(help);
                        break;
                    }
                    case "create":{
                        if(!isAuth){
                            out.println(ServerSettings.ERROR+"Вход не выполнен. Пройдите авторизацию командой auth (подробнее смотрите в help)");
                            break;
                        }
                        int key = 0;
                        if(components.length == 3) {
                            try {
                                key = Integer.parseInt(components[1]);
                                Object value = components[2];
                                if (TreeController.getInstance().create(key, value) != null) {
                                    out.println(ServerSettings.SUCCESSFUL + "Дерево с корнем " + key + "было успешно создано!");
                                    tree =key;
                                }
                                else
                                    out.println(ServerSettings.ERROR+"Дерево не было создано, проверьте правильность входных параметров (справка help).");
                            } catch (Exception e) {
                                out.println(ServerSettings.ERROR+"Функция create в качестве первого параметра должна принимать целое число (справка help).");
                            }
                        }
                        else
                            out.println(ServerSettings.ERROR+"Функция create должна принимать 2 параметра (справка help).");

                        break;
                    }
                    case "add":{
                        if(!isAuth){
                            out.println(ServerSettings.ERROR+"Вход не выполнен. Пройдите авторизацию командой auth (подробнее смотрите в help)");
                            break;
                        }
                        int key = 0;
                        if(components.length == 3) {
                            try {
                                key = Integer.parseInt(components[1]);
                                Object value = components[2];
                                if (tree != null) {
                                    if (TreeController.getInstance().addNode(TreeController.getInstance().getTree(tree), key, value) == null){
                                        out.println(ServerSettings.ERROR+"Узел с таким ключом существует! Не добавлен.");

                                    }
                                    else{
                                        out.println(ServerSettings.SUCCESSFUL+Json.toJson(TreeController.getInstance().getTree(tree)));
                                    }
                                } else
                                    out.println(ServerSettings.ERROR+"Не открыто дерево для выполнения команды (справка help).");
                            } catch (Exception e) {
                                out.println(ServerSettings.ERROR+"Функция add в качестве первого параметра должна принимать целое число (справка help).");
                            }
                        }
                        else
                            out.println(ServerSettings.ERROR+"Функция add должна принимать 2 параметра (справка help).");
                        break;
                    }
                    case "set":{
                        if(!isAuth){
                            out.println(ServerSettings.ERROR+"Вход не выполнен. Пройдите авторизацию командой auth (подробнее смотрите в help)");
                            break;
                        }
                        int key = 0;
                        if(components.length == 3) {
                            try {
                                key = Integer.parseInt(components[1]);
                                Object value = components[2];
                                if (tree != null) {
                                    if (TreeController.getInstance().setNode(TreeController.getInstance().getTree(tree), key, value) == null){
                                        out.println(ServerSettings.ERROR+"Узел с таким ключом не существует.");
                                        break;
                                    }
                                    else{
                                        out.println(ServerSettings.SUCCESSFUL+Json.toJson(TreeController.getInstance().getTree(tree)));
                                    }
                                } else
                                    out.println(ServerSettings.ERROR+"Не открыто дерево для выполнения команды (справка help).");
                            } catch (Exception e) {
                                out.println(ServerSettings.ERROR+"Функция set в качестве первого параметра должна принимать целое число (справка help).");
                            }
                        }
                        else
                            out.println(ServerSettings.ERROR+"Функция set должна принимать 2 параметра (справка help).");
                        break;
                    }
                    case "split":{
                        if(!isAuth){
                            out.println(ServerSettings.ERROR+"Вход не выполнен. Пройдите авторизацию командой auth (подробнее смотрите в help)");
                            break;
                        }
                        int key = 0;
                        if(components.length == 2) {
                            try {
                                key = Integer.parseInt(components[1]);

                                if (tree != null) {
                                    if (TreeController.getInstance().getTree(tree).getKey() != key) {
                                        TreeController.getInstance().splitNode(TreeController.getInstance().getTree(tree), key);
                                        out.println(ServerSettings.SUCCESSFUL+Json.toJson(TreeController.getInstance().getTree(tree)));
                                    } else
                                        out.println(ServerSettings.ERROR+"Воспользуйтесь удалением дерева целиком");
                                } else
                                    out.println(ServerSettings.ERROR+"Не открыто дерево для выполнения команды (справка help).");
                            } catch (Exception e) {
                                out.println(ServerSettings.ERROR+"Функция split должна принимать целочисленный параметр (справка help).");
                            }
                        }
                        else
                            out.println(ServerSettings.ERROR+"Функция remove должна принимать 1 параметра (справка help).");
                        break;
                    }
                    case "remove":{
                        if(!isAuth){
                            out.println(ServerSettings.ERROR+"Вход не выполнен. Пройдите авторизацию командой auth (подробнее смотрите в help)");
                            break;
                        }
                        int key = 0;
                        if(components.length == 2) {
                            try {
                                key = Integer.parseInt(components[1]);

                                if (tree != null) {
                                    if (TreeController.getInstance().getTree(tree).getKey() != key) {
                                        TreeController.getInstance().removeNode(TreeController.getInstance().getTree(tree), key);
                                        out.println(ServerSettings.SUCCESSFUL+Json.toJson(TreeController.getInstance().getTree(tree)));
                                    } else
                                        out.println(ServerSettings.ERROR+"Воспользуйтесь удалением дерева целиком");
                                } else
                                    out.println(ServerSettings.ERROR+"Не открыто дерево для выполнения команды (справка help).");
                            } catch (Exception e) {
                                out.println(ServerSettings.ERROR+"Функция remove должна принимать целочисленный параметр (справка help).");
                            }
                        }
                        else
                            out.println(ServerSettings.ERROR+"Функция remove должна принимать 1 параметра (справка help).");
                        break;
                    }
                    case "find":{
                        if(!isAuth){
                            out.println(ServerSettings.ERROR+"Вход не выполнен. Пройдите авторизацию командой auth (подробнее смотрите в help)");
                            break;
                        }
                        int key = 0;
                        if(components.length == 2) {
                            try {
                                key = Integer.parseInt(components[1]);
                                if (tree != null) {
                                    if (TreeController.getInstance().findNode(TreeController.getInstance().getTree(tree), key)!=null) {
                                        out.println(ServerSettings.SUCCESSFUL+Json.toJson(TreeController.getInstance().findNode(TreeController.getInstance().getTree(tree), key)));
                                    } else
                                        out.println(ServerSettings.ERROR+"Узел с таким ключом не найден.");
                                } else
                                    out.println(ServerSettings.ERROR+"Не открыто дерево для выполнения команды (справка help).");
                            } catch (Exception e) {
                                out.println(ServerSettings.ERROR+"Функция find должна принимать целочисленный параметр (справка help).");
                            }
                        }
                        else
                            out.println(ServerSettings.ERROR+"Функция find должна принимать 1 параметра (справка help).");
                        break;
                    }

                    case "open":{
                        if(!isAuth){
                            out.println(ServerSettings.ERROR+"Вход не выполнен. Пройдите авторизацию командой auth (подробнее смотрите в help)");
                            break;
                        }
                        if(components.length == 2){
                            try{
                                Integer buf = Integer.parseInt(components[1]);
                                if(buf!=null && TreeController.getInstance().getTree(buf)!=null){
                                    tree = buf;
                                    out.println(ServerSettings.SUCCESSFUL+Json.toJson(TreeController.getInstance().getTree(tree)));
                                }
                                else
                                    out.println(ServerSettings.ERROR+"Дерево с таким ключом не найдено (справка help).");
                            }
                            catch(Exception e){
                                out.println(ServerSettings.ERROR+"Функция open должна принимать целочисленный параметр (справка help).");
                            }
                        }
                        else
                            out.println(ServerSettings.ERROR+"Функция open должна принимать 1 параметр (справка help).");
                        break;
                    }
                    case "show":{
                        if(!isAuth){
                            out.println(ServerSettings.ERROR+"Вход не выполнен. Пройдите авторизацию командой auth (подробнее смотрите в help)");
                            break;
                        }
                        int key = 0;
                        if(components.length == 2) {
                            try {
                                key = Integer.parseInt(components[1]);
                                if (tree != null)
                                    if(TreeController.getInstance().findNode(TreeController.getInstance().getTree(tree), key)!=null)
                                        out.println(ServerSettings.SUCCESSFUL + ""+key+" "+Json.toJson(TreeController.getInstance().getTree(tree)));
                                    else
                                        out.println(ServerSettings.ERROR+"Невозможно раскрыть несуществующий узел (справка help):");
                                else
                                    out.println(ServerSettings.ERROR+"Не открыто дерево для выполнения команды (справка help):");
                            } catch (Exception e) {
                                out.println(ServerSettings.ERROR+"Функция show должна принимать целочисленный параметр (справка help).");
                            }
                        }
                        else
                            out.println(ServerSettings.ERROR+"Функция show должна принимать 1 параметр (справка help).");
                        break;
                    }
                    case "hide":{
                        if(!isAuth){
                            out.println(ServerSettings.ERROR+"Вход не выполнен. Пройдите авторизацию командой auth (подробнее смотрите в help)");
                            break;
                        }
                        int key = 0;
                        if(components.length == 2) {
                            try {
                                key = Integer.parseInt(components[1]);
                                if (tree != null)
                                    if(TreeController.getInstance().findNode(TreeController.getInstance().getTree(tree), key)!=null)
                                        out.println(ServerSettings.SUCCESSFUL + ""+key+" "+Json.toJson(TreeController.getInstance().getTree(tree)));
                                    else
                                        out.println(ServerSettings.ERROR+"Невозможно свернуть несуществующий узел (справка help):");
                                else
                                    out.println(ServerSettings.ERROR+"Не открыто дерево для выполнения команды (справка help):");
                            } catch (Exception e) {
                                out.println(ServerSettings.ERROR+"Функция hide должна принимать целочисленный параметр (справка help).");
                            }
                        }
                        else
                            out.println(ServerSettings.ERROR+"Функция hide должна принимать 1 параметр (справка help).");
                        break;
                    }
                    case "removeTree":{
                        if(!isAuth){
                            out.println(ServerSettings.ERROR+"Вход не выполнен. Пройдите авторизацию командой auth (подробнее смотрите в help)");
                            break;
                        }
                        if (tree != null) {
                            TreeController.getInstance().removeTree(tree);
                            tree= null;
                            out.println(ServerSettings.SUCCESSFUL+"Операция удаления дерева выполнена успешно.");
                        } else
                            out.println(ServerSettings.ERROR+"Не открыто дерево для выполнения команды (справка help).");
                        break;
                    }
                    case "CloneTree":{
                        if(!isAuth){
                            out.println(ServerSettings.ERROR+"Вход не выполнен. Пройдите авторизацию командой auth (подробнее смотрите в help)");
                            break;
                        }
                        int key = 0;
                        if (tree == null) {
                            out.println(ServerSettings.ERROR+"Не открыто дерево для выполнения команды (справка help).");
                            break;
                        }
                        if(components.length > 1)
                            out.println(ServerSettings.ERROR+"Функция CloneTree не должна принимать параметров (справка help). Выполнена без учета параметров.");
                        TreeController.getInstance().addTreeInPool( TreeController.getInstance().cloneTree(TreeController.getInstance().getTree(tree)));
                        out.println(ServerSettings.SUCCESSFUL+"Операция выполнена успешно.");
                        break;
                    }
                    case "load":{
                        if(!isAuth){
                            out.println(ServerSettings.ERROR+"Вход не выполнен. Пройдите авторизацию командой auth (подробнее смотрите в help)");
                            break;
                        }
                        if(components.length == 2) {
                            try {

                                Integer key = TreeController.getInstance().load(components[1]).getKey();
                                if(key!=null){
                                    tree = key;
                                    TreeController.getInstance().addTreeInPool(TreeController.getInstance().load(components[1]));
                                    out.println(ServerSettings.SUCCESSFUL+Json.toJson(TreeController.getInstance().getTree(key)));
                                }
                                else
                                    out.println(ServerSettings.ERROR+"Не удалось открыть дерево (справка help).");
                            } catch (IOException e) {
                                out.println(ServerSettings.ERROR+"Не удалось открыть/прочитать файл с таким именем (справка help).");
                            }
                        }
                        else
                            out.println(ServerSettings.ERROR+"Функция load должна принимать 1 параметр (справка help).");
                        break;
                    }
                    case "save":{
                        if(!isAuth){
                            out.println(ServerSettings.ERROR+"Вход не выполнен. Пройдите авторизацию командой auth (подробнее смотрите в help)");
                            break;
                        }
                        if(components.length == 2) {
                            try {
                                if(TreeController.getInstance().save(TreeController.getInstance().getTree(tree), components[1]))
                                    out.println(ServerSettings.SUCCESSFUL+"Дерево успешно сохранено (справка help).");
                                else
                                    out.println(ServerSettings.ERROR+"Не удалось сохранить дерево (справка help).");
                            } catch (IOException e) {
                                out.println(ServerSettings.ERROR+"Не удалось сохранить дерево (справка help).");
                            }
                        }
                        else
                            out.println(ServerSettings.ERROR+"Функция save должна принимать 1 параметр (справка help).");
                        break;
                    }
                    case "sortTree":{
                        if(!isAuth){
                            out.println(ServerSettings.ERROR+"Вход не выполнен. Пройдите авторизацию командой auth (подробнее смотрите в help)");
                            break;
                        }
                        String strComparator = "key";
                        if(components.length == 2) {
                            try {
                                strComparator = components[1];
                                if ( strComparator.equals("data") || strComparator.equals("key") ) {
                                    if (tree != null) {
                                        out.println(ServerSettings.SUCCESSFUL+Json.toJson(TreeController.getInstance().sortTree(TreeController.getInstance().getTree(tree), strComparator)));
                                    } else
                                        out.println(ServerSettings.ERROR+"Не открыто дерево для выполнения команды (справка help).");
                                } else
                                    out.println(ServerSettings.ERROR+"Неверный критерий сортировки");
                            } catch (Exception e) {
                                out.println(ServerSettings.ERROR+"Функция sortTree в качестве первого параметра должна принимать cтроку(key или data) (справка help).");
                            }
                        }
                        else
                            out.println(ServerSettings.ERROR+"Функция add должна принимать 2 параметра (справка help).");
                        break;
                    }


                    default:
                        out.println(ServerSettings.ERROR+"Неизвестная команда "+components[0] +" (справка help).");
                        break;

                }
                //out.flush();
            }
            try {
                command = in.readLine();
            } catch (IOException e) {
                System.out.println((socket!=null) + " "+socket.isInputShutdown() + " ");
                e.printStackTrace();
            }
        }
        System.out.println("Сеанс завершен. Закрытие соединения.");
    }


    public boolean isConnected() {
        return socket!=null && socket.isConnected();
    }
}
