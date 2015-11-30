package eg.edu.guc.rolltheball.logic.grammer;

public class ThereExist extends Quantifier implements Formula{

    public ThereExist(Variable var, Formula formula) {
        super();
        this.var = var;
        this.formula = formula;
    }

    @Override
    public String toString() {
        return "∃" + var + "(" + formula + ")";
    }
}
