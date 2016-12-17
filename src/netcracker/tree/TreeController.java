package netcracker.tree;

import netcracker.json.Json;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ВладПК
 */
public class TreeController implements ITreeController{

    private static TreeController instance;

    private TreeController(){}

    public static TreeController getInstance(){
        if(instance == null)
            instance = new TreeController();
        return instance;
    }


    @Override
    public ITreeNode create(int key, Object data) {
        if(data!=null){
            TreeNode tree = new TreeNode(key, data);
            TreePool.getInstance().put(tree);
            return tree;
        }
        return null;
    }
    @Override
    public boolean addTreeInPool(ITreeNode tree) {
        if(tree!=null){
            TreePool.getInstance().put(tree);
            return true;
        }
        return false;
    }

    @Override
    public ITreeNode addNode(ITreeNode parent, int key, Object data) {
        ITreeNode root = null;
        if(parent!=null){
            root = parent;
            if(key>parent.getKey()) {
                ITreeNode node = addNode(parent.getRight(), key, data);
                if (node == null) return null;
                root.setRight(node);
                node.setParent(root);
            }
            else
            if(key<parent.getKey()) {
                ITreeNode node = addNode(parent.getLeft(), key, data);
                if (node == null) return null;
                root.setLeft(node);
                node.setParent(root);
            }
            else
                return null;// Искл - узел с таким ключом уже существует//parent.setData(data);
        }
        else{
            root = new TreeNode(key, data, null, null);
        }
        return root;
    }
    @Override
    public ITreeNode setNode(ITreeNode parent, int key, Object data) {
        if(parent!=null){
            if(key>parent.getKey()) {
                return setNode(parent.getRight(), key, data);
            }
            else
            if(key<parent.getKey()) {
                return setNode(parent.getLeft(), key, data);
            }
            else {
                parent.setData(data);
                return parent;
            }
        }
        else return null;
    }

    @Override
    public void splitNode(ITreeNode root, int key){
        if ( root == null )
            return;
        else{
            if ( key > root.getKey() ) splitNode(root.getRight(), key);
            else if ( key < root.getKey() ) splitNode(root.getLeft(), key);
            else if ( key == root.getKey() ){
                if ( root.getLeft() == null && root.getRight() == null ) {
                    if (root.getParent().getRight() == root) root.getParent().setRight(null);
                    if (root.getParent().getLeft() == root) root.getParent().setLeft(null);
                }
                else if ( root.getLeft() == null || root.getRight() == null ){
                    if (root.getLeft() == null){
                        if ( root.getParent().getRight() == root) root.getParent().setRight(root.getRight());
                        if ( root.getParent().getLeft() == root) root.getParent().setLeft(root.getRight());
                        root.getRight().setParent(root.getParent());
                    }
                    else if (root.getRight() == null){
                        if (root.getParent().getRight() == root) root.getParent().setRight(root.getLeft());
                        if (root.getParent().getLeft() == root) root.getParent().setLeft(root.getLeft());
                        root.getLeft().setParent(root.getParent());
                    }
                }
                else if ( root.getLeft() != null && root.getRight() != null ){
                    if (root.getRight().getLeft() == null){
                        root.setData(root.getRight().getData());
                        root.setKey(root.getRight().getKey());
                        root.setRight(root.getRight().getRight());
                        root.getRight().setParent(root);
                    }
                    else {
                        ITreeNode byf = minimumNode(root.getRight());
                        root.setData(byf.getData());
                        root.setKey(byf.getKey());
                        splitNode(root.getRight(), byf.getKey());
                    }
                }
            }
        }
    }

    private ITreeNode minimumNode ( ITreeNode node ){
        ITreeNode result = node;
        while (result.getLeft() != null)
            result = result.getLeft();
        return result;
    }

    @Override
    public void removeNode(ITreeNode root, int key){
        if ( root == null )
            return;
        else{
            if ( key > root.getKey() ) removeNode(root.getRight(), key);
            else if ( key < root.getKey() ) removeNode(root.getLeft(), key);
            else if ( key == root.getKey() ){
                if (root.getParent().getRight() == root) root.getParent().setRight(null);
                if (root.getParent().getLeft() == root) root.getParent().setLeft(null);
            }
        }
    }

    @Override
    public ITreeNode sortTree(ITreeNode tree, String strComparator) {
        Comparator<ITreeNode> c = null;
        System.out.println(strComparator);
        if (strComparator.equals("key")) c = new keyComparator();
        if (strComparator.equals("data")) c = new dataStringComparator();
        ITreeNode tmp = tree;
        if (removeTree(tree.getKey())==true){
            ArrayList<ITreeNode> ar = new ArrayList<ITreeNode>();
            ar = getArray(ar, tmp);
            System.out.println(ar.size());
            ar.sort(c);
            tmp = null;
            ITreeNode result = getTreeFromArray(ar, 0, ar.size()-1, tmp);
            addTreeInPool(result);
            return result;
        }
        else return null;
    }

    private ArrayList<ITreeNode> getArray (ArrayList<ITreeNode> ar, ITreeNode node){
        if (node == null) return null;
        if (!(node.getLeft()==null && node.getRight()==null)){
            if (node.getLeft()!=null){
                getArray(ar, node.getLeft());
                node.setLeft(null);
            }
            if (node.getRight()!=null){
                getArray(ar, node.getRight());
                node.setRight(null);
            }
        }
        ar.add(node);
        return ar;
    }

    private ITreeNode getTreeFromArray(ArrayList<ITreeNode> ar, int start, int end, ITreeNode tree){
        int base = (end-start+1)/2;
        tree=ar.get(start+base);
        tree.setLeft(null);
        tree.setRight(null);
        if (base+start+1<=end) tree.setRight(getTreeFromArray(ar, start+base+1, end, tree.getRight()));
        if (start+base-1>=start) tree.setLeft(getTreeFromArray(ar, start, start+base-1, tree.getLeft()));
        return tree;
    }

    @Override
    public ITreeNode addNode(ITreeNode parent, ITreeNode node)throws Exception{
        if (parent==null || node == null ) return null;
        if (node.getLeft() != null )
            addNode(parent, node.getLeft());
        if (node.getRight() == null )
            addNode(parent, node.getRight());
        addNodeWithoutChildren(parent, node);
        return parent;
    }

    private ITreeNode addNodeWithoutChildren(ITreeNode parent, ITreeNode node) throws Exception { //упорядоченное добавление 1 вершины, возвращает фактического предка(ближайшего)
        if (node.getLeft() != null || node.getRight() != null) return null; //Следует воспользоваться ф-ей добавления дерева в дерева
        if (parent.getKey() == node.getKey()) throw new Exception(); // должен выбрасить исключение, если есть вершины с одинаковыми ключами
        if (parent.getKey() > node.getKey()){
            if (parent.getLeft() == null) {
                parent.setLeft(node);
                if (node.getParent()!=null){
                    if (node.getParent().getLeft()==node) node.getParent().setLeft(null);
                    if (node.getParent().getRight()==node) node.getParent().setRight(null);
                }
                node.setParent(node);
                return  parent;
            }
            else return addNodeWithoutChildren(parent.getLeft(), node);
        }
        else {
            if (parent.getRight() == null) {
                parent.setRight(node);
                if (node.getParent()!=null){
                    if (node.getParent().getLeft()==node) node.getParent().setLeft(null);
                    if (node.getParent().getRight()==node) node.getParent().setRight(null);
                }
                node.setParent(node);
                return  parent;
            }
            else return addNodeWithoutChildren(parent.getRight(), node);
        }
    }

    @Override
    public ITreeNode getTree(int key){
        return TreePool.getInstance().get(key);
    }

    @Override
    public boolean removeTree(int key) {
        return TreePool.getInstance().removeTreeFromPool(key);
    }

    @Override
    public ITreeNode findNode(ITreeNode tree, int key) {
        ITreeNode root = null;
        if(tree!=null){
            if(key>tree.getKey()) {
                root = findNode(tree.getRight(), key);
            }
            else
            if(key<tree.getKey()) {
                root = findNode(tree.getLeft(), key);
            }
            else
                root = tree;
        }
        return root;
    }

    @Override
    public boolean save(ITreeNode tree, String fileName) throws IOException{
        Writer fw = new FileWriter(fileName);
        boolean isSaved = false;
        try {
            fw.write(Json.toJson(tree));
            isSaved = true;
        }
        finally {
            fw.close();
        }
        return isSaved;
    }

    @Override
    public ITreeNode load(String fileName) throws IOException{
        Reader fr = new FileReader(fileName);
        ITreeNode node = null;
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader bf = new BufferedReader(fr);
            while(bf.ready()){
                sb.append(bf.readLine());
            }
            node = Json.parseJson(sb.toString());
        }
        finally {
            fr.close();
        }
        return node;
    }

    @Override
    public ITreeNode copyTo(ITreeNode sourceTree, ITreeNode destTree, ITreeNode sourceNode, ITreeNode destNode) { // ВОПРОС!!!
        return null;
    }

    @Override
    public TreeNode cloneTree(ITreeNode tree) {
        if (tree == null) return null;
        TreeNode cloneNode = (TreeNode)tree.clone();
        settingParents(cloneNode);

        return cloneNode;
    }

    private void settingParents (ITreeNode parent){
        if (parent.getLeft() != null) {
            parent.getLeft().setParent(parent);
            settingParents(parent.getLeft());
        }
        if (parent.getRight() != null) {
            parent.getRight().setParent(parent);
            settingParents(parent.getRight());
        }
    }

}

