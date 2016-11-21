package netcracker.tree;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    
    public static synchronized TreeController getInstance(){
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
    public boolean addTreeInPool(TreeNode tree) {
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
                root.setRight(node);
                node.setParent(root);
            }
            else
            if(key<parent.getKey()) {
                ITreeNode node = addNode(parent.getLeft(), key, data);
                root.setLeft(node);
                node.setParent(root);
            }
            else
                parent.setData(data);
        }
        else{
            root = new TreeNode(key, data, null, null);
        }
        return root;
    }

    @Override
    public void removeNode(ITreeNode root, int key){
        if ( root == null )
            return;
        else{
            if ( key > root.getKey() ) removeNode(root.getRight(), key);
            else if ( key < root.getKey() ) removeNode(root.getLeft(), key);
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
                        removeNode(root.getRight(), byf.getKey());
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
    public ITreeNode regularize(ITreeNode tree) {
        return null;
    }



    @Override
    public ITreeNode addNode(ITreeNode parent, ITreeNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            fw.write(toJson(tree));
            isSaved = true;
        }
        finally {
            fw.close();
        }
        return isSaved;
    }

    private String toJson(ITreeNode node){
        String json = "";
        if(node!=null){
            json +="{";
            json+="key:"+node.getKey()+",value:"+node.getData()+",";
            json+="left:"+toJson(node.getLeft());
            json+=",";
            json+="right:"+toJson(node.getRight())+"}";
        }
        else
            json = "null";
        return json;
    }

    public ITreeNode load(String fileName) throws IOException{
        Reader fr = new FileReader(fileName);
        ITreeNode node = null;
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader bf = new BufferedReader(fr);
            while(bf.ready()){
                sb.append(bf.readLine());
            }
            node = parseJson(sb.toString());
        }
        finally {
            fr.close();
        }
        return node;
    }

    private ITreeNode parseJson(String json){
        ITreeNode root = null;

        List<JsonObject> list = getJsonObjects(json);
        if(list.size()==4){
            Integer key = null;
            String value = null;
            String left = null;
            String right = null;
            for(int i =0; i < list.size(); i++){
                switch (list.get(i).getKey()){
                    case "key":
                        key = Integer.parseInt(list.get(i).getValue());
                        break;
                    case "value":
                        value = list.get(i).getValue();
                        break;
                    case "left":
                        left = list.get(i).getValue();
                        break;
                    case "right":
                        right = list.get(i).getValue();
                        break;
                    default:
                        break;
                }
            }
            if(key!=null && value!=null){
                root= new TreeNode(key, root);
                if(left!=null){
                    root.setLeft(parseJson(left));
                    if(root.getLeft()!=null)
                        root.getLeft().setParent(root);
                }
                if(right!=null){
                    root.setRight(parseJson(right));
                    if(root.getRight()!=null)
                        root.getRight().setParent(root);
                }
            }
        }
        return root;
    }

    private List<JsonObject> getJsonObjects(String s){
        List<JsonObject> list = new ArrayList<JsonObject>();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int iList = 0;
        char ch;
        int lScobeCount = 0;
        int rScobeCount = 0;
        if(s.length()>=2) {
            s = s.substring(1);
            s = s.substring(0, s.length() - 1);

            while (i < s.length()) {
                ch = s.charAt(i);
                switch (ch) {
                    case ':':
                        if (lScobeCount == rScobeCount) {
                            list.add(new JsonObject());
                            list.get(iList).setKey(sb.toString());
                            sb.delete(0, sb.length());
                        } else
                            sb.append(ch);
                        break;
                    case ',':
                        if (lScobeCount == rScobeCount) {
                            list.get(iList).setValue(sb.toString());
                            sb.delete(0, sb.length());
                            iList++;
                            lScobeCount = 0;
                            rScobeCount = 0;
                        } else
                            sb.append(ch);
                        break;
                    case '}':
                        rScobeCount++;
                        sb.append(ch);
                        break;
                    case '{':
                        lScobeCount++;
                        sb.append(ch);
                        break;
                    default:
                        sb.append(ch);
                        break;
                }
                i++;
            }
            if (iList<list.size() && !s.equals("") && lScobeCount == rScobeCount) {
                list.get(iList).setValue(sb.toString());
                sb.delete(0, sb.length());
            }
        }
        return list;
    }

    private class JsonObject{
        private String key;
        private String value;
        public JsonObject(String key, String value){
            this.key = key;
            this.value = value;
        }

        public JsonObject(){}

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @Override
    public ITreeNode copyTo(ITreeNode sourceTree, ITreeNode destTree, ITreeNode sourceNode, ITreeNode destNode) {
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
