package yard.hunting.s.Interfaces;

import yard.hunting.s.Tokens.Valid.Operands.NumericTypes.Calculatable;

public interface FloatingPointArithmetics {
    public Calculatable add(double summand);
    public Calculatable subtract(double subtractor);
    public Calculatable multiplicateWith(double multiplicator);
    public Calculatable divideBy(double divisor);
}
