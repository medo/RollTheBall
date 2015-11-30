package eg.edu.guc.rolltheball.logic.grammer;

public class ForAll extends Quantifier implements Formula {


    public ForAll(Variable var, Formula formula) {
        super();
        this.var = var;
        this.formula = formula;
    }

    @Override
    public String toString() {
        return "∀" + var + "(" + formula + ")";
    }
}
