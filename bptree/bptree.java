import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class bptree {
    static Node root;
    static int degree = 0;
    static int ver = 0;

    public static void main(String[] args) {
        switch (args[0]) {
            case "-c":
                if(args.length != 3){
                    System.out.println("<Data File Creation Fail> Wrong command.");
                    return;
                }
                dataFileCreation(args[1], Integer.parseInt(args[2]));
                break;

            case "-i":
                if(args.length != 3){
                    System.out.println("<Insertion Fail> Wrong command.");
                    return;
                }
                insertion(args[1], args[2]);
                break;

            case "-d":
                if(args.length != 3){
                    System.out.println("<Deletion Fail> Wrong command.");
                    return;
                }
                deletion(args[1], args[2]);
                break;

            case "-s":
                if(args.length != 3){
                    System.out.println("<Single Key Search Fail> Wrong command.");
                    return;
                }
                singleSearch(args[1], Integer.parseInt(args[2]));
                break;

            case "-r":
                if(args.length != 4){
                    System.out.println("<Ranged Search Fail> Wrong command.");
                    return;
                }
                rangeSearch(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                break;

             default:
                 System.out.println("<ERROR> Wrong command.");
        }
    }

    /*** Creation ***/
    public static void dataFileCreation(String indexName, int degree) {
        File indexFile = new File(indexName);

        try {
            PrintWriter outputStream = new PrintWriter(indexFile);
            outputStream.println(degree);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*** Insertion ***/
    private static void insertion(String indexName, String dataName) {
        File indexFile = new File(indexName);
        File dataFile = new File(dataName);
        String[] pair;
        Node tmpNode;

        loadIndexFile(indexFile);

        try {
            Scanner inputStream = new Scanner(dataFile);
            while (inputStream.hasNext()) {
                pair = inputStream.nextLine().split(",");
                tmpNode = insert(Integer.parseInt(pair[0]), Integer.parseInt(pair[1]), root);
                if (tmpNode != null) root = tmpNode;
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        saveInfo(indexFile);
    }

    /*** Deletion ***/
    private static void deletion(String indexName, String dataName) {
        File indexFile = new File(indexName);
        File dataFile = new File(dataName);
        int keyToDelete = 0;

        loadIndexFile(indexFile);

        try {
            Scanner inputStream = new Scanner(dataFile);
            while(inputStream.hasNext()) {
                keyToDelete = inputStream.nextInt();
                ver=0;
                root=delete(keyToDelete, root);
                if(ver==1) root = delete(keyToDelete,root);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        saveInfo(indexFile);
    }

    /*** Single Key Search ***/
    private static void singleSearch(String indexName, int key) {
        File indexFile = new File(indexName);

        loadIndexFile(indexFile);

        //empty tree
        if(root==null){
            System.out.println("NOT FOUND");
            return;
        }
        search(key);
    }

    /*** Range Search ***/
    private static void rangeSearch(String indexName, int startKey, int endKey) {
        File indexFile = new File(indexName);
        int exit =0;

        loadIndexFile(indexFile);

        if(root==null){
            System.out.println("<Ranged Search Fail> tree is empty.");
            return;
        }

        ver++;
        Node presentNode = search(startKey);
        while(presentNode!=null) {
            for (int i = 0; i < presentNode.getM(); i++) {
                if (presentNode.getKey(i) >= startKey && presentNode.getKey(i) <= endKey) {
                    System.out.println(presentNode.getKey(i) + "," + presentNode.getValue(i));
                }
                if (presentNode.getKey(i) > endKey){
                    exit++;
                    break;
                }
            }
            if(exit==1) break;
            presentNode = presentNode.getR();
        }
    }

    /* Index File Load */
    private static void loadIndexFile (File indexFile){
        try {
            Scanner inputStream = new Scanner(indexFile);
            degree = inputStream.nextInt();
            root = initTree(inputStream, null);
            inputStream.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    /* Tree Initialization */
    private static Node initTree (Scanner inputStream, Node r){
        int m, isLeaf;
        Node tmpNode, MostRightLeaf, MostLeftLeaf;
        String[] pair;

        if (!inputStream.hasNext()) return null;

        r = new Node();
        m = inputStream.nextInt();
        isLeaf = inputStream.nextInt();
        inputStream.nextLine();

        for (int i = 0; i < m; i++) {
            String tmp = inputStream.nextLine();
            pair = tmp.split(",");
            r.addPair(Integer.parseInt(pair[0]), Integer.parseInt(pair[1]));
        }
        if (isLeaf==1) {
            for (int i = 0; i < m + 1; i++) r.setLeftChild(i, null);
        }
        else {
            for (int i = 0; i < m + 1; i++) {
                tmpNode = initTree(inputStream, null);
                r.setLeftChild(i, tmpNode);
                // link leaf nodes
                if (tmpNode != null && i > 0) {
                    MostRightLeaf = r.getLeftChild(i - 1).findMostRightLeaf();
                    MostLeftLeaf = r.getLeftChild(i).findMostLeftLeaf();
                    MostRightLeaf.setR(MostLeftLeaf);
                }
            }
        }

        return r;
    }

    /* insert key */
    private static Node insert ( int key, int value, Node r){
        int index = 0;
        Node child = null;

        if(r==null) r = new Node();

        //already exist key
        for (int i = 0; i < r.getM(); i++) {
            if (r.getKey(i) == key) {
                System.out.println("<Insertion Fail> " + key + " is already exist.");
                return r;
            }
        }

        //r == leaf node (insert key)
        if (r.getLeftChild(0) == null) {
            if(r.getM()==0) r.addPair(key, value);
            else if (r.getKey(r.getM() - 1) < key) r.addPair(key, value);
            else if (r.getKey(0) > key) r.addPair(key, value, 0);
            else {
                for (int i = 0; i < r.getM(); i++) {
                    if ((i != r.getM() - 1) && r.getKey(i) < key && r.getKey(i + 1) > key) {
                        r.addPair(key, value, i + 1);
                        break;
                    }
                }
            }
        }

        //r == non leaf node
        else {
            if (r.getKey(0) > key) {
                r.setLeftChild(0, insert(key, value, r.getLeftChild(0)));
                child = r.getLeftChild(0);
                index = 0;
            } else {
                for (int i = 0; i < r.getM(); i++) {
                    if (i==r.getM()-1 || (r.getKey(i) < key && r.getKey(i + 1) > key)) {
                        r.setLeftChild(i + 1, insert(key, value, r.getLeftChild(i + 1)));
                        child = r.getLeftChild(i + 1);
                        index = i + 1;
                        break;
                    }
                }
            }
            if(child!=null && child.getM() >= degree) r =split(child,r,index);
        }
        if(r==root && r.getM() >= degree) r = split(r, null, -1);
        return r;
    }

    /* split node (c==child(to split), p==parent) */
    private static Node split(Node c, Node p, int index){
        int mid =(degree/2);
        Node tmpNode;

        //split root node
        if(p==null && index==-1){
            p=new Node();
            p.addPair(c.getKey(mid),c.getValue(mid),c);
            p.setR(new Node());
            p.getR().setR(c.getR());

            if(c.getLeftChild(0)!=null){
                c.setR(c.getLeftChild(mid));
                c.removePair(mid);
            }
            else c.setR(p.getR());
            for(int i=mid; i<c.getM(); i++){
                p.getR().addPair(c.getKey(i),c.getValue(i),c.getLeftChild(i));
                c.removePair(i);
                i--;
            }
        }

        //split leaf node
        else if (c.getLeftChild(0) == null) {
            p.addPair(c.getKey(mid), c.getValue(mid), index);
            p.setLeftChild(index, c);

            tmpNode = new Node();
            tmpNode.setR(c.getR());
            for (int i = mid; i < c.getM(); i++) {
                tmpNode.addPair(c.getKey(i), c.getValue(i), c.getLeftChild(i));
                c.removePair(i);
                i--;
            }
            c.setR(tmpNode);
            p.setLeftChild(index + 1, tmpNode);
        }
        //split non leaf node
        else {
            tmpNode = new Node();
            tmpNode.setR(c.getR());
            for (int i = mid + 1; i < c.getM(); i++) {
                tmpNode.addPair(c.getKey(i), c.getValue(i), c.getLeftChild(i));
                c.removePair(i);
                i--;
            }
            c.setR(c.getLeftChild(mid));
            p.addPair(c.getKey(mid), c.getValue(mid), index);
            p.setLeftChild(index, c);
            p.setLeftChild(index + 1, tmpNode);
            c.removePair(mid);
        }
        return p;
    }

    /* delete key (ver 0: delete key in leaf node// ver 1: delete key in non leaf node */
    private static Node delete(int key, Node r) {
        Node child = null, tmp;
        int index = 0, isExist = 0;
        // empty tree
        if(r==null || r.getM()==0){
            System.out.println("<Deletion Fail> tree is empty.");
            return r;
        }
        if(ver==1) {
            for (int i = 0; i < r.getM(); i++) {
                if (r.getKey(i) == key) {
                    tmp = r.getLeftChild(i+1).findMostLeftLeaf();
                    r.addPair(tmp.getKey(0), tmp.getValue(0), i);
                    r.setLeftChild(i, r.getLeftChild(i + 1));
                    r.removePair(i + 1);
                    return r;
                }
            }
        }
        //r == leaf node
        if(r== root&& r.getLeftChild(0)==null){
            for (int i = 0; i < r.getM(); i++) {
                if (r.getKey(i) == key) {
                    r.removePair(i);
                    isExist++;
                }
            }
            if(isExist==0){
                if (ver == 1) return r;
                System.out.println("<Deletion Fail> "+ key+ " is not exist.");
                return r;
            }
        }
        //child == leaf node
        else if (r.getLeftChild(0).getLeftChild(0) == null) {
            if (ver == 1) return r;
            if (r.getKey(0) > key) {
                child = r.getLeftChild(0);
                index = 0;
            } else if (r.getKey(r.getM() - 1) < key) {
                child = r.getR();
                index = r.getM();
            } else {
                for (int i = 0; i < r.getM(); i++) {
                    if (r.getKey(i) == key || (r.getKey(i) < key && r.getKey(i + 1) > key)) {
                        child = r.getLeftChild(i + 1);
                        index = i + 1;
                        break;
                    }
                }
            }
            //delete key in leaf node
            for (int i = 0; i < child.getM(); i++) {
                if (child.getKey(i) == key) {
                    isExist++;
                    if (i == 0 && index !=0) {
                        if(child.getM()==1) {
                            child.removePair(i);
                            if(r.getM()==1 && r.getLeftChild(0).getM()==1){
                                r.getLeftChild(0).setR(r.getR().getR());
                                r.setR(r.getLeftChild(0));
                                r.removePair(0);
                            }
                        }
                        else {
                            r.addPair(child.getKey(1), child.getValue(1), index);
                            r.setLeftChild(index, r.getLeftChild(index - 1));
                            r.removePair(index - 1);
                            child.removePair(0);
                        }
                    }
                    else{
                        if(child.getM()==1 && r.getM()==1 && r.getR().getM()==1){
                            child.addPair(r.getR().getKey(0),r.getR().getValue(0));
                            child.setR(r.getR().getR());
                            r.setR(child);
                            r.removePair(0);
                            child.removePair(0);
                            r=mergeLeafNode(child,r,index);
                        }
                        else child.removePair(i);
                        ver++;
                    }

                    if (child.getM() < (degree - 1) / 2) {
                        //borrow from left
                        if (index > 0 && r.getLeftChild(index - 1).getM() > (degree - 1) / 2) r = borrowFromLeft(child,r,index,1);
                        //borrow from right
                        else if (index < r.getM() && r.getLeftChild(index + 1).getM() > (degree - 1) / 2) r = borrowFromRight(child,r,index,1);
                        //merge
                        else r = mergeLeafNode(child,r,index);
                    }
                    break;
                }
            }
            if (isExist == 0) {
                System.out.println("<Deletion Fail> " + key + " is not exist.");
                return r;
            }
        }
        //child == non leaf node
        else {
            if (r.getKey(0) > key){
                r.setLeftChild(0,delete(key, r.getLeftChild(0)));
                child = r.getLeftChild(0);
                index = 0;
            }
            else if(r.getKey(r.getM()-1) < key ){
                r.setR(delete(key,r.getR()));
                child=r.getR();
                index = r.getM();
            }
            else{
                for (int i = 0; i < r.getM(); i++) {
                    if (r.getKey(i) == key || (r.getKey(i) < key && r.getKey(i + 1) > key)) {
                        r.setLeftChild(i+1,delete(key, r.getLeftChild(i + 1)));
                        child = r.getLeftChild(i+1);
                        index = i+1;
                        break;
                    }
                }
            }
            if(child.getM()<(degree - 1) / 2){
                //borrow from left
                if (index > 0 && r.getLeftChild(index - 1).getM() > (degree - 1) / 2) r = borrowFromLeft(child,r,index,0);
                //borrow from right
                else if (index < r.getM() && r.getLeftChild(index + 1).getM() > (degree - 1) / 2) r = borrowFromRight(child,r,index,0);
                //merge
                else r = mergeNonLeafNode(child,r,index);
            }
        }
        return r;
    }

    /* 'c' borrows key from left sibling */
    private static Node borrowFromLeft(Node c, Node p, int index, int leaf){
        Node sibling = p.getLeftChild(index - 1);
        //child == leaf node
        if (leaf==1) {
            c.addPair(sibling.getKey(sibling.getM() - 1), sibling.getValue(sibling.getM() - 1), 0);
            sibling.removePair(sibling.getM() - 1);
            p.addPair(c.getKey(0), c.getValue(0), index);
            p.setLeftChild(index, sibling);
            p.removePair(index - 1);
        }
        else {
            c.addPair(p.getKey(index - 1), p.getValue(index - 1), 0);
            c.setLeftChild(0, sibling.getR());
            p.addPair(sibling.getKey(sibling.getM() - 1), sibling.getValue(sibling.getM() - 1), index);
            p.setLeftChild(index, sibling);
            p.removePair(index - 1);
            sibling.setR(sibling.getLeftChild(sibling.getM() - 1));
            sibling.removePair(sibling.getM() - 1);
        }
        return p;
    }

    /* 'c' borrows key from right sibling */
    private static Node borrowFromRight(Node c, Node p, int index, int leaf){
        Node sibling = p.getLeftChild(index+1);
        //child == leaf node
        if(leaf==1){
            c.addPair(sibling.getKey(0), sibling.getValue(0));
            sibling.removePair(0);
            p.addPair(sibling.getKey(0), sibling.getValue(0), index);
            p.setLeftChild(index, c);
            p.removePair(index + 1);
            if(c.getM()==1 && index!=0){
                p.addPair(c.getKey(0),c.getValue(0),index);
                p.setLeftChild(index,p.getLeftChild(index-1));
                p.removePair(index-1);
            }
        }
        else{
            c.addPair(p.getKey(index),p.getValue(index), c.getR());
            p.addPair(sibling.getKey(0),sibling.getValue(0),index+1);
            p.setLeftChild(index+1,p.getLeftChild(index));
            p.removePair(index);
            c.setR(sibling.getLeftChild(0));
            sibling.removePair(0);
        }
        return p;
    }

    /* merge leaf node 'c' with its sibling */
    private static Node mergeLeafNode(Node c, Node p, int index){
        // add all key in child to root
        if(p.getM()==1 && p==root){
            int tmp = p.getLeftChild(0).getM();
            for(int i=0; i<tmp; i++) p.addPair(p.getLeftChild(i).getKey(i),p.getLeftChild(i).getValue(i),i);
            for (int i = 1; i < p.getR().getM(); i++) p.addPair(p.getR().getKey(i), p.getR().getValue(i));
            p.setLeftChild(tmp,null);
            p.setR(null);
        }
        // add all key in child to root
        else if(p.getM()==0 && p==root){
            for(int i=0; i<p.getR().getM(); i++) p.addPair(p.getR().getKey(i), p.getR().getValue(i));
            p.setR(null);
        }
        //if p!=root, p.getM()==0 -> do nothing, to merge p with its sibling or borrow key from its sibling
        else if(p.getM()==0){
            return p;
        }
        else if(index!=0){
            for(int i=0; i<c.getM(); i++){
                p.getLeftChild(index-1).addPair(c.getKey(i),c.getValue(i));
            }
            p.getLeftChild(index-1).setR(c.getR());
            p.setLeftChild(index,p.getLeftChild(index-1));
            p.removePair(index-1);
            if (p.getLeftChild(index - 1).getM() >= degree) {
                p = split(c, p, index - 1);
            }
        }
        //index==0
        else {
            for(int i=0; i<p.getLeftChild(index+1).getM(); i++){
                c.addPair(p.getLeftChild(index+1).getKey(i),p.getLeftChild(index+1).getValue(i));
            }
            c.setR(p.getLeftChild(index+1).getR());
            p.setLeftChild(index+1,c);
            p.removePair(index);
            if (p.getLeftChild(index).getM() >= degree) {
                p = split(p.getLeftChild(index), p, index);
            }
        }
        return p;
    }

    /* merge non leaf node 'c' with its sibling */
    private static Node mergeNonLeafNode(Node c, Node p, int index){
        if(p.getM()==1 && p!=root){
            p.getR().addPair(p.getKey(0),p.getValue(0),0);
            p.getR().setLeftChild(0,p.getLeftChild(0).getR());
            for(int i=0; i<p.getLeftChild(0).getM(); i++){
                p.getR().addPair(p.getLeftChild(0).getKey(i),p.getLeftChild(0).getValue(i),i);
                p.getR().setLeftChild(i,p.getLeftChild(0).getLeftChild(i));
            }
            p.removePair(0);
        }
        else {
            if (index == p.getM()) {
                c = p.getLeftChild(index - 1);
                index--;
            }
            c.addPair(p.getKey(index), p.getValue(index), c.getR());
            for (int i = 0; i < p.getLeftChild(index + 1).getM(); i++) {
                c.addPair(p.getLeftChild(index + 1).getKey(i), p.getLeftChild(index + 1).getValue(i), p.getLeftChild(index + 1).getLeftChild(i));
            }
            c.setR(p.getLeftChild(index + 1).getR());
            if (c.getM() >= degree) p = split(c, p, index);
            else {
                if (p==root && root.getM()==1) p = c;
                else{
                    p.setLeftChild(index+1, c);
                    p.removePair(index);
                }
            }
        }
        return p;
    }

    /* search key (ver 0: single, ver 1: range) */
    private static Node search(int key){
        int notFound =1;
        Node n = root;
        while(n.getLeftChild(0)!=null) {
            if (ver == 0) {
                System.out.print(n.getKey(0));
                for (int i = 1; i < n.getM(); i++) {
                    System.out.print("," + n.getKey(i));
                }
                System.out.println();
            }
            if (n.getKey(0) > key) n = n.getLeftChild(0);
            else if(n.getKey(n.getM()-1) < key ) n = n.getR();
            else{
                for (int i = 0; i < n.getM(); i++) {
                    if (n.getKey(i) == key || (n.getKey(i) < key && n.getKey(i + 1) > key)) {
                        n = n.getLeftChild(i+1);
                        break;
                    }
                }
            }
        }
        if(ver==0){
            for (int i = 0; i < n.getM(); i++) {
                if (n.getKey(i) == key) {
                    System.out.println(n.getValue(i));
                    notFound--;
                    break;
                }
            }
            if (notFound == 1) System.out.println("NOT FOUND");
        }
        return n;
    }

    /* update index file */
    private static void saveInfo (File indexFile){
        try {
            PrintWriter outputStream = new PrintWriter(indexFile);
            outputStream.println(degree);
            saveTree(outputStream, root);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* Save B+tree information */
    private static void saveTree (PrintWriter outputStream, Node r){
        if(r==null || r.getM()==0) return;
        outputStream.println(r.getM());
        if (r.getLeftChild(0) == null) outputStream.println(1);
        else outputStream.println(0);
        for (int i = 0; i < r.getM(); i++) {
            outputStream.println(r.getKey(i) + "," + r.getValue(i));
        }
        if(r.getLeftChild(0)!=null) {
            for (int i = 0; i < r.getM() + 1; i++) {
                saveTree(outputStream, r.getLeftChild(i));
            }
        }
    }
}