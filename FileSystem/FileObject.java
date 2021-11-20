package FileSystem;

import org.w3c.dom.Node;

import java.io.Serializable;


public class FileObject implements Comparable<Node>, Serializable {
    public String fn,fs,fstat;

    public FileObject(String filename, String filesize, String fileststatus){
        this.fn=filename;
        this.fs=filesize;
        this.fstat=fileststatus;
    }


    public String name() { return fn;}
    public String nameLen(){ return Integer.toString(fn.length());}
    public String reference(){
        int s =(int) (Math.log10(fn.length())+1);

        return Integer.toString(s);}
    public String size() { return fs;}
    public String lastmodefied() { return fstat;}

    public String display(){
        return fn+" "+fs+" "+fstat;
    }


    @Override
    public int compareTo(Node o) {
        return 0;
    }
}
