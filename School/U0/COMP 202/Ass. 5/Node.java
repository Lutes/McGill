public class Node{

    // TODO modify this class so that it 
    // stores a reference to the previous 
    // element in the list

    String data;
    Node next;
    Node prev;
    //Constructor
    public Node(String input_data){
        this.data = input_data;
        this.next = null;
        this.prev = null;
    }

    //Various getters and setters.
    public void setString(String s) {
    	data = s;
    }
    
    public void addString(String s) {
    	data += s;
    }
    
    public void setNext(Node n) {
    	next = n;
    }
    
    public void setPrev(Node p) {
    	prev = p;
    }
    
    public String getString() {
    	return data;
    }
    
    public Node getNext() {
    	return next;
    }
    
    public Node getPrev() {
    	return prev;
    }
    
}




