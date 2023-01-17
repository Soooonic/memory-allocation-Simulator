public class Process {
    private String name ;
    private int size ;
    private boolean state = true ;

    public Process(){
        this.name = "" ;
        this.size = 0 ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setState(boolean s){state = s ;}
    public Boolean getstate(){return state ;}

 }
