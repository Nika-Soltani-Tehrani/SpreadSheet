This project is a simulation of common spreadsheets available in the market, mainly focused on design concepts.

It has several sections.

#TBC It is able to detect cycles and dependencies in formulas.

In order to handle dependencies efficiently, we follow lazy calculation. Whenever changes are made to a cell, instead of updating the whole sheet, all cells dependent to that cell become aware that their value is outdated. Whenever the value of these outdated cells are requested, their value are being recalculated.

#TPC Parser Section Algorithm.

✔ Formula parsing

✔ Composite pattern

✔ Lazy evaluation

✔ Dependency invalidation

✔ Efficient recalculation

✔ Cycle detection
