public class Partition {
    private String name ;
    private int size ;
    Process process ;
    // constructor
    public Partition(){
        this.name = "" ;
        this.size = 0 ;
        this.process = null ;
    }
    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
