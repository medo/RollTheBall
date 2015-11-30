package eg.edu.guc.rolltheball.logic.grammer;

public class Not implements Formula {
    public Formula formula;

    public Not(Formula formula) {
        super();
        this.formula = formula;
    }

    @Override
    public String toString(){
        return "¬" + formula;
    }
}
