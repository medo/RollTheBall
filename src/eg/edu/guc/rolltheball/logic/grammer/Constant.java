package eg.edu.guc.rolltheball.logic.grammer;

public class Constant extends Term {
    String name;

    public Constant(String name) {
        super();
        this.name = name;
    }

    @Override
    public Constant clone(){
        return new Constant(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Constant)
            return ((Constant) obj).hashCode() == hashCode();
        else
            return false;
    }
}
