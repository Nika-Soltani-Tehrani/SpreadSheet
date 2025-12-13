# Spreadsheet Engine Simulation

This project is a **modular simulation of a spreadsheet engine**, inspired by common spreadsheet applications, with a strong emphasis on **software design concepts**, **clean architecture**, and **efficient computation**.

Rather than focusing on a graphical user interface, the project concentrates on the **core mechanics of spreadsheets**, including formula parsing, dependency management, and persistent storage.

---

## ✨ Core Features

- Numeric, textual, and formula-based cells
- Arithmetic expressions (`+`, `-`, `*`, `/`)
- Cell references (e.g. `A1`)
- Rectangular ranges (e.g. `A1:B5`)
- Built-in functions:
    - `SUMA`
    - `MIN`
    - `MAX`
    - `AVERAGE`
- Persistent storage using the **S2V (Semicolon Separated Values)** format

---

## 🧠 Formula Evaluation & Parsing

### Parsing Algorithm
Formulas are parsed using a **Recursive Descent Parser**, which explicitly enforces operator precedence and associativity.

### Supported Grammar
expression ::= term ((+ | -) term)*  
term ::= factor ((* | /) factor)*  
factor ::= number  
| cellReference  
| range  
| functionCall  
| '(' expression ')'  


---

## 🧩 Design Patterns Used

### 1️⃣ Composite Pattern (Formula Tree)

Formulas are represented as an **expression tree**:

- **Component**: `Expression`
- **Leaf Nodes**:
    - `ConstantNumber`
    - `CellReference`
- **Composite Nodes**:
    - `Operation`
    - `RangeReference`
    - `FunctionCall`

This allows uniform evaluation through `getValue()` and makes the system easily extensible.

---

### 2️⃣ Lazy Evaluation (On-Demand Recalculation)

To improve performance, the engine follows a **lazy evaluation strategy**:

- Formula cells cache their computed values
- Each formula tracks whether its value is valid
- When a referenced cell changes, dependent formulas are **invalidated**
- Recalculation happens **only when the value is requested**

This avoids unnecessary recalculation of unaffected cells.

---

### 3️⃣ Dependency Graph Management

Cell dependencies are modeled as a **directed graph**:

- Nodes represent cells
- An edge `A → B` means *B depends on A*

This structure enables:
- Efficient dependency invalidation
- Targeted recalculation
- Robust cycle detection

---

### 4️⃣ Cycle Detection

Before applying a new formula, the engine performs a **depth-first search (DFS)** on the dependency graph to detect cycles.

If adding a dependency creates a path back to the originating cell, the update is rejected, preventing infinite evaluation loops.

---

### 5️⃣ Range Handling

Cell ranges (e.g. `A1:C3`) are treated as iterable collections:

- Coordinates are expanded using column-index arithmetic
- Ranges are evaluated lazily
- Used both for value computation and dependency tracking

---

## 💾 Storage & Persistence

### S2V File Format

- Each row of the spreadsheet corresponds to one line in the file
- Columns are separated by semicolons (`;`)
- Formulas are stored verbatim
- Empty cells are represented by empty fields

### Storage Architecture

- Storage logic is isolated in a dedicated `SpreadsheetStorage` class
- The spreadsheet model remains independent of storage implementation
- Enables future support for alternative storage formats (CSV, JSON, databases)

---

## 🧪 Testing

The project is tested using **JUnit 5**, with coverage for:

- Formula parsing
- Arithmetic and function evaluation
- Dependency tracking
- Cycle detection
- Storage and reload consistency

---

## 🏗 Architectural Highlights

- Clear separation of concerns
- No I/O or UI logic inside model classes
- Extensible design for new operators, functions, and storage backends
- Designed with scalability and maintainability in mind

---

## 🚀 Summary

This project demonstrates how a spreadsheet engine can be implemented using:

- Classic design patterns
- Efficient graph algorithms
- Lazy computation strategies

It serves both as an educational example of advanced object-oriented design and as a solid foundation for building more complex spreadsheet systems.
