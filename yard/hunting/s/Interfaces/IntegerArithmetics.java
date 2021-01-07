package yard.hunting.s.Interfaces;

import yard.hunting.s.Tokens.Valid.Operands.NumericTypes.Calculatable;


public interface IntegerArithmetics {
    public Calculatable add(long summand);
    public Calculatable subtract(long subtractor);
    public Calculatable multiplicateWith(long multiplicator);
}
