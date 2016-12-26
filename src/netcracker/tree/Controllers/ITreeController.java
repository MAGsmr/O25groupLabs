package netcracker.tree.Controllers;

import netcracker.tree.Nodes.ITreeNode;

import java.io.IOException;

public interface ITreeController {
    public ITreeNode create(int key, Object data);
    public boolean addTreeInPool(ITreeNode tree);
    public boolean removeTree(int key); //удаление дерево по ключу корневого узла
    public ITreeNode findNode(ITreeNode tree, int key);
    public ITreeNode copyTo(ITreeNode sourceTree, ITreeNode destTree, ITreeNode sourceNode, ITreeNode destNode);
    public ITreeNode cloneTree(ITreeNode tree);
    public ITreeNode addNode(ITreeNode parent, ITreeNode node) throws Exception;
    public ITreeNode addNode(ITreeNode parent, int key, Object data);
    public ITreeNode setNode(ITreeNode parent, int key, Object data);
    public void splitNode(ITreeNode parent, int key);//расщепление
    public void removeNode(ITreeNode parent, int key);//удаление с потомками
    public ITreeNode sortTree(ITreeNode tree, String c); //Упорядочить дерево
    public ITreeNode getTree(int key);
    public boolean save(ITreeNode tree, String fileName) throws IOException;
    public ITreeNode load(String fileName) throws IOException;

    //Необходимо еще добавить:
    //Функция по сохранению на диск
    //Функция по загрузке с диска
    //Функция по расщеплению


}
