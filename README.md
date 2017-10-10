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
