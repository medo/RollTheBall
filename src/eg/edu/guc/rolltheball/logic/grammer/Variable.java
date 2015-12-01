package eg.edu.guc.rolltheball.logic.grammer;

public class Variable extends Term {
    public String name;


    public Variable(String name) {
        super();
        this.name = name;
    }

    @Override
    public Variable clone(){
        return new Variable(name);
    }

    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public int hashCode() {
    	return name.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
    	return ((Variable) obj).hashCode() == hashCode();
    }
}
