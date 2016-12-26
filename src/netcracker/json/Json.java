package netcracker.json;

import netcracker.tree.Nodes.ITreeNode;
import netcracker.tree.Nodes.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ВладПК on 16.12.2016.
 */
public class Json {
    public static String toJson(ITreeNode node){
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

    public static ITreeNode parseJson(String json){
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
                root= new TreeNode(key, value);
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

    public static List<JsonObject> getJsonObjects(String s){
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
}
