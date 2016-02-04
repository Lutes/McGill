public class DoublyLinkedList{
   
	private Node start;
    private int length;
    
    public DoublyLinkedList(){
    	String blank = "";
       start = new Node(blank);
       length = 0;
    }

    // Find the first node that holds value string. 
    public Node find( String value ){
        
    	Node currNode = start;	
    	for (int i = 0; i < length; i++) {
    		if (currNode != start && currNode.getString().equals(value)) {
    			return currNode;
    		}
    		if (currNode.getNext() != null) {
    			currNode = currNode.getNext();
    		}	
    	}
        return null;
    }
    
    // places new node at start of list
    public void insertStart( String value ){
        
    	Node n1 = new Node(value);
    	Node firstNode = start.getNext();
    	start.setNext(n1);
    	n1.setPrev(start);
    	if (firstNode != null) {
        	n1.setNext(firstNode);  
        	firstNode.setPrev(n1);
    	}
    	length++;
    }
    
    // places new node at end of list
    public void insertEnd( String value ){
    	
    	Node currtNode = start;
    	Node newNode = new Node(value);
    	while (currtNode.getNext() != null) {
    		currtNode = currtNode.getNext();
    	}
    	currtNode.setNext(newNode);
    	newNode.setPrev(currtNode);
    	length++;
    }

    // removes all object that hold the string value.
    public void remove( String value ){
    	
       	Node tarNode = find(value);   	
    	while (tarNode != null) {
        	if (tarNode != start) {
        		tarNode.getPrev().setNext(tarNode.getNext());
        		tarNode.getNext().setPrev(tarNode.getPrev());
        		length--;
        	}
           	tarNode = find(value);
    	}
    }

    // Removes node specified by index. 
    public void removeAtIndex( int index ){
       
    	Node currNode = start;
    	if (index < 1) {
    		return;
    	}
    	for (int i = 0; i < index; i++) {
    		if (currNode.getNext() != null) {
    			currNode = currNode.getNext();
    		}
    		else {
    			return;
    		}
    	}
		currNode.getPrev().setNext(currNode.getNext());
		currNode.getNext().setPrev(currNode.getPrev());
		length--;
    }
   
    //Creates a reverse string.
    public String toStringReverse(){
        String result = "";
        Node curNode = start;
        while (curNode.getNext() != null) {
        	curNode = curNode.getNext();
        }
        for (int i = 0; i < length; i++) {
        	result += curNode.getString();
        	curNode = curNode.getPrev();
        }
        return result;
    }

    // This prints the String.
    public String toString(){
        String str = "";
        Node curNode = start;
        while (curNode != null) {
            str += curNode.getString();
            curNode = curNode.getNext();
        }        
        return str;
    }
    
    public static void main (String[] args){
        DoublyLinkedList list = new DoublyLinkedList();
        String once = "And you may find yourself ";
        
        list.insertStart("I am helpless. ");
        list.insertEnd(once);
        list.insertEnd("There is hope. ");
        list.insertEnd("I am helpless. ");
        list.insertEnd("Hello!. ");
        list.insertEnd("I do not believe. ");
        
        Node n = list.find(once);
        n.addString("in a shotgun shack. ");
        
        System.out.println(list);
        
        list.remove(once);
        list.removeAtIndex(0);
        
        System.out.println(list);
        
        System.out.println(list.toStringReverse());
    }
}
