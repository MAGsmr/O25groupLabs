package netcracker.tree.Views;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import netcracker.tree.Controllers.TreeController;
import netcracker.tree.Nodes.ITreeNode;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author ВладПК
 */
public class TreeView implements ITreeView{


    @Override
    public void input() throws IOException {
        Scanner sc = new Scanner(System.in);
        String help ="Создать дерево с корневым узлом: create <key> <value>\n";//
        help+="Открыть дерево: open <key>\n";
        help+="Открыть узел текущего дерева: show <key>\n";
        help+="Закрыть узел текущего дерева: hide <key>\n";
        help+="Добавить узел в открытое дерево: add <key> <value>\n";
        help+="Изменить значение узла по ключу в открытом дереве: set <key> <value>\n";
        help+="Раcщепление(удалить 1 узел в открытом дереве): split <key> \n";//расщепление
        help+="Удалить узел с потомками в открытом дереве: remove <key>\n";
        help+="Поиск узла в открытом дереве: find <key> \n";
        help+="Удалить открытое дерево: removeTree \n";
        help+="Клонировать открытое дерево: CloneTree \n";
        help+="Сохранить текущее открытое дерево: save <file_name>\n";
        help+="Загрущить дерево: load <file_name> \n";
        help+="Отсортировать текущее дерево: sortTree <value_for_sorting> //key или data\n";


        String command;
        String[] components;
        System.out.println("Введите команду (справка help):");
        ITreeNode tree = null;
        while(!(command = sc.nextLine()).equals("stop")){
            components = command.trim().split("\\s+");
            if(components.length>=1 && !components[0].equals("")){
                switch(components[0]){
                    case "help":{
                        System.out.println(help);
                        break;
                    }
                    case "create":{
                        int key = 0;
                        if(components.length == 3) {
                            try {
                                key = Integer.parseInt(components[1]);
                                Object value = components[2];
                                tree = TreeController.getInstance().create(key, value);
                                if (tree != null)
                                    showTree(tree, 0, 2);
                                else
                                    System.out.println("Дерево не было создано, проверьте правильность входных параметров (справка help).");
                            } catch (Exception e) {
                                System.out.println("Функция create в качестве первого параметра должна принимать целое число (справка help).");
                            }
                        }
                        else
                            System.out.println("Функция create должна принимать 2 параметра (справка help).");

                        break;
                    }
                    case "add":{
                        int key = 0;
                        if(components.length == 3) {
                            try {
                                key = Integer.parseInt(components[1]);
                                Object value = components[2];
                                if (tree != null) {
                                    if (TreeController.getInstance().addNode(tree, key, value) == null){
                                        System.out.println("Узел с таким ключом существует! Не добавлен.");
                                        break;
                                    }
                                    showTree(tree, 0, 2);
                                } else
                                    System.out.println("Не открыто дерево для выполнения команды (справка help).");
                            } catch (Exception e) {
                                System.out.println("Функция add в качестве первого параметра должна принимать целое число (справка help).");
                            }
                        }
                        else
                            System.out.println("Функция add должна принимать 2 параметра (справка help).");
                        break;
                    }
                    case "set":{
                        int key = 0;
                        if(components.length == 3) {
                            try {
                                key = Integer.parseInt(components[1]);
                                Object value = components[2];
                                if (tree != null) {
                                    if (TreeController.getInstance().setNode(tree, key, value) == null){
                                        System.out.println("Узел с таким ключом не уществует.");
                                        break;
                                    }
                                    showTree(tree, 0, 2);
                                } else
                                    System.out.println("Не открыто дерево для выполнения команды (справка help).");
                            } catch (Exception e) {
                                System.out.println("Функция set в качестве первого параметра должна принимать целое число (справка help).");
                            }
                        }
                        else
                            System.out.println("Функция set должна принимать 2 параметра (справка help).");
                        break;
                    }
                    case "split":{
                        int key = 0;
                        if(components.length == 2) {
                            try {
                                key = Integer.parseInt(components[1]);

                                if (tree != null) {
                                    if (tree.getKey() != key) {
                                        TreeController.getInstance().splitNode(tree, key);
                                        showTree(tree, 0, 2);
                                    } else
                                        System.out.println("Воспользуйтесь удалением дерева целиком");
                                } else
                                    System.out.println("Не открыто дерево для выполнения команды (справка help).");
                            } catch (Exception e) {
                                System.out.println("Функция split должна принимать целочисленный параметр (справка help).");
                            }
                        }
                        else
                            System.out.println("Функция remove должна принимать 1 параметра (справка help).");
                        break;
                    }
                    case "remove":{
                        int key = 0;
                        if(components.length == 2) {
                            try {
                                key = Integer.parseInt(components[1]);

                                if (tree != null) {
                                    if (tree.getKey() != key) {
                                        TreeController.getInstance().removeNode(tree, key);
                                        showTree(tree, 0, 2);
                                    } else
                                        System.out.println("Воспользуйтесь удалением дерева целиком");
                                } else
                                    System.out.println("Не открыто дерево для выполнения команды (справка help).");
                            } catch (Exception e) {
                                System.out.println("Функция remove должна принимать целочисленный параметр (справка help).");
                            }
                        }
                        else
                            System.out.println("Функция remove должна принимать 1 параметра (справка help).");
                        break;
                    }
                    case "find":{
                        int key = 0;
                        if(components.length == 2) {
                            try {
                                key = Integer.parseInt(components[1]);
                                if (tree != null) {
                                    ITreeNode node = TreeController.getInstance().findNode(tree, key);
                                    if (node!=null) {
                                        printNode(node, 2);
                                    } else
                                        System.out.println("Узел с таким ключом не найден.");
                                } else
                                    System.out.println("Не открыто дерево для выполнения команды (справка help).");
                            } catch (Exception e) {
                                System.out.println("Функция find должна принимать целочисленный параметр (справка help).");
                            }
                        }
                        else
                            System.out.println("Функция find должна принимать 1 параметра (справка help).");
                        break;
                    }

                    case "open":{
                        int key = 0;
                        if(components.length == 2){
                            try{
                                key = Integer.parseInt(components[1]);
                                tree = TreeController.getInstance().getTree(key);
                                if(tree!=null)
                                    showTree(tree, 0, 2);
                                else
                                    System.out.println("Дерево с таким ключом не найдено (справка help).");
                            }
                            catch(Exception e){
                                System.out.println("Функция open должна принимать целочисленный параметр (справка help).");
                            }
                        }
                        else
                            System.out.println("Функция open должна принимать 1 параметр (справка help).");
                        break;
                    }
                    case "show":{
                        int key = 0;
                        if(components.length == 2) {
                            try {
                                key = Integer.parseInt(components[1]);
                                if (tree != null)
                                    if(TreeController.getInstance().findNode(tree, key)!=null)
                                        showNode(tree, key, 0, 2);
                                    else
                                        System.out.println("Невозможно раскрыть несуществующий узел (справка help):");
                                else
                                    System.out.println("Не открыто дерево для выполнения команды (справка help):");
                            } catch (Exception e) {
                                System.out.println("Функция show должна принимать целочисленный параметр (справка help).");
                            }
                        }
                        else
                            System.out.println("Функция show должна принимать 1 параметр (справка help).");
                        break;
                    }
                    case "hide":{
                        int key = 0;
                        if(components.length == 2) {
                            try {
                                key = Integer.parseInt(components[1]);
                                if (tree != null)
                                    if(TreeController.getInstance().findNode(tree, key)!=null)
                                        hideNode(tree, key, 0, 2);
                                    else
                                        System.out.println("Невозможно свернуть несуществующий узел (справка help):");
                                else
                                    System.out.println("Не открыто дерево для выполнения команды (справка help):");
                            } catch (Exception e) {
                                System.out.println("Функция hide должна принимать целочисленный параметр (справка help).");
                            }
                        }
                        else
                            System.out.println("Функция hide должна принимать 1 параметр (справка help).");
                        break;
                    }
                    case "removeTree":{
                        if (tree != null) {
                            TreeController.getInstance().removeTree(tree.getKey());
                            tree= null;
                        } else
                            System.out.println("Не открыто дерево для выполнения команды (справка help).");

                        break;
                    }
                    case "CloneTree":{
                        int key = 0;
                        if (tree == null) {
                            System.out.println("Не открыто дерево для выполнения команды (справка help).");
                            break;
                        }
                        if(components.length > 1)
                            System.out.println("Функция CloneTree не должна принимать параметров (справка help). Выполнена без учета параметров.");
                        TreeController.getInstance().addTreeInPool( TreeController.getInstance().cloneTree(tree));
                        break;
                    }
                    case "load":{
                        if(components.length == 2) {
                            try {
                                System.out.println("Загрузка дерева...");
                                ITreeNode loadedTree = TreeController.getInstance().load(components[1]);
                                if(loadedTree!=null){
                                    System.out.println("Дерево успешно загружено!");
                                    tree = loadedTree;
                                    TreeController.getInstance().addTreeInPool(tree);
                                    showTree(tree, 0, 2);
                                }
                                else
                                    System.out.println("Не удалось открыть дерево (справка help).");
                            } catch (IOException e) {
                                System.out.println("Не удалось открыть/прочитать файл с таким именем (справка help).");
                            }
                        }
                        else
                            System.out.println("Функция load должна принимать 1 параметр (справка help).");
                        break;
                    }
                    case "save":{
                        if(components.length == 2) {
                            try {
                                if(TreeController.getInstance().save(tree, components[1]))
                                    System.out.println("Дерево успешно сохранено (справка help).");
                                else
                                    System.out.println("Не удалось сохранить дерево (справка help).");
                            } catch (IOException e) {
                                System.out.println("Не удалось сохранить дерево (справка help).");
                            }
                        }
                        else
                            System.out.println("Функция save должна принимать 1 параметр (справка help).");
                        break;
                    }
                    case "sortTree":{
                        String strComparator = "key";
                        if(components.length == 2) {
                            try {
                                strComparator = components[1];
                                if ( strComparator.equals("data") || strComparator.equals("key") ) {
                                    if (tree != null) {
                                        tree = TreeController.getInstance().sortTree(tree, strComparator);
                                        showTree(tree, 0, 2);
                                    } else
                                        System.out.println("Не открыто дерево для выполнения команды (справка help).");
                                } else
                                    System.out.println("Неверный критерий сортировки");
                            } catch (Exception e) {
                                System.out.println("Функция sortTree в качестве первого параметра должна принимать cтроку(key или data) (справка help).");
                            }
                        }
                        else
                            System.out.println("Функция add должна принимать 2 параметра (справка help).");
                        break;
                    }


                    default:
                        System.out.println("Неизвестная команда "+components[0] +" (справка help).");
                        break;
                }
                System.out.println("\nВведите команду (stop - выход из программы):");
            }

        }
    }

    @Override
    public void showNode(ITreeNode tree, int key, int startOffset, int offset) {

        System.out.println("[===| Открытие узла |===]");
        showNodeRec(tree, key, startOffset, offset);
        System.out.println("[=======================]");
    }

    private void showNodeRec(ITreeNode tree, int key, int startOffset, int offset) {
        if(tree!=null){
            printNode(tree, startOffset);
            if(tree.getKey()<key){

                printNode(tree.getLeft(), startOffset+offset);
                showNodeRec(tree.getRight(), key, startOffset+offset, offset);
            }
            else
            if(tree.getKey()>key){
                printNode(tree.getRight(), startOffset+offset);
                showNodeRec(tree.getLeft(), key, startOffset+offset, offset);
            }
            else
            {
                printNode(tree.getLeft(), startOffset+offset);
                printNode(tree.getRight(), startOffset+offset);
            }
        }
    }

    @Override
    public void showTree(ITreeNode tree, int startOffset, int offset) {
        System.out.println("[===| Вывод дерева |===]");
        showTreeRec(tree, startOffset, offset);
        System.out.println("[======================]");
    }

    private void showTreeRec(ITreeNode tree, int startOffset, int offset) {
        printNode(tree, startOffset);
        if(tree!=null) {
            showTreeRec(tree.getRight(), startOffset + offset, offset);
            showTreeRec(tree.getLeft(), startOffset + offset, offset);
        }
    }

    @Override
    public void hideNode(ITreeNode tree, int key, int startOffset, int offset) {
        System.out.println("[===| Вывод дерева |===]");
        hideNodeRec(tree, key, startOffset, offset);
        System.out.println("[======================]");
    }

    private void hideNodeRec(ITreeNode tree, int key, int startOffset, int offset) {
        if(tree!=null){
            printNode(tree, startOffset);
            if(tree.getKey()<key){
                if(tree.getLeft()!=null)
                    printNode(tree.getLeft(), startOffset+offset);
                if(tree.getRight()!=null)
                    hideNodeRec(tree.getRight(), key, startOffset+offset, offset);
            }
            else
            if(tree.getKey()>key){
                if(tree.getRight()!=null)
                    printNode(tree.getRight(), startOffset+offset);
                if(tree.getLeft()!=null)
                    hideNodeRec(tree.getLeft(), key, startOffset+offset, offset);
            }
        }
    }

    protected void printNode(ITreeNode node, int offset){

        StringBuffer spaces = new StringBuffer();
        for(int i =0; i < offset; i++) spaces.append('-');
        if(node!=null)
            System.out.println(spaces.toString()+"["+node.getKey()+"] value: " + node.getData());
        else
            System.out.println(spaces.toString()+"[null]");
    }
    
}
