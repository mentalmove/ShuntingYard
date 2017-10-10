# About this project

Different programming languages provide different capabilities.  
The same application, a console term calculator, using the same technique
(i.e. the shunting yard algorithm to transform infix notation into postfix notation),
is implemented in different languages here; these are

- [C](https://github.com/mentalmove/ShuntingYard/tree/C)
- [C++](https://github.com/mentalmove/ShuntingYard/tree/C++)
- [Javascript](https://github.com/mentalmove/ShuntingYard/tree/Javascript)
- [PHP](https://github.com/mentalmove/ShuntingYard/tree/PHP)
- [Swift](https://github.com/mentalmove/ShuntingYard/tree/Swift)

Although the underlying algorithmic principle is always the same,
this approach tries to take advantage of the particular properties
of each language, namely

- **C** has a small set of commands, provides direct access to memory addresses and can therefore be very fast.
- **C++** has (more or less) the same features as _C_, but simplifies the developer's work,
especially in questions related to memory management.
- **Javascript** is a completely dynamic language, having functions being so-called _First Class Citizens_.
- **PHP** is a typeless language (there is at least no general distinction between numbers and strings)
with an emphasis on collection based operations.
- **Swift**, even though the youngest in this formation, is quite conservative
and extremely typesafe.

&nbsp;

# Reverse Polish Notation

In (elementary) arithmetics, operators are usually written between numbers;
when an operator is found, the execution command is
> Do something with number left of operator and number right of operator

If the term contains more than one operator, it is ambiguous:
```
2 + 3 * 4
```
will lead to different results, depending on whether
`2 + 3` or `3 * 4` is calculated first.

Following common convention, `*` and `/` are preferred over `+` and `-`,
but this (as well as any other imaginable rule) does not cover
the cases if a different behaviour is wanted. The problem is solved
by setting brackets: Brackets always have higher precedence than operators.

In the 1920s, [Jan Łukasiewicz](https://en.wikipedia.org/wiki/Jan_Łukasiewicz)
invented a notation where operators
precede their numbers (more generally: their operands),
known as _Prefix Notation_ or _Polish Notation_; for practical reasons,
this is often used in reverse order such that operators follow their operands,
known as _Postfix Notation_ or _Reverse Polish Notation_.

Without any usage of brackets, the execution rule always is unambiguous:
> Do something with number two positions left of operator and number one position left of operator

Extended to a complete algorithm for term execution, this is
```
While term still has more than one element
    Replace most left operator with result of calculation [position - 2] [operator] [position - 1]
    Delete both used numbers
```

The above example would be written as
```
2 3 4 * +
```

A more complex example would be
```
((1 - (2 - (3 - (4 - (5 - 6))))) - 7 * (7 - (6 - (5 - (4 - (3 - 2))))) + 1) * -1
```
what in Postfix Notation is written as
```
1 2 3 4 5 6 - - - - - 7 7 6 5 4 3 2 - - - - - * - 1 + -1 *
```

---

Considerations about the usefulness of _Reverse Polish Notation_
obviously lead to the question
> Is it possible to rewrite any possible vaild term in _Reverse Polish Notation_?

The answer can be found either in giving mathematical proof
either in providing an algorithm which transforms traditional (_infix_)
notation into _postfix_ notation; if this algorithm can deal
with any given term, the above question is answered.

[Edsger W. Dijkstra](https://en.wikipedia.org/wiki/Edsger_W._Dijkstra)
invented such an algorithm and called it _Shunting Yard_. Since Dijkstra
was not only programmer but also mathmatican and logician, the method's
practical purpose is not self-evident.

The costs for executing a term written in infix notation directly
are obviously not only proportional to the amount of elements
but also proportional to the amount of bracket pairs; the overall
costs will grow exponentially for longer terms.

A term written in postfix notation has, due to the absence of bracket,
linearly growing costs. The Shunting Yard Algorithm itself also has
linearly growing costs. Therefore, for longer terms (where the base
costs do not have to be taken into consideration), execution will
benefit of transforming traditionally written terms into _Reverse Polish Notation_.
