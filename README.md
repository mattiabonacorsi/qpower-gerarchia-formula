# Formula Dependency Tracer

## Overview
This project implements a dependency tracing system for formula-based calculations in an energy management system. It analyzes formulas written in Freemarker syntax to track both forward and backward dependencies between different types of variables (Counters, KPIs, and Indices).

## Business Context
Among the various applications we manage, we have one that deals with energy-related topics. It starts from company consumption data (mainly Electrical, but also Natural Gas, Water, Steam, etc.) and builds:
- Consumption trees, to monitor potential losses or abnormal consumption
- Future simulations, to try to predict consumption (starting from similar production days in the past)
- All calculations of how these consumptions translate into costs

## Original Requirements
A new function was requested to analyze formula dependencies. Within the system, there are several points where formulas can be defined. The formulas are written in Freemarker (https://freemarker.apache.org/), which is a Java library that takes a formula text and parameters as input and returns the result.

Example Formula: `${ CON0001_EAS_H + CON0002_EAS_H }`
This formula adds two variables.

In our system, variable names (like those above) are always composed as:
`<entity prefix><progressive>_<detail>_<frequency>`

Formulas can reference variables that are calculated in turn. For example, CON0002_EAS_H from the previous formula might be calculated as:
`${ CON0003_EAS_H + IDX_PUNIT_H }`

## Database Structure
The involved tables are:
- FORMULA: contains formula texts
- CONTATORE: contains counter records (variables starting with CON...)
- KPI: contains KPI records (variables starting with KPI...). KPIs typically have two formulas as they are ratios
- INDICI: contains index records (variables like IDX_<code>_...)

## Example Cases

### Example 1: CON0140
Process:
1. Read contatoreId from CONTATORE table for CON0140
2. Find formulas in FORMULA table using this ID
3. Parse formula using regex split on [{}()+-/*<> ]
4. Check for parts starting with CON, KPI, or IDX
5. Recursively process found dependencies

Expected output:
CON0140
dipende da:
CON0001
CON0002
serve per:
CON0141

### Example 2: KPI0001

## Technical Implementation


## Project Structure

### Core Components
1. **Tracer.java**
   - Main tracing logic
   - Dependency resolution
   - Result generation

2. **FormulaParser.java**
   - Formula text parsing
   - Variable extraction
   - Dependency identification

3. **DatabaseUtils.java**
   - Database interactions
   - Entity mapping
   - Formula retrieval

4. **ResultDTO.java**
   - Result structure
   - Tree representation
   - Output formatting

### Supporting Classes
- **EntityType.java**: Entity type enumeration
- **Formula.java**: Formula data record
- **DatabaseException.java**: Custom exception handling
- **TracingException.java**: Tracing-specific errors

## Configuration
- Database settings in `config.properties`
- Logging configuration in `logback.xml`
- Maven dependencies in `pom.xml`

## Dependencies
- Java 23
- MySQL Connector
- SLF4J for logging
- Microsoft SQL Server JDBC driver

